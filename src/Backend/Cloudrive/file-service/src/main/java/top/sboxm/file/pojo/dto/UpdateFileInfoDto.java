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
public class UpdateFileInfoDto {
    /**
     * 文件ID
     */
    @NotBlank(message = "文件ID不能为空")
    private String fileId;

    /**
     * 新文件名
     */
    @NotBlank(message = "新文件名不能为空")
    private String newFileName;

    /**
     * 新虚拟路径
     */
    @NotBlank(message = "新虚拟路径不能为空")
    private String newVirtualPath;
}
