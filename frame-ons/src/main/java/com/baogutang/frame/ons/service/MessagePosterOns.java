
package com.baogutang.frame.ons.service;

import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.shade.com.alibaba.fastjson.JSON;
import com.baogutang.frame.ons.common.TopicTagEnum;
import com.baogutang.frame.ons.event.EventType;
import com.baogutang.frame.ons.event.IEvent;
import com.baogutang.frame.ons.event.OnsMessage;
import com.baogutang.frame.ons.event.ons.IQueueMsg;
import com.baogutang.frame.ons.event.ons.QueueMsg;
import com.baogutang.frame.ons.event.ons.QueueType;
import com.baogutang.frame.ons.event.ons.producer.impl.ProducerFactory;
import com.baogutang.frame.ons.event.sub.QueueEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * @author N1KO
 */
@Slf4j
public class MessagePosterOns {

    private MessagePosterOns() {
    }

    /**
     * init
     *
     * @param accessKey   accessKey
     * @param secretKey   secretKey
     * @param nameSrvAddr nameSrvAddr
     */
    public static void init(String accessKey, String secretKey, String nameSrvAddr) {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, accessKey);
        properties.setProperty(PropertyKeyConst.SecretKey, secretKey);
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, nameSrvAddr);
        ProducerFactory.FACTORY.init(properties);

    }

    /**
     * post
     *
     * @param message message
     * @return res
     */
    public static SendResult post(OnsMessage<IEvent> message) {
        log.info("SendMsgContent: {}", JSON.toJSONString(message));
        try {
            IEvent event = message.getMsgBody();
            if (null != event) {
                if (event.getEventType() == EventType.queue) {
                    QueueEvent queueEvent = (QueueEvent) event;
                    return ProducerFactory.FACTORY.production(queueEvent.getMsg());
                }
            }
        } catch (Exception e) {
            log.error("postOnsError:{},message:{}", e, message);
        }

        return null;
    }

    /**
     * post
     *
     * @param queueMsg queueMsg
     * @return res
     */
    public static SendResult post(IQueueMsg queueMsg) {
        QueueEvent queueEvent = new QueueEvent(queueMsg);
        OnsMessage<IEvent> message = new OnsMessage<>();
        message.setMsgBody(queueEvent);
        return post(message);
    }


    /**
     * sendMsg
     *
     * @param tagName          tagName
     * @param msgBody          msgBody
     * @param msgBizKey        msgBizKey
     * @param startDeliverTime startDeliverTime
     * @param queueType        queueType
     * @return res
     */
    public static SendResult sendMsg(String tagName, Object msgBody, String msgBizKey, long startDeliverTime, QueueType queueType) {
        QueueMsg queueMsg = new QueueMsg(queueType, msgBody, tagName, msgBizKey);
        if (startDeliverTime > 0L) {
            queueMsg.setStartDeliverTime(startDeliverTime);
        }
        QueueEvent queueEvent = new QueueEvent(queueMsg);
        OnsMessage<IEvent> message = new OnsMessage<>();
        message.setMsgBody(queueEvent);
        return post(message);
    }

    public static SendResult sendMsg(String tagName, Object msgBody, String msgBizKey, QueueType queueType) {
        return sendMsg(tagName, msgBody, msgBizKey, 0L, queueType);
    }

    /**
     * sendOrderMsg
     *
     * @param tagName     tagName
     * @param msgBody     msgBody
     * @param msgBizKey   msgBizKey
     * @param shardingKey shardingKey
     * @param queueType   queueType
     * @return res
     */
    public static SendResult sendOrderMsg(String tagName, Object msgBody, String msgBizKey, String shardingKey, QueueType queueType) {
        if (StringUtils.isBlank(shardingKey)) {
            throw new IllegalArgumentException("shardingKeyIsBlank");
        }
        QueueMsg queueMsg = new QueueMsg(queueType, msgBody, tagName, msgBizKey, shardingKey);
        QueueEvent queueEvent = new QueueEvent(queueMsg);
        OnsMessage<IEvent> message = new OnsMessage<>();
        message.setMsgBody(queueEvent);
        return post(message);
    }

    public static SendResult sendMessage(Object msgBody, String msgBizKey) {
        QueueMsg queueMsg = new QueueMsg(TopicTagEnum.MESSAGE_TAG, msgBody, msgBizKey);
        QueueEvent queueEvent = new QueueEvent(queueMsg);
        OnsMessage<IEvent> message = new OnsMessage<>();
        message.setMsgBody(queueEvent);
        return post(message);
    }

    public static SendResult sendMessageList(Object msgBody, String msgBizKey) {
        QueueMsg queueMsg = new QueueMsg(TopicTagEnum.MESSAGE_LIST_TAG, msgBody, msgBizKey);
        QueueEvent queueEvent = new QueueEvent(queueMsg);
        OnsMessage<IEvent> message = new OnsMessage<>();
        message.setMsgBody(queueEvent);
        return post(message);
    }

    /**
     * @param tagName   tagName
     * @param msgBody   msgBody
     * @param msgBizKey msgBizKey
     * @return res
     */
    public static SendResult sendHcMsg(String tagName, Object msgBody, String msgBizKey) {
        return sendMsg(tagName, msgBody, msgBizKey, QueueType.HAPPY_CASH);
    }


}
