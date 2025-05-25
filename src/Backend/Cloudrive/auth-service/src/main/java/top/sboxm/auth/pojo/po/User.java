package top.sboxm.auth.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cd_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 密码
     */
    private String password;

    /**
     * 类型：0=普通用户1=管理员
     */
    private String role;

    /**
     * 状态：0=正常1=封禁
     */
    private String status;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 创建人
     */
    private Long createBy;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 更新人
     */
    private Long updateBy;

    /**
     * 0=未删除1=已删除
     */
    private Integer delFlag;


}
