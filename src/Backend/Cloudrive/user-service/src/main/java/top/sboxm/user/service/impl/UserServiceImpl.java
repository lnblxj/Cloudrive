package top.sboxm.user.service.impl;
 
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.result.RestResult;
import top.sboxm.user.constants.RedisConstants;
import top.sboxm.user.exceptions.UserException;
import top.sboxm.user.mapper.CapacityInfoMapper;
import top.sboxm.user.mapper.UserMapper;
import top.sboxm.user.pojo.dto.MailDTO;
import top.sboxm.user.pojo.dto.RegisterDTO;
import top.sboxm.user.pojo.dto.ResetPwdDTO;
import top.sboxm.user.pojo.po.CapacityInfo;
import top.sboxm.user.pojo.vo.ImageBedVO;
import top.sboxm.user.pojo.vo.UserInfoVO;
import top.sboxm.user.pojo.po.User;
import top.sboxm.user.remote.EdgeClient;
import top.sboxm.user.remote.FileClient;
import top.sboxm.user.service.UserService;
import cn.hutool.core.util.IdUtil;

import java.sql.Timestamp;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private FileClient fileClient;

    @Resource
    private EdgeClient edgeClient;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private CapacityInfoMapper capacityInfoMapper;

    @Resource
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserInfoVO getUserInfo(Long userId) {
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }

        // 转换为VO
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setEmail(user.getEmail());
        userInfoVO.setNickname(user.getNickname());
        userInfoVO.setAvatar(user.getAvatar());
        userInfoVO.setStatus(user.getStatus());
        userInfoVO.setCreateTime(user.getCreateTime());

        return userInfoVO;
    }

    @Override
    public boolean updateNickname(Long userId, String nickname) {
        if (userId == null || nickname == null || nickname.trim().isEmpty()) {
            return false;
        }

        User user = new User();
        user.setId(userId);
        user.setNickname(nickname.trim());
        user.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        return userMapper.updateById(user) > 0;
    }

    @Override
    public boolean updateAvatar(Long userId, MultipartFile file) {
        if (userId == null || file == null || file.isEmpty()) {
            return false;
        }

        try {
            // 上传图片到图床
            RestResult<ImageBedVO> result = fileClient.uploadImageInternal(file, userId.toString());
            String avatarUrl = result.getData().getImageUrl();
            
            // 更新用户头像
            User user = new User();
            user.setId(userId);
            user.setAvatar(avatarUrl);
            user.setUpdateTime(new Timestamp(System.currentTimeMillis()));

            try {
                return userMapper.updateById(user) > 0;
            } catch (Exception e) {
                throw new UserException(RestResultEnum.SYSTEM_ERROR);
            }
        } catch (Exception e) {
            throw new UserException(RestResultEnum.UPLOAD_ERROR);
        }
    }

    @Override
    public boolean sendResetPwdCode(String email) {
        // 查询用户是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new UserException(RestResultEnum.USER_NOT_FOUND);
        }

        // 生成6位数字验证码
        String verifyCode = String.format("%06d", new Random().nextInt(1000000));
        stringRedisTemplate.opsForValue().set(
                RedisConstants.RESET_PWD_CODE + email,
                verifyCode,
                RedisConstants.RESET_PWD_CODE_TTL,
                TimeUnit.MINUTES
        );

        // 发送邮件
        MailDTO mailDTO = new MailDTO();
        mailDTO.setToEmail(email);
        mailDTO.setVerificationCode(verifyCode);
        try {
            edgeClient.sendMail(mailDTO);
            return true;
        } catch (Exception e) {
            throw new UserException(RestResultEnum.SEND_MAIL_ERROR);
        }
    }

    @Override
    public boolean resetPassword(ResetPwdDTO resetPwdDTO) {
        // 获取Redis中的验证码
        String storedCode = stringRedisTemplate.opsForValue().get(RedisConstants.RESET_PWD_CODE + resetPwdDTO.getEmail());
        if (storedCode == null) {
            throw new UserException(RestResultEnum.VERIFY_CODE_EXPIRATION);
        }
        if (!storedCode.equals(resetPwdDTO.getVerifyCode())) {
            throw new UserException(RestResultEnum.VERIFY_CODE);
        }

        // 更新密码
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, resetPwdDTO.getEmail());
        User user = new User();
        user.setPassword(passwordEncoder.encode(resetPwdDTO.getNewPassword()));
        user.setUpdateTime(new Timestamp(System.currentTimeMillis()));

        try {
            // 更新密码
            int result = userMapper.update(user, queryWrapper);
            if (result > 0) {
                // 清除验证码
                stringRedisTemplate.delete(RedisConstants.RESET_PWD_CODE + resetPwdDTO.getEmail());
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new UserException(RestResultEnum.SYSTEM_ERROR);
        }
    }
    
    @Override
    public boolean sendRegisterCode(String email) {
        // 检查邮箱是否已注册
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            throw new UserException(RestResultEnum.EMAIL_EXIST);
        }

        // 生成6位数字验证码
        String verifyCode = String.format("%06d", new Random().nextInt(1000000));

        // 存入Redis
        stringRedisTemplate.opsForValue().set(
                RedisConstants.REGISTER_CODE + email,
                verifyCode,
                RedisConstants.REGISTER_CODE_TTL,
                TimeUnit.MINUTES
        );

        // 发送邮件
        MailDTO mailDTO = new MailDTO();
        mailDTO.setToEmail(email);
        mailDTO.setVerificationCode(verifyCode);
        try {
            edgeClient.sendMail(mailDTO);
            return true;
        } catch (Exception e) {
            throw new UserException(RestResultEnum.SEND_MAIL_ERROR);
        }
    }
    
    @Override
    public boolean checkNicknameExists(String nickname) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickname, nickname);
        return userMapper.selectCount(queryWrapper) > 0;
    }
    
    @Override
    public boolean register(RegisterDTO registerDTO) {
        // 获取Redis中的验证码
        String storedCode = stringRedisTemplate.opsForValue().get(RedisConstants.REGISTER_CODE + registerDTO.getEmail());
        if (storedCode == null) {
            throw new UserException(RestResultEnum.VERIFY_CODE_EXPIRATION);
        }

        // 验证码比对
        if (!storedCode.equals(registerDTO.getVerifyCode())) {
            throw new UserException(RestResultEnum.VERIFY_CODE);
        }
        
        // 检查邮箱是否已注册
        LambdaQueryWrapper<User> emailQueryWrapper = new LambdaQueryWrapper<>();
        emailQueryWrapper.eq(User::getEmail, registerDTO.getEmail());
        if (userMapper.selectCount(emailQueryWrapper) > 0) {
            throw new UserException(RestResultEnum.EMAIL_EXIST);
        }
        
        // 检查昵称是否已存在
        LambdaQueryWrapper<User> nicknameQueryWrapper = new LambdaQueryWrapper<>();
        nicknameQueryWrapper.eq(User::getNickname, registerDTO.getNickname());
        if (userMapper.selectCount(nicknameQueryWrapper) > 0) {
            throw new UserException(RestResultEnum.USERNAME_EXIST);
        }
        
        // 创建用户
        User user = new User();
        user.setId(IdUtil.getSnowflake().nextId());
        user.setEmail(registerDTO.getEmail());
        user.setNickname(registerDTO.getNickname());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setAvatar("http://cdnjson.com/images/2025/04/16/logo-light.png");
        user.setRole("0");
        user.setStatus("0");
        user.setDelFlag(0);
        
        try {
            // 保存用户
            int result = userMapper.insert(user);
            if (result > 0) {
                // 清除验证码
                stringRedisTemplate.delete(RedisConstants.REGISTER_CODE + registerDTO.getEmail());
                
                // 创建容量记录
                CapacityInfo capacityInfo = new CapacityInfo();
                capacityInfo.setUserId(user.getId());
                capacityInfo.setTotalCapacity(10737418240L); // 10GB
                capacityInfo.setUsedCapacity(0L);
                capacityInfoMapper.insert(capacityInfo);
                
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException(RestResultEnum.SYSTEM_ERROR);
        }
    }
    
    @Override
    public boolean updatePassword(Long userId, String oldPassword, String newPassword) {
        if (userId == null || oldPassword == null || newPassword == null) {
            return false;
        }
        
        // 查询用户信息
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new UserException(RestResultEnum.USER_NOT_FOUND);
        }
        
        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new UserException(RestResultEnum.OLD_PASSWORD_ERROR);
        }
        
        // 更新密码
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPassword(passwordEncoder.encode(newPassword));
        updateUser.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        
        try {
            return userMapper.updateById(updateUser) > 0;
        } catch (Exception e) {
            throw new UserException(RestResultEnum.SYSTEM_ERROR);
        }
    }
}
