package top.sboxm.file.exception;

import top.sboxm.common.enums.RestResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class SystemException extends RuntimeException{

    private Integer code;


    private String message;

    public SystemException(RestResultEnum restResultEnum){

        setCode(restResultEnum.getCode());
        setMessage(restResultEnum.getMessage());
    }
}
