package top.sboxm.user.pojo.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UpdateNicknameDTO {

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空")
    private String nickname;
}