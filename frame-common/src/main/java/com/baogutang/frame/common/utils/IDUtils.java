package com.baogutang.frame.common.utils;

import java.util.UUID;

/**
 * 唯一ID生成工具类
 * @author N1KO
 */
public class IDUtils {

    private static byte[] lock = new byte[0]; 	// 位数，默认是4位

    private final static long w = 10000;

    public static String createOrderId(String var1) {
        long r = 0;
        synchronized (lock) {
            r = (long) ((Math.random() + 1) * w);
        }
        return var1 + System.currentTimeMillis() + String.valueOf(r).substring(1);
    }

    public static String getUUID(){
        String orderId = UUID.randomUUID().toString();
        orderId = orderId.replace("-","");
        return orderId;
    }
}
