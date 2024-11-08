package com.baogutang.frame.common.utils;

import com.baogutang.frame.common.component.SnowflakeIdComponent;

/**
 * 唯一单号生产类
 *
 */
public class IdCenterUtils {

  public static String getSequenceNo(String preFix) {
    return preFix + SnowflakeIdComponent.getInstance().nextId();
  }

  public static String getSequenceNo() {
    return SnowflakeIdComponent.getInstance().nextId();
  }

}
