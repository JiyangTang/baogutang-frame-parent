package com.baogutang.frame.common.constant.enums;

import com.baogutang.frame.common.exception.BusinessException;

/**
 * @author N1KO
 */
public interface ErrorCodeEnums {
    /**
     * getCode
     *
     * @return code
     */
    Integer getCode();

    /**
     * getMessage
     *
     * @return message
     */
    String getMessage();

    /**
     * toExceptionThrow
     */
    default void toExceptionThrow() {
        throw new BusinessException(this);
    }

}
