package top.sboxm.common.enums;

public enum RestResultEnum {

    SUCCESS(200, "操作成功"),
    INVALID_PARAM(400, "非法参数"),
    NEED_LOGIN(401, "需要登录后操作"),
    NO_OPERATOR(403, "无操作权限"),

    SYSTEM_ERROR(500, "系统发生错误"),
    USERNAME_EXIST(501, "用户名已存在"),
    USER_NOT_FOUND(502, "用户不存在"),
    REQUIRE_USERNAME(503, "必须填写用户名"),
    LOGIN_ERROR(504, "用户名或密码错误"),
    EMAIL_EXIST(505, "邮箱已存在"),

    UPLOAD_ERROR(506, "文件上传失败"),
    FILE_SIZE_EXCEED_LIMIT(507, "文件大小超过限制"),
    FILE_TYPE_ILLEGAL(508, "不支持该文件类型"),

    OPERATION_FAILED(509, "操作失败"),

    FILE_NOT_FOUND(510, "找不到文件"),

    UPDATE_PASSWORD_FAILED(511, "密码更新失败"),

    ACCOUNT_EXISTS(512, "该账号已存在"),

    VERIFY_CODE(513, "验证码错误"),
    VERIFY_CODE_EXPIRATION(514,"验证码过期"),
    SEND_MAIL_ERROR(515,"发送邮件失败"),
    QR_CODE_NOT_EXIST(516, "二维码不存在或已过期"),
    QR_CODE_NOT_SCANNED(517, "二维码未被扫描"),
    QR_CODE_ALREADY_CONFIRMED(518, "二维码已被确认"),
    QR_CODE_EXPIRED(519, "二维码已过期"),
    
    ARTICLE_NOT_FOUND(520, "文章不存在"),
    NO_PERMISSION_ARTICLE(521, "无权操作该文章"),
    ARTICLE_AUDIT_FAILED(522, "文章审核失败"),
    
    COMMENT_NOT_FOUND(523, "评论不存在"),
    NO_PERMISSION_COMMENT(524, "无权操作该评论"),
    
    // 文件服务相关异常
    FILE_SHARE_NOT_FOUND(525, "分享不存在"),
    FILE_SHARE_EXPIRED(526, "分享已过期"),
    NO_PERMISSION_FILE_SHARE(527, "无权操作该分享"),
    FILE_SHARE_PASSWORD_ERROR(528, "分享密码错误"),
    INSUFFICIENT_CAPACITY(529, "存储空间不足"),
    FILE_VERIFICATION_FAILED(530, "文件验证失败"),

    OLD_PASSWORD_ERROR(531, "旧密码错误"),

    FAIL(666,"操作失败");

    Integer code;

    String message;

    RestResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}