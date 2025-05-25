package top.sboxm.blog.pojo.dto;

import lombok.Data;

/**
 * 文章列表查询参数DTO
 */
@Data
public class PostListDTO {
    /**
     * 当前页码，从1开始
     */
    private Integer pageNum = 1;

    /**
     * 每页数量，固定为6
     */
    private Integer pageSize = 6;

    /**
     * 分类ID，0表示不按分类筛选
     */
    private Long categoryId = 0L;
}