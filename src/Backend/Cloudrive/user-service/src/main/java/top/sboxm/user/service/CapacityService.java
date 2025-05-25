package top.sboxm.user.service;

import top.sboxm.user.pojo.vo.CapacityInfoVO;
import top.sboxm.user.pojo.dto.UpdateCapacityDTO;

/**
 * 用户容量服务接口
 */
public interface CapacityService {
    /**
     * 获取用户容量信息
     *
     * @param userId 用户ID
     * @return 用户容量信息VO
     */
    CapacityInfoVO getCapacityInfo(Long userId);

    /**
     * 更新用户容量信息
     *
     * @param updateCapacityDTO 容量更新信息
     * @return 更新是否成功
     */
    boolean updateCapacity(UpdateCapacityDTO updateCapacityDTO);
}
