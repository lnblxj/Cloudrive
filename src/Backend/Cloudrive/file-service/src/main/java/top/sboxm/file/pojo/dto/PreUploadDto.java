package top.sboxm.file.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class PreUploadDto {
    /**
     * 文件虚拟路径
     */
    @NotBlank(message = "文件虚拟路径不能为空")
    private String virtualPath;

    /**
     * 文件名
     */
    @NotBlank(message = "文件名不能为空")
    private String fileName;

    /**
     * 文件大小
     */
    @NotBlank(message = "文件大小不能为空")
    private Long fileSize;

    /**
     * 文件SHA256值
     */
    @NotBlank(message = "文件SHA256值不能为空")
    private String sha256;
}
