package com.baogutang.frame.ons.event.ons.consumer.listener;

import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.order.ConsumeOrderContext;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.aliyun.openservices.ons.api.order.OrderAction;
import com.baogutang.frame.ons.event.ons.consumer.handler.IOrderMessageHandler;
import com.baogutang.frame.ons.event.ons.consumer.handler.MessageHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/**
 * @author N1KO
 **/
@Component
@Slf4j
public class TopicMessageOrderListener implements MessageOrderListener {

    @Autowired
    private MessageHandlerFactory messageHandlerFactory;

    @Override
    public OrderAction consume(Message message, ConsumeOrderContext consumeOrderContext) {
        MDC.put("X-Request-Id", message.getMsgID());
        String bodyString = new String(message.getBody());
        log.info("receive order message {} , msgBody {}", message, bodyString);
        String tag = message.getTag();
        String topic = message.getTopic();
        try {
            Set<IOrderMessageHandler> handlers = messageHandlerFactory.getOrderMsgHandlerByTopicName(topic);
            for (IOrderMessageHandler handler : handlers) {
                if (handler != null && Objects.equals(handler.getTagName(), tag)) {
                    handler.handler(bodyString, message);
                    break;
                }
            }
            return OrderAction.Success;
        } catch (Exception e) {
            log.error("handle order message error: {}, message:{}", e, message);
            return OrderAction.Suspend;
        } finally {
            MDC.remove("X-Request-Id");
        }
    }
}
