package top.sboxm.file.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.sql.Timestamp;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 图床实体类，映射数据库表cd_image_bed。
 * 存储图片上传的基本信息及操作审计信息。
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cd_image_bed")
public class ImageBed implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * MinIO对象名称
     */
    private String minioObjectName;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 是否删除（0=未删除，1=已删除）
     */
    private Integer delFlag;

}