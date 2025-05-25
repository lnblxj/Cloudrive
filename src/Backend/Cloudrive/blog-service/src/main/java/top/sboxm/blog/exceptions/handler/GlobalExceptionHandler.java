
package top.sboxm.blog.exceptions.handler;

import top.sboxm.blog.exceptions.ArticleException;
import top.sboxm.blog.exceptions.CommentException;
import top.sboxm.blog.exceptions.SearchException;
import top.sboxm.common.enums.RestResultEnum;
import top.sboxm.common.result.RestResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Object doOnException(Exception e) {

        e.printStackTrace();
        return RestResult.fail(RestResultEnum.SYSTEM_ERROR);
    }

    @ExceptionHandler(CommentException.class)
    public Object doOnCommentException(CommentException e){
        e.printStackTrace();
        return RestResult.fail(e.getMessage(),e.getCode());
    }

    @ExceptionHandler(ArticleException.class)
    public Object doOnArticleException(ArticleException e){
        e.printStackTrace();
        return RestResult.fail(e.getMessage(),e.getCode());
    }

    @ExceptionHandler(SearchException.class)
    public Object doOnSearchException(SearchException e){
        e.printStackTrace();
        return RestResult.fail(e.getMessage(),e.getCode());
    }
}