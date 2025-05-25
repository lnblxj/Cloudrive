package top.sboxm.user.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import top.sboxm.common.result.RestResult;
import top.sboxm.user.pojo.vo.ImageBedVO;

@FeignClient(value = "file-service", path = "/imagebed")
public interface FileClient {
    /**
     * 上传图片到图床
     * @param file 图片文件
     * @return 图片信息
     */
    @PostMapping(value = "/images", consumes = "multipart/form-data")
    RestResult<ImageBedVO> uploadImage(@RequestPart("file") MultipartFile file);
    
    /**
     * 内部上传图片到图床
     * @param file 图片文件
     * @param userId 用户ID
     * @return 图片信息
     */
    @PostMapping(value = "/internal/images", consumes = "multipart/form-data")
    RestResult<ImageBedVO> uploadImageInternal(@RequestPart("file") MultipartFile file, @RequestParam("userId") String userId);
}