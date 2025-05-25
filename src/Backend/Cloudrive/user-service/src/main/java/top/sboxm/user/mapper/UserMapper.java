package top.sboxm.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sboxm.user.pojo.po.User;

/**
 * 用户数据Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}