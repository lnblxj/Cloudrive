package top.sboxm.file.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetFolderDto {
    /**
     * 文件夹ID
     */
    @NotBlank(message = "虚拟路径不能为空")
    private String virtualPath;
}
