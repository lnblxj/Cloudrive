package top.sboxm.user.constants;

/**
 * Redis相关常量
 */
public class RedisConstants {
    /**
     * 重置密码验证码key前缀
     */
    public static final String RESET_PWD_CODE = "reset_code:";

    /**
     * 重置密码验证码过期时间（分钟）
     */
    public static final long RESET_PWD_CODE_TTL = 15;
    
    /**
     * 注册验证码key前缀
     */
    public static final String REGISTER_CODE = "register_code:";

    /**
     * 注册验证码过期时间（分钟）
     */
    public static final long REGISTER_CODE_TTL = 15;
}