package top.sboxm.user.pojo.vo;

import lombok.Data;

/**
 * 用户容量信息VO类，用于向客户端返回用户存储容量使用情况。
 */
@Data
public class CapacityInfoVO {
    /**
     * 总容量（字节）
     */
    private Long totalCapacity;

    /**
     * 已用容量（字节）
     */
    private Long usedCapacity;

    /**
     * 剩余容量（字节）
     */
    private Long remainingCapacity;

    /**
     * 容量使用率（百分比）
     */
    private Double usageRate;
}