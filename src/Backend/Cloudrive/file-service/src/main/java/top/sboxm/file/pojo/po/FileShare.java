package top.sboxm.file.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 文件分享实体类，映射数据库表cd_file_share。
 * 存储文件分享的基本信息、状态及操作审计信息。
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cd_file_share")
public class FileShare implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 是否需要提取密码：0=不需要 1=需要
     */
    private Boolean needPassword;

    /**
     * 提取密码
     */
    private String password;

    /**
     * Minio对象名称
     */
    private String minioObjectName;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 状态：0=审核中 1=正常 2=下架
     */
    private Integer status;

    /**
     * 是否删除0=否
     */
    private Integer delFlag;

}
