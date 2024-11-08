package com.baogutang.frame.redis.redislock.annotation;

import java.lang.annotation.*;

/**
 * 被控制的sourceId
 *
 * @author N1KO
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConcurrentId {
  String[] value() default "__self";
}
