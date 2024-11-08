package com.baogutang.frame.redis.redislock.annotation;

public enum FailPolicy {

  /** 失败处理 */
  FAIL_FAST,

  /** 重试处理 */
  FAIL_RETRY,

  /** 自定义处理方式 */
  FAIL_CUSTOM
}
