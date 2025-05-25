package top.sboxm.file.service;

import org.springframework.web.multipart.MultipartFile;
import top.sboxm.file.pojo.po.FileDocument;

import java.util.List;

public interface FileService {
    // 文件上传
//    FileDocument uploadFile(String userId, String virtualPath, MultipartFile file) throws Exception;
    
    // 创建文件记录
    String createFileRecord(String userId, String virtualPath, String fileName, Long fileSize, String contentType, String sha256) throws Exception;

    // 文件下载（返回预签名 URL）
    String downloadFile(String fileId, String userId) throws Exception;

    // 创建文件夹
    FileDocument createFolder(String userId, String virtualPath, String folderName);

    // 查询用户所有文件和文件夹
    List<FileDocument> getAllFilesAndFolders(String userId);

    // 查询某个文件夹下的内容
    List<FileDocument> getFolderContents(String userId, String virtualPath);

    // 删除文件
    void deleteFile(String fileId, String userId) throws Exception;

    // 删除文件夹及其下所有内容
    void deleteFolderAndContents(String folderId, String userId);

    // 修改文件信息
    FileDocument updateFileInfo(String fileId, String newFileName, String newVirtualPath);
    
    // 获取文件上传的预签名URL
    String getPresignedUploadUrl(String fileId) throws Exception;
    
    // 根据ID获取文件
    FileDocument getFileById(String fileId) throws Exception;
    
    // 验证文件完整性
    void verifyFileIntegrity(String fileId) throws Exception;
}
