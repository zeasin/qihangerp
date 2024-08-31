package cn.qihangerp.common;

import java.io.Serializable;

/**
 * 描述：
 *
 * @author qlp
 * @date 2019-01-07 11:53 AM
 */
public class ApiResult<T> implements Serializable {
    private int result;
    private String msg;
    private T data;

    public ApiResult() {
//        this.code = ApiResultEnum.SUCCESS.getIndex();
//        msg = "SUCCESS";
    }
    public static <T> ApiResult<T> success(){
        ApiResult<T> result = new ApiResult<>();
        result.setResult(ApiResultEnum.SUCCESS.getIndex());
        result.setMsg("SUCCESS");
        return result;
    }
    public static <T> ApiResult<T> success(T data){
        ApiResult<T> result = new ApiResult<>();
        result.setResult(ApiResultEnum.SUCCESS.getIndex());
        result.setMsg("SUCCESS");
        result.setData(data);
        return result;
    }
    public static <T> ApiResult<T> error(String msg){
        ApiResult<T> result = new ApiResult<>();
        result.setResult(ApiResultEnum.SystemException.getIndex());
        result.setMsg(msg);
        return result;
    }
    public static <T> ApiResult<T> error(ApiResultEnum resultEnum,String msg){
        ApiResult<T> result = new ApiResult<>();
        result.setResult(resultEnum.getIndex());
        result.setMsg(msg);
        return result;
    }
    public static <T> ApiResult<T> error(ApiResultEnum resultEnum,String msg,T data){
        ApiResult<T> result = new ApiResult<>();
        result.setResult(resultEnum.getIndex());
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public ApiResult(ApiResultEnum result, String msg) {
        this.result = result.getIndex();
        this.msg = msg;
    }

    public ApiResult(ApiResultEnum result) {
        this.result = result.getIndex();
        this.msg = result.getName();
    }

    public ApiResult(ApiResultEnum result, T data) {
        this.result = result.getIndex();
        this.msg = result.getName();
        this.data = data;
    }

    public ApiResult(ApiResultEnum result, String msg, T data) {
        this.result = result.getIndex();
        this.msg = msg;
        this.data = data;
    }

    public ApiResult(int result, String msg) {
        this.result = result;
        this.msg = msg;
    }

    public ApiResult(int code, String msg, T data) {
        this.result = code;
        this.msg = msg;
        this.data = data;
    }

    public static ApiResult ok(){
        return new ApiResult(ApiResultEnum.SUCCESS,"SUCCESS");
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
