package top.sboxm.common.result;

import lombok.*;
import top.sboxm.common.enums.RestResultEnum;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class RestResult<T> {

    /**
     * 业务是否成功
     * */
    @Getter
    public Boolean isSuccess;

    /**
     * 状态码，主要是否方便前端分类处理
     * */
    public Integer code;

    public String message;

    public T data;

    public static <T> RestResult<T> ok(){



        return RestResult
                .<T>builder()
                .code(RestResultEnum.SUCCESS.getCode())
                .isSuccess(true)
                .message(RestResultEnum.SUCCESS.getMessage())
                .build();
    }

    public static <T> RestResult<T> ok(T content){

        return RestResult
                .<T>builder()
                .code(RestResultEnum.SUCCESS.getCode())
                .data(content)
                .isSuccess(true)
                .message(RestResultEnum.SUCCESS.getMessage())
                .build();
    }

    public static <T> RestResult<T> fail(RestResultEnum restResultEnum){

        return RestResult
                .<T>builder()
                .code(restResultEnum.getCode())
                .isSuccess(false)
                .message(restResultEnum.getMessage())
                .data(null)
                .build();
    }

    public static <T> RestResult<T> fail(String message,Integer code){

        return RestResult
                .<T>builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    public static <T> RestResult<T> fail() {
        return RestResult
                .<T>builder()
                .isSuccess(false)
                .code(RestResultEnum.FAIL.getCode())
                .message(RestResultEnum.FAIL.getMessage())
                .build();
    }
}
