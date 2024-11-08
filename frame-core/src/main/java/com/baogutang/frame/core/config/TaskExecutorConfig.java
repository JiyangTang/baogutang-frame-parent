package com.baogutang.frame.core.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 多线程配置
 *
 * @author N1KO
 */
@Data
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "thread.pool", name = "core")
@ConfigurationProperties(prefix = "thread.pool")
public class TaskExecutorConfig {

    @Value("${spring.application.name}")
    private String appName;

    private Integer core;

    private Integer max;

    private Integer alive;

    private Integer capacity;

    @Bean
    public ThreadPoolTaskExecutor threadExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(appName);
        executor.setCorePoolSize(core);
        executor.setMaxPoolSize(max);
        executor.setKeepAliveSeconds(alive);
        executor.setQueueCapacity(capacity);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        log.info("####{} ThreadPoolTaskExecutor initialized", appName);
        return executor;
    }
}
