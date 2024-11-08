package com.baogutang.frame.common.exception;

/**
 * ParamsValidException
 * @author N1KO
 */
public class ParamsValidException extends RuntimeException {

    public ParamsValidException(String message) {
        super(message);
    }

    public ParamsValidException(String message, Throwable e) {
        super(message, e);
    }
}
