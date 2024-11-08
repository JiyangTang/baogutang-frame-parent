package com.baogutang.frame.common.constant.enums;

/**
 * 服务降级返回
 */
public enum HystrixCodeEnum {
    /**
     *
     */
    SYSTEM_FALLBACK(500, "Service error, downgrade processing!");

    public int code;

    public String message;


    HystrixCodeEnum(int i, String message) {
    }
}