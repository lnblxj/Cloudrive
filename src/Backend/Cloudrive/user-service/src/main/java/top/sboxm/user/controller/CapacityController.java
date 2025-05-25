package top.sboxm.user.controller;

import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;
import top.sboxm.common.result.RestResult;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.utils.SecurityContextUtil;
import top.sboxm.user.pojo.vo.CapacityInfoVO;
import top.sboxm.user.pojo.dto.UpdateCapacityDTO;
import top.sboxm.user.service.CapacityService;
import org.springframework.util.StringUtils;

import static java.lang.Long.parseLong;

/**
 * 用户容量控制器
 */
@RestController
@RequestMapping("/capacity")
public class CapacityController {
    @Resource
    private CapacityService capacityService;

    /**
     * 获取用户容量信息
     *
     * @return 用户容量信息
     */
    @GetMapping("/info")
    public Object getCapacityInfo(@RequestParam("userId") String userId) {
        if (!StringUtils.hasText(userId)) {
            return RestResult.fail(RestResultEnum.INVALID_PARAM);
        }

        CapacityInfoVO capacityInfo = capacityService.getCapacityInfo(parseLong(userId));
        if (capacityInfo == null) {
            return RestResult.fail(RestResultEnum.USER_NOT_FOUND);
        }
        return RestResult.ok().setData(capacityInfo);
    }

    /**
     * 更新用户容量信息（仅供文件服务调用）
     *
     * @param updateCapacityDTO 容量更新信息
     * @return 更新结果
     */
    @PostMapping("/update")
    public Object updateCapacity(@RequestBody UpdateCapacityDTO updateCapacityDTO) {
        if (updateCapacityDTO == null || updateCapacityDTO.getUserId() == null || updateCapacityDTO.getCapacityChange() == null) {
            return RestResult.fail(RestResultEnum.INVALID_PARAM);
        }

        boolean success = capacityService.updateCapacity(updateCapacityDTO);
        return success ? RestResult.ok() : RestResult.fail(RestResultEnum.OPERATION_FAILED);
    }
}
