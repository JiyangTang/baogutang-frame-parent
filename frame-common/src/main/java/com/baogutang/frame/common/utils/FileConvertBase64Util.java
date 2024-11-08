package com.baogutang.frame.common.utils;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static java.util.Base64.getMimeDecoder;

/**
 * @version 1.0
 * @description: 文件base64转换工具类
 */
public class FileConvertBase64Util {
    /**
     *  将文件转化为字节数组字符串，并对其进行Base64编码处理
     * @param input
     * @return
     */
    public static String fileConvertStr(InputStream input) throws IOException {
        return toBase64(IOUtils.toByteArray(input));
    }

    /**
     *  将字节数组字符串转化为文件
     * @param src
     * @return
     */
    public static InputStream strConvertFile(String src) throws IOException {
        // 将base64编码的字符串解码成字节数组
        byte[] bytes = decodeFromString(src);
        return new ByteArrayInputStream(bytes);
    }

    public static String toBase64(byte[] bytes) {
        return bytesEncode2Base64(bytes);
    }

    private static String bytesEncode2Base64(byte[] bytes) {
        return new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
    }

    public static byte[] decodeFromString(String src) {
        return src.isEmpty() ? new byte[0] : getMimeDecoder().decode(src.getBytes(StandardCharsets.UTF_8));
    }



}
