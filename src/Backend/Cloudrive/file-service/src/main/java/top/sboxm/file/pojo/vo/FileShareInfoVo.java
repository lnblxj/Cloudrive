package top.sboxm.file.pojo.vo;

import lombok.Data;
import java.sql.Timestamp;

/**
 * 文件分享信息VO，用于返回分享的基本信息
 */
@Data
public class FileShareInfoVo {
    /**
     * 分享ID
     */
    private String id;

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
     * 是否需要提取密码
     */
    private Boolean needPassword;

    /**
     * 更新时间
     */
    private Timestamp updateTime;
}