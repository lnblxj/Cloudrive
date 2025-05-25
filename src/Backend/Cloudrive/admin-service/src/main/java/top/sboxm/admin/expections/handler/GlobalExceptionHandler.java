//package top.sboxm.user.expections.handler;
//
//public class GlobalExceptionHandler {
//    package top.sboxm.user.exceptions.handler;
//
//import top.sboxm.common.enums.RestResultEnum;
//import top.sboxm.common.result.RestResult;
//import top.sboxm.user.exceptions.FollowException;
//import top.sboxm.user.exceptions.UploadException;
//import top.sboxm.user.exceptions.UserException;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//    @RestControllerAdvice
//    public class GlobalExceptionHandler {
//
//        @ExceptionHandler(Exception.class)
//        public Object doOnException(Exception e) {
//
//            e.printStackTrace();
//            return RestResult.fail(RestResultEnum.SYSTEM_ERROR);
//        }
//
//        @ExceptionHandler(UserException.class)
//        public Object doOnUserException(UserException e) {
//
//            e.printStackTrace();
//            return RestResult.fail(e.getMessage(), e.getCode());
//        }
//
//        @ExceptionHandler(UploadException.class)
//        public Object doOnUploadException(UploadException e) {
//
//            e.printStackTrace();
//            return RestResult.fail(e.getMessage(), e.getCode());
//        }
//
//        @ExceptionHandler(FollowException.class)
//        public Object doOnFollowException(FollowException e) {
//
//            e.printStackTrace();
//            return RestResult.fail(e.getMessage(), e.getCode());
//        }
//    }
//
//}
