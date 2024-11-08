package com.baogutang.frame.auth.common.aspect;

import com.baogutang.frame.auth.common.annotation.HeaderRequired;
import com.baogutang.frame.auth.common.header.Header;
import com.baogutang.frame.auth.controller.BaseController;
import com.baogutang.frame.common.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 公共请求头必传校验
 *
 * @author N1KO
 */
@Aspect
@Component
public class HeaderRequiredAspect {

    protected static final Logger loggers = LoggerFactory.getLogger(HeaderRequiredAspect.class);

    public HeaderRequiredAspect() {
        loggers.warn("HeaderRequiredAspect");
    }

    static final Header[] NO_HEADER = new Header[0];

    protected static final ConcurrentHashMap<MethodSignature, Header[]> HEADER_FILTER =
            new ConcurrentHashMap<>(128);

    final Function<MethodSignature, Header[]> HEADERS_NEEDED_GET =
            signature -> {
                Method method = signature.getMethod();
                HeaderRequired headerFilter = method.getAnnotation(HeaderRequired.class);
                if (headerFilter.headers() != null) {
                    return headerFilter.headers();
                } else {
                    return NO_HEADER;
                }
            };

    private Header[] headersNeeded(final MethodSignature method) {
        return HEADER_FILTER.computeIfAbsent(method, HEADERS_NEEDED_GET);
    }

    /**
     * Customer JWT must be validated and
     *
     * @param point point
     * @return Object
     * @throws Throwable Throwable
     */
    @Around(
            "@annotation(com.baogutang.frame.auth.common.annotation.HeaderRequired) || "
                    + "@within(com.baogutang.frame.auth.common.annotation.HeaderRequired)")
    public Object headerRequest(ProceedingJoinPoint point) throws Throwable {

        HttpServletRequest request = BaseController.getRequest();

        if (request != null) {
            Header[] needed = headersNeeded((MethodSignature) point.getSignature());
            if (NO_HEADER != needed) {

                for (final Header header : needed) {

                    final String hValue = request.getHeader(header.key);
                    if (StringUtils.isEmpty(hValue)) {
                        // Header is needed while it failed.
                        logger(point)
                                .warn(
                                        "REQUEST_REQUIRED_HEADER_MISSED {},{}",
                                        point.getSignature().getName(),
                                        header.key);
                        return Response.failed("REQUEST_REQUIRED_HEADER_MISSED");
                    }

                    final int validate = header.verify.validate(hValue);

                    if (validate > 0) {

                        logger(point)
                                .warn(
                                        "REQUEST_REQUIRED_HEADER_FAIL_VALIDATED {},{},{}",
                                        point.getSignature().getName(),
                                        header.key,
                                        validate);

                        return Response.failed("REQUEST_REQUIRED_HEADER_FAIL_VALIDATED");
                    }
                }
            }
        } else {
            logger(point)
                    .warn(
                            "NO_HTTP_SESSION_OF_HEADER_FILTER_ASPECT  {}() with argument[s] = {}",
                            point.getSignature().getName(),
                            Arrays.toString(point.getArgs()));
        }
        return point.proceed();
    }

    private static Logger logger(JoinPoint joinPoint) {
        return LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringTypeName());
    }
}
