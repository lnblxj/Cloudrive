package top.sboxm.file.exception.handler;

import top.sboxm.file.exception.FileNotFoundException;
import top.sboxm.file.exception.FileOperationException;
import top.sboxm.file.exception.FileShareException;
import top.sboxm.file.exception.SystemException;
import top.sboxm.file.exception.UploadException;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.result.RestResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

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

    @ExceptionHandler(SQLException.class)
    public Object doOnSqlException(SQLException e){
        e.printStackTrace();
        return RestResult.fail(RestResultEnum.INVALID_PARAM);
    }

    @ExceptionHandler(FileNotFoundException.class)
    public Object doOnFileNotFoundException(FileNotFoundException e) {
        return RestResult.fail(e.getMessage(), e.getCode());
    }

    @ExceptionHandler(FileOperationException.class)
    public Object doOnFileOperationException(FileOperationException e) {
        return RestResult.fail(e.getMessage(), e.getCode());
    }
    
    @ExceptionHandler(FileShareException.class)
    public Object doOnFileShareException(FileShareException e) {
        return RestResult.fail(e.getMessage(), e.getCode());
    }
    
    @ExceptionHandler(UploadException.class)
    public Object doOnUploadException(UploadException e) {
        return RestResult.fail(e.getMessage(), e.getCode());
    }
}
