package com.baogutang.frame.common.utils;

import org.springframework.util.StringUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @version 1.0
 * @description: SHA 加密工具
 */
public class SHAUtil {

    /**
     * 生成 SHA
     * @param content 加密内容
     * @param shaType 加密类型（SHA-256，SHA-512）
     * @param salt
     * @return
     */
    public static String shaHex(final String content, final String shaType, String salt) {
        // 返回值
        String strResult = null;
        if (! StringUtils.isEmpty(content)) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance(shaType);
                messageDigest.update((content+salt).getBytes());
                byte byteBuffer[] = messageDigest.digest();
                StringBuffer strHexString = new StringBuffer();
                for (int i = 0; i < byteBuffer.length; i++) {
                    String hex = Integer.toHexString(0xff & byteBuffer[i]);
                    if (hex.length() == 1) {
                        strHexString.append('0');
                    }
                    strHexString.append(hex);
                }
                strResult = strHexString.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return strResult;
    }
}
