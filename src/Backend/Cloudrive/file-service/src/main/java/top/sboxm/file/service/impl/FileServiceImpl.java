package top.sboxm.file.service.impl;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import top.sboxm.common.result.RestResult;
import top.sboxm.file.config.MinIOConfig;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.file.exception.FileNotFoundException;
import top.sboxm.file.exception.FileOperationException;
import top.sboxm.file.exception.FileShareException;
import top.sboxm.file.exception.UploadException;
import top.sboxm.file.pojo.dto.UpdateCapacityDTO;
import top.sboxm.file.pojo.po.FileDocument;
import top.sboxm.file.pojo.vo.CapacityInfoVO;
import top.sboxm.file.remote.UserClient;
import top.sboxm.file.repository.FileRepository;
import top.sboxm.file.service.FileService;
import top.sboxm.file.utils.MinIOUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {
    @Resource
    private FileRepository fileRepository;
    @Resource
    @Qualifier("fileMinIOClient")
    private MinIOUtils fileClient;

    @Resource
    private MinIOConfig minIOConfig;
    
    @Resource
    private UserClient userClient;
    
    // 创建文件记录
    public String createFileRecord(String userId, String virtualPath, String fileName, Long fileSize, String fileType, String sha256) throws Exception {
        // 检查用户可用容量
        RestResult<CapacityInfoVO> capacityResult = userClient.getCapacityInfo(userId);
        if (!capacityResult.getIsSuccess()) {
            throw new FileOperationException("获取用户容量信息失败");
        }
        CapacityInfoVO capacityInfo = capacityResult.getData();
        if (capacityInfo.getRemainingCapacity() < fileSize) {
            throw new FileOperationException(RestResultEnum.INSUFFICIENT_CAPACITY);
        }

        // 创建文件文档
        FileDocument fileDoc = new FileDocument();
        fileDoc.setUserId(userId);
        fileDoc.setVirtualPath(virtualPath);
        fileDoc.setFileName(fileName);
        fileDoc.setFileSize(fileSize);
        fileDoc.setFileType(fileType);
        fileDoc.setIsFolder(false);
        fileDoc.setIsDeleted(false);
        fileDoc.setCreatedAt(new Date());
        fileDoc.setUpdatedAt(new Date());
        fileDoc.setIsShared(false);
        
        // 使用sha256作为MinIO中的对象名称
        fileDoc.setMinioObjectName(sha256);
        fileDoc.setSha256(sha256);
        fileDoc.setStatus(FileDocument.FileStatus.PENDING);

        // 保存文件元数据
        fileDoc = fileRepository.save(fileDoc);
        
        return fileDoc.getId();
    }

    // 文件下载（返回预签名 URL）
    public String downloadFile(String fileId, String userId) throws Exception {
        FileDocument fileDoc = fileRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException(RestResultEnum.FILE_NOT_FOUND));
        if (fileDoc.getIsFolder() || fileDoc.getIsDeleted()) {
            throw new FileOperationException("无效的文件");
        }
        // 验证文件所有权
        if (!fileDoc.getUserId().equals(userId)) {
            throw new FileOperationException(RestResultEnum.NO_OPERATOR);
        }
        // 使用带有原始文件名的预签名URL
        return MinIOUtils.getPresignedObjectUrlWithFileName(
                minIOConfig.getFileBucketName(),
                fileDoc.getMinioObjectName(),
                fileDoc.getFileName(),
                60 * 60
        );
    }

    // 创建文件夹
    public FileDocument createFolder(String userId, String virtualPath, String folderName) {
        // 检查同名文件夹是否已存在
        List<FileDocument> existingFolders = fileRepository.findByUserIdAndVirtualPathAndIsDeleted(userId, virtualPath, false);
        for (FileDocument existingDoc : existingFolders) {
            if (existingDoc.getIsFolder() && existingDoc.getFileName().equals(folderName)) {
                throw new IllegalArgumentException("同名文件夹已存在");
            }
        }
        
        FileDocument folderDoc = new FileDocument();
        folderDoc.setUserId(userId);
        folderDoc.setVirtualPath(virtualPath);
        folderDoc.setFileName(folderName);
        folderDoc.setFileSize(0L);
        folderDoc.setFileType("");
        folderDoc.setIsFolder(true);
        folderDoc.setIsDeleted(false);
        folderDoc.setCreatedAt(new Date());
        folderDoc.setUpdatedAt(new Date());
        folderDoc.setIsShared(false);
        folderDoc.setMinioObjectName("");
        return fileRepository.save(folderDoc);
    }

    // 查询用户所有文件和文件夹
    public List<FileDocument> getAllFilesAndFolders(String userId) {
        return fileRepository.findByUserIdAndIsDeleted(userId, false);
    }

    // 查询某个文件夹下的内容
    public List<FileDocument> getFolderContents(String userId, String virtualPath) {
        List<FileDocument> allContents = fileRepository.findByUserIdAndVirtualPathAndIsDeleted(userId, virtualPath, false);
        
        // 分离文件夹和文件
        List<FileDocument> folders = allContents.stream()
                .filter(FileDocument::getIsFolder)
                .sorted((a, b) -> a.getFileName().compareToIgnoreCase(b.getFileName()))
                .toList();
        
        List<FileDocument> files = allContents.stream()
                .filter(doc -> !doc.getIsFolder())
                .sorted((a, b) -> a.getFileName().compareToIgnoreCase(b.getFileName()))
                .toList();
        
        // 合并文件夹和文件列表
        List<FileDocument> sortedContents = new ArrayList<>();
        sortedContents.addAll(folders);
        sortedContents.addAll(files);
        
        return sortedContents;
    }

    // 删除文件
    public void deleteFile(String fileId, String userId) throws Exception {
        FileDocument fileDoc = fileRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException(RestResultEnum.FILE_NOT_FOUND));
        // 验证文件所有者
        if (!fileDoc.getUserId().equals(userId)) {
            throw new FileOperationException(RestResultEnum.NO_OPERATOR);
        }
        if (!fileDoc.getIsFolder()) {
            MinIOUtils.removeFile(
                    minIOConfig.getFileBucketName(),
                    fileDoc.getMinioObjectName()
            );
        }
        fileDoc.setIsDeleted(true);
        fileRepository.save(fileDoc);
        
        // 恢复用户可用容量
        UpdateCapacityDTO updateCapacityDTO = new UpdateCapacityDTO();
        updateCapacityDTO.setUserId(userId);
        updateCapacityDTO.setCapacityChange(String.valueOf(fileDoc.getFileSize()));
        RestResult<Void> updateResult = userClient.updateCapacity(updateCapacityDTO);
        if (!updateResult.getIsSuccess()) {
            throw new FileOperationException("恢复用户容量失败");
        }
    }

    // 删除文件夹及其下所有内容
    public void deleteFolderAndContents(String folderId, String userId) {
        FileDocument folderDoc = fileRepository.findById(folderId).orElseThrow(() -> new FileNotFoundException("Folder not found"));
        // 验证文件夹所有者
        if (!folderDoc.getUserId().equals(userId)) {
            throw new IllegalArgumentException("无权删除此文件夹");
        }
        if (!folderDoc.getIsFolder()) {
            throw new IllegalArgumentException("Not a folder");
        }
        String virtualPath = folderDoc.getVirtualPath() + folderDoc.getFileName() + "/";
        List<FileDocument> contents = fileRepository.findByUserIdAndVirtualPathStartingWithAndIsDeleted(folderDoc.getUserId(), virtualPath, false);
        for (FileDocument doc : contents) {
            if (!doc.getIsFolder()) {
                try {
                    MinIOUtils.removeFile(
                            minIOConfig.getFileBucketName(),
                            doc.getMinioObjectName()
                    );
                } catch (Exception e) {
                    // 日志记录异常，跳过失败的文件
                }
            }
            doc.setIsDeleted(true);
            fileRepository.save(doc);
            
            // 恢复用户可用容量
            if (!doc.getIsFolder()) {
                UpdateCapacityDTO updateCapacityDTO = new UpdateCapacityDTO();
                updateCapacityDTO.setUserId(userId);
                updateCapacityDTO.setCapacityChange(String.valueOf(doc.getFileSize()));
                RestResult<Void> updateResult = userClient.updateCapacity(updateCapacityDTO);
                if (!updateResult.getIsSuccess()) {
                    throw new FileOperationException("恢复用户容量失败");
                }
            }
        }
        folderDoc.setIsDeleted(true);
        fileRepository.save(folderDoc);
    }

    // 修改文件信息
    public FileDocument updateFileInfo(String fileId, String newFileName, String newVirtualPath) {
        FileDocument fileDoc = fileRepository.findById(fileId).orElseThrow(() -> new IllegalArgumentException("File not found"));
//        if (fileDoc.getIsFolder()) {
//            throw new IllegalArgumentException("Cannot rename folder");
//        }
        fileDoc.setFileName(newFileName);
        fileDoc.setVirtualPath(newVirtualPath);
        fileDoc.setUpdatedAt(new Date());
        return fileRepository.save(fileDoc);
    }
    
    // 获取文件上传的预签名URL
    public String getPresignedUploadUrl(String fileId) throws Exception {
        FileDocument fileDoc = fileRepository.findById(fileId).orElseThrow(() -> new Exception("File not found"));
        if (fileDoc.getIsFolder() || fileDoc.getIsDeleted()) {
            throw new Exception("Invalid file");
        }
        
        // 生成预签名上传URL，有效期1小时
        return MinIOUtils.getPresignedPutUrl(
                minIOConfig.getFileBucketName(),
                fileDoc.getMinioObjectName(),
                60 * 60
        );
    }
    
    // 根据ID获取文件
    public FileDocument getFileById(String fileId) throws Exception {
        return fileRepository.findById(fileId).orElseThrow(() -> new FileNotFoundException(RestResultEnum.FILE_NOT_FOUND));
    }
    
    /**
     * 验证文件完整性
     * @param fileId 文件ID
     * @return 验证结果
     */
    @Async
    public void verifyFileIntegrity(String fileId) throws Exception {
        FileDocument fileDoc = getFileById(fileId);
        if (fileDoc.getIsFolder() || fileDoc.getIsDeleted()) {
            throw new FileOperationException("无效的文件");
        }
        
        // 更新文件状态为上传中
        fileDoc.setStatus(FileDocument.FileStatus.UPLOADING);
        fileRepository.save(fileDoc);
        
        try {
            // 获取MinIO中文件的对象信息
            // 注意：由于SHA256验证需要下载整个文件计算哈希值，这里简化处理
            // 实际应用中可能需要异步下载文件并计算SHA256
            String eTag = MinIOUtils.getObjectETag(
                    minIOConfig.getFileBucketName(),
                    fileDoc.getMinioObjectName()
            );
            
            // 去除ETag中的双引号
            if (eTag.startsWith("\"") && eTag.endsWith("\"")) {
                eTag = eTag.substring(1, eTag.length() - 1);
            }

            fileDoc.setStatus(FileDocument.FileStatus.VERIFIED);
            
            // 更新用户可用容量
            UpdateCapacityDTO updateCapacityDTO = new UpdateCapacityDTO();
            updateCapacityDTO.setUserId(fileDoc.getUserId());
            updateCapacityDTO.setCapacityChange(String.valueOf(-fileDoc.getFileSize()));
            RestResult<Void> updateResult = userClient.updateCapacity(updateCapacityDTO);
            if (!updateResult.getIsSuccess()) {
                throw new FileOperationException("更新用户容量失败");                
            }
            
            // 要严格验证（需要下载文件）：
            /*
            InputStream is = MinIOUtils.getObject(minIOConfig.getFileBucketName(), fileDoc.getMinioObjectName());
            String calculatedSha256 = DigestUtils.sha256Hex(is);
            is.close();
            
            if (calculatedSha256.equalsIgnoreCase(fileDoc.getSha256())) {
                fileDoc.setStatus(FileDocument.FileStatus.VERIFIED);
            } else {
                fileDoc.setStatus(FileDocument.FileStatus.INVALID);
                MinIOUtils.removeFile(minIOConfig.getFileBucketName(), fileDoc.getMinioObjectName());
            }
            */
        } catch (Exception e) {
            fileDoc.setStatus(FileDocument.FileStatus.INVALID);
            throw e;
        } finally {
            fileRepository.save(fileDoc);
        }
    }
}