package top.sboxm.file.service.impl;

import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import top.sboxm.file.config.MinIOConfig;
import top.sboxm.file.exception.FileNotFoundException;
import top.sboxm.file.exception.FileOperationException;
import top.sboxm.file.exception.UploadException;
import top.sboxm.file.mapper.ImageBedMapper;
import top.sboxm.file.pojo.po.ImageBed;
import top.sboxm.file.service.ImageBedService;
import top.sboxm.file.utils.MinIOUtils;
import top.sboxm.common.enums.RestResultEnum;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ImageBedServiceImpl implements ImageBedService {
    @Resource
    private ImageBedMapper imageBedMapper;

    @Resource
    private MinIOConfig minIOConfig;

    @Override
    public ImageBed uploadImage(MultipartFile file, String userId) throws Exception {
        // 生成唯一文件名
        String originalFilename = file.getOriginalFilename();
        String fileExtension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // 上传文件到MinIO
        MinIOUtils.uploadFile(minIOConfig.getImgBucketName(), uniqueFileName, file.getInputStream());

        // 保存图片信息到数据库
        ImageBed imageBed = new ImageBed()
                .setId(IdUtil.getSnowflake().nextId())
                .setUserId(Long.parseLong(userId))
                .setMinioObjectName(uniqueFileName)
                .setCreateTime(new Timestamp(System.currentTimeMillis()))
                .setDelFlag(0);
        imageBedMapper.insert(imageBed);

        // 返回图片实体
        return imageBed;
    }

    @Override
    public List<ImageBed> listImagesByUserId(String userId) {
        return imageBedMapper.selectList(new QueryWrapper<ImageBed>().eq("user_id", Long.parseLong(userId)).eq("del_flag", 0));
    }

    @Override
    public boolean deleteImage(String id, String userId) {
        // 获取图片信息
        ImageBed imageBed = imageBedMapper.selectById(Long.parseLong(id));
        if (imageBed == null || imageBed.getDelFlag() == 1) {
            throw new FileNotFoundException(RestResultEnum.FILE_NOT_FOUND);
        }
        
        // 验证图片所有权
        if (!imageBed.getUserId().toString().equals(userId)) {
            log.warn("用户{}尝试删除不属于自己的图片{}", userId, id);
            throw new FileOperationException(RestResultEnum.NO_OPERATOR);
        }

        try {
            // 删除MinIO中的文件
            MinIOUtils.removeFile(minIOConfig.getImgBucketName(), imageBed.getMinioObjectName());
            
            // 更新数据库记录为已删除
            imageBed.setDelFlag(1);
            return imageBedMapper.updateById(imageBed) > 0;
        } catch (Exception e) {
            log.error("删除图片失败", e);
            throw new FileOperationException("删除图片失败");
        }
    }
}
