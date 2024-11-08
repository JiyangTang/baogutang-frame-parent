package com.baogutang.frame.common.utils;


import org.apache.commons.lang3.StringUtils;

/**
 * 描述  掩码工具
 */
public class MaskUtil {
    /**
     * 获取用户真实名字的隐藏字符串，只隐藏最后一位
     * @param realName 真实名字
     * @return str
     */
    public static String getAnonymousRealName(String realName) {
        if (StringUtils.isNotEmpty(realName)) {
            int len = realName.length();
            return realName.replace(String.valueOf(realName.charAt(len-1)),"*");
        }
        return "";
    }

    /**
     * 获取手机号、隐藏中间四位
     * @param mobile 手机号
     * @return str
     */
    public static String getAnonymousMobile(String mobile) {
        if (StringUtils.isNotEmpty(mobile)) {
            int len = mobile.length();
            StringBuilder replace = new StringBuilder();
            for (int i = 0; i < len; i++) {
                if ((i > 2 && i < 7)) {
                    replace.append("*");
                } else {
                    replace.append(mobile.charAt(i));
                }
            }
            return replace.toString();
        }
        return "";
    }
    /**
     * 获取掩码email、
     * @param email 邮箱
     * @return str
     */
    public static String getAnonymousEmail(String email) {
        if (StringUtils.isNotEmpty(email)) {
            int special = email.indexOf("@");
            int len = email.length();
            String preStr = email.substring(0, special);
            int start = 2;
            if (preStr.length() < 4){
                start = 0;
            }
            StringBuilder replace = new StringBuilder();
            for (int i = 0; i < len; i++) {
                if ((i > start && i < special)) {
                    replace.append("*");
                } else {
                    replace.append(email.charAt(i));
                }
            }
            return replace.toString();
        }
        return "";
    }

    /**
     * 获取用户身份号码的隐藏字符串
     *
     * @param idNumber idNumber
     * @return str
     */
    public static String getAnonymousIdNumber(String idNumber) {
        if (StringUtils.isNotEmpty(idNumber)) {
            int len = idNumber.length();
            StringBuilder replace = new StringBuilder();
            for (int i = 0; i < len; i++) {
                if ((i > 5 && i < 10) || (i > 13 && i < 16)) {
                    replace.append("*");
                } else {
                    replace.append(idNumber.charAt(i));
                }
            }
            return replace.toString();
        }
        return "";
    }

    /**
     * 获取用户真实名字的隐藏字符串，只隐藏第二位
     *
     * @param realName 真实名字
     * @return str
     */
    public static String getAnonymousRealNameTwo(String realName) {
        if (StringUtils.isNotEmpty(realName)) {
            int len = realName.length();
            if (len == 2) {
                return realName.charAt(0) + "*";
            } else if (len == 3) {
                return realName.charAt(0) + "*" + realName.substring(2);
            } else if (len > 3) {
                return realName.charAt(0) + "**" + realName.substring(3);
            } else {
                return realName;
            }
        }
        return "";
    }

}

