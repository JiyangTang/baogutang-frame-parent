package com.baogutang.frame.redis.redislock.consts;

import java.util.HashMap;
import java.util.Map;

public class ConcurrentExceptionCodes {

  private static Map<String, String> codes = new HashMap<String, String>();

  public static final String CURRENT_REQUEST = "CONCURRENT.001";

  public static final String NOT_FOUND_CONCURRENT_ID = "CONCURRENT.002";

  public static final String CONCURRENT_PATH_PARAM_EMPTY = "CONCURRENT.003";

  public static final String CONCURRENTID_OBJECT_EMPTY = "CONCURRENT.004";

  public static final String CONCURRENT_PROPERTY_EMPTY = "CONCURRENT.005";

  public static final String ANNOTATION_NOT_FOUND = "CONCURRENT.006";

  public static final String XPATH_PROPERTY_NOT_EXIST = "CONCURRENT.006";

  public static final String NOT_FOUND_IMPL_METHOD = "CONCURRENT.007";

  public static final String RETRY_ERROR = "CONCURRENT.008";

  public static final String NOT_FOUND_CUSTOM_STRATEGY = "CONCURRENT.009";

  public static final String CUSTOM_STRATEGY_TYPE_ERROR = "CONCURRENT.010";

  public static final String RETRY_COUNT_PARAM_ERROR = "CONCURRENT.011";

  public static final String RETRY_PERIOD_PARAM_ERROR = "CONCURRENT.012";

  static {
    codes.put(CURRENT_REQUEST, "current request");
    codes.put(NOT_FOUND_CONCURRENT_ID, "not found concurrent id");
    codes.put(CONCURRENT_PATH_PARAM_EMPTY, "concurrent path param is empty");
    codes.put(CONCURRENTID_OBJECT_EMPTY, "concurrent object is empty ");
    codes.put(CONCURRENT_PROPERTY_EMPTY, "concurrent property value is empty ");
    codes.put(ANNOTATION_NOT_FOUND, "annotation not found, interface must have annotation");
    codes.put(XPATH_PROPERTY_NOT_EXIST, "xpath property not exist");
    codes.put(NOT_FOUND_IMPL_METHOD, "not found impl method");
    codes.put(RETRY_ERROR, "retry error");
    codes.put(NOT_FOUND_CUSTOM_STRATEGY, "not found custom strategy");
    codes.put(CUSTOM_STRATEGY_TYPE_ERROR, "custom strategy type error");
    codes.put(RETRY_COUNT_PARAM_ERROR, "retry count param error");
    codes.put(RETRY_PERIOD_PARAM_ERROR, "retry period param error");
  }

  public static String getMsg(String key) {
    return codes.get(key);
  }
}
