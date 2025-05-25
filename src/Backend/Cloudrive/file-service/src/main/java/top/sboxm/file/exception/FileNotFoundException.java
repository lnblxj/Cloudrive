package top.sboxm.file.exception;

import top.sboxm.common.enums.RestResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileNotFoundException extends RuntimeException {
    private Integer code;
    private String message;

    public FileNotFoundException(RestResultEnum restResultEnum) {
        this.code = restResultEnum.getCode();
        this.message = restResultEnum.getMessage();
    }

    public FileNotFoundException(String message) {
        this.code = RestResultEnum.FILE_NOT_FOUND.getCode();
        this.message = message;
    }
}