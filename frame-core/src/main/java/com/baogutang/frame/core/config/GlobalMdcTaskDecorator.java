package com.baogutang.frame.core.config;

import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Map;

/**
 * 多线程情况下， 将链路id带入子线程
 * @author N1KO
 */
@Configuration
public class GlobalMdcTaskDecorator implements TaskDecorator, BeanPostProcessor {

    @NotNull
    @Override
    public Runnable decorate(@NotNull Runnable runnable) {
        Map<String, String> mdcContext = MDC.getCopyOfContextMap();
        return () -> {
            try {
                if (mdcContext!=null) {
                    MDC.setContextMap(mdcContext);
                }
                runnable.run();
            } finally {
                MDC.clear();
            }
        };
    }


    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName) throws BeansException {
        if (bean instanceof ThreadPoolTaskExecutor) {
            ((ThreadPoolTaskExecutor) bean).setTaskDecorator(this);
        }
        return bean;
    }

}
