package com.baogutang.frame.redis.redislock.annotation;

import com.baogutang.frame.redis.redislock.strategy.FailFastStrategy;

import java.lang.annotation.*;

/**
 * 被并发控制的方法
 *
 * @author N1KO
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Concurrent {
  String value();

  int expireSeconds() default 60;

  FailPolicy failPolicy() default FailPolicy.FAIL_FAST;

  Class<?> customStrategy() default FailFastStrategy.class;

  int retryCount() default 3;

  int retryPeriod() default 3;
}
