package top.sboxm.blog.pojo.vo;

import lombok.Data;

/**
 * 文章列表返回VO
 */
@Data
public class PostListVO {
    /**
     * 文章ID
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 浏览量
     */
    private Long viewCount;

    /**
     * 评论量
     */
    private Long commentCount;

    /**
     * 标签，JSON格式
     */
    private String tags;

    /**
     * 顶级标签
     */
    private String topTag;
}