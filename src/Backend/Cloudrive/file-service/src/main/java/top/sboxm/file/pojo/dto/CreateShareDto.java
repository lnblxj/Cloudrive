package top.sboxm.file.pojo.dto;

import lombok.Data;

@Data
public class CreateShareDto {
    /**
     * 文件ID
     */
    private String fileId;
    
    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 是否需要提取密码
     */
    private Boolean needPassword;

    /**
     * 提取密码，当needPassword为true时必填
     */
    private String password;
}