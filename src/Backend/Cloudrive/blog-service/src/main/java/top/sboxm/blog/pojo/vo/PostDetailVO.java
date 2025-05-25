package top.sboxm.blog.pojo.vo;

import lombok.Data;
import java.sql.Timestamp;

/**
 * 文章详情返回VO
 */
@Data
public class PostDetailVO {
    /**
     * 文章ID
     */
    private String id;

    /**
     * 标题
     */
    private String title;

    /**
     * 正文内容
     */
    private String content;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 浏览量
     */
    private Long viewCount;

    /**
     * 评论量
     */
    private Long commentCount;

    /**
     * 作者ID
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 是否是markdown
     */
    private Integer isMarkdown;

    /**
     * 配图地址
     */
    private String thumbnail;

    /**
     * 分享链接
     */
    private String shareLink;

    /**
     * 是否需要提取密码
     */
    private Boolean needPassword;

    /**
     * 提取密码
     */
    private String password;

    /**
     * 自定义标签
     */
    private String tags;

    /**
     * 顶级标签
     */
    private String topTag;

    /**
     * 配图地址，JSON格式
     */
    private String imageUrls;
}