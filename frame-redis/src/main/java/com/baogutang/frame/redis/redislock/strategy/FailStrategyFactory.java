package com.baogutang.frame.redis.redislock.strategy;

import com.baogutang.frame.redis.redislock.ConcurrentException;
import com.baogutang.frame.redis.redislock.annotation.FailPolicy;
import com.baogutang.frame.redis.redislock.consts.ConcurrentExceptionCodes;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * 失败策略工厂
 *
 * @author N1KO
 */
@Service
public class FailStrategyFactory implements ApplicationContextAware, InitializingBean {

  private Map<FailPolicy, FailStrategy> strategyMap = new HashMap<>();

  private ApplicationContext applicationContext;

  @Autowired private FailFastStrategy failFastStrategy;

  @Autowired private FailRetryStrategy failRetryStrategy;

  public FailStrategy getFailStrategy(FailPolicy policy, Class<?> customStrategyClass) {
    FailStrategy failStrategy = strategyMap.get(policy);
    if (failStrategy == null) {
      try {
        Object bean = applicationContext.getBean(customStrategyClass);
        if (!(bean instanceof FailStrategy)) {
          throw new ConcurrentException(
              ConcurrentExceptionCodes.CUSTOM_STRATEGY_TYPE_ERROR,
              ConcurrentExceptionCodes.getMsg(ConcurrentExceptionCodes.CUSTOM_STRATEGY_TYPE_ERROR));
        }
        failStrategy = (FailStrategy) bean;
      } catch (BeansException e) {
        throw new ConcurrentException(
            ConcurrentExceptionCodes.NOT_FOUND_CUSTOM_STRATEGY,
            ConcurrentExceptionCodes.getMsg(ConcurrentExceptionCodes.NOT_FOUND_CUSTOM_STRATEGY),
            e);
      }
    }
    return failStrategy;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    strategyMap.put(FailPolicy.FAIL_FAST, failFastStrategy);
    strategyMap.put(FailPolicy.FAIL_RETRY, failRetryStrategy);
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
