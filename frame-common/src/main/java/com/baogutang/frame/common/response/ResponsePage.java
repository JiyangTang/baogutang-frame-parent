package com.baogutang.frame.common.response;

import com.baogutang.frame.common.constant.enums.ResponseEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author N1KO
 * @param <T>
 */
@Data
@AllArgsConstructor
@Builder
public class ResponsePage<T> implements Serializable {

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
    @ApiModelProperty("响应码")
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
    private PageDTO<T> data;

    public ResponsePage() {
    }

    public static <T> ResponsePage<T> ok() {
        return restResult(null, null, SUCCESS_CODE, null);
    }

    public static <T> ResponsePage<T> ok(PageInfo pageInfo, List<T> data) {
        return restResult(pageInfo, data, ResponseEnum.COMMON_SUCCESS.getCode(), ResponseEnum.COMMON_SUCCESS.getMessage());
    }

    public static <T> ResponsePage<T> ok(PageInfo pageInfo, List<T> data, int i, String message) {
        return restResult(pageInfo, data, SUCCESS_CODE, null);
    }

    public static <T> ResponsePage<T> ok(PageInfo pageInfo, List<T> data, String message) {
        return restResult(pageInfo, data, SUCCESS_CODE, message);
    }

    public static <T> ResponsePage<T> result(PageInfo pageInfo, List<T> data, int code, String message) {
        return restResult(pageInfo, data, code, message);
    }

    public static <T> ResponsePage<T> failed() {
        return restResult(null, null, FAIL_CODE, null);
    }

    public static <T> ResponsePage<T> failed(String message) {
        return restResult(null, null, FAIL_CODE, message);
    }

    public static <T> ResponsePage<T> failed(int code, String message) {
        return restResult(null, null, code, message);
    }

    public static <T> ResponsePage<T> failed(PageInfo pageInfo, List<T> data) {
        return restResult(pageInfo, data, FAIL_CODE, null);
    }

    public static <T> ResponsePage<T> failed(PageInfo pageInfo, List<T> data, String message) {
        return restResult(pageInfo, data, FAIL_CODE, message);
    }

    public static <T> ResponsePage<T> restResult(PageInfo pageInfo, List<T> data, int code, String message) {
        ResponsePage<T> apiResult = new ResponsePage<>();
        apiResult.setCode(code);
        apiResult.setData(new PageDTO<T>(data, pageInfo));
        apiResult.setMessage(message);
        return apiResult;
    }

    public Boolean isSuccess() {
        return (this.code == SUCCESS_CODE);
    }

    public static <T> ResponsePage<T> restfulResult(ResponseEnum respEnum, PageInfo pageInfo, List<T> data) {
        ResponsePage<T> apiResult = new ResponsePage<>();
        apiResult.setCode(respEnum.getCode());
        apiResult.setMessage(respEnum.getMessage());
        apiResult.setData(new PageDTO<T>(data, pageInfo));
        return apiResult;
    }


}
