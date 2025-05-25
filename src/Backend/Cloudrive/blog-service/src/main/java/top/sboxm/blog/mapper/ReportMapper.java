package top.sboxm.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sboxm.blog.pojo.po.Report;

/**
 * 文章举报信息Mapper接口
 */
@Mapper
public interface ReportMapper extends BaseMapper<Report> {
}