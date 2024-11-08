package com.baogutang.frame.ons.event.ons.consumer.handler;

import com.aliyun.openservices.ons.api.Message;
import com.baogutang.frame.ons.common.MsgConstant;

/**
 * @author N1KO
 */
public interface ImessageHandler {
    /**
     * handler
     *
     * @param bodyString bodyStr
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
    default String getTopicName() {
        return MsgConstant.DEFAULT_TOPIC_NAME;
    }
}
