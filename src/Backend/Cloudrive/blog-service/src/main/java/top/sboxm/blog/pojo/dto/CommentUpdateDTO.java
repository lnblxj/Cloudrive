package top.sboxm.blog.pojo.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 更新评论请求DTO
 */
@Data
public class CommentUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    @NotBlank(message = "评论ID不能为空")
    private String id;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    private String content;
}