package com.baogutang.frame.common.annotation;

import java.lang.annotation.*;


/**
 * ip 白名单校验
 *
 */
@Target(ElementType.METHOD)
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface WhiteIpRequired {
}
