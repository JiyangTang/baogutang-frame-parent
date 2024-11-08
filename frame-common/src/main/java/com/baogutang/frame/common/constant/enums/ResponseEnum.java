package com.baogutang.frame.common.constant.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 通用Response枚举类
 * @author N1KO
 */
@Getter
@AllArgsConstructor
public enum ResponseEnum {

    /**
     * 错误码及描述定义
     */
    COMMON_SUCCESS(200, "Successful request"),

    PARAM_EXCEPTION(300, "Parameter exception"),
    ;

    private final Integer code;

    private final String message;

}
