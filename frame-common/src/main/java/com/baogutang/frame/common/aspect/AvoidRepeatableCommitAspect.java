package com.baogutang.frame.common.aspect;

import com.baogutang.frame.common.annotation.AvoidRepeatableCommit;
import com.baogutang.frame.common.response.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * @author N1KO
 */
@Aspect
@Component
public class AvoidRepeatableCommitAspect {

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 含有@AvoidRepeatableCommit注解的任何方法
     */
    @Pointcut("@annotation(com.baogutang.frame.common.annotation.AvoidRepeatableCommit)")
    public void avoidRepeatable() {
    }

    @Around("avoidRepeatable()")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String ip = getIpAddr(request);

        //获取注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        AvoidRepeatableCommit avoidRepeatableCommit = method.getAnnotation(AvoidRepeatableCommit.class);

        if (avoidRepeatableCommit != null) {
            // 获取提示消息
            String message = avoidRepeatableCommit.message();
            // 获取key
            String key = avoidRepeatableCommit.key();
            if (StringUtils.isEmpty(key)) {
                //目标类、方法
                String className = method.getDeclaringClass().getName();
                String name = method.getName();
                String args = Arrays.toString(point.getArgs());
                String ipKey = String.format("%s#%s#%s", className, name, args);
                int hashCode = Math.abs(ipKey.hashCode());
                // 组装key
                key = String.format("%s_%d", ip, hashCode);
            }
            if (avoidRepeatableCommit.includeToken()) {
                String token = request.getHeader("token");
                if (token != null) {
                    key = key.concat("_").concat(DigestUtils.md5Hex(token));
                }
            }

            long timeout = avoidRepeatableCommit.timeout();
            TimeUnit timeUnit = avoidRepeatableCommit.timeUnit();
            if (timeout < 0) {
                //过期时间5s
                timeout = 5;
            }
            // 校验redis
            boolean success = Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(key,
                    UUID.randomUUID().toString(),
                    timeout,
                    timeUnit));
            if (!success) {
                return Response.failed(StringUtils.isEmpty(message) ? "正在赶来中，请稍后再试～" : message);
            }
        }
        //执行方法
        return point.proceed();
    }

    /**
     * 获取请求用户的IP地址
     *
     * @param request request
     * @return return
     */
    public String getIpAddr(HttpServletRequest request) {

        String ipAddresses = request.getHeader("x-forwarded-for");

        ipAddresses = getString(request, ipAddresses);

        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (StringUtils.isNotBlank(ipAddresses)) {
            ipAddresses = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            ipAddresses = request.getRemoteAddr();
        }

        return ipAddresses;
    }

    public static String getString(HttpServletRequest request, String ipAddresses) {
        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isBlank(ipAddresses) || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }
        return ipAddresses;
    }
}
