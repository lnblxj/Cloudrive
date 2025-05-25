package top.sboxm.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.sboxm.user.pojo.po.CapacityInfo;

/**
 * 用户容量信息Mapper
 */
@Mapper
public interface CapacityInfoMapper extends BaseMapper<CapacityInfo> {
}