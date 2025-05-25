package top.sboxm.file.pojo.dto;

import lombok.Data;

@Data
public class UpdateCapacityDTO {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 容量变化值（字节），正数表示增加，负数表示减少
     */
    private String capacityChange;
}