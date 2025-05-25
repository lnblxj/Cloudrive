package top.sboxm.file.service;

import top.sboxm.file.pojo.po.FileShare;
import top.sboxm.file.pojo.vo.FileShareInfoVo;

public interface FileShareService {
    /**
     * 创建文件分享
     * @param userId 用户ID
     * @param nickName 用户昵称
     * @param fileId 文件ID
     * @param needPassword 是否需要提取密码
     * @param password 提取密码
     * @return 文件分享信息
     */
    FileShare createShare(Long userId, String nickName, String fileId, Boolean needPassword, String password);

    /**
     * 删除文件分享
     * @param shareId 分享ID
     * @param userId 用户ID
     */
    void deleteShare(Long shareId, Long userId);

    /**
     * 修改文件分享状态
     * @param shareId 分享ID
     * @param status 状态
     * @param userId 用户ID
     * @return 更新后的文件分享信息
     */
    FileShare updateShareStatus(Long shareId, Integer status, Long userId);

    /**
     * 获取分享文件信息
     * @param shareId 分享ID
     * @return 文件分享信息
     */
    FileShareInfoVo getShareInfo(Long shareId);

    /**
     * 验证分享密码并获取下载地址
     * @param shareId 分享ID
     * @param password 提取密码
     * @return 预签名下载地址
     */
    String verifyPasswordAndGetDownloadUrl(Long shareId, String password);
}
