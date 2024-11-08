package com.baogutang.frame.auth.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author N1KO
 */
@Configuration
public class AuthWebConfig implements WebMvcConfigurer {

    @Resource
    private AuthUserHandler authUserHandler;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authUserHandler);
    }

}
