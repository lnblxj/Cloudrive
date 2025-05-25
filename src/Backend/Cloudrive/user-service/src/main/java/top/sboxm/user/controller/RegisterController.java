package top.sboxm.user.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.result.RestResult;
import top.sboxm.user.pojo.dto.RegisterDTO;
import top.sboxm.user.service.UserService;

/**
 * 用户注册相关接口
 */
@RestController
@RequestMapping("/register")
public class RegisterController {
    @Resource
    private UserService userService;
    
    /**
     * 发送注册验证码
     * @param email 用户邮箱
     * @return 发送结果
     */
    @PostMapping("/code/{email}")
    public Object sendRegisterCode(@PathVariable String email) {
        boolean success = userService.sendRegisterCode(email);
        return success ? RestResult.ok() : RestResult.fail();
    }
    
    /**
     * 检查昵称是否已存在
     * @param nickname 用户昵称
     * @return 检查结果
     */
    @GetMapping("/check/{nickname}")
    public Object checkNickname(@PathVariable String nickname) {
        boolean exists = userService.checkNicknameExists(nickname);
        return exists ? 
                RestResult.fail(RestResultEnum.USERNAME_EXIST) : 
                RestResult.ok();
    }
    
    /**
     * 用户注册
     * @param registerDTO 注册信息DTO
     * @return 注册结果
     */
    @PostMapping
    public Object register(@RequestBody RegisterDTO registerDTO) {
        boolean success = userService.register(registerDTO);
        return success ? RestResult.ok() : RestResult.fail(RestResultEnum.OPERATION_FAILED);
    }
}
