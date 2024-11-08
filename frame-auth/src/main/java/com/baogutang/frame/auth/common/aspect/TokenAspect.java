package com.baogutang.frame.auth.common.aspect;

import com.baogutang.frame.auth.common.annotation.AuthToken;
import com.baogutang.frame.auth.common.utils.UserThreadLocal;
import com.baogutang.frame.auth.model.dto.JwtBody;
import com.baogutang.frame.auth.model.entity.User;
import com.baogutang.frame.auth.model.enums.TokenCodeEnum;
import com.baogutang.frame.auth.service.TokenService;
import com.baogutang.frame.common.exception.TokenException;
import com.baogutang.frame.common.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

import static com.baogutang.frame.auth.common.constants.JwtConstant.TOKEN_NAME;

/**
 * @author N1KO
 */
@Aspect
@Component
@Slf4j
public class TokenAspect {

    @Resource
    private TokenService tokenService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String USER_KEY = "user";


    /**
     * 用户Token key
     */
    public static final String USER_TOKEN = "baogutang-user:token:";

    @Pointcut("@annotation(com.baogutang.frame.auth.common.annotation.AuthToken)")
    public void point() {
    }

    /**
     * 切面验证主入口
     *
     * @throws Throwable 异常抛出
     */
    @Before(
            value = "point() && @annotation(authToken)")
    public void verifyTokenForClass(AuthToken authToken) throws Throwable {
        if (authToken.required()) {
            checkToken();
        } else {
            checkTokenWithOutRequired();
        }
    }


    /**
     * 校验token
     *
     * @throws NoSuchAlgorithmException s
     */
    private void checkToken() throws NoSuchAlgorithmException {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader(TOKEN_NAME);
        if (StringUtils.isEmpty(token)) {
            throw new TokenException(
                    TokenCodeEnum.AUTH_TOKEN_EMPTY.getCode(), TokenCodeEnum.AUTH_TOKEN_EMPTY.getMessage());
        }
        JwtBody body = tokenService.parseToken(token);
        checkTokenExit(body, token);
        request.setAttribute(USER_KEY, body);
        UserThreadLocal.set(JacksonUtil.fromJson(Objects.toString(body.getObject()), User.class));


    }


    /**
     * 校验token
     *
     * @throws NoSuchAlgorithmException s
     */
    private void checkTokenWithOutRequired() throws NoSuchAlgorithmException {
        ServletRequestAttributes requestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.isNull(requestAttributes)) {
            return;
        }
        HttpServletRequest request = requestAttributes.getRequest();
        String token = request.getHeader(TOKEN_NAME);
        if (StringUtils.isNotEmpty(token)) {
            JwtBody body = tokenService.parseToken(token);
            checkTokenExit(body, token);
            request.setAttribute(USER_KEY, body);
            UserThreadLocal.set(JacksonUtil.fromJson(Objects.toString(body.getObject()), User.class));
        }

    }

    /**
     * 判断token是否合法
     *
     * @param body  body
     * @param token token
     */
    private void checkTokenExit(JwtBody body, String token) {
        try {
            User user = JacksonUtil.fromJson(Objects.toString(body.getObject()), User.class);
            Object value = redisTemplate.opsForValue().get(USER_TOKEN + user.getUserId());
            if (!token.equals(value)) {
                log.warn("checkTokenExit fail token:{}, redisToken:{}", token, value);
                throw new TokenException(
                        TokenCodeEnum.AUTH_TOKEN_ILLEGAL.getCode(),
                        TokenCodeEnum.AUTH_TOKEN_ILLEGAL.getMessage());
            }

        } catch (Exception e) {
            log.warn("checkTokenExit fail:{}, token:{}", e, token);
            throw new TokenException(
                    TokenCodeEnum.AUTH_TOKEN_ILLEGAL.getCode(),
                    TokenCodeEnum.AUTH_TOKEN_ILLEGAL.getMessage());
        }
    }
}
