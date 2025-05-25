package top.sboxm.edge.pojo.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.sql.Timestamp;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 博客文章实体类，映射数据库表cd_blog_01。
 * 存储文章的基本信息、统计数据及操作审计信息。
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("cd_blog_01")
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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
     * 浏览量
     */
    private Long viewCount;

    /**
     * 评论量
     */
    private Long commentCount;

    /**
     * 作者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 更新者
     */
    private Long updateBy;

    /**
     * 更新时间
     */
    private Timestamp updateTime;

    /**
     * 状态：0=审核中 1=正常 2=下架
     */
    private Integer status;

    /**
     * 是否删除0=否
     */
    private Integer delFlag;

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

    /**
     * 顶级标签
     */
    private String topTag;
}
