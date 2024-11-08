package com.baogutang.frame.ons.event.ons.producer;

import com.aliyun.openservices.ons.api.SendResult;
import com.baogutang.frame.ons.event.ons.IQueueMsg;

/**
 * @author N1KO
 */
public interface IMsgProducer {

    /**
     * 同步发送
     *
     * @param msg msg
     * @return res
     */
    SendResult syncSend(IQueueMsg msg);

    /**
     * close
     */
    void close();


}
