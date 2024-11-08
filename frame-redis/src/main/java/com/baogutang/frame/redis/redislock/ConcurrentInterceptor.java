package com.baogutang.frame.redis.redislock;

import com.baogutang.frame.redis.redislock.annotation.FailPolicy;
import com.baogutang.frame.redis.redislock.consts.ConcurrentExceptionCodes;
import com.baogutang.frame.redis.redislock.strategy.FailStrategy;
import com.baogutang.frame.redis.redislock.strategy.FailStrategyFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 切面
 *
 * @author N1KO
 */
@Aspect
@Component
@Order(-1)
public class ConcurrentInterceptor {
  private Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  @Qualifier("infConcurrentCtrl")
  private ConcurrentCtrl concurrentCtrl;

  @Autowired private FailStrategyFactory failStrategyFactory;

  @Pointcut("@annotation(com.baogutang.frame.redis.redislock.annotation.Concurrent)")
  public void concurrentAspect() {}

  @Around("concurrentAspect()")
  private Object doAround(ProceedingJoinPoint jp) throws Throwable {
    ConcurrentProperty property = null;
    try {
      long start = System.currentTimeMillis();

      property = ConcurrentPropertyGenerator.genConcurrentProperty(jp);
      doConcurrent(property);

      return jp.proceed();
    } finally {
      if (property != null) {
        List<SourceKey> sourceKeys = property.getSourceKeys();
        if (sourceKeys != null) {
          for (SourceKey sourceKey : sourceKeys) {
            boolean result =
                concurrentCtrl.tryRemoveLock(
                    property.getSourceType(), sourceKey.getKey(), sourceKey.getRequestId());
          }
        }
      }
    }
  }

  private void doConcurrent(ConcurrentProperty property) {
    List<SourceKey> sourceKeys = property.getSourceKeys();
    String sourceType = property.getSourceType();
    Integer expireSeconds = property.getExpireSeconds();
    FailPolicy failPolicy = property.getFailPolicy();

    for (SourceKey sourceKey : sourceKeys) {
      if (!concurrentCtrl.tryGetLock(
          sourceType, sourceKey.getKey(), sourceKey.getRequestId(), expireSeconds)) {
        FailStrategy failStrategy =
            failStrategyFactory.getFailStrategy(failPolicy, property.getCustomStrategy());
        boolean continueFlag = failStrategy.operate(sourceKey, property);
        if (!continueFlag) {
          throw new ConcurrentException(
              ConcurrentExceptionCodes.CURRENT_REQUEST,
              ConcurrentExceptionCodes.getMsg(ConcurrentExceptionCodes.CURRENT_REQUEST));
        }
      }
    }
  }
}
