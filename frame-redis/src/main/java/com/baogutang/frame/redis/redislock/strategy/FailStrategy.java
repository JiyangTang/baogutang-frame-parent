package com.baogutang.frame.redis.redislock.strategy;

import com.baogutang.frame.redis.redislock.ConcurrentProperty;
import com.baogutang.frame.redis.redislock.SourceKey;

/**
 * 失败策略
 *
 * @author N1KO
 */
public interface FailStrategy {

  /**
   * 被并发后的操作
   *
   * @return 是否正常进行
   */
  boolean operate(SourceKey sourceKey, ConcurrentProperty property);
}
