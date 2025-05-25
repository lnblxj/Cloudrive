package top.sboxm.blog.pojo.dto;

import lombok.Data;

@Data
public class SearchDTO {
    private String keyword;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
}