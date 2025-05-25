package top.sboxm.auth.service;


import top.sboxm.auth.pojo.dto.UserLoginDto;
import top.sboxm.auth.pojo.vo.LoginVo;
import top.sboxm.auth.pojo.vo.UserInfoVo;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface UserLoginService {


    LoginVo<UserInfoVo> login(UserLoginDto userLoginDto);

    void verifyCode(HttpServletResponse response, UserLoginDto userLoginDto)throws Exception;
    
    /**
     * 获取登录二维码
     * @return 包含token和二维码内容的Map
     */
    Map<String, String> getQrCode();
    
    /**
     * 确认扫码登录
     * @param token 二维码token
     * @param userId 用户ID
     */
    void confirmQrLogin(String token, String userId);
    
    /**
     * 检查扫码登录状态
     * @param token 二维码token
     * @return 登录成功返回用户登录信息，否则返回null
     */
    LoginVo<UserInfoVo> checkQrLoginStatus(String token);
}
