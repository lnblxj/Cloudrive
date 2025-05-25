package top.sboxm.auth.exception.handler;

import top.sboxm.auth.exception.SystemException;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.result.RestResult;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object doOnException(Exception e){

        e.printStackTrace();
        return RestResult.fail(RestResultEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(SystemException.class)
    public Object doOnSystemException(SystemException e){

        return RestResult.fail(e.getMessage(),e.getCode());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public Object doOnBadCredentialsException(BadCredentialsException e){
        e.printStackTrace();

        return RestResult.fail(e.getMessage(),401);
    }

    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public Object doOnInternalAuthenticationServiceException(InternalAuthenticationServiceException e){
        return RestResult.fail("用户名或密码错误",401);
    }

}
