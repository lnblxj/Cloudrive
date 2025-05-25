package top.sboxm.file.controller;

import jakarta.annotation.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.sboxm.common.result.RestResult;
import top.sboxm.common.utils.SecurityContextUtil;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.file.config.MinIOConfig;
import top.sboxm.file.exception.SystemException;
import top.sboxm.file.pojo.dto.CreateFolderDto;
import top.sboxm.file.pojo.dto.GetFolderDto;
import top.sboxm.file.pojo.dto.PreUploadDto;
import top.sboxm.file.pojo.dto.UpdateFileInfoDto;
import top.sboxm.file.pojo.po.FileDocument;
import top.sboxm.file.pojo.vo.FileVo;
import top.sboxm.file.pojo.vo.FolderVo;
import top.sboxm.file.pojo.vo.PreUploadVo;
import top.sboxm.file.service.FileService;
import top.sboxm.file.utils.MinIOUtils;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    @Resource
    private FileService fileService;
    
    @Resource
    private MinIOConfig minIOConfig;

    /**
     * 创建文件记录并获取预签名上传URL
     * @return 文件记录和上传URL
     */
    @PostMapping("/preUpload")
    public Object prepareUpload(@RequestBody PreUploadDto preUploadDto) throws Exception {
        String userId = SecurityContextUtil.getUserId();
        if(!StringUtils.hasText(userId)){
           return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }

        String virtualPath = preUploadDto.getVirtualPath();
        if (!StringUtils.hasText(virtualPath)){
            throw new SystemException(RestResultEnum.INVALID_PARAM);
        }

        String fileName = preUploadDto.getFileName();
        if (!StringUtils.hasText(fileName)){
            throw new SystemException(RestResultEnum.INVALID_PARAM);
        } else if (fileName.matches(".*[<>:\"/\\\\|?*].*")) {
            throw new SystemException(RestResultEnum.INVALID_PARAM);
        }

        Long fileSize = preUploadDto.getFileSize();
        if (fileSize == null || fileSize <= 0) {
            throw new SystemException(RestResultEnum.INVALID_PARAM);
        }else if (fileSize > minIOConfig.getFileSize() * 1024 * 1024L){
            throw new SystemException(RestResultEnum.FILE_SIZE_EXCEED_LIMIT);
        }

        // 从文件名获取文件类型
        String fileType = "";
        if (fileName.contains(".")) {
            fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        
        String sha256 = preUploadDto.getSha256();
        if (!StringUtils.hasText(sha256)){
            throw new SystemException(RestResultEnum.INVALID_PARAM);
        }

        // 创建文件记录
        String fileId = fileService.createFileRecord(userId, virtualPath, fileName, fileSize, fileType, sha256);
        
        // 获取预签名上传URL
        String uploadUrl = fileService.getPresignedUploadUrl(fileId);
        
        // 返回文件记录和上传URL
        PreUploadVo preUploadVO = new PreUploadVo();
        preUploadVO.setFileId(fileId);
        preUploadVO.setUploadUrl(uploadUrl);
        return RestResult.ok().setData(preUploadVO);
    }

    /**
     * 文件上传确认
     * @param fileId 文件ID
     * @return 文件记录
     */
    @PostMapping("/confirmUpload/{fileId}")
    public Object confirmUpload(@PathVariable String fileId) throws Exception {
        FileDocument fileDoc = fileService.getFileById(fileId);
        // 验证文件是否已成功上传
        if (!MinIOUtils.isObjectExist(minIOConfig.getFileBucketName(), fileDoc.getMinioObjectName())) {
            throw new Exception("File not uploaded to MinIO yet");
        }
        // 验证文件完整性
        fileService.verifyFileIntegrity(fileId);
        return RestResult.ok();
    }
    
    /**
     * 查询文件上传状态
     * @param fileId 文件ID
     * @return 文件记录
     */
    @GetMapping("/status/{fileId}")
    public Object getFileStatus(@PathVariable String fileId) throws Exception {
        return RestResult.ok().setData(FileVo.fromFileDocument(fileService.getFileById(fileId)));
    }

    /**
     * 文件下载
     * @param fileId 文件ID
     * @return 下载URL
     */
    @GetMapping("/download/{fileId}")
    public Object downloadFile(@PathVariable String fileId) throws Exception {
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        return RestResult.ok().setData(fileService.downloadFile(fileId, userId));
    }

    /**
     * 创建文件夹
     */
    @PostMapping("/folder")
    public Object createFolder(@RequestBody CreateFolderDto createFolderDto) {
        String virtualPath = createFolderDto.getVirtualPath();
        if (!StringUtils.hasText(virtualPath)) {
            throw new IllegalArgumentException("虚拟路径不能为空");
        }
        String folderName = createFolderDto.getFolderName();
        if (!StringUtils.hasText(folderName)) {
            throw new IllegalArgumentException("文件夹名不能为空");
        }
        String userId = SecurityContextUtil.getUserId();

        if (userId == null) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        return RestResult.ok().setData(FolderVo.fromFileDocument(fileService.createFolder(userId, virtualPath, folderName)));
    }

    /**
     * 查询用户所有文件和文件夹
     */
    @GetMapping("/all")
    public Object getAllFilesAndFolders() {
        String userId = SecurityContextUtil.getUserId();
        if (userId == null) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        return RestResult.ok().setData(fileService.getAllFilesAndFolders(userId).stream()
            .map(FileVo::fromFileDocument)
            .toList());
    }

    /**
     * 查询文件夹内容
     * @param virtualPath 虚拟路径
     * @return 文件夹内容
     */
    @GetMapping("/folder")
    public Object getFolderContents(@RequestParam String virtualPath) {
        if (!StringUtils.hasText(virtualPath)) {
            return RestResult.fail(RestResultEnum.INVALID_PARAM);
        }
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        return RestResult.ok().setData(fileService.getFolderContents(userId, virtualPath).stream()
            .map(FileVo::fromFileDocument)
            .toList());
    }

    /**
     * 删除文件
     * @param fileId 文件ID
     */
    @DeleteMapping("/{fileId}")
    public RestResult<Void> deleteFile(@PathVariable String fileId) throws Exception {
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        if (!StringUtils.hasText(fileId)) {
            return RestResult.fail(RestResultEnum.INVALID_PARAM);
        }
        fileService.deleteFile(fileId, userId);
        return RestResult.ok();
    }

    /**
     * 删除文件夹和内容
     * @param folderId 文件夹ID
     */
    @DeleteMapping("/folder/{folderId}")
    public RestResult<Void> deleteFolderAndContents(@PathVariable String folderId) {
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        if (!StringUtils.hasText(folderId)) {
            return RestResult.fail(RestResultEnum.INVALID_PARAM);
        }
        fileService.deleteFolderAndContents(folderId, userId);
        return RestResult.ok();
    }

    /**
     * 更新文件信息
     */
    @PutMapping("/{fileId}")
    public Object updateFileInfo(@RequestBody UpdateFileInfoDto updateFileInfoDto) {
        String fileId = updateFileInfoDto.getFileId();
        String newFileName = updateFileInfoDto.getNewFileName();
        String newVirtualPath = updateFileInfoDto.getNewVirtualPath();
        if (fileId == null || newFileName == null || newVirtualPath == null) {
            return RestResult.fail(RestResultEnum.INVALID_PARAM);
        }
        return RestResult.ok().setData(FileVo.fromFileDocument(fileService.updateFileInfo(fileId, newFileName, newVirtualPath)));
    }
}