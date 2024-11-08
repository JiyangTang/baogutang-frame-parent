package com.baogutang.frame.ons.common;

/**
 * @author N1KO
 */
public class MsgConstant {

    /**
     * 消费端策略：异常时再次消费
     */
    public static final String CONSUMER_STRATEGY_RECONSUME = "RECONSUME";

    /**
     * 调整支持多topic订阅时，兼容旧的项目，未设置handler的topic时，使用默认值
     */
    public static final String DEFAULT_TOPIC_NAME = "DEFAULT_TOPIC_NAME";
}
