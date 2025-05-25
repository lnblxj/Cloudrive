package top.sboxm.edge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sboxm.edge.pojo.po.Post;

/**
 * 博客文章Mapper接口
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
    
}