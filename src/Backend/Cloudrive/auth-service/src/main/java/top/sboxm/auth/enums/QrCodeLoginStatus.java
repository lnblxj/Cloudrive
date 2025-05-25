package top.sboxm.auth.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 二维码登录状态枚举
 */
@Getter
@AllArgsConstructor
public enum QrCodeLoginStatus {
    
    /**
     * 未扫描
     */
    NOT_SCANNED(0, "未扫描"),
    
    /**
     * 已扫描
     */
    SCANNED(1, "已扫描"),
    
    /**
     * 已确认登录
     */
    SUCCESS(2, "已确认登录"),
    
    /**
     * 已过期
     */
    EXPIRED(3, "已过期");
    
    private final Integer code;
    private final String desc;
}