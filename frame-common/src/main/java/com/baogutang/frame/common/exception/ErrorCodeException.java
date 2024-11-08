package com.baogutang.frame.common.exception;

import lombok.Getter;

/**
 * @author N1KO
 */
@Getter
public class ErrorCodeException extends RuntimeException {

    private static final long serialVersionUID = -4556515656361708799L;

    private int errorCode = 200;

    public ErrorCodeException(int errorCode, String message) {
        super(message);
        setErrorCode(errorCode);
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
