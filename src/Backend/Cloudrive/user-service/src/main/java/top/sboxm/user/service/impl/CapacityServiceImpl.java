package top.sboxm.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.sboxm.user.mapper.CapacityInfoMapper;
import top.sboxm.user.pojo.dto.UpdateCapacityDTO;
import top.sboxm.user.pojo.po.CapacityInfo;
import top.sboxm.user.pojo.vo.CapacityInfoVO;
import top.sboxm.user.service.CapacityService;

/**
 * 用户容量服务实现类
 */
@Slf4j
@Service
public class CapacityServiceImpl implements CapacityService {

    @Resource
    private CapacityInfoMapper capacityInfoMapper;

    @Override
    public CapacityInfoVO getCapacityInfo(Long userId) {
        CapacityInfo capacityInfo = capacityInfoMapper.selectOne(
                new LambdaQueryWrapper<CapacityInfo>()
                        .eq(CapacityInfo::getUserId, userId)
        );

        if (capacityInfo == null) {
            return null;
        }

        CapacityInfoVO vo = new CapacityInfoVO();
        vo.setTotalCapacity(capacityInfo.getTotalCapacity());
        vo.setUsedCapacity(capacityInfo.getUsedCapacity());
        vo.setRemainingCapacity(capacityInfo.getTotalCapacity() - capacityInfo.getUsedCapacity());
        vo.setUsageRate(capacityInfo.getUsedCapacity().doubleValue() / capacityInfo.getTotalCapacity() * 100);

        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCapacity(UpdateCapacityDTO updateCapacityDTO) {
        CapacityInfo capacityInfo = capacityInfoMapper.selectOne(
                new LambdaQueryWrapper<CapacityInfo>()
                        .eq(CapacityInfo::getUserId, Long.parseLong(updateCapacityDTO.getUserId()) )
        );

        if (capacityInfo == null) {
            log.error("用户容量不存在");
            return false;
        }

        try {
            // 计算新的已用容量
            long newUsedCapacity = capacityInfo.getUsedCapacity() - Long.parseLong(updateCapacityDTO.getCapacityChange());
            if (newUsedCapacity < 0 || newUsedCapacity > capacityInfo.getTotalCapacity()) {
                return false;
            }
            // 更新容量
            capacityInfo.setUsedCapacity(newUsedCapacity);

        } catch (NumberFormatException e) {
             throw new RuntimeException("更新容量出错");
        }
        return capacityInfoMapper.updateById(capacityInfo) > 0;
    }
}
