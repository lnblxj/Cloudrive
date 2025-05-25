package top.sboxm.file.exception;

import top.sboxm.common.enums.RestResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileShareException extends RuntimeException {
    private Integer code;
    private String message;

    public FileShareException(RestResultEnum restResultEnum) {
        this.code = restResultEnum.getCode();
        this.message = restResultEnum.getMessage();
    }

    public FileShareException(String message) {
        this.code = RestResultEnum.OPERATION_FAILED.getCode();
        this.message = message;
    }
}