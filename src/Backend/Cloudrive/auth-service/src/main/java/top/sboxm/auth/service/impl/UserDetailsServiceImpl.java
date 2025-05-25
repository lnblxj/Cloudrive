package top.sboxm.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import top.sboxm.auth.exception.SystemException;
import top.sboxm.auth.mapper.UserMapper;
import top.sboxm.auth.pojo.po.LoginUserDetails;
import top.sboxm.auth.pojo.po.User;
import jakarta.annotation.Resource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import top.sboxm.common.enums.RestResultEnum;

import java.util.Objects;

/**
 * 用于从数据源加载用户信息
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //从数据库中，根据用户名查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getEmail, email);

        User user = userMapper.selectOne(wrapper);

        if (Objects.isNull(user)) {
            throw new SystemException(RestResultEnum.LOGIN_ERROR);
        }

        //补充完整后返回
        LoginUserDetails loginUserDetails = new LoginUserDetails();

        return loginUserDetails.setUser(user);
    }
}
