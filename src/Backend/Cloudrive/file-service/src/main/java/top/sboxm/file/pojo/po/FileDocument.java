package top.sboxm.file.pojo.po;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "files")
@Data
public class FileDocument {
    @Id
    private String id;           // 文件或文件夹的唯一标识
    private String userId;       // 用户 ID
    private String virtualPath;  // 虚拟路径
    private String fileName;     // 文件或文件夹名称
    private Long fileSize;       // 文件大小（字节），文件夹为0
    private String fileType;     // 文件的MIME类型或扩展名
    private Boolean isFolder;    // 是否为文件夹
    private Boolean isDeleted;   // 是否已删除（软删除）
    private Date createdAt;      // 创建时间
    private Date updatedAt;      // 更新时间
    private Boolean isShared;    // 是否共享
    private String minioObjectName; // MinIO中的对象名称（SHA256哈希值）
    private String sha256;      // 文件的SHA256值
    private FileStatus status;   // 文件状态
    
    /**
     * 文件状态枚举
     */
    public enum FileStatus {
        PENDING,    // 等待上传
        UPLOADING,  // 上传中
        VERIFIED,   // 已验证
        INVALID     // 无效（验证失败）
    }
}