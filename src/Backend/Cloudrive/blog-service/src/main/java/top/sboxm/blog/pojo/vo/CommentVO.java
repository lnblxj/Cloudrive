package top.sboxm.blog.pojo.vo;

import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 评论返回VO
 */
@Data
public class CommentVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 评论ID
     */
    private String id;

    /**
     * 所属文章ID
     */
    private Long articleId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 创建者ID
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户头像
     */
    private String avatar;
}