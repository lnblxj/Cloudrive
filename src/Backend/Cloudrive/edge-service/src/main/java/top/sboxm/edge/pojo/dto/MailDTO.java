package top.sboxm.edge.pojo.dto;

import lombok.Data;

@Data
public class MailDTO {
    /**
     * 目标邮件地址
     */
    private String toEmail;

    /**
     * 验证码
     */
    private String verificationCode;
}