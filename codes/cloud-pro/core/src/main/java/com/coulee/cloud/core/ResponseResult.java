package com.coulee.cloud.core;

/**
 * json响应结果包装类
 * 
 * @author zitong
 * @date 2020/09/03
 */
public class ResponseResult<R> {

    /***
     * 成功与否
     */
    private boolean success;
    /**
     * 错误码，成功为0.
     */
    private int code;
    /***
     * 错误提示Or消息提示
     */
    private String msg;
    /***
     * 响应数据
     */
    private R data;

    public static <R> ResponseResult<R> ofSuccess(R data) {
        return new ResponseResult<R>().setSuccess(true).setMsg("success").setData(data);
    }

    public static <R> ResponseResult<R> ofSuccessMsg(String msg) {
        return new ResponseResult<R>().setSuccess(true).setMsg(msg);
    }

    public static <R> ResponseResult<R> ofFail(int code, String msg) {
        ResponseResult<R> result = new ResponseResult<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <R> ResponseResult<R> ofThrowable(int code, Throwable throwable) {
        ResponseResult<R> result = new ResponseResult<>();
        result.setSuccess(false);
        result.setCode(code);
        result.setMsg(throwable.getClass().getName() + ", " + throwable.getMessage());
        return result;
    }

    public boolean isSuccess() {
        return success;
    }

    public ResponseResult<R> setSuccess(boolean success) {
        this.success = success;
        return this;
    }

    public int getCode() {
        return code;
    }

    public ResponseResult<R> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResponseResult<R> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public R getData() {
        return data;
    }

    public ResponseResult<R> setData(R data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "Result{" + "success=" + success + ", code=" + code + ", msg='" + msg + '\'' + ", data=" + data + '}';
    }
}