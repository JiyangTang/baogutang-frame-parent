package com.baogutang.frame.common.annotation;

import java.lang.annotation.*;

/**
 * RequestParamChecker
 *
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParamChecker {

  /*** 是否非空*/
  boolean notNull() default false;

  /*** 是否为数值*/
  boolean isNum() default false;

  /*** <=最大值*/
  int maxVal() default 999999999;

  /*** >=最小值*/
  int minVal() default -999999999;

  /***固定长度*/
  int fixLen() default -1;

  /*** 最大长度*/
  int maxLen() default -1;

  /*** 最小长度*/
  int minLen() default -1;

  /***正则表达式*/
  String pattern() default "*";

  /***身份证*/
  boolean idCard() default false;
}
