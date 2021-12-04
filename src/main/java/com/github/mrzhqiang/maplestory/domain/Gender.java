package com.github.mrzhqiang.maplestory.domain;

import io.ebean.annotation.DbEnumType;
import io.ebean.annotation.DbEnumValue;

/**
 * 性别。
 * <p>
 * 0 -- 男
 * <p>
 * 1 -- 女
 * <p>
 * 10 -- 默认/未选择
 */
public enum Gender {

    MALE("0"),
    FEMALE("1"),
    UNKNOWN("10"),
    ;

    final String code;

    Gender(String code) {
        this.code = code;
    }

    public static Gender of(int code) {
        for (Gender value : Gender.values()) {
            if (value.getCodeInt() == code) {
                return value;
            }
        }
        return UNKNOWN;
    }

    @DbEnumValue(storage = DbEnumType.INTEGER)
    public String getCode() {
        return code;
    }

    public byte getCodeByte() {
        return Byte.parseByte(code);
    }

    public int getCodeInt() {
        return Integer.parseInt(code);
    }
}
