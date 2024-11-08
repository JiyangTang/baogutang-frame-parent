package com.baogutang.frame.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 防止重复请求
 * @author N1KO
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface AvoidRepeatableCommit {
    /**
     * 指定时间内不可重复提交,单位秒
     *
     * @return
     */
    long timeout() default 1;


    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 指定key
     *
     * @return
     */
    String key() default "";

    /**
     * 是否限制单个人的， 包含token
     *
     * @return
     */
    boolean includeToken() default true;

    /**
     * 指定提示消息
     *
     * @return
     */
    String message() default "";

}
