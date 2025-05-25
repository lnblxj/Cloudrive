package top.sboxm.auth.service.impl;

import cn.hutool.json.JSONUtil;
import top.sboxm.auth.pojo.dto.UserLoginDto;
import top.sboxm.auth.pojo.po.LoginUserDetails;
import top.sboxm.auth.pojo.po.User;
import top.sboxm.auth.pojo.vo.LoginVo;
import top.sboxm.auth.pojo.vo.ManagerInfoVo;
import top.sboxm.auth.service.ManagerLoginService;
import top.sboxm.common.utils.BeanCopyUtil;
import top.sboxm.common.utils.JwtUtils;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ManagerLoginServiceImpl implements ManagerLoginService {

    @Resource
    AuthenticationManager authenticationManager;

    @Resource
    StringRedisTemplate stringRedisTemplate;


    @Override
    public LoginVo<ManagerInfoVo> login(UserLoginDto userLoginDto) {

        String account = userLoginDto.getEmail();
        String credential = userLoginDto.getPassword();

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(account, credential);

        Authentication result = authenticationManager.authenticate(token);

        if (Objects.isNull(result)){
            throw new RuntimeException("邮箱或者密码错误");
        }
        LoginUserDetails userDetails = (LoginUserDetails) result.getPrincipal();
        User user = userDetails.getUser();

        if (!"1".equals(user.getRole())){
            throw new RuntimeException("非法用户无法使用本系统");
        }

        stringRedisTemplate.opsForValue().set("login:"+user.getId(), JSONUtil.toJsonStr(user));

        String jwt = JwtUtils.createToken("userId", user.getId(), 1);

        LoginVo<ManagerInfoVo> managerInfoVoLoginVo = new LoginVo<>();

        managerInfoVoLoginVo.setToken(jwt)
                .setInfoVo(BeanCopyUtil.copyBean(user, ManagerInfoVo.class));

        return managerInfoVoLoginVo;
    }
}
