package top.sboxm.auth.controller;

import top.sboxm.auth.exception.SystemException;
import top.sboxm.auth.service.UserLogoutService;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.result.RestResult;
import top.sboxm.common.utils.SecurityContextUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class UserLogoutController {

    @Resource
    UserLogoutService userLogoutService;


    @PostMapping("/logout")
    public Object logout(){
        String userId = SecurityContextUtil.getUserId();
        System.out.println(StringUtils.hasText(userId));
        if (!StringUtils.hasText(userId)){
            throw new SystemException(RestResultEnum.NEED_LOGIN);
        }
        userLogoutService.logout(userId);

        return RestResult.ok();
    }
}
