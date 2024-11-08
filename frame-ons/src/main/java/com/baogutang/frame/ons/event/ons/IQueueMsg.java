package com.baogutang.frame.ons.event.ons;

import java.io.Serializable;

/**
 * @author N1KO
 */
public interface IQueueMsg extends Serializable {

    /**
     * getMsgType
     *
     * @return QueueType
     */
    QueueType getMsgType();

    /**
     * getTag
     *
     * @return tag
     */
    String getTag();

    /**
     * body
     *
     * @return body
     */
    Object getMsgBody();

    /**
     * getMsgBizKey
     *
     * @return getMsgBizKey
     */
    String getMsgBizKey();

    /**
     * getShardingKey
     *
     * @return getShardingKey
     */
    String getShardingKey();

    /**
     * setStartDeliverTime
     *
     * @param time time
     */
    void setStartDeliverTime(long time);

    /**
     * getStartDeliverTime
     *
     * @return getStartDeliverTime
     */
    long getStartDeliverTime();

    /**
     * getRetryTimes
     *
     * @return getRetryTimes
     */
    int getRetryTimes();

    /**
     * setRetryTimes
     *
     * @param retryTimes retryTimes
     */
    void setRetryTimes(int retryTimes);

}
