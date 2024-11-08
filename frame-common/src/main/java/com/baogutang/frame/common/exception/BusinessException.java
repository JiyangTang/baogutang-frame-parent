package com.baogutang.frame.common.exception;

import com.baogutang.frame.common.constant.enums.ErrorCodeEnums;
import com.baogutang.frame.common.constant.enums.ResponseEnum;
import com.baogutang.frame.common.response.Response;
import lombok.Getter;
import org.slf4j.helpers.MessageFormatter;

/**
 * 业务异常类
 * @author N1KO
 */
@Getter
public class BusinessException extends ErrorCodeException {

    public BusinessException(int code, String message) {
        super(code, message);
    }

    public BusinessException(ErrorCodeEnums errorCodeEnums) {
        super(errorCodeEnums.getCode(), errorCodeEnums.getMessage());
    }

    public BusinessException(String message) {
        super(Response.FAIL_CODE, message);
    }

    public BusinessException(String message, Object... obs) {
        super(Response.FAIL_CODE, MessageFormatter.arrayFormat(message, obs).getMessage());
    }

    public BusinessException(ResponseEnum responseEnum) {
        super(responseEnum.getCode(), responseEnum.getMessage());
    }
}
