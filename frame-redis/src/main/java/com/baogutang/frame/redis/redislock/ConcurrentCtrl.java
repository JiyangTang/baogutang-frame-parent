package com.baogutang.frame.redis.redislock;

import com.baogutang.frame.redis.utils.RedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 锁定服务
 *
 * @author N1KO
 */
@Service("infConcurrentCtrl")
public class ConcurrentCtrl {

  private static final String redisKeyPrefix = "inf-concurrent-redis:";

  @Autowired private RedisUtils redisUtils;

  /** 获得lock时，requestId作为value */
  public boolean tryGetLock(String sourceType, String key, String requestId, int expiredSeconds) {
    if (StringUtils.isBlank(sourceType) || StringUtils.isBlank(key)) {
      return false;
    }

    String lockKey = generateCtrlKey(sourceType, key);
    return redisUtils.lock(lockKey, requestId, expiredSeconds, TimeUnit.SECONDS);
  }

  /** 删除lock时，校验requestId */
  public boolean tryRemoveLock(String sourceType, String key, String requestId) {
    String lockKey = generateCtrlKey(sourceType, key);
    return redisUtils.unlock(lockKey, requestId);
  }

  private String generateCtrlKey(String sourceType, String key) {
    return redisKeyPrefix + sourceType + ":" + key;
  }
}
