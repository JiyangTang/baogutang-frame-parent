package com.baogutang.frame.core.filter;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * 使用过滤器，防止有些异常情况，切面无法进入
 *
 * @author N1KO
 */
@Component
@Order(1)
@Slf4j
public class MdcRequestIdFilter extends OncePerRequestFilter {
    private static final String REQUEST_ID_KEY = "X-Request-Id";
    private static final String BUILD_VERSION = "Build-Version";
    private static final String LONGITUDE = "Longitude";
    private static final String LATITUDE = "Latitude";
    private static final String CITY_CODE = "City-Code";
    private static final String OA_ID = "Oa-Id";
    private static final String OS = "OS";
    private static final String PKG = "Pkg-Delivery-Code";

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {
        try {
            String traceId = request.getHeader(REQUEST_ID_KEY);
            String version = request.getHeader(BUILD_VERSION);
            if (traceId == null) {
                traceId = UUID.randomUUID().toString().replace("-", "");
                request.setAttribute(REQUEST_ID_KEY, traceId);
            }
            if (version == null) {
                version = "1.0.0";
            }
            MDC.put(REQUEST_ID_KEY, traceId);
            MDC.put(BUILD_VERSION, version);
            MDC.put(LONGITUDE, request.getHeader(LONGITUDE));
            MDC.put(LATITUDE, request.getHeader(LATITUDE));
            MDC.put(CITY_CODE, request.getHeader(CITY_CODE));
            MDC.put(OA_ID, request.getHeader(OA_ID));
            MDC.put(OS, request.getHeader(OS));
            MDC.put(PKG, request.getHeader(PKG));
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID_KEY);
            MDC.remove(BUILD_VERSION);
            MDC.remove(LONGITUDE);
            MDC.remove(LATITUDE);
            MDC.remove(CITY_CODE);
            MDC.remove(OA_ID);
            MDC.remove(OS);
            MDC.remove(PKG);
        }

    }
}
