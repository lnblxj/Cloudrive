package top.sboxm.user.pojo.dto;

import lombok.Data;

/**
 * 密码重置相关DTO
 */
@Data
public class ResetPwdDTO {
    /**
     * 邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 新密码
     */
    private String newPassword;
}