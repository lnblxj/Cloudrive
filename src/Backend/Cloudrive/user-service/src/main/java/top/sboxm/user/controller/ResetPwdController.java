package top.sboxm.user.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.sboxm.common.result.RestResult;
import top.sboxm.user.pojo.dto.ResetPwdDTO;
import top.sboxm.user.service.UserService;

@RestController
@RequestMapping("/reset")
public class ResetPwdController {
    @Resource
    private UserService userService;

    /**
     * 发送密码重置验证码
     * @param email 用户邮箱
     * @return 发送结果
     */
    @PostMapping("/code/{email}")
    public Object sendResetCode(@PathVariable String email) {
        boolean success = userService.sendResetPwdCode(email);
        return success ? RestResult.ok() : RestResult.fail();
    }

    /**
     * 重置密码
     * @param resetPwdDTO 重置密码DTO
     * @return 重置结果
     */
    @PostMapping("/password")
    public Object resetPassword(@RequestBody ResetPwdDTO resetPwdDTO) {
        boolean success = userService.resetPassword(resetPwdDTO);
        return success ? RestResult.ok() : RestResult.fail();
    }
}
