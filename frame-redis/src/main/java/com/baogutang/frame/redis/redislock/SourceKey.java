package com.baogutang.frame.redis.redislock;

public class SourceKey {
  /** redis锁的key */
  private String key;

  /** redis锁的value(随机值UUID) */
  private String requestId;

  public SourceKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getRequestId() {
    return requestId;
  }

  public void setRequestId(String requestId) {
    this.requestId = requestId;
  }

  @Override
  public String toString() {
    return "SourceKey{" + "key='" + key + '\'' + ", requestId=" + requestId + '}';
  }
}
