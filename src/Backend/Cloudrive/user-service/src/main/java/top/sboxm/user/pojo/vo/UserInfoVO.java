package top.sboxm.user.pojo.vo;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class UserInfoVO {
    /**
     * 邮箱
     */
    private String email;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 状态：0=正常1=封禁
     */
    private String status;

    /**
     * 创建时间
     */
    private Timestamp createTime;
}