package top.sboxm.blog.pojo.dto;

import lombok.Data;

/**
 * 创建文章的请求参数
 */
@Data
public class CreatePostDTO {
    /**
     * 标题
     */
    private String title;

    /**
     * 正文内容
     */
    private String content;

    /**
     * 摘要
     */
    private String summary;

    /**
     * 分类
     */
    private Long categoryId;

    /**
     * 缩略图
     */
    private String thumbnail;

    /**
     * 是否是markdown
     */
    private Integer isMarkdown;

    /**
     * 配图地址，JSON格式
     */
    private String imageUrls;

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
     * 自定义标签，JSON格式
     */
    private String tags;
}