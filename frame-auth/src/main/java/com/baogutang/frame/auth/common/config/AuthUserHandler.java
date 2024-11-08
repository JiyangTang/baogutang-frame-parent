package com.baogutang.frame.auth.common.config;

import com.baogutang.frame.auth.common.utils.UserThreadLocal;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author N1KO
 */
@Component
public class AuthUserHandler implements HandlerInterceptor {
    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, Exception ex) {
        UserThreadLocal.remove();
    }
}
