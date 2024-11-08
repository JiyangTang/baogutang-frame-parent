package com.baogutang.frame.common.exception;

import com.baogutang.frame.common.constant.enums.ErrorCodeEnums;
import com.baogutang.frame.common.constant.enums.ResponseEnum;
import com.baogutang.frame.common.response.Response;
import lombok.Getter;

/**
 * 异常提示到钉盯告警
 *
 * @author N1KO
 */
@Getter
public class Business2DdException extends ErrorCodeException {

    public Business2DdException(int code, String message) {
        super(code, message);
    }

    public Business2DdException(ErrorCodeEnums errorCodeEnums) {
        super(errorCodeEnums.getCode(), errorCodeEnums.getMessage());
    }

    public Business2DdException(String message) {
        super(Response.FAIL_CODE, message);
    }

    public Business2DdException(ResponseEnum responseEnum) {
        super(responseEnum.getCode(), responseEnum.getMessage());
    }
}
