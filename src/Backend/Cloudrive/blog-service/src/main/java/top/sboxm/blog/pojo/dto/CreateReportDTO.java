package top.sboxm.blog.pojo.dto;

import lombok.Data;

/**
 * 创建举报请求DTO
 */
@Data
public class CreateReportDTO {
    /**
     * 被举报文章ID
     */
    private String blogId;

    /**
     * 举报类型：0=垃圾广告 1=违规内容 2=侵权 3=其他
     */
    private Integer reportType;

    /**
     * 举报详情
     */
    private String reportContent;

    /**
     * 证据材料(JSON格式)
     */
    private String evidence;
} 