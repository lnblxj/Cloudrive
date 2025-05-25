package top.sboxm.user.pojo.dto;

import lombok.Data;

/**
 * 用户注册相关DTO
 */
@Data
public class RegisterDTO {
    /**
     * 邮箱
     */
    private String email;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;
}