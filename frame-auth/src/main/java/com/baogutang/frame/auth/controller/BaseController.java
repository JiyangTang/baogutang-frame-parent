package com.baogutang.frame.auth.controller;

import com.baogutang.frame.auth.common.header.HeadParams;
import com.baogutang.frame.auth.model.dto.JwtBody;
import com.baogutang.frame.auth.model.entity.User;
import com.baogutang.frame.common.utils.JacksonUtil;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Objects;

/**
 * @author N1KO
 */
public interface BaseController {
    /**
     * 获取 HttpServletRequest
     *
     * @return {HttpServletRequest}
     */
    static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (requestAttributes == null)
                ? null
                : ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    default User getUser() {
        JwtBody obj = (JwtBody) Objects.requireNonNull(getRequest()).getAttribute("user");
        if (Objects.isNull(obj)) {
            return null;
        }
        return JacksonUtil.fromJson(Objects.toString(obj.getObject()), User.class);
    }

    /**
     * 获取用户id
     *
     * @return userId
     */
    default Long getUserId() {
        JwtBody obj = (JwtBody) Objects.requireNonNull(getRequest()).getAttribute("user");
        if (Objects.isNull(obj)) {
            return null;
        }
        User user = JacksonUtil.fromJson(Objects.toString(obj.getObject()), User.class);
        if (Objects.isNull(user)) {
            return null;
        }
        return user.getUserId();
    }


    /**
     * 获取请求真实IP
     *
     * @return res
     */
    default String getIp() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return "";
        }
        String ip = request.getHeader("X-Original-Forwarded-For");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                int index = ip.indexOf(",");
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            }
        }
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                int index = ip.indexOf(",");
                if (index != -1) {
                    return ip.substring(0, index);
                } else {
                    return ip;
                }
            }
        }
        ip = request.getHeader("X-Real-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip = request.getHeader("Proxy-Client-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip = request.getHeader("WL-Proxy-Client-IP");
        if (ip != null) {
            if (!ip.isEmpty() && !"unKnown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        ip = request.getRemoteAddr();
        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    /**
     * 获取统一请求头
     *
     * @return HeadParams
     */
    default HeadParams getHeadParams() {
        HeadParams headParams = new HeadParams();
        headParams.setIp(getIp());
        HttpServletRequest request = getRequest();
        Enumeration<String> it = Objects.requireNonNull(request).getHeaderNames();
        while (it.hasMoreElements()) {
            String key = it.nextElement();
            key = key.toLowerCase();
            switch (key) {
                case "x-auth-token":
                    headParams.setToken(Objects.requireNonNull(request).getHeader(key));
                    break;
                case "network-env":
                    headParams.setNetworkEnv(Objects.requireNonNull(request).getHeader(key));
                    break;
                case "network-provider":
                    headParams.setNetworkProvider(Objects.requireNonNull(request).getHeader(key));
                    break;
                case "longitude":
                    headParams.setLongitude(Objects.requireNonNull(request).getHeader(key));
                    break;
                case "latitude":
                    headParams.setLatitude(Objects.requireNonNull(request).getHeader(key));
                    break;
                case "ip":
                    headParams.setIp(getIp());
                    break;
                default:
                    break;
            }
        }
        return headParams;
    }

    /**
     * 获取请求路径
     *
     * @return getPath
     */
    default String getPath() {
        HttpServletRequest request = getRequest();
        return Objects.requireNonNull(request).getServletPath();
    }

}
