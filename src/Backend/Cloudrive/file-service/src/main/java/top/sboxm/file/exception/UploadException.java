package top.sboxm.file.exception;

import top.sboxm.common.enums.RestResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UploadException extends RuntimeException{

    private Integer code;


    private String message;

    public UploadException(RestResultEnum restResultEnum){

        this.code=restResultEnum.getCode();

        this.message=restResultEnum.getMessage();
    }
}
