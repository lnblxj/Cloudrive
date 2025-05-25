package top.sboxm.file.pojo.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import top.sboxm.file.pojo.po.ImageBed;

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

    /**
     * 将ImageBed实体转换为VO对象
     */
    public static ImageBedVO fromImageBed(ImageBed imageBed, String fileHost, String bucketName) {
        if (imageBed == null) {
            return null;
        }
        return new ImageBedVO()
                .setId(String.valueOf(imageBed.getId()))
                .setUserId(imageBed.getUserId())
                .setImageUrl(fileHost + "/" + bucketName + "/" + imageBed.getMinioObjectName())
                .setCreateTime(imageBed.getCreateTime());
    }
}