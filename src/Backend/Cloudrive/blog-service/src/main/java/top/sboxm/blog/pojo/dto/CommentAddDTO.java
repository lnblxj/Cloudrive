package top.sboxm.blog.pojo.dto;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 添加评论请求DTO
 */
@Data
public class CommentAddDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 所属文章ID
     */
    @NotBlank(message = "文章ID不能为空")
    private String articleId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    private String content;
}