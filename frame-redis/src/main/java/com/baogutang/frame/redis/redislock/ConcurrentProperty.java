package com.baogutang.frame.redis.redislock;

import com.baogutang.frame.redis.redislock.annotation.FailPolicy;
import com.baogutang.frame.redis.redislock.consts.ConcurrentExceptionCodes;

import java.util.List;

public class ConcurrentProperty {

  private String sourceType;

  List<SourceKey> sourceKeys;

  private Integer expireSeconds;

  private FailPolicy failPolicy;

  private Class<?> customStrategy;

  private int retryCount;

  private int retryPeriod;

  public String getSourceType() {
    return sourceType;
  }

  public void setSourceType(String sourceType) {
    this.sourceType = sourceType;
  }

  public List<SourceKey> getSourceKeys() {
    return sourceKeys;
  }

  public void setSourceKeys(List<SourceKey> sourceKeys) {
    this.sourceKeys = sourceKeys;
  }

  public Integer getExpireSeconds() {
    return expireSeconds;
  }

  public void setExpireSeconds(Integer expireSeconds) {
    this.expireSeconds = expireSeconds;
  }

  public FailPolicy getFailPolicy() {
    return failPolicy;
  }

  public void setFailPolicy(FailPolicy failPolicy) {
    this.failPolicy = failPolicy;
  }

  public Class<?> getCustomStrategy() {
    return customStrategy;
  }

  public void setCustomStrategy(Class<?> customStrategy) {
    this.customStrategy = customStrategy;
  }

  public int getRetryCount() {
    return retryCount;
  }

  public void setRetryCount(int retryCount) {
    this.retryCount = retryCount;
  }

  public int getRetryPeriod() {
    return retryPeriod;
  }

  public void setRetryPeriod(int retryPeriod) {
    this.retryPeriod = retryPeriod;
  }

  @Override
  public String toString() {
    return "ConcurrentProperty{"
        + "sourceType='"
        + sourceType
        + '\''
        + ", sourceKeys="
        + sourceKeys
        + ", expireSeconds="
        + expireSeconds
        + ", failPolicy="
        + failPolicy
        + ", customStrategy="
        + customStrategy
        + ", retryCount="
        + retryCount
        + ", retryPeriod="
        + retryPeriod
        + '}';
  }

  public void checkSelf() {
    if (retryCount < 0) {
      throw new ConcurrentException(
          ConcurrentExceptionCodes.RETRY_COUNT_PARAM_ERROR,
          ConcurrentExceptionCodes.getMsg(ConcurrentExceptionCodes.RETRY_COUNT_PARAM_ERROR));
    }

    if (retryPeriod < 0) {
      throw new ConcurrentException(
          ConcurrentExceptionCodes.RETRY_PERIOD_PARAM_ERROR,
          ConcurrentExceptionCodes.getMsg(ConcurrentExceptionCodes.RETRY_PERIOD_PARAM_ERROR));
    }
  }
}
