package com.baogutang.frame.auth.service.impl;

import com.baogutang.frame.auth.common.constants.JwtConstant;
import com.baogutang.frame.auth.model.dto.JwtBody;
import com.baogutang.frame.auth.model.entity.User;
import com.baogutang.frame.auth.model.enums.TokenCodeEnum;
import com.baogutang.frame.auth.service.TokenService;
import com.baogutang.frame.common.exception.TokenException;
import com.baogutang.frame.common.utils.JacksonUtil;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;

import javax.annotation.Resource;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static com.baogutang.frame.auth.common.aspect.TokenAspect.USER_TOKEN;

/**
 * token工具类Service接口实现
 *
 * @author N1KO
 */
@Service
@Slf4j
public class TokenUtilServiceImpl implements TokenService {

    public static final long TOKEN_EXPIRE = 30 * 24 * 60 * 60;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * token解析
     *
     * @param token token
     * @return body
     */
    @Override
    public JwtBody parseToken(String token) {
        Jws<Claims> claimsJws;
        try {
            claimsJws = Jwts.parser().setSigningKey(publicKeyFromBase64()).parseClaimsJws(token);
        } catch (JwtException e) {
            log.warn("parseToken expired:{} e:{}", token, e.getMessage());
            if (e.getMessage().contains("expired")) {
                throw new TokenException(
                        TokenCodeEnum.AUTH_TIME_OUT.getCode(), TokenCodeEnum.AUTH_TIME_OUT.getMessage());
            }
            throw new TokenException(
                    TokenCodeEnum.AUTH_FAILED.getCode(), TokenCodeEnum.AUTH_FAILED.getMessage());
        }
        return new JwtBody(claimsJws.getBody());
    }

    /**
     * 公钥64位序列化
     *
     * @return PublicKey
     */
    private static PublicKey publicKeyFromBase64() {
        try {
            byte[] keyBytes = Base64Utils.decodeFromString(JwtConstant.PUBLIC_KEY);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(JwtConstant.ALGORITHM_FAMILY_NAME);
            return keyFactory.generatePublic(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    /**
     * 生成token
     *
     * @param userId  用户id
     * @param mobile  手机号
     * @param subject 主键
     * @return token
     */
    @Override
    public String createToken(Long userId, String mobile, String subject) {
        String data = JacksonUtil.toJson(User.builder().userId(userId).mobile(mobile).build());
        JwtBuilder jwtBuilder = Jwts.builder();
        subject = subject != null ? subject : UUID.randomUUID().toString();
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotEmpty(data)) {
            map.put("data", data);
        }
        jwtBuilder.setClaims(map).setSubject(subject);
        long currentTime = System.currentTimeMillis();
        Date newDate = new Date(currentTime + TOKEN_EXPIRE * 1000);
        jwtBuilder.setExpiration(newDate);
        String token = jwtBuilder.signWith(SignatureAlgorithm.RS256, privateKeyFromBase64()).compact();
        String key = USER_TOKEN + userId;
        redisTemplate.opsForValue().set(key, token);
        redisTemplate.expire(key, TOKEN_EXPIRE, TimeUnit.SECONDS);
        return token;
    }

    /**
     * 私钥64位序列化
     *
     * @return PrivateKey
     */
    private static PrivateKey privateKeyFromBase64() {
        try {
            byte[] keyBytes = Base64Utils.decodeFromString(JwtConstant.PRIVATE_KEY);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(JwtConstant.ALGORITHM_FAMILY_NAME);
            return keyFactory.generatePrivate(keySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
