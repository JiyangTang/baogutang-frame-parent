package com.baogutang.frame.ons.event.ons.producer.impl;

import com.aliyun.openservices.ons.api.SendResult;
import com.baogutang.frame.ons.event.ons.IQueueMsg;
import com.baogutang.frame.ons.event.ons.producer.IMsgProducer;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

/**
 * @author N1KO
 */
@Slf4j
public class ProducerFactory {

    public final static ProducerFactory FACTORY = new ProducerFactory();

    private IMsgProducer producer;

    private ProducerFactory() {

    }

    public void init(Properties properties) {
        if (null == producer) {
            log.info("first init message producer");
        } else {
            log.warn("init message producer again!");
            shutdown();
        }
        producer = new MsgProducer(properties);
    }

    public SendResult production(IQueueMsg msg) {
        if (null != msg && null != msg.getMsgType() && null != msg.getMsgBody()) {
            return producer.syncSend(msg);
        }
        return null;
    }

    public void shutdown() {
        producer.close();
        producer = null;
    }
}
