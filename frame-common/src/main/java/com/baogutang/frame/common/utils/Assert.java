package com.baogutang.frame.common.utils;

/**
 * 扩充断言函数
 *
 * @date
 */
public class Assert extends org.springframework.util.Assert {

    /**
     * 获取非null数据，如果为null，则根据msg抛{@link IllegalArgumentException}异常，否则原样返回
     * 
     * @param obj
     * @param msg
     * @return
     */
    public static <T> T getNotNull(T obj, String msg) {
        notNull(obj, msg);
        return obj;
    }
}
