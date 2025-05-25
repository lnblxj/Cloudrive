package top.sboxm.user.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;
import top.sboxm.common.enums.RestResultEnum;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserException extends RuntimeException{

    private Integer code;


    private String message;

    public UserException(RestResultEnum restResultEnum){

        setCode(restResultEnum.getCode());
        setMessage(restResultEnum.getMessage());
    }

}