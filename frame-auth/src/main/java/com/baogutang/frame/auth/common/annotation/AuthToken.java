package com.baogutang.frame.auth.common.annotation;

import java.lang.annotation.*;

/**
 * token注解
 *
 * @author N1KO
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthToken {

    boolean required() default true;

}
