package top.sboxm.auth.service.impl;


import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import top.sboxm.auth.constants.LoginConstants;
import top.sboxm.auth.enums.QrCodeLoginStatus;
import top.sboxm.auth.exception.SystemException;
import top.sboxm.auth.mapper.UserMapper;
import top.sboxm.auth.pojo.dto.UserLoginDto;
import top.sboxm.auth.pojo.po.LoginUserDetails;
import top.sboxm.auth.pojo.po.User;
import top.sboxm.auth.pojo.vo.LoginVo;
import top.sboxm.auth.pojo.vo.UserInfoVo;
import top.sboxm.auth.service.UserLoginService;
import top.sboxm.auth.utils.QrCodeUtils;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.exception.BusinessException;
import top.sboxm.common.utils.BeanCopyUtil;
import top.sboxm.common.utils.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static top.sboxm.auth.enums.QrCodeLoginStatus.NOT_SCANNED;
import static top.sboxm.auth.enums.QrCodeLoginStatus.SUCCESS;

@Service
public class UserLoginServiceImpl implements UserLoginService {


    @Resource
    AuthenticationManager authenticationManager;

    @Resource
    StringRedisTemplate stringRedisTemplate;
    
    @Resource
    UserMapper userMapper;

    @Override
    public LoginVo<UserInfoVo> login(UserLoginDto userLoginDto) {

        String verifyCode = stringRedisTemplate.opsForValue().get(LoginConstants.LOGIN_CODE + userLoginDto.getEmail());
//        if (verifyCode==null){
//            throw new SystemException(RestResultEnum.VERIFY_CODE_EXPIRATION);
//        }
//        if (!verifyCode.equalsIgnoreCase(userLoginDto.getVerifyCode())){
//            throw new SystemException(RestResultEnum.VERIFY_CODE);
//        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userLoginDto.getEmail(),
                userLoginDto.getPassword());

        //认证
        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        //判定认证是否通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("邮箱或密码错误");
        }

        //创建token
        LoginUserDetails loginUserDetails = (LoginUserDetails) authenticate.getPrincipal();
        User user = loginUserDetails.getUser();

        String token = JwtUtils.createToken(LoginConstants.USER_ID, user.getId().toString(), 1);
        //缓存token
        stringRedisTemplate.opsForValue().set(LoginConstants.USER_LOGIN + user.getId(), JSONUtil.toJsonStr(loginUserDetails));
        //封装成对象
        LoginVo<UserInfoVo> loginVo = new LoginVo<>();

        loginVo.setInfoVo(BeanCopyUtil.copyBean(user, UserInfoVo.class));
        loginVo.setToken(token);

        return loginVo;
    }

    @Override
    public void verifyCode(HttpServletResponse response, UserLoginDto userLoginDto) throws Exception {

        CircleCaptcha captcha = CaptchaUtil.createCircleCaptcha(50, 25, 4, 20);

        //1.缓存验证码
        stringRedisTemplate.opsForValue().set(
                LoginConstants.LOGIN_CODE + userLoginDto.getEmail(),
                captcha.getCode(),
                60,
                TimeUnit.SECONDS
        );

        //2.将图像写出
        try (
                ServletOutputStream outputStream = response.getOutputStream()
        ) {
            captcha.write(outputStream);
        }

    }
    
    @Override
    public Map<String, String> getQrCode() {
        // 生成唯一的二维码内容（token）
        String token = QrCodeUtils.generateQrCodeContent();
        
        // 将二维码状态存入Redis，设置5分钟过期时间
        stringRedisTemplate.opsForValue().set(
                LoginConstants.QR_CODE_LOGIN + token,
                String.valueOf(NOT_SCANNED.getCode()),
                5,
                TimeUnit.MINUTES
        );
        
        // 返回token和二维码内容
        Map<String, String> result = new HashMap<>();
        result.put("content", token);
        return result;
    }
    
    @Override
    public void confirmQrLogin(String token, String userId) {
        // 验证用户状态是否正常
        User user = getUserById(userId);
        if (user == null) {
            throw new SystemException(RestResultEnum.USER_NOT_FOUND);
        }
        
        // 检查用户是否被封禁
        if ("1".equals(user.getStatus())) {
            throw new BusinessException("账号已被封禁，无法登录");
        }
        
        // 使用分布式锁防止并发操作同一个二维码
        String lockKey = LoginConstants.QR_CODE_LOCK + token;
        try {
            // 尝试获取锁，超时时间3秒，锁过期时间10秒
            Boolean locked = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, userId, 10, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(locked)) {
                throw new BusinessException("操作过于频繁，请稍后再试");
            }
            
            String key = LoginConstants.QR_CODE_LOGIN + token;
            Boolean updated = stringRedisTemplate.execute(new RedisCallback<Boolean>() {
                @Override
                public Boolean doInRedis(RedisConnection connection) {
                    connection.watch(key.getBytes());
                    String status = stringRedisTemplate.opsForValue().get(key);
                    if (status == null) {
                        throw new SystemException(RestResultEnum.QR_CODE_NOT_EXIST);
                    }
                    
                    // 允许未扫描或已扫描状态进行确认登录
                    if (String.valueOf(NOT_SCANNED.getCode()).equals(status) ||
                        String.valueOf(QrCodeLoginStatus.SCANNED.getCode()).equals(status)) {
                        connection.multi();
                        connection.setEx(key.getBytes(), 300,
                                (SUCCESS.getCode() + "-" + userId).getBytes());
                        return connection.exec().size() > 0;
                    } else if (status.contains("-")) {
                        throw new SystemException(RestResultEnum.QR_CODE_ALREADY_CONFIRMED);
                    } else {
                        throw new SystemException(RestResultEnum.QR_CODE_EXPIRED);
                    }
                }
            });
            if (!Boolean.TRUE.equals(updated)) {
                throw new BusinessException("二维码状态修改异常");
            }
        } finally {
            // 释放锁
            stringRedisTemplate.delete(lockKey);
        }
    }
    
    @Override
    public LoginVo<UserInfoVo> checkQrLoginStatus(String token) {
        // 使用分布式锁防止并发检查
        String lockKey = LoginConstants.QR_CODE_CHECK_LOCK + token;
        try {
            // 尝试获取锁，超时时间2秒，锁过期时间5秒
            Boolean locked = stringRedisTemplate.opsForValue().setIfAbsent(lockKey, "1", 5, TimeUnit.SECONDS);
            if (Boolean.FALSE.equals(locked)) {
                // 如果获取锁失败，等待一小段时间后继续处理，避免立即抛出异常
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            
            // 获取二维码状态
            String value = stringRedisTemplate.opsForValue().get(LoginConstants.QR_CODE_LOGIN + token);
            // 如果二维码不存在或已过期
            if (value == null) {
                throw new SystemException(RestResultEnum.QR_CODE_NOT_EXIST);
            }
            
            // 如果状态包含分隔符，说明已经确认登录
            if (value.contains("-")) {
                String[] parts = value.split("-");
                String status = parts[0];
                String userId = parts[1];
                
                // 如果状态为已确认登录
                if (status.equals(String.valueOf(QrCodeLoginStatus.SUCCESS.getCode()))) {
                    // 原子操作删除二维码状态，防止重复使用
                    stringRedisTemplate.delete(LoginConstants.QR_CODE_LOGIN + token);
                    
                    // 验证用户状态是否正常
                    User user = getUserById(userId);
                    if (user == null) {
                        throw new SystemException(RestResultEnum.USER_NOT_FOUND);
                    }
                    
                    // 检查用户是否被封禁
                    if ("1".equals(user.getStatus())) {
                        throw new BusinessException("账号已被封禁，无法登录");
                    }
                    
                    // 创建JWT token
                    String jwtToken = JwtUtils.createToken(LoginConstants.USER_ID, userId, 1);
                    
                    // 缓存用户信息，设置24小时过期时间
                    stringRedisTemplate.opsForValue().set(
                            LoginConstants.USER_LOGIN + user.getId(),
                            JSONUtil.toJsonStr(new LoginUserDetails(user)),
                            24,
                            TimeUnit.HOURS
                    );
                    
                    // 构建返回对象
                    LoginVo<UserInfoVo> loginVo = new LoginVo<>();
                    loginVo.setToken(jwtToken);
                    loginVo.setInfoVo(BeanCopyUtil.copyBean(user, UserInfoVo.class));
                    
                    return loginVo;
                }
            }
            throw new SystemException(RestResultEnum.QR_CODE_NOT_SCANNED);
        } finally {
            // 释放锁
            stringRedisTemplate.delete(lockKey);
        }
    }
    
    /**
     * 根据用户ID获取用户信息
     * 增加缓存穿透防护和合理的缓存过期时间
     */
    private User getUserById(String userId) {
        // 使用专门的用户缓存前缀
        String cacheKey = LoginConstants.USER_LOGIN + userId;
        
        try {
            // 从Redis中获取用户信息
            String userJson = stringRedisTemplate.opsForValue().get(cacheKey);
            
            // 如果缓存中有数据，直接返回
            if (userJson != null) {
                // 刷新缓存过期时间
                stringRedisTemplate.expire(cacheKey, 24, TimeUnit.HOURS);
                LoginUserDetails userDetails = JSONUtil.toBean(userJson, LoginUserDetails.class);
                return userDetails.getUser();
            }

            Long userIdLong;
            try {
                userIdLong = Long.parseLong(userId);
            } catch (NumberFormatException e) {
                return null;
            }

            // 从数据库查询用户
            User user = userMapper.selectById(userIdLong);

            // 如果用户不存在，设置空值缓存防止缓存穿透
            if (user == null) {
                String nullCacheKey = LoginConstants.NULL_USER_CACHE + userId;
                stringRedisTemplate.opsForValue().set(nullCacheKey, "1", 5, TimeUnit.MINUTES);
                return null;
            }

            // 如果用户存在，将用户信息存入Redis缓存
            LoginUserDetails userDetails = new LoginUserDetails(user);
            try {
                stringRedisTemplate.opsForValue().set(
                    cacheKey,
                    JSONUtil.toJsonStr(userDetails),
                    24,
                    TimeUnit.HOURS
                );
            } catch (Exception e) {
                // 缓存更新失败不影响返回结果
            }
            return user;
            
        } catch (Exception e) {
            return null;
        }
    }
}
