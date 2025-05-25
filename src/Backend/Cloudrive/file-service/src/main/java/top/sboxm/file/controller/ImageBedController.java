package top.sboxm.file.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.result.RestResult;
import top.sboxm.common.utils.SecurityContextUtil;
import top.sboxm.file.config.MinIOConfig;
import top.sboxm.file.pojo.po.ImageBed;
import top.sboxm.file.pojo.vo.ImageBedVO;
import top.sboxm.file.service.ImageBedService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/imagebed")
@Slf4j
public class ImageBedController {
    @Resource
    private ImageBedService imageBedService;

    /**
     * 上传图片
     * @param file 图片文件
     * @return 图片永久链接
     */
    @Resource
    private MinIOConfig minIOConfig;

    @PostMapping("/images")
    public Object uploadImage(@RequestParam("file") MultipartFile file) throws Exception {
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        ImageBed imageBed = imageBedService.uploadImage(file, userId);
        return RestResult.ok().setData(ImageBedVO.fromImageBed(imageBed, minIOConfig.getFileHost(), minIOConfig.getImgBucketName()));
    }

    /**
     * 内部服务上传图片接口
     * @param file 图片文件
     * @param userId 用户ID
     * @return 图片永久链接
     */
    @PostMapping("/internal/images")
    public Object uploadImageInternal(@RequestParam("file") MultipartFile file, @RequestParam("userId") String userId) throws Exception {
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.INVALID_PARAM);
        }
        ImageBed imageBed = imageBedService.uploadImage(file, userId);
        return RestResult.ok().setData(ImageBedVO.fromImageBed(imageBed, minIOConfig.getFileHost(), minIOConfig.getImgBucketName()));
    }

    /**
     * 获取用户的所有图片列表
     * @param userId 用户ID
     * @return 图片列表
     */
    @GetMapping("/users/{userId}/images")
    public Object listUserImages(@PathVariable String userId) {
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.INVALID_PARAM);
        }
        List<ImageBed> images = imageBedService.listImagesByUserId(userId);
        List<ImageBedVO> imageVOs = images.stream()
                .map(image -> ImageBedVO.fromImageBed(image, minIOConfig.getFileHost(), minIOConfig.getImgBucketName()))
                .collect(Collectors.toList());
        return RestResult.ok().setData(imageVOs);
    }

    /**
     * 删除图片
     * @param id 图片ID
     * @return 是否删除成功
     */
    @DeleteMapping("/images/{id}")
    public Object deleteImage(@PathVariable String id) {
        String userId = SecurityContextUtil.getUserId();
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.NEED_LOGIN);
        }
        boolean success = imageBedService.deleteImage(id, userId);
        return success ? RestResult.ok() : RestResult.fail();
    }

    
}
