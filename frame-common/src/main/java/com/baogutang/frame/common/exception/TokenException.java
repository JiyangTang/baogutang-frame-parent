package com.baogutang.frame.common.exception;

import lombok.Getter;

/**
 * token异常
 *
 * @author N1KO
 */
@Getter
public class TokenException extends ErrorCodeException {

    public TokenException(int code, String message) {
        super(code, message);
    }


}
