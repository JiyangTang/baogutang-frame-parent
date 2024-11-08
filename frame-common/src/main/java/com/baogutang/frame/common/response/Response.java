package com.baogutang.frame.common.response;

import com.baogutang.frame.common.constant.enums.ErrorCodeEnums;
import com.baogutang.frame.common.constant.enums.ResponseEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;

/**
 * @author N1KO
 * @param <T>
 */
@Data
@AllArgsConstructor
@Builder
public class Response<T> implements Serializable {

    /**
     * 成功
     */
    public static final int SUCCESS_CODE = 200;

    /**
     * 失败
     */
    public static final int FAIL_CODE = 300;

    /**
     * 参数不合法
     */
    public static final int PARAM_ILLEGAL_CODE = 301;

    /**
     * 响应码
     */
    @ApiModelProperty("响应码,200为请求成功,300为运行时异常,301为参数不合法,-200为token不合法跳登录页;其他异常均为业务异常")
    private int code;

    /**
     * 响应信息
     */
    @ApiModelProperty("响应信息")
    private String message;

    /**
     * 业务数据
     */
    @ApiModelProperty("业务数据")
    private T data;

    @ApiModelProperty("全局链路请求id")
    private String rid;

    private boolean success;

    public Response() {
    }

    public Response(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public Response(ErrorCodeEnums errorCodeEnums) {
        this.code = errorCodeEnums.getCode();
        this.message = errorCodeEnums.getMessage();
    }

    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> Response<T> ok() {
        return restResult(null, SUCCESS_CODE, null);
    }

    public static <T> Response<T> ok(T data) {
        return restResult(data, ResponseEnum.COMMON_SUCCESS.getCode(), ResponseEnum.COMMON_SUCCESS.getMessage());
    }

    public static <T> Response<T> ok(T data, int i, String message) {
        return restResult(data, SUCCESS_CODE, null);
    }

    public static <T> Response<T> ok(String key, T data) {
        HashMap<String, String> map = new HashMap<>();
        map.put(key, String.valueOf(data));
        return (Response<T>) restResult(map, SUCCESS_CODE, null);
    }

    public static <T> Response<T> ok(T data, String message) {
        return restResult(data, SUCCESS_CODE, message);
    }

    public static <T> Response<T> result(T data, int code, String message) {
        return restResult(data, code, message);
    }

    public static <T> Response<T> failed() {
        return restResult(null, FAIL_CODE, null);
    }

    public static <T> Response<T> failed(String message) {
        return restResult(null, FAIL_CODE, message);
    }

    public static <T> Response<T> failed(int code, String message) {
        return restResult(null, code, message);
    }

    public static <T> Response<T> failed(ErrorCodeEnums errorCodeEnums) {
        return restResult(null, errorCodeEnums.getCode(), errorCodeEnums.getMessage());
    }

    public static <T> Response<T> failed(T data) {
        return restResult(data, FAIL_CODE, null);
    }

    public static <T> Response<T> failed(T data, String message) {
        return restResult(data, FAIL_CODE, message);
    }

    public static <T> Response<T> restResult(T data, int code, String message) {
        Response<T> apiResult = new Response<>();
        apiResult.setCode(code);
        apiResult.setData(data);
        apiResult.setMessage(message);
        return apiResult;
    }

    public Boolean isSuccess() {
        return (this.code == SUCCESS_CODE);
    }

    public static <T> boolean isNotSuccess(Response<T> response) {
        return !isSuccess(response);
    }

    public static <T> boolean isSuccess(Response<T> response) {
        return response != null && response.code == SUCCESS_CODE;
    }

    public static <T> Response<T> restfulResult(ResponseEnum respEnum, T data) {
        Response<T> apiResult = new Response<>();
        apiResult.setCode(respEnum.getCode());
        apiResult.setMessage(respEnum.getMessage());
        apiResult.setData(data);
        return apiResult;
    }

    public void setErrorCodeEnum(ErrorCodeEnums errorCodeEnums) {
        this.code = errorCodeEnums.getCode();
        this.message = errorCodeEnums.getMessage();
    }

    public void setErrorCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
