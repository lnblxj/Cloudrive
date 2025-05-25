package top.sboxm.auth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import top.sboxm.auth.pojo.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 *  用户Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
