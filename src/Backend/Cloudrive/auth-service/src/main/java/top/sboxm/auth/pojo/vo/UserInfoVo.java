package top.sboxm.auth.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserInfoVo {

    private String id;

    /**
     * 昵称
     */
    private String nickname;


    /**
     * 邮箱
     */
    private String email;


    public void setId(Long id) {
        this.id = id.toString();
    }
}
