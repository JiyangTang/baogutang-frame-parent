package com.baogutang.frame.auth.model.dto;

import io.jsonwebtoken.Claims;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * JWT Body
 *
 * @author N1KO
 */
@Data
public class JwtBody {

    private String subject;

    private Object object;

    public JwtBody(Claims claims) {
        if (Objects.nonNull(claims)) {
            this.subject = claims.getSubject();
            Map<String, Object> map = new HashMap<>(claims);
            map.remove("sub");
            this.object = map.get("data");
        }
    }
}
