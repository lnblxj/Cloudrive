package top.sboxm.blog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sboxm.blog.pojo.po.Post;

/**
 * 文章数据访问层
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
    
}