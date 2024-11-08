package com.baogutang.frame.ons.event.ons.consumer;

import com.aliyun.openservices.ons.api.MessageListener;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.bean.ConsumerBean;
import com.aliyun.openservices.ons.api.bean.OrderConsumerBean;
import com.aliyun.openservices.ons.api.bean.Subscription;
import com.aliyun.openservices.ons.api.order.MessageOrderListener;
import com.baogutang.frame.ons.common.MsgConstant;
import com.baogutang.frame.ons.event.ons.consumer.handler.MessageHandlerFactory;
import com.baogutang.frame.ons.event.ons.consumer.listener.TopicMessageListener;
import com.baogutang.frame.ons.event.ons.consumer.listener.TopicMessageOrderListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author N1KO
 */
@Component
@Slf4j
public class ConsumerClientFactory implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    private MessageHandlerFactory handlerFactory;
    @Autowired
    private TopicMessageListener messageListener;
    @Autowired
    private TopicMessageOrderListener messageOrderListener;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        Map<String, ConsumerBean> consumerBeanMap = applicationContext.getBeansOfType(ConsumerBean.class);
        for (Map.Entry<String, ConsumerBean> consumerBeanEntry : consumerBeanMap.entrySet()) {
            String beanName = consumerBeanEntry.getKey();
            ConsumerBean consumerBean = consumerBeanEntry.getValue();
            if (consumerBean.getProperties() == null || consumerBean.getProperties().isEmpty()) {
                continue;
            }
            log.info("ApplicationReadyEventStartedConsumerBeanBeanName:[{}]", beanName);
            startConsumerBean(consumerBean.getProperties().getProperty(PropertyKeyConst.GROUP_ID), consumerBean);
        }

        Map<String, OrderConsumerBean> orderConsumerBeanMap = applicationContext.getBeansOfType(OrderConsumerBean.class);
        for (Map.Entry<String, OrderConsumerBean> consumerBeanEntry : orderConsumerBeanMap.entrySet()) {
            String beanName = consumerBeanEntry.getKey();
            OrderConsumerBean orderConsumerBean = consumerBeanEntry.getValue();
            if (orderConsumerBean.getProperties() == null || orderConsumerBean.getProperties().isEmpty()) {
                continue;
            }
            log.info("ApplicationReadyEventStartedOrderConsumerBeanBeanName:[{}]", beanName);
            startOrderConsumerBean(orderConsumerBean.getProperties().getProperty(PropertyKeyConst.GROUP_ID), orderConsumerBean);
        }
    }

    @Bean
    public ConsumerBean buildConsumer() {
        ConsumerBean consumerBean = new ConsumerBean();
        try {
            ConsumerConfig consumerConfig = handlerFactory.getApplicationContext().getBean(ConsumerConfig.class);
            if (StringUtils.isBlank(consumerConfig.getGroupId())) {
                log.warn("noGroupId，could not init consumer");
                return consumerBean;
            }
            int handlerSize = handlerFactory.getHandlerTags().size();
            if (handlerSize == 0) {
                return consumerBean;
            }
            consumerBean.setProperties(buildProperties(consumerConfig, consumerConfig.getGroupId()));
            Map<Subscription, MessageListener> subscriptionTable = new HashMap<>();
            String topic = consumerConfig.getTopic();
            String[] topicArray = topic.split(",");
            for (int i = 0; i < topicArray.length; i++) {
                String topicName = topicArray[i];
                if (StringUtils.isBlank(topicName)) {
                    continue;
                }
                String tags;
                if (i == 0) {
                    tags = getHandlerTagsByTopic(topicName, true);
                } else {
                    tags = getHandlerTagsByTopic(topicName, false);
                }
                Subscription subscription = new Subscription();
                subscription.setTopic(topicName);
                subscription.setExpression(tags);
                subscriptionTable.put(subscription, messageListener);
                log.info("==== init message consumer ,  accessKey: {} , groupId {}, topic: {}, tags: {}",
                        consumerConfig.getAccessKey(), consumerConfig.getGroupId(), topicName, tags);
            }
            consumerBean.setSubscriptionTable(subscriptionTable);
        } catch (NoSuchBeanDefinitionException e1) {
            log.warn("no consumer config ，could not init consumer");
        } catch (Exception e) {
            log.error("buildConsumerError:", e);
        }
        return consumerBean;
    }

    @Bean
    public OrderConsumerBean buildOrderConsumer() {
        OrderConsumerBean orderConsumerBean = new OrderConsumerBean();
        try {
            ConsumerConfig consumerConfig = handlerFactory.getApplicationContext().getBean(ConsumerConfig.class);
            if (StringUtils.isBlank(consumerConfig.getOrderMsgGroupId())) {
                log.warn("noGroupId，could not init order consumer");
                return orderConsumerBean;
            }
            if (handlerFactory.getOrderMsgHandlerTags().size() == 0) {
                return orderConsumerBean;
            }
            Properties properties = buildProperties(consumerConfig, consumerConfig.getOrderMsgGroupId());
            //最大重试次数设置为60
            properties.setProperty(PropertyKeyConst.MaxReconsumeTimes, "60");
            orderConsumerBean.setProperties(properties);
            //订阅关系
            Map<Subscription, MessageOrderListener> subscriptionTable = new HashMap<>();
            String topic = consumerConfig.getOrderMsgTopic();
            Arrays.stream(topic.split(",")).filter(StringUtils::isNotBlank).forEach(topicName -> {
                String tags = getOrderMsgHandlerTagsByTopic(topicName);
                Subscription subscription = new Subscription();
                subscription.setTopic(topicName);
                subscription.setExpression(tags);
                subscriptionTable.put(subscription, messageOrderListener);
                log.info("init order message consumer ,  accessKey: {} , groupId {}, topic: {}, tags: {}",
                        consumerConfig.getAccessKey(), consumerConfig.getOrderMsgGroupId(), topicName, tags);
            });
            orderConsumerBean.setSubscriptionTable(subscriptionTable);
        } catch (NoSuchBeanDefinitionException e1) {
            log.warn("noGroupId，could not init order consumer");
        } catch (Exception e) {
            log.error("buildOrderConsumerError", e);
        }
        return orderConsumerBean;
    }

    private Properties buildProperties(ConsumerConfig consumerConfig, String groupId) {
        Properties properties = new Properties();
        properties.setProperty(PropertyKeyConst.AccessKey, consumerConfig.getAccessKey());
        properties.setProperty(PropertyKeyConst.SecretKey, consumerConfig.getSecretKey());
        properties.setProperty(PropertyKeyConst.NAMESRV_ADDR, consumerConfig.getNameSrvAddr());
        properties.setProperty(PropertyKeyConst.GROUP_ID, groupId);
        properties.setProperty(PropertyKeyConst.ConsumeThreadNums, String.valueOf(consumerConfig.getConsumeThreadNums()));
        return properties;
    }

    private void startConsumerBean(String groupId, ConsumerBean consumerBean) {
        boolean startResult = false;
        try {
            log.info("lazyStart {} 的consumer", groupId);
            consumerBean.start();
            startResult = consumerBean.isStarted();
        } catch (Exception e) {
            log.error("startConsumerBeanError msg:" + e.getMessage(), e);
        }
        log.info("rocketmq consumer {} startResult {}", groupId, startResult);
    }

    private void startOrderConsumerBean(String groupId, OrderConsumerBean orderConsumerBean) {
        boolean startResult = false;
        try {
            log.info("lazyStart {} 的orderConsumer", groupId);
            orderConsumerBean.start();
            startResult = orderConsumerBean.isStarted();
        } catch (Exception e) {
            log.error("startOrderConsumerBeanError msg:" + e.getMessage(), e);
        }
        log.info("rocketmq orderConsumer {} startResult {}", groupId, startResult);
    }


    /**
     * getTagsByTopic
     *
     * @param topic               topic
     * @param includeDefaultTopic includeDefaultTopic
     * @return str
     */
    private String getHandlerTagsByTopic(String topic, boolean includeDefaultTopic) {
        List<String> list = handlerFactory.getHandlerTags(topic);
        if (includeDefaultTopic) {
            list.addAll(handlerFactory.getHandlerTags(MsgConstant.DEFAULT_TOPIC_NAME));
        }
        return buildTagsExpression(list);
    }

    /**
     * getOrderMsgHandlerTagsByTopic
     *
     * @param topic topic
     * @return str
     */
    private String getOrderMsgHandlerTagsByTopic(String topic) {
        List<String> list = handlerFactory.getOrderMsgHandlerTags(topic);
        return buildTagsExpression(list);
    }

    private String buildTagsExpression(List<String> tagList) {
        if (tagList == null || tagList.size() == 0) {
            return "demo-not-subscribe";
        }
        StringBuilder sb = new StringBuilder();
        Collections.sort(tagList);
        for (String s : tagList) {
            sb.append(s).append("||");
        }
        return sb.substring(0, sb.length() - 2);
    }
}
