package com.baogutang.frame.ons.event.ons.consumer.handler;

import com.baogutang.frame.ons.common.MsgConstant;
import com.baogutang.frame.ons.event.ons.consumer.ConsumerConfig;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author N1KO
 */
@Component
@Slf4j
public class MessageHandlerFactory implements ApplicationContextAware {
    private final Map<String, ImessageHandler> handlers = new HashMap<>();
    private final Map<String, Set<ImessageHandler>> topicHandlers = new ConcurrentHashMap<>();
    private final Map<String, Set<String>> topicTags = new ConcurrentHashMap<>();

    private final Map<String, IOrderMessageHandler> orderMsgHandlers = new HashMap<>();

    private final Map<String, Set<IOrderMessageHandler>> orderMsgTopicHandlers = new ConcurrentHashMap<>();

    private final Map<String, Set<String>> orderMsgTopicTags = new ConcurrentHashMap<>();

    private ApplicationContext applicationContext;
    private ConsumerConfig consumerConfig;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        try {
            consumerConfig = applicationContext.getBean(ConsumerConfig.class);
            Map<String, ImessageHandler> handlerMap = applicationContext.getBeansOfType(ImessageHandler.class);
            for (ImessageHandler handler : handlerMap.values()) {
                handlers.put(handler.getTagName(), handler);
                // topic 和tag 关系
                Set<String> tags = topicTags.getOrDefault(handler.getTopicName(), new HashSet<>());
                tags.add(handler.getTagName());
                topicTags.put(handler.getTopicName(), tags);

                Set<ImessageHandler> handlerSet = topicHandlers.getOrDefault(handler.getTopicName(), new HashSet<>());
                handlerSet.add(handler);
                topicHandlers.put(handler.getTopicName(), handlerSet);
            }
            log.info("init message tag handler size {}", handlers.size());

            Map<String, IOrderMessageHandler> orderMsgHandlerMap = applicationContext.getBeansOfType(IOrderMessageHandler.class);
            for (IOrderMessageHandler handler : orderMsgHandlerMap.values()) {
                orderMsgHandlers.put(handler.getTagName(), handler);
                // topic 和tag 关系
                Set<String> tags = orderMsgTopicTags.getOrDefault(handler.getTopicName(), new HashSet<>());
                tags.add(handler.getTagName());
                orderMsgTopicTags.put(handler.getTopicName(), tags);

                Set<IOrderMessageHandler> handlerSet = orderMsgTopicHandlers.getOrDefault(handler.getTopicName(), new HashSet<>());
                handlerSet.add(handler);
                orderMsgTopicHandlers.put(handler.getTopicName(), handlerSet);
            }
            log.info("init order message tag handler size {}", orderMsgHandlers.size());
        } catch (NoSuchBeanDefinitionException e1) {
            log.warn("no consumer config ，could not init consumer");
        } catch (Exception e) {
            log.error("buildOrderConsumerError", e);
        }
    }

    /**
     * getHandlerTags
     *
     * @return getHandlerTags
     */
    public List<String> getHandlerTags() {
        return new ArrayList<>(handlers.keySet());
    }

    /**
     * getHandlerTags
     *
     * @param topicName topicName
     * @return getHandlerTags
     */
    public List<String> getHandlerTags(String topicName) {
        Set<String> tags = topicTags.get(topicName);
        List<String> list = new ArrayList<>();
        if (tags != null) {
            list.addAll(tags);
        }
        return list;
    }

    /**
     * getHandler
     *
     * @param tag tag
     * @return getHandler
     */
    public ImessageHandler getHandler(String tag) {
        return handlers.get(tag);
    }

    /**
     * getHandlerByTopicName
     *
     * @param topic topic
     * @return set
     */
    public Set<ImessageHandler> getHandlerByTopicName(String topic) {
        Set<ImessageHandler> set = topicHandlers.getOrDefault(topic, new HashSet<>());
        Set<ImessageHandler> handlers = new HashSet<>(set);
        if (consumerConfig != null && consumerConfig.getTopic().startsWith(topic)) {
            Set<ImessageHandler> defaultHandlers = topicHandlers.getOrDefault(MsgConstant.DEFAULT_TOPIC_NAME, new HashSet<>());
            handlers.addAll(defaultHandlers);
        }
        return handlers;
    }

    /**
     * getOrderMsgHandlerTags
     *
     * @return list
     */
    public List<String> getOrderMsgHandlerTags() {
        return new ArrayList<>(orderMsgHandlers.keySet());
    }

    /**
     * getOrderMsgHandlerTags
     *
     * @param topicName topic
     * @return list
     */
    public List<String> getOrderMsgHandlerTags(String topicName) {
        Set<String> tags = orderMsgTopicTags.get(topicName);
        List<String> list = new ArrayList<>();
        if (tags != null) {
            list.addAll(tags);
        }
        return list;
    }

    /**
     * getOrderMsgHandler
     *
     * @param tag tag
     * @return IOrderMessageHandler
     */
    public IOrderMessageHandler getOrderMsgHandler(String tag) {
        return orderMsgHandlers.get(tag);
    }

    /**
     * getOrderMsgHandlerByTopicName
     *
     * @param topic topic
     * @return set
     */
    public Set<IOrderMessageHandler> getOrderMsgHandlerByTopicName(String topic) {
        Set<IOrderMessageHandler> set = orderMsgTopicHandlers.getOrDefault(topic, new HashSet<>());
        return new HashSet<>(set);
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
