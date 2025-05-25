package top.sboxm.common.restful;

import top.sboxm.common.enums.RestResultEnum;

import java.io.Serializable;
import java.util.Objects;

public class AbstractRestfulVo<T> implements Serializable {
    /**
     * 响应状态码
     */
    private Integer respCode;

    /**
     * 响应消息
     */
    private String respMsg;

    /**
     * 响应BEAN
     */
    private T respData;


    public boolean validateResult() {
        return Objects.equals(this.respCode, RestResultEnum.SUCCESS.getCode());
    }

    public boolean successWhether() {

        return Objects.equals(this.respCode, RestResultEnum.SUCCESS.getCode());
    }

    public boolean errorWhether() {

        return !Objects.equals(this.respCode, RestResultEnum.SUCCESS.getCode());
    }

    public Integer getRespCode() {
        return respCode;
    }

    public void setRespCode(Integer respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public T getRespData() {
        return respData;
    }

    public void setRespData(T respData) {
        this.respData = respData;
    }

    @Override
    public String toString() {
        return "AbstractResultDTO{" +
                "code=" + respCode +
                ", message='" + respMsg + '\'' +
                ", data=" + respData +
                '}';
    }
}
