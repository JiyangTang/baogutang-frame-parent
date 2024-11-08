package com.baogutang.frame.ons.event.ons.consumer.handler;

import com.aliyun.openservices.ons.api.Message;

/**
 * @author N1KO
 **/
public interface IOrderMessageHandler {

    /**
     * handler
     *
     * @param bodyString body
     * @param message    message
     */
    void handler(String bodyString, Message message);

    /**
     * getTagName
     *
     * @return tag
     */
    String getTagName();

    /**
     * getTopicName
     *
     * @return topic
     */
    String getTopicName();
}
