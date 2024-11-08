package com.baogutang.frame.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @description md5工具类
 */
@Slf4j
public class MD5Util {


    //生成MD5
    public static String md5Hex(String password, String salt) {
        String md5 = "";
        try {
            byte[] messageByte = (password+salt).getBytes(StandardCharsets.UTF_8);
            return DigestUtils.md5DigestAsHex(messageByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5;
    }

    /**
     * 小象加密方式
     * @param inStr
     * @return
     */
    public static String compute(String inStr) {
        StringBuffer hexValue = null;
        try {
            char[] charArray = inStr.toCharArray();
            byte[] byteArray = new byte[charArray.length];

            for(int i = 0; i < charArray.length; ++i) {
                byteArray[i] = (byte)charArray[i];
            }
            MessageDigest md5 = MessageDigest.getInstance("MD5");  // 创建一个md5算法对象

            byte[] md5Bytes = md5.digest(byteArray);
            hexValue = new StringBuffer();

            for(int i = 0; i < md5Bytes.length; ++i) {
                int val = md5Bytes[i] & 255;
                if (val < 16) {
                    hexValue.append("0");
                }

                hexValue.append(Integer.toHexString(val));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hexValue.toString();
    }

    public static String getEncryptedPwd(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] str = null;
        byte[] strTemp = s.getBytes();
        MessageDigest mdTemp;
        try {
            mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }

        } catch (NoSuchAlgorithmException e) {
            log.error("MD5 encryption failed", e);
        }
        if (str == null) {
            return null;
        }
        return new String(str);
    }
}
