package top.sboxm.blog.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sboxm.common.enums.RestResultEnum;

@EqualsAndHashCode(callSuper = true)
@Data
public class ArticleException extends RuntimeException {

    private Integer code;
    private String message;

    public ArticleException(RestResultEnum restResultEnum) {
        this.code = restResultEnum.getCode();
        this.message = restResultEnum.getMessage();
    }
}