package top.sboxm.file.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFolderDto {

    /**
     * 文件虚拟路径
     */
   @NotBlank(message = "文件虚拟路径不能为空")
    private String virtualPath;

    /**
     * 文件夹名称
     */
    @NotBlank(message = "文件夹名称不能为空")
    private String folderName;
}
