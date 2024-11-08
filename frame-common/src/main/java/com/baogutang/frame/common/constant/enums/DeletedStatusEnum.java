package com.baogutang.frame.common.constant.enums;

import lombok.Getter;

/**
 * 公共正常逻辑删除值
 *
 * @author N1KO
 */
@Getter
public enum DeletedStatusEnum {
    /**
     * DeletedStatusEnum
     */

    DELETE_0("0", "正常"),

    DELETE_1("1", "逻辑删除");

    private final String type;
    private final String desc;

    DeletedStatusEnum(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

}
