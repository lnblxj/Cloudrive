package top.sboxm.file.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import top.sboxm.file.pojo.po.FileDocument;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends MongoRepository<FileDocument, String> {
    // 查询用户的所有未删除文件和文件夹
    List<FileDocument> findByUserIdAndIsDeleted(String userId, Boolean isDeleted);

    // 查询某个路径下的未删除文件和文件夹
    List<FileDocument> findByUserIdAndVirtualPathAndIsDeleted(String userId, String virtualPath, Boolean isDeleted);

    // 查询以某个路径开头的所有未删除内容（用于递归删除文件夹）
    List<FileDocument> findByUserIdAndVirtualPathStartingWithAndIsDeleted(String userId, String virtualPath, Boolean isDeleted);
    
    // 根据MinIO对象名（SHA256哈希值）查找未删除的文件（用于秒传）
    Optional<FileDocument> findByMinioObjectNameAndIsDeletedAndIsFolder(String minioObjectName, Boolean isDeleted, Boolean isFolder);
}