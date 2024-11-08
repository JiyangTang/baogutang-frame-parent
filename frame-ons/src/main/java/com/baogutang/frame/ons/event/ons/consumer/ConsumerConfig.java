package com.baogutang.frame.ons.event.ons.consumer;

import lombok.Data;

/**
 * @author N1KO
 */
@Data
public class ConsumerConfig {
    private String accessKey;
    private String secretKey;
    private String nameSrvAddr;
    private String groupId;
    private String topic;
    private String orderMsgTopic;
    private String orderMsgGroupId;
    private int consumeThreadNums = 20;

    public ConsumerConfig(String accessKey, String secretKey, String nameSrvAddr, String groupId, String topic) {

        this(accessKey, secretKey, nameSrvAddr, groupId, topic, null, null);
    }

    public ConsumerConfig(String accessKey, String secretKey, String nameSrvAddr, String groupId, String topic, String orderMsgTopic, String orderMsgGourpId) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
        this.nameSrvAddr = nameSrvAddr;
        this.groupId = groupId;
        this.topic = topic;
        this.orderMsgTopic = orderMsgTopic;
        this.orderMsgGroupId = orderMsgGourpId;
    }

}
