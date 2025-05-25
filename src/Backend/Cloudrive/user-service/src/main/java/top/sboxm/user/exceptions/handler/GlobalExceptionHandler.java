package top.sboxm.user.exceptions.handler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.result.RestResult;
import top.sboxm.user.exceptions.UserException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object doOnException(Exception e){

        e.printStackTrace();
        return RestResult.fail(RestResultEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(UserException.class)
    public Object doOnUserException(UserException e){

        e.printStackTrace();
        return RestResult.fail(e.getMessage(),e.getCode());
    }
}