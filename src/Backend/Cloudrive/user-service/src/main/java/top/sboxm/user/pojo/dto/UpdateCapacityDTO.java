package top.sboxm.user.pojo.dto;

import lombok.Data;

/**
 * 更新用户容量信息DTO
 */
@Data
public class UpdateCapacityDTO {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 容量变化值（字节）
     */
    private String capacityChange;
}