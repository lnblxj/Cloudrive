package top.sboxm.file.pojo.vo;

import lombok.Data;
import top.sboxm.file.pojo.po.FileDocument;

import java.util.Date;

@Data
public class FileVo {
    private String id;           // 文件或文件夹的唯一标识
    private String virtualPath;  // 虚拟路径
    private String fileName;     // 文件或文件夹名称
    private Long fileSize;       // 文件大小（字节），文件夹为0
    private String fileType;     // 文件的MIME类型或扩展名
    private Boolean isFolder;    // 是否为文件夹
    private Date createdAt;      // 创建时间
    private Date updatedAt;      // 更新时间
    private Boolean isShared;    // 是否共享
    private String status;       // 文件状态

    public static FileVo fromFileDocument(FileDocument document) {
        if (document == null) {
            return null;
        }
        FileVo vo = new FileVo();
        vo.setId(document.getId());
        vo.setVirtualPath(document.getVirtualPath());
        vo.setFileName(document.getFileName());
        vo.setFileSize(document.getFileSize());
        vo.setFileType(document.getFileType());
        vo.setIsFolder(document.getIsFolder());
        vo.setCreatedAt(document.getCreatedAt());
        vo.setUpdatedAt(document.getUpdatedAt());
        vo.setIsShared(document.getIsShared());
        vo.setStatus(document.getStatus() != null ? document.getStatus().name() : null);
        return vo;
    }
}