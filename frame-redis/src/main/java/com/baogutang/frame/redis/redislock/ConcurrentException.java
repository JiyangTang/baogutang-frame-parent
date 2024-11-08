package com.baogutang.frame.redis.redislock;

/**
 * 异常
 *
 * @author N1KO
 */
public class ConcurrentException extends RuntimeException {
  private static final long serialVersionUID = 7126601916487893261L;

  private String code;
  private String localizedMessage;

  public ConcurrentException(String code, String message) {
    super("ConcurrentException[" + code + "]: " + message);
    this.code = code;
    this.localizedMessage = message;
  }

  public ConcurrentException(String code, String message, Throwable t) {
    super("ConcurrentException[" + code + "]: " + message, t);
    this.code = code;
    this.localizedMessage = message;
  }

  public String getCode() {
    return this.code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String getLocalizedMessage() {
    return localizedMessage;
  }

  public void setLocalizedMessage(String localizedMessage) {
    this.localizedMessage = localizedMessage;
  }
}
