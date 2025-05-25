package top.sboxm.file.service;

import org.springframework.web.multipart.MultipartFile;
import top.sboxm.file.pojo.po.ImageBed;

import java.util.List;

/**
 * 图床服务接口
 */
public interface ImageBedService {
    /**
     * 上传图片到图床
     * @param file 图片文件
     * @param userId 用户ID
     * @return 图片访问URL
     */
    ImageBed uploadImage(MultipartFile file, String userId) throws Exception;

    /**
     * 获取用户的所有图片列表
     * @param userId 用户ID
     * @return 图片列表
     */
    List<ImageBed> listImagesByUserId(String userId);

    /**
     * 删除图片
     * @param id 图片ID
     * @param userId 用户ID
     * @return 是否删除成功
     */
    boolean deleteImage(String id, String userId);
}
