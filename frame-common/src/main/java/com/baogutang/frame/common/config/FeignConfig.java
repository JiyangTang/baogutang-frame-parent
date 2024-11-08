package com.baogutang.frame.common.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author N1KO
 */
@Configuration
@Slf4j
public class FeignConfig implements RequestInterceptor {

    private static final String TOKEN = "X-AUTH-TOKEN";
    private static final String OS = "OS";
    private static final String REQUEST_ID = "X-Request-Id";
    private static final String OS_VERSION = "OS-Version";
    private static final String APP_VERSION = "APP-Version";
    private static final String DEVICE_ID = "Device-Id";
    private static final String MOBILE_BRAND = "Mobile-Brand";
    private static final String MOBILE_MODEL = "Mobile-Type";
    private static final String NETWORK_ENV = "Network-Env";
    private static final String NETWORK_PROVIDER = "Network-Provider";
    private static final String MEDIA_CHANNEL = "Media-Channel";
    private static final String PKG_DELIVERY_CODE = "Pkg-Delivery-Code";
    private static final String LANGUAGE = "Language";
    private static final String PRODUCT = "Product";
    private static final String OA_ID = "Oa-Id";
    private static final String LONGITUDE = "Longitude";
    private static final String LATITUDE = "Latitude";
    private static final String IP = "ip";
    private static final String BLACK_BOX = "BlackBox";
    private static final String GA_ID = "Ga-Id";
    private static final String ID_FV = "Id-Fv";
    private static final String ID_FA = "Id-Fa";

    private static final String USER = "user";


    private static final String[] ALLOW_HEADERS = {USER,TOKEN.toLowerCase(),
            REQUEST_ID.toLowerCase(),
            PRODUCT.toLowerCase(),
            OS.toLowerCase(),
            OS_VERSION.toLowerCase(),
            APP_VERSION.toLowerCase(),
            DEVICE_ID.toLowerCase(),
            MOBILE_BRAND.toLowerCase(),
            MOBILE_MODEL.toLowerCase(),
            NETWORK_ENV.toLowerCase(),
            NETWORK_PROVIDER.toLowerCase(),
            MEDIA_CHANNEL.toLowerCase(),
            PKG_DELIVERY_CODE.toLowerCase(),
            LANGUAGE.toLowerCase(),
            OA_ID.toLowerCase(),
            LONGITUDE.toLowerCase(),
            LATITUDE.toLowerCase(),
            IP.toLowerCase(),
            BLACK_BOX.toLowerCase(),
            GA_ID.toLowerCase(),
            ID_FA.toLowerCase(),
            ID_FV.toLowerCase()
    };

    /**
     * 往下一级传递的header
     */
    private static final Set<String> PASS_HEADERS = new HashSet<>(Arrays.asList(ALLOW_HEADERS));

    /**
     * feign请求携带user、token
     *
     * @param requestTemplate:
     **/
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // 防止子线程拿不到 或者 请求头没有传， 从MDC中获取
        if (StringUtils.isNotBlank(MDC.get(USER))){
            requestTemplate.header(USER, StringUtils.trimToEmpty(MDC.get(USER)));
        }
        if (StringUtils.isNotBlank(MDC.get(REQUEST_ID))) {
            requestTemplate.header(REQUEST_ID, StringUtils.trimToEmpty(MDC.get(REQUEST_ID)));
        }
        if (StringUtils.isNotBlank(MDC.get(PRODUCT))) {
            requestTemplate.header(PRODUCT, StringUtils.trimToEmpty(MDC.get(PRODUCT)));
        }
        if (StringUtils.isNotBlank(MDC.get(APP_VERSION))) {
            requestTemplate.header(APP_VERSION, StringUtils.trimToEmpty(MDC.get(APP_VERSION)));
        }
        if (StringUtils.isNotBlank(MDC.get(OS))) {
            requestTemplate.header(OS, StringUtils.trimToEmpty(MDC.get(OS)));
        }
        if (StringUtils.isNotBlank(MDC.get(OS_VERSION))) {
            requestTemplate.header(OS_VERSION, StringUtils.trimToEmpty(MDC.get(OS_VERSION)));
        }
        if (StringUtils.isNotBlank(MDC.get(DEVICE_ID))) {
            requestTemplate.header(DEVICE_ID, StringUtils.trimToEmpty(MDC.get(DEVICE_ID)));
        }
        if (StringUtils.isNotBlank(MDC.get(OS))) {
            requestTemplate.header(OS, StringUtils.trimToEmpty(MDC.get(OS)));
        }
        if (StringUtils.isNotBlank(MDC.get(MOBILE_BRAND))) {
            requestTemplate.header(MOBILE_BRAND, StringUtils.trimToEmpty(MDC.get(MOBILE_BRAND)));
        }
        if (StringUtils.isNotBlank(MDC.get(MOBILE_MODEL))) {
            requestTemplate.header(MOBILE_MODEL, StringUtils.trimToEmpty(MDC.get(MOBILE_MODEL)));
        }
        if (StringUtils.isNotBlank(MDC.get(NETWORK_ENV))) {
            requestTemplate.header(NETWORK_ENV, StringUtils.trimToEmpty(MDC.get(NETWORK_ENV)));
        }
        if (StringUtils.isNotBlank(MDC.get(NETWORK_PROVIDER))) {
            requestTemplate.header(NETWORK_PROVIDER, StringUtils.trimToEmpty(MDC.get(NETWORK_PROVIDER)));
        }
        if (StringUtils.isNotBlank(MDC.get(MEDIA_CHANNEL))) {
            requestTemplate.header(MEDIA_CHANNEL, StringUtils.trimToEmpty(MDC.get(MEDIA_CHANNEL)));
        }
        if (StringUtils.isNotBlank(MDC.get(PKG_DELIVERY_CODE))) {
            requestTemplate.header(PKG_DELIVERY_CODE, StringUtils.trimToEmpty(MDC.get(PKG_DELIVERY_CODE)));
        }
        if (StringUtils.isNotBlank(MDC.get(LANGUAGE))) {
            requestTemplate.header(LANGUAGE, StringUtils.trimToEmpty(MDC.get(LANGUAGE)));
        }
        if (StringUtils.isNotBlank(MDC.get(OA_ID))) {
            requestTemplate.header(OA_ID, StringUtils.trimToEmpty(MDC.get(OA_ID)));
        }
        if (StringUtils.isNotBlank(MDC.get(LONGITUDE))) {
            requestTemplate.header(LONGITUDE, StringUtils.trimToEmpty(MDC.get(LONGITUDE)));
        }
        if (StringUtils.isNotBlank(MDC.get(LATITUDE))) {
            requestTemplate.header(LATITUDE, StringUtils.trimToEmpty(MDC.get(LATITUDE)));
        }
        if (StringUtils.isNotBlank(MDC.get(IP))) {
            requestTemplate.header(IP, StringUtils.trimToEmpty(MDC.get(IP)));
        }
        if (StringUtils.isNotBlank(MDC.get(BLACK_BOX))) {
            requestTemplate.header(BLACK_BOX, StringUtils.trimToEmpty(MDC.get(BLACK_BOX)));
        }
        if (StringUtils.isNotBlank(MDC.get(GA_ID))) {
            requestTemplate.header(GA_ID, StringUtils.trimToEmpty(MDC.get(GA_ID)));
        }
        if (StringUtils.isNotBlank(MDC.get(ID_FA))) {
            requestTemplate.header(ID_FA, StringUtils.trimToEmpty(MDC.get(ID_FA)));
        }
        if (StringUtils.isNotBlank(MDC.get(ID_FV))) {
            requestTemplate.header(ID_FV, StringUtils.trimToEmpty(MDC.get(ID_FV)));
        }

        if (attributes == null) {
            log.warn("requestTemplate attributes is null");
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames == null) {
            return;
        }

        try {
            while (headerNames.hasMoreElements()) {
                String key = headerNames.nextElement();
                String value = request.getHeader(key);
                if (PASS_HEADERS.contains(key.toLowerCase()) && value != null) {
                    requestTemplate.header(key, value);
                }
            }
        } catch (Exception e) {
            log.error("applyError:", e);
        }

    }
}
