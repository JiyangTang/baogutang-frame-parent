package com.baogutang.frame.core.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.baogutang.frame.common.exception.TokenException;
import com.baogutang.frame.common.response.Response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 日志切面
 *
 * @author N1KO
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    private static final String USER_KEY = "user";
    private static final String EXCLUDE_URI = "checkServer";

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void logPointCut() {
    }

    @Around("logPointCut()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        String requestId = MDC.get("X-Request-Id");
        long startMills = System.currentTimeMillis();
        long cost = 0;
        Headers headers = getHeaders(request);
        try {
            result = pjp.proceed();
            cost = System.currentTimeMillis() - startMills;
            if (!request.getRequestURI().contains(EXCLUDE_URI)) {
                log.info("EndOfRequest！ThisRequestTakesTime:{},url: {},method: {},headers:{},params: {},rid: {},user: {},ResponseResult:{}",
                        cost, request.getRequestURL().toString(),
                        request.getMethod(), JSON.toJSONString(headers), pjp.getArgs(),
                        requestId, request.getAttribute(USER_KEY), JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect));
            }
            if (result instanceof Response) {
                Response<?> response = (Response<?>) result;
                response.setRid(requestId);
            }
        } catch (TokenException e) {
            log.warn("TokenFail:{},url: {},method: {},headers:{},params: {},rid: {},user: {}", e, request.getRequestURL().toString(),
                    request.getMethod(), JSON.toJSONString(headers), pjp.getArgs(), requestId, request.getAttribute(USER_KEY));
            throw e;
        } catch (Exception e) {
            log.error("RequestException！！！ThisRequestTakesTime:{},error:{},url: {},method: {},headers:{},params: {},rid: {},user: {}",
                    cost, e, request.getRequestURL().toString(), request.getMethod(), JSON.toJSONString(headers), pjp.getArgs(), requestId, request.getAttribute(USER_KEY));
            throw e;
        }
        return result;
    }


    private Headers getHeaders(HttpServletRequest request) {
        return Headers.builder()
                .appVersion(request.getHeader("app-version"))
                .os(request.getHeader("os"))
                .deviceId(request.getHeader("device-id"))
                .build();
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    private static class Headers {
        private String os;

        private String appVersion;

        private String deviceId;

    }


}
