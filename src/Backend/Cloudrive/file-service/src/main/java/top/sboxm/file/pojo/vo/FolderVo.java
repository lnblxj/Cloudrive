package top.sboxm.file.pojo.vo;

import lombok.Data;
import top.sboxm.file.pojo.po.FileDocument;

import java.util.Date;
import java.util.List;

@Data
public class FolderVo {
    private String id;           // 文件夹的唯一标识
    private String virtualPath;  // 虚拟路径
    private String folderName;   // 文件夹名称
    private Date createdAt;      // 创建时间
    private Date updatedAt;      // 更新时间
    private List<FileVo> contents; // 文件夹内容

    public static FolderVo fromFileDocument(FileDocument document) {
        if (document == null) {
            return null;
        }
        FolderVo vo = new FolderVo();
        vo.setId(document.getId());
        vo.setVirtualPath(document.getVirtualPath());
        vo.setFolderName(document.getFileName());
        vo.setCreatedAt(document.getCreatedAt());
        vo.setUpdatedAt(document.getUpdatedAt());
        return vo;
    }
}