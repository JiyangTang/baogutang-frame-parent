package com.baogutang.frame.auth.service;

import com.baogutang.frame.auth.model.dto.JwtBody;

import java.security.NoSuchAlgorithmException;

/**
 * token工具类Service接口
 *
 * @author N1KO
 */
public interface TokenService {

    /**
     * token解析
     *
     * @param token token
     * @return JwtBody
     * @throws NoSuchAlgorithmException e
     */
    JwtBody parseToken(String token) throws NoSuchAlgorithmException;

    /**
     * 生成token
     *
     * @param userId  用户id
     * @param mobile  手机号
     * @param subject 主键
     * @return token
     * @throws NoSuchAlgorithmException e
     */
    String createToken(Long userId, String mobile, String subject) throws NoSuchAlgorithmException;

}
