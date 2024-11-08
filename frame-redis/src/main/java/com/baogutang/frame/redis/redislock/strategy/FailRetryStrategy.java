package com.baogutang.frame.redis.redislock.strategy;

import com.baogutang.frame.redis.redislock.ConcurrentCtrl;
import com.baogutang.frame.redis.redislock.ConcurrentException;
import com.baogutang.frame.redis.redislock.ConcurrentProperty;
import com.baogutang.frame.redis.redislock.SourceKey;
import com.baogutang.frame.redis.redislock.consts.ConcurrentExceptionCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 默认获取锁失败重试
 *
 * @author N1KO
 */
@Service
public class FailRetryStrategy implements FailStrategy {

  private Logger logger = LoggerFactory.getLogger(this.getClass());

  private ConcurrentCtrl concurrentCtrl;

  @Autowired
  public void setConcurrentCtrl(ConcurrentCtrl concurrentCtrl) {
    this.concurrentCtrl = concurrentCtrl;
  }

  @Override
  public boolean operate(SourceKey sourceKey, ConcurrentProperty property) {
    boolean continueFlag = false;

    int retryCount = property.getRetryCount();
    for (int i = 0; i < retryCount; i++) {
      try {
        Thread.sleep(property.getRetryPeriod() * 1000);
        boolean getLockResult =
            concurrentCtrl.tryGetLock(
                property.getSourceType(),
                sourceKey.getKey(),
                sourceKey.getRequestId(),
                property.getExpireSeconds());
        logger.debug(
            "fail retrying , count is "
                + i
                + ", concurrent is "
                + getLockResult
                + " property="
                + property.toString());
        if (getLockResult) {
          continueFlag = true;
          break;
        }
      } catch (InterruptedException e) {
        throw new ConcurrentException(
            ConcurrentExceptionCodes.RETRY_ERROR,
            ConcurrentExceptionCodes.getMsg(ConcurrentExceptionCodes.RETRY_ERROR),
            e);
      }
    }

    logger.debug("fail retry end, result continueFlag is " + continueFlag);
    return continueFlag;
  }
}
