package top.sboxm.auth.controller;

import top.sboxm.auth.exception.SystemException;
import top.sboxm.auth.pojo.dto.UserLoginDto;
import top.sboxm.auth.service.UserLoginService;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.result.RestResult;
import top.sboxm.common.utils.SecurityContextUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/*登陆api*/
@RestController
@RequestMapping("/auth")
public class UserLoginController {


    @Resource
    UserLoginService userLoginService;

    @PostMapping("/login")
    public Object login(@RequestBody UserLoginDto userLoginDto){

        String email = userLoginDto.getEmail();
        if (!StringUtils.hasText(email)) {
            throw new SystemException(RestResultEnum.LOGIN_ERROR);
        }

        String password = userLoginDto.getPassword();
        if (!StringUtils.hasText(password)){
            throw new SystemException(RestResultEnum.LOGIN_ERROR);
        }
        return RestResult.ok().setData(userLoginService.login(userLoginDto));
    }

    @PostMapping("/verifyCode")
    public void verifyCode(HttpServletResponse response,@RequestBody UserLoginDto userLoginDto)throws Exception{

        userLoginService.verifyCode(response,userLoginDto);
    }
    
    /**
     * 获取登录二维码
     * @return 包含token和二维码内容的Map
     */
    @GetMapping("/qrcode")
    public Object getQrCode() {
        Map<String, String> qrCodeInfo = userLoginService.getQrCode();
        return RestResult.ok().setData(qrCodeInfo);
    }
    
    /**
     * 确认扫码登录
     * @param params 包含token的请求体
     */
    @PostMapping("/qrcode/confirm")
    public Object confirmQrLogin(@RequestBody Map<String, String> params) {
        // 获取当前登录用户ID
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            throw new SystemException(RestResultEnum.NEED_LOGIN);
        }
        
        String token = params.get("token");
        if (!StringUtils.hasText(token)) {
            throw new SystemException(RestResultEnum.INVALID_PARAM);
        }
        
        userLoginService.confirmQrLogin(token, userId);
        return RestResult.ok();
    }
    
    /**
     * 检查扫码登录状态
     * @param token 二维码token
     * @return 登录成功返回用户登录信息，否则返回null
     */
    @GetMapping("/qrcode/status")
    public Object checkQrLoginStatus(@RequestParam String token) {
        return RestResult.ok().setData(userLoginService.checkQrLoginStatus(token));
    }
}
