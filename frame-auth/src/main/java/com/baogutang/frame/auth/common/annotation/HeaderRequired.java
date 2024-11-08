package com.baogutang.frame.auth.common.annotation;

import com.baogutang.frame.auth.common.header.Header;

import java.lang.annotation.*;

/**
 * 公共请求头校验
 *
 * @author N1KO
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface HeaderRequired {
    Header[] headers();
}
