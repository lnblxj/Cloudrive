package top.sboxm.file.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import cn.hutool.core.util.IdUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.exception.BusinessException;
import top.sboxm.file.config.MinIOConfig;
import top.sboxm.file.exception.FileShareException;
import top.sboxm.file.mapper.FileShareMapper;
import top.sboxm.file.pojo.po.FileDocument;
import top.sboxm.file.pojo.po.FileShare;
import top.sboxm.file.pojo.vo.FileShareInfoVo;
import top.sboxm.file.service.FileService;
import top.sboxm.file.service.FileShareService;
import top.sboxm.file.utils.MinIOUtils;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class FileShareServiceImpl implements FileShareService {

    @Resource
    private FileShareMapper fileShareMapper;

    @Resource
    private FileService fileService;
    @Resource
    @Qualifier("fileMinIOClient")
    private MinIOUtils fileClient;
    @Resource
    private MinIOConfig minIOConfig;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileShare createShare(Long userId, String nickName, String fileId, Boolean needPassword, String password) {

        // 验证文件是否存在且属于当前用户
        FileDocument fileDoc;
        try {
            fileDoc = fileService.getFileById(fileId);
            if (!fileDoc.getUserId().equals(String.valueOf(userId))) {
                throw new BusinessException("无权分享此文件");
            }
        } catch (Exception e) {
            throw new BusinessException("文件不存在");
        }

        // 创建分享记录
        FileShare fileShare = new FileShare()
                .setId(IdUtil.getSnowflake().nextId()) // 使用雪花算法生成唯一ID
                .setUserId(userId)
                .setNickName(nickName)
                .setFileName(fileDoc.getFileName())
                .setFileSize(fileDoc.getFileSize())
                .setNeedPassword(needPassword)
                .setMinioObjectName(fileDoc.getMinioObjectName())
                .setCreateBy(userId)
                .setCreateTime(new Timestamp(System.currentTimeMillis()))
                .setUpdateBy(userId)
                .setUpdateTime(new Timestamp(System.currentTimeMillis()))
                .setStatus(1) // 默认状态为正常
                .setDelFlag(0); // 设置删除标志为0（未删除）

        // 如果需要密码且未提供，则生成随机密码
        if (Boolean.TRUE.equals(needPassword)) {
            if (!StringUtils.hasText(password)) {
                password = UUID.randomUUID().toString().substring(0, 6);
            }
            fileShare.setPassword(password);
        }

        // 保存分享记录
        fileShareMapper.insert(fileShare);
        log.info("用户{}创建了文件{}的分享，分享ID：{}", userId, fileId, fileShare.getId());
        return fileShare;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteShare(Long shareId, Long userId) {
        // 验证分享是否存在且属于当前用户
        FileShare fileShare = validateShareOwnership(shareId, userId);

        // 删除分享记录
        fileShareMapper.deleteById(shareId);
        log.info("用户{}删除了分享{}", userId, shareId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileShare updateShareStatus(Long shareId, Integer status, Long userId) {
        // 验证分享是否存在且属于当前用户
        FileShare fileShare = validateShareOwnership(shareId, userId);

        // 更新状态
        fileShare.setStatus(status)
                .setUpdateBy(userId)
                .setUpdateTime(new Timestamp(System.currentTimeMillis()));
        fileShareMapper.updateById(fileShare);

        log.info("用户{}更新了分享{}的状态为{}", userId, shareId, status);
        return fileShare;
    }

    @Override
    public FileShareInfoVo getShareInfo(Long shareId) {
        FileShare fileShare = fileShareMapper.selectById(shareId);
        if (fileShare == null) {
            throw new BusinessException("分享不存在");
        }
        if (fileShare.getStatus() != 1) {
            throw new BusinessException("分享已失效");
        }
        
        FileShareInfoVo vo = new FileShareInfoVo();
        vo.setId(String.valueOf(fileShare.getId()));
        vo.setNickName(fileShare.getNickName());
        vo.setFileName(fileShare.getFileName());
        vo.setFileSize(fileShare.getFileSize());
        vo.setNeedPassword(fileShare.getNeedPassword());
        vo.setUpdateTime(fileShare.getUpdateTime());
        return vo;
    }

    @Override
    public String verifyPasswordAndGetDownloadUrl(Long shareId, String password) {
        // 从数据库获取分享信息
        FileShare fileShare = fileShareMapper.selectById(shareId);
        if (fileShare == null) {
            throw new FileShareException(RestResultEnum.FILE_SHARE_NOT_FOUND);
        }
        if (fileShare.getStatus() != 1) {
            throw new FileShareException(RestResultEnum.FILE_SHARE_EXPIRED);
        }

        // 验证密码
        if (Boolean.TRUE.equals(fileShare.getNeedPassword())) {
            if (!StringUtils.hasText(password)) {
                throw new FileShareException("请输入提取密码");
            }
            if (!Objects.equals(fileShare.getPassword(), password)) {
                throw new FileShareException(RestResultEnum.FILE_SHARE_PASSWORD_ERROR);
            }
        }

        try {
            return MinIOUtils.getPresignedObjectUrlWithFileName(
                    minIOConfig.getFileBucketName(),
                    fileShare.getMinioObjectName(),
                    fileShare.getFileName(),
                    60 * 60
            );
        } catch (Exception e) {
            log.error("生成文件下载链接失败", e);
            throw new BusinessException("生成下载链接失败");
        }
    }

    /**
     * 验证分享是否存在且属于当前用户
     */
    private FileShare validateShareOwnership(Long shareId, Long userId) {
        FileShare fileShare = fileShareMapper.selectOne(
                new LambdaQueryWrapper<FileShare>()
                        .eq(FileShare::getId, shareId)
                        .eq(FileShare::getUserId, userId)
        );

        if (fileShare == null) {
            throw new BusinessException("分享不存在或无权限操作");
        }
        return fileShare;
    }
}
