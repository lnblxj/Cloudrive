package top.sboxm.file.remote;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import top.sboxm.common.result.RestResult;
import top.sboxm.file.pojo.dto.UpdateCapacityDTO;
import top.sboxm.file.pojo.vo.CapacityInfoVO;


@FeignClient(value = "user-service", path = "/capacity")
public interface UserClient {
    /**
     * 获取用户容量信息
     * @return 用户容量信息
     */
    @GetMapping("/info")
    RestResult<CapacityInfoVO> getCapacityInfo(@RequestParam("userId") String userId);

    /**
     * 更新用户容量信息
     * @param updateCapacityDTO 容量更新信息
     * @return 更新结果
     */
    @PostMapping("/update")
    RestResult<Void> updateCapacity(@RequestBody UpdateCapacityDTO updateCapacityDTO);
}
