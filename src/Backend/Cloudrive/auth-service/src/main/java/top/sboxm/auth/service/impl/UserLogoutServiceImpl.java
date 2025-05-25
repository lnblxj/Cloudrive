package top.sboxm.auth.service.impl;

import cn.hutool.json.JSONUtil;
import top.sboxm.auth.constants.LoginConstants;
import top.sboxm.auth.exception.SystemException;
import top.sboxm.auth.pojo.po.LoginUserDetails;
import top.sboxm.auth.service.UserLogoutService;
import top.sboxm.common.enums.RestResultEnum;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserLogoutServiceImpl implements UserLogoutService {

    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void logout(String userId) {
        //从redis中获取用户信息
        String loginUserDetailsJson = stringRedisTemplate.opsForValue().get(LoginConstants.USER_LOGIN + userId);
        if (!StringUtils.hasText(loginUserDetailsJson)){
            throw new SystemException(RestResultEnum.NEED_LOGIN);
        }
//
//        LoginUserDetails loginUserDetails = JSONUtil.toBean(loginUserDetailsJson, LoginUserDetails.class);
        stringRedisTemplate.delete(LoginConstants.USER_LOGIN+userId);
    }
}
