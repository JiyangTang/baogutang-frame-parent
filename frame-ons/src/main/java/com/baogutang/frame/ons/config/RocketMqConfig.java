package com.baogutang.frame.ons.config;

import com.baogutang.frame.ons.event.ons.consumer.ConsumerConfig;
import com.baogutang.frame.ons.service.MessagePosterOns;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author N1KO
 */
@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "aliyun.rocketmq")
@ConditionalOnProperty(prefix = "aliyun.rocketmq", name = "accessKey")
public class RocketMqConfig implements InitializingBean {

    private String accessKey;

    private String secretKey;

    private String nameSrvAddr;

    private String groupId;

    private String topic;

    @Bean
    public ConsumerConfig consumerConfig() {
        log.info("####RocketMqConsumer initialized");
        return new ConsumerConfig(accessKey, secretKey, nameSrvAddr, groupId, topic);
    }

    @Override
    public void afterPropertiesSet() {
        MessagePosterOns.init(accessKey, secretKey, nameSrvAddr);
        log.info("####RocketMqProducer initialized");
    }
}
