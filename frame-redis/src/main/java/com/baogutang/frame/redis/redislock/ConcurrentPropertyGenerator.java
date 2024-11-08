package com.baogutang.frame.redis.redislock;

import com.baogutang.frame.redis.redislock.annotation.Concurrent;
import com.baogutang.frame.redis.redislock.annotation.ConcurrentId;
import com.baogutang.frame.redis.redislock.annotation.FailPolicy;
import com.baogutang.frame.redis.redislock.consts.ConcurrentExceptionCodes;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotReadablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
public class ConcurrentPropertyGenerator {


  static ConcurrentProperty genConcurrentProperty(ProceedingJoinPoint jp) {
    Concurrent annotation = findConcurrent(jp);
    String sourceType = annotation.value();
    int expireSeconds = annotation.expireSeconds();
    FailPolicy failPolicy = annotation.failPolicy();
    int retryCount = annotation.retryCount();
    int retryPeriod = annotation.retryPeriod();
    List<SourceKey> sourceKeys = findSourceKeys(jp);

    ConcurrentProperty property = new ConcurrentProperty();
    property.setSourceType(sourceType);
    property.setSourceKeys(sourceKeys);
    property.setExpireSeconds(expireSeconds);
    property.setFailPolicy(failPolicy);
    property.setRetryCount(retryCount);
    property.setRetryPeriod(retryPeriod);
    property.setCustomStrategy(annotation.customStrategy());

    property.checkSelf();
    return property;
  }

  private static Concurrent findConcurrent(ProceedingJoinPoint jp) {
    Method currentMethod = getCurrentMethod(jp);
    return currentMethod.getAnnotation(Concurrent.class);
  }

  private static List<SourceKey> findSourceKeys(ProceedingJoinPoint jp) {
    List<SourceKey> sourceKeys = findFromParam(jp);

    if (sourceKeys.isEmpty()) {
      throw new ConcurrentException(
          ConcurrentExceptionCodes.NOT_FOUND_CONCURRENT_ID,
          ConcurrentExceptionCodes.getMsg(ConcurrentExceptionCodes.NOT_FOUND_CONCURRENT_ID));
    }
    for (SourceKey sourceKey : sourceKeys) {
      sourceKey.setRequestId(UUID.randomUUID().toString()); // 为每个key绑定requestId，作为唯一标识
    }
    return sourceKeys;
  }

  /** 从方法参数中获取key */
  private static List<SourceKey> findFromParam(ProceedingJoinPoint jp) {
    List<SourceKey> sourceKeys = new ArrayList<>();

    Object[] args = jp.getArgs();
    Method currentMethod = getCurrentMethod(jp);
    final Annotation[][] an = currentMethod.getParameterAnnotations();
    if (an.length > 0) {
      for (int i = 0; i < an.length; i++) {
        for (int j = 0; j < an[i].length; j++) {
          Annotation annotation = an[i][j];
          if (annotation instanceof ConcurrentId) {
            if (args[i] == null) {
              throw new ConcurrentException(
                  ConcurrentExceptionCodes.CONCURRENTID_OBJECT_EMPTY,
                  ConcurrentExceptionCodes.getMsg(
                      ConcurrentExceptionCodes.CONCURRENTID_OBJECT_EMPTY));
            }

            ConcurrentId concurrentId = (ConcurrentId) annotation;
            String[] values = concurrentId.value();
            for (String path : values) {
              if (StringUtils.isBlank(path)) {
                throw new ConcurrentException(
                    ConcurrentExceptionCodes.CONCURRENTID_OBJECT_EMPTY,
                    ConcurrentExceptionCodes.getMsg(
                        ConcurrentExceptionCodes.CONCURRENTID_OBJECT_EMPTY));
              }

              String key;
              if ("__self".equals(path)) {
                key = transObjToString(args[i]);
              } else {
                key = findByXPath(args[i], path);
              }
              if (StringUtils.isNotBlank(key)) {
                sourceKeys.add(new SourceKey(path + key));
              }
            }
          }
        }
      }
    }
    return sourceKeys;
  }

  private static Method getCurrentMethod(ProceedingJoinPoint jp) {
    Object target = jp.getTarget();
    MethodSignature signature = (MethodSignature) jp.getSignature();
    Method currentMethod;
    try {
      currentMethod =
          target.getClass().getMethod(signature.getName(), signature.getParameterTypes());
    } catch (NoSuchMethodException e) {
      log.error("not found impl method", e);
      throw new ConcurrentException(
          ConcurrentExceptionCodes.NOT_FOUND_IMPL_METHOD,
          ConcurrentExceptionCodes.getMsg(ConcurrentExceptionCodes.NOT_FOUND_IMPL_METHOD));
    }
    return currentMethod;
  }

  private static String transObjToString(Object obj) {
    if (obj == null) {
      return null;
    }
    return obj.toString();
  }

  private static String findByXPath(Object obj, String path) {
    BeanWrapper b = PropertyAccessorFactory.forBeanPropertyAccess(obj);
    Object propertyValue;
    try {
      propertyValue = b.getPropertyValue(path);
    } catch (NotReadablePropertyException e) {
      throw new ConcurrentException(
          ConcurrentExceptionCodes.XPATH_PROPERTY_NOT_EXIST,
          ConcurrentExceptionCodes.getMsg(ConcurrentExceptionCodes.XPATH_PROPERTY_NOT_EXIST),
          e);
    }
    return transObjToString(propertyValue);
  }
}
