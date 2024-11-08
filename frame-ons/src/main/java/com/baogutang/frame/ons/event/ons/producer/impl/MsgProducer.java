package com.baogutang.frame.ons.event.ons.producer.impl;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.SendResult;
import com.aliyun.openservices.ons.api.bean.OrderProducerBean;
import com.aliyun.openservices.ons.api.bean.ProducerBean;
import com.baogutang.frame.common.utils.JacksonUtil;
import com.baogutang.frame.ons.event.ons.IQueueMsg;
import com.baogutang.frame.ons.event.ons.QueueType;
import com.baogutang.frame.ons.event.ons.producer.IMsgProducer;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * 阿里云rocketMq 发送
 *
 * @author N1KO
 */
@Slf4j
public class MsgProducer implements IMsgProducer {

    private final ProducerBean producer;

    private final OrderProducerBean orderProducer;

    public MsgProducer(Properties props) {
        producer = new ProducerBean();
        producer.setProperties(props);
        producer.start();
        orderProducer = new OrderProducerBean();
        orderProducer.setProperties(props);
        orderProducer.start();
    }


    @Override
    public SendResult syncSend(IQueueMsg queueMsg) {
        int retryTimes = queueMsg.getRetryTimes();
        int maxRetryTimes = 10;
        if (retryTimes > maxRetryTimes) {
            log.warn("msgReachMaxRetryTimes {}", JacksonUtil.toJson(queueMsg));
            return null;
        }
        String bodyJson = JacksonUtil.toJson(queueMsg.getMsgBody());
        Message message = new Message(
                queueMsg.getMsgType().getTopicName(),
                queueMsg.getTag(),
                bodyJson.getBytes());
        message.setKey(queueMsg.getMsgBizKey());
        boolean orderMsgFlag = QueueType.isOrderMsg(queueMsg.getMsgType());
        if (orderMsgFlag) {
            message.setShardingKey(queueMsg.getShardingKey());
        } else if (queueMsg.getStartDeliverTime() > 0L) {
            message.setStartDeliverTime(queueMsg.getStartDeliverTime());
        }
        SendResult sendResult = null;
        try {
            long start = System.currentTimeMillis();
            if (orderMsgFlag) {
                sendResult = orderProducer.send(message, message.getShardingKey());
            } else {
                sendResult = producer.send(message);
            }
            log.info("send{} msg success {} , cost time ms {}", orderMsgFlag ? " order " : " ", sendResult, (System.currentTimeMillis() - start));
        } catch (Throwable e) {
            log.error("send {} msg error {} , message: {} ", orderMsgFlag ? " order " : " ", e.getMessage(), JacksonUtil.toJson(message), e);
            queueMsg.setRetryTimes(retryTimes + 1);
            saveMsgToCache(queueMsg);
        }

        return sendResult;
    }

    private void saveMsgToCache(IQueueMsg queueMsg) {
        String json = JacksonUtil.toJson(queueMsg);
        log.info("saveMsgToCache, content:{} ", json);
    }


    @Override
    public void close() {
        try {
            if (null != producer) {
                producer.shutdown();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        try {
            if (null != orderProducer) {
                orderProducer.shutdown();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

}
