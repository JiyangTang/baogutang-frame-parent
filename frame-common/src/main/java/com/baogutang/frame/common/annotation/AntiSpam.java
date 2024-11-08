package com.baogutang.frame.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 *
 * 反 SPAM 注解
 * 作用于方法上面，限定用户请求某个接口所发相同内容固定时间内允许请求的次数
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AntiSpam {

    /**
     * 频率/s
     * @return
     */
    int frequency() default 1;

    /**
     * 时长
     * @return
     */
    int time() default 60;

    /**
     * 时间单位
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
