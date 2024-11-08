package com.baogutang.frame.common.constant.enums;

import lombok.AllArgsConstructor;

/**
 * 描述  HeaderOS类型枚举类
 *
 * @author N1KO
 */
@AllArgsConstructor
public enum HeaderOsEnum {

    /**
     * OS类型
     */
    IOS("iOS", "iOS"),
    ANDROID("Android", "Android"),
    Window("Window", "Window"),
    Mac("Mac", "Mac");


    private final String desc;

    private final String code;

    public String desc() {
        return desc;
    }

    public String code() {
        return code;
    }

    /**
     * 根据code获取desc
     *
     * @param code code
     * @return desc
     */
    public static String getDesc(String code) {
        HeaderOsEnum[] carTypeEnums = values();
        for (HeaderOsEnum headerOsEnum : carTypeEnums) {
            if (headerOsEnum.code().equals(code)) {
                return headerOsEnum.desc();
            }
        }
        return null;
    }

    /**
     * 根据desc获取code
     *
     * @param desc desc
     * @return code
     */
    public static String getCode(String desc) {
        HeaderOsEnum[] carTypeEnums = values();
        for (HeaderOsEnum headerOsEnum : carTypeEnums) {
            if (headerOsEnum.desc().equals(desc)) {
                return headerOsEnum.code();
            }
        }
        return null;
    }


}
