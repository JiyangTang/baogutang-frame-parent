package com.baogutang.frame.ons.common;

import lombok.AllArgsConstructor;

/**
 * @author N1KO
 */
@AllArgsConstructor
public enum TopicTagEnum {

    /**
     * 消息推送tag sms push mail
     */
    MESSAGE_TAG("baogutang", "MESSAGE_SEND_TAG", "baogutang-message:"),
    /**
     * 批量发送消息推送
     */
    MESSAGE_LIST_TAG("baogutang", "MESSAGE_LIST_SEND_TAG", "baogutang-message-list:"),
    ;
    /**
     * topic
     */
    private final String topic;
    /**
     * topic
     */
    private final String tag;
    /**
     * msgKeyPrefix
     */
    private final String msgKeyPrefix;

    public String getTopic() {
        return topic;
    }

    public String getTag() {
        return tag;
    }

    public String getMsgKeyPrefix() {
        return msgKeyPrefix;
    }

}
