package com.baogutang.frame.redis.redislock.strategy;

import com.baogutang.frame.redis.redislock.ConcurrentProperty;
import com.baogutang.frame.redis.redislock.SourceKey;
import org.springframework.stereotype.Service;

/**
 * 默认获取锁失败什么都不干
 *
 * @author N1KO
 */
@Service
public class FailFastStrategy implements FailStrategy {

  @Override
  public boolean operate(SourceKey sourceKey, ConcurrentProperty property) {
    return false;
  }
}
