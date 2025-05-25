package top.sboxm.file.pojo.vo;

import lombok.Data;
import top.sboxm.file.pojo.po.FileShare;

import java.sql.Timestamp;

/**
 * 文件分享VO，用于返回分享的详细信息
 */
@Data
public class FileShareVo {
    /**
     * 分享ID
     */
    private String id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 文件ID
     */
    private String fileId;

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
     * 分享状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 从FileShare实体转换为VO对象
     */
    public static FileShareVo fromFileShare(FileShare fileShare) {
        if (fileShare == null) {
            return null;
        }
        FileShareVo vo = new FileShareVo();
        vo.setId(String.valueOf(fileShare.getId()));
        vo.setUserId(fileShare.getUserId());
        vo.setNickName(fileShare.getNickName());
        vo.setFileName(fileShare.getFileName());
        vo.setFileSize(fileShare.getFileSize());
        vo.setNeedPassword(fileShare.getNeedPassword());
        vo.setStatus(fileShare.getStatus());
        vo.setCreateTime(fileShare.getCreateTime());
        vo.setUpdateTime(fileShare.getUpdateTime());
        return vo;
    }
}