package top.sboxm.blog.pojo.vo;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * 用户文章列表VO
 */
@Data
public class UserPostListVO {
    /**
     * 文章id
     */
    private String id;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 文章摘要
     */
    private String summary;

    /**
     * 文章状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Timestamp createTime;
}