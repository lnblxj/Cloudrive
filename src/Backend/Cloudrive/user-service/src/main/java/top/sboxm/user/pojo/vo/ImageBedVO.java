package top.sboxm.user.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * 图床信息响应VO类
 */
@Data
@Accessors(chain = true)
public class ImageBedVO {
    /**
     * 图片ID
     */
    private String id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 图片访问URL
     */
    private String imageUrl;

    /**
     * 创建时间
     */
    private Timestamp createTime;
}