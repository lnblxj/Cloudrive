package top.sboxm.file.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.sboxm.file.utils.MinIOUtils;

@Configuration
@Data
public class MinIOConfig {

    @Value("${minio.endpoint}")
    private String endpoint;
    @Value("${minio.fileHost}")
    private String fileHost;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.imgSize}")
    private Integer imgSize;
    @Value("${minio.fileSize}")
    private Integer fileSize;
    @Value("${minio.imgBucketName}")
    private String imgBucketName;
    @Value("${minio.fileBucketName}")
    private String fileBucketName;

    @Bean
    public MinIOUtils imageMinIOClient() {
        return new MinIOUtils(endpoint, imgBucketName, accessKey, secretKey, imgSize, fileSize);
    }
    @Bean
    public MinIOUtils fileMinIOClient() {
        return new MinIOUtils(endpoint, fileBucketName, accessKey, secretKey, imgSize, fileSize);
    }
}