package top.sboxm.file.pojo.dto;

import lombok.Data;

@Data
public class VerifySharePasswordDto {
    /**
     * 分享ID
     */
    private String shareId;

    /**
     * 提取密码
     */
    private String password;
}