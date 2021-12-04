package com.github.mrzhqiang.maplestory.util;

import com.google.common.base.Strings;

import java.math.BigDecimal;

/**
 * 数字辅助工具。
 * <p>
 * 这个类是从我自己的辅助工具框架 helper 复制而来。
 */
public enum Numbers {
    ; // no instances

    /**
     * <pre>
     *     ofInt(null) == 0
     *     ofInt("")   == 0
     *     ofInt("1")  == 1
     * </pre>
     *
     * @param number 仅包含整型数字的字符串。
     * @return 整型数字。无效的数字字符串将默认返回 0。
     */
    public static int ofInt(String number) {
        return ofInt(number, 0);
    }

    /**
     * <pre>
     *     ofInt(null,0) == 0
     *     ofInt("",1)   == 1
     *     ofInt("1",0)  == 1
     * </pre>
     *
     * @param number       仅包含整型数字的字符串。
     * @param defaultValue 默认返回的整型数字。
     * @return 整型数字。
     */
    public static int ofInt(String number, int defaultValue) {
        if (Strings.isNullOrEmpty(number)) {
            return defaultValue;
        }

        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * <pre>
     *     ofLong(null) == 0L
     *     ofLong("")   == 0L
     *     ofLong("1")  == 1L
     * </pre>
     *
     * @param number 仅包含长整型数字的字符串。
     * @return 长整型数字。无效的数字字符串将默认返回 0L。
     */
    public static long ofLong(String number) {
        return ofLong(number, 0L);
    }

    /**
     * <pre>
     *     ofLong(null,0L) == 0L
     *     ofLong("",1L)   == 1L
     *     ofLong("1",0L)  == 1L
     * </pre>
     *
     * @param number       仅包含长整型数字的字符串。
     * @param defaultValue 默认返回的长整型数字。
     * @return 长整型数字。
     */
    public static long ofLong(String number, long defaultValue) {
        if (Strings.isNullOrEmpty(number)) {
            return defaultValue;
        }

        try {
            return Long.parseLong(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * <pre>
     *     ofFloat(null)    == 0.0f
     *     ofFloat("")      == 0.0f
     *     ofFloat("1.0")   == 1.0f
     * </pre>
     *
     * @param number 包含单精度浮点型数字的字符串。
     * @return 单精度浮点型数字。无效的字符串将默认返回 0.0f。
     */
    public static float ofFloat(String number) {
        return ofFloat(number, 0.0f);
    }

    /**
     * <pre>
     *     ofFloat(null,0.0f) == 0.0f
     *     ofFloat("",1.0f)   == 1.0f
     *     ofFloat("1",0.0f)  == 1.0f
     * </pre>
     *
     * @param number       包含单精度浮点型数字的字符串。
     * @param defaultValue 默认返回的单精度浮点型数字。
     * @return 单精度浮点型数字。
     */
    public static float ofFloat(String number, float defaultValue) {
        if (Strings.isNullOrEmpty(number)) {
            return defaultValue;
        }

        try {
            return Float.parseFloat(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * <pre>
     *     ofDouble(null) == 0.0d
     *     ofDouble("")   == 0.0d
     *     ofDouble("1")  == 1.0d
     * </pre>
     *
     * @param number 包含双精度浮点型数字的字符串。
     * @return 双精度浮点型数字。无效的字符串将默认返回 0.0d。
     */
    public static double ofDouble(String number) {
        return ofDouble(number, 0.0d);
    }

    /**
     * <pre>
     *     ofDouble(null,0.0d) == 0.0d
     *     ofDouble("",1.0d)   == 1.0d
     *     ofDouble("1",0.0d)  == 1.0d
     * </pre>
     *
     * @param number       包含双精度浮点型数字的字符串。
     * @param defaultValue 默认返回的双精度浮点型数字。
     * @return 双精度浮点型数字。
     */
    public static double ofDouble(String number, double defaultValue) {
        if (Strings.isNullOrEmpty(number)) {
            return defaultValue;
        }

        try {
            return Double.parseDouble(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * <pre>
     *   ofDouble(null)                     == 0.0d
     *   ofDouble(BigDecimal.valueOf(1.0d)) == 1.0d
     * </pre>
     *
     * @param number 大数值数字，带小数点。
     * @return 双精度浮点型数字。无效的字符串将默认返回 0.0d。
     */
    public static double ofDouble(BigDecimal number) {
        return ofDouble(number, 0.0d);
    }

    /**
     * <pre>
     *   ofDouble(null, 1.1d)                     == 1.1d
     *   ofDouble(BigDecimal.valueOf(1.0d), 1.1d) == 1.0d
     * </pre>
     *
     * @param number       大数值数字，带小数点。
     * @param defaultValue 默认返回的双精度浮点型数字。
     * @return 双精度浮点型数字。
     */
    public static double ofDouble(BigDecimal number, double defaultValue) {
        return number == null ? defaultValue : number.doubleValue();
    }

    /**
     * <pre>
     *   ofByte(null) ==  0
     *   ofByte("")   ==  0
     *   ofByte("1")  ==  1
     * </pre>
     *
     * @param number 仅包含字节数字的字符串。
     * @return 字节数字。
     */
    public static byte ofByte(String number) {
        return ofByte(number, (byte) 0);
    }

    /**
     * <pre>
     *   ofByte(null, 1) ==  1
     *   ofByte("", 1)   ==  1
     *   ofByte("1", 0)  ==  1
     * </pre>
     *
     * @param number       仅包含字节数字的字符串。
     * @param defaultValue 默认返回的字节数字。
     * @return 字节数字。
     */
    public static byte ofByte(String number, byte defaultValue) {
        if (Strings.isNullOrEmpty(number)) {
            return defaultValue;
        }

        try {
            return Byte.parseByte(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * <pre>
     *   ofShort(null) ==  0
     *   ofShort("")   ==  0
     *   ofShort("1")  ==  1
     * </pre>
     *
     * @param number 仅包含短整型数字的字符串。
     * @return 短整型数字。
     */
    public static short ofShort(String number) {
        return ofShort(number, (short) 0);
    }

    /**
     * <pre>
     *   ofShort(null, 1) ==  1
     *   ofShort("", 1)   ==  1
     *   ofShort("1", 0)  ==  1
     * </pre>
     *
     * @param number       仅包含短整型数字的字符串。
     * @param defaultValue 默认返回的短整型数字。
     * @return 短整型数字。
     */
    public static short ofShort(String number, short defaultValue) {
        if (Strings.isNullOrEmpty(number)) {
            return defaultValue;
        }

        try {
            return Short.parseShort(number);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}

