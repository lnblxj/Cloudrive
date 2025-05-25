package top.sboxm.user.pojo.dto;

import lombok.Data;

/**
 * 更新密码的数据传输对象
 */
@Data
public class UpdatePasswordDTO {
    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;
}