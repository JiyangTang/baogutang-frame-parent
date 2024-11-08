package com.baogutang.frame.ons.event.ons.consumer.listener;

import com.aliyun.openservices.ons.api.Action;
import com.aliyun.openservices.ons.api.ConsumeContext;
import com.aliyun.openservices.ons.api.Message;
import com.aliyun.openservices.ons.api.MessageListener;
import com.baogutang.frame.ons.common.MsgConstant;
import com.baogutang.frame.ons.common.NeedReconsumeException;
import com.baogutang.frame.ons.event.ons.consumer.handler.ImessageHandler;
import com.baogutang.frame.ons.event.ons.consumer.handler.MessageHandlerFactory;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/**
 * @author N1KO
 */
@Component
@Slf4j
public class TopicMessageListener implements MessageListener {

    @Autowired
    private MessageHandlerFactory messageHandlerFactory;

    @Override
    public Action consume(Message message, ConsumeContext context) {
        MDC.put("X-Request-Id", message.getMsgID());
        String bodyString = new String(message.getBody());
        log.info("receive message {} , msgBody {}", message, bodyString);
        String tag = message.getTag();
        String topic = message.getTopic();
        try {
            Set<ImessageHandler> handlers = messageHandlerFactory.getHandlerByTopicName(topic);
            for (ImessageHandler handler : handlers) {
                if (handler != null && Objects.equals(handler.getTagName(), tag)) {
                    long start = System.currentTimeMillis();
                    handler.handler(bodyString, message);
                    log.info("consume tag {} cost {} ms", tag, System.currentTimeMillis() - start);
                    break;
                }
            }
            return Action.CommitMessage;
        } catch (NeedReconsumeException nre) {
            log.error("handleMessageNeedReconsume error:{}, message:{}", nre, message);
            return Action.ReconsumeLater;
        } catch (Exception e) {
            log.error("handle message error:{},message:{}", e, message);
            if (tag.endsWith(MsgConstant.CONSUMER_STRATEGY_RECONSUME)) {
                return Action.ReconsumeLater;
            }
            return Action.CommitMessage;
        } finally {
            MDC.remove("X-Request-Id");
        }
    }
}
