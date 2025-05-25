package top.sboxm.blog.pojo.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.sql.Timestamp;

/**
 * 文章举报信息实体类，映射数据库表cd_blog_report_01。
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cd_blog_report")
public class Report {

    private static final long serialVersionUID = 1L;

    /**
     * 举报记录ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 被举报文章ID
     */
    private Long blogId;

    /**
     * 举报人ID
     */
    private Long reportUser;

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

    /**
     * 举报时间
     */
    private Timestamp reportTime;

    /**
     * 处理人ID
     */
    private Long handler;

    /**
     * 处理时间
     */
    private Timestamp handleTime;

    /**
     * 状态：0=待处理 1=已处理 2=已驳回
     */
    private Integer status;

    /**
     * 处理备注
     */
    private String handleRemark;

    /**
     * 删除标志：0=有效 1=删除
     */
    @TableLogic
    private Integer delFlag;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Timestamp createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Timestamp updateTime;
}