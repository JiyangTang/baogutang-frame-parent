package com.baogutang.frame.auth.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * token相关返回码
 *
 * @author N1KO
 */
@Getter
@AllArgsConstructor
public enum TokenCodeEnum {
    /**
     * tokeCodeEnum
     */
    AUTH_TOKEN_EMPTY(-200, "Unable to obtain Token."),
    AUTH_FAILED(-200, "No permission."),
    AUTH_TIME_OUT(-200, "Token has expired."),
    AUTH_TOKEN_ILLEGAL(-200, "Token is illegal.");

    public final int code;

    public final String message;
}
