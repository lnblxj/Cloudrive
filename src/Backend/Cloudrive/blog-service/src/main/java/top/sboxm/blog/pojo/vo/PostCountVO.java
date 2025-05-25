package top.sboxm.blog.pojo.vo;

import lombok.Data;

/**
 * 文章数量统计VO
 */
@Data
public class PostCountVO {
    
    /**
     * 热门文章数量
     */
    private Long popularCount;
    
    /**
     * 精选文章数量
     */
    private Long selectedCount;
}