package com.github.mrzhqiang.maplestory.util;

import com.google.common.base.Strings;

import java.math.BigDecimal;

/**
 * 数字辅助工具。
 * <p>
 * 这个类是从我自己的辅助工具框架 helper 复制而来。
 */
public final class Numbers {
    private Numbers() {
        // no instances
    }

    /**
     * 字符串的整型值。
     * <p>
     * 简单的示例如下：
     * <pre>
     *     ofInt(null) == 0
     *     ofInt("")   == 0
     *     ofInt("1")  == 1
     * </pre>
     *
     * @param number 字符串。
     * @return 字符串对应的整型数字。如果字符串无效，将默认返回 0 值。
     */
    public static int ofInt(String number) {
        return ofInt(number, 0);
    }

    /**
     * 字符串的整型值——支持自定义默认值。
     * <p>
     * 简单的示例如下：
     * <pre>
     *     ofInt(null,0) == 0
     *     ofInt("",1)   == 1
     *     ofInt("1",0)  == 1
     * </pre>
     *
     * @param number       字符串。
     * @param defaultValue 默认值。
     * @return 字符串对应的整型数字。如果字符串无效，将返回默认值。
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
     * 字符串的长整型值。
     * <p>
     * 简单的示例如下：
     * <pre>
     *     ofLong(null) == 0L
     *     ofLong("")   == 0L
     *     ofLong("1")  == 1L
     * </pre>
     *
     * @param number 字符串。
     * @return 字符串对应的长整型数字。如果字符串无效，将默认返回 0L 值。
     */
    public static long ofLong(String number) {
        return ofLong(number, 0L);
    }

    /**
     * 字符串的长整型值——支持自定义默认值。
     *
     * <pre>
     *     ofLong(null,0L) == 0L
     *     ofLong("",1L)   == 1L
     *     ofLong("1",0L)  == 1L
     * </pre>
     *
     * @param number       字符串。
     * @param defaultValue 默认值。
     * @return 字符串对应的长整型数字。如果字符串无效，将返回默认值。
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
     * 字符串的单精度浮点值。
     * <p>
     * 简单的示例如下：
     * <pre>
     *     ofFloat(null)    == 0.0f
     *     ofFloat("")      == 0.0f
     *     ofFloat("1.0")   == 1.0f
     * </pre>
     *
     * @param number 字符串。
     * @return 字符串对应的单精度浮点数字。如果字符串无效，将默认返回 0.0f 值。
     */
    public static float ofFloat(String number) {
        return ofFloat(number, 0.0f);
    }

    /**
     * 字符串的单精度浮点值——支持自定义默认值。
     * <p>
     * 简单的示例如下：
     * <pre>
     *     ofFloat(null,0.0f) == 0.0f
     *     ofFloat("",1.0f)   == 1.0f
     *     ofFloat("1",0.0f)  == 1.0f
     * </pre>
     *
     * @param number       字符串。
     * @param defaultValue 默认值。
     * @return 字符串对应的单精度浮点型数字。如果字符串无效，将返回默认值。
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
     * 字符串的双精度浮点值。
     * <p>
     * 简单的示例如下：
     * <pre>
     *     ofDouble(null) == 0.0d
     *     ofDouble("")   == 0.0d
     *     ofDouble("1")  == 1.0d
     * </pre>
     *
     * @param number 字符串。
     * @return 字符串对应的双精度浮点型数字。如果字符串无效，将默认返回 0.0d 值。
     */
    public static double ofDouble(String number) {
        return ofDouble(number, 0.0d);
    }

    /**
     * 字符串的双精度浮点值——支持自定义默认值。
     * <p>
     * 简单的示例如下：
     * <pre>
     *     ofDouble(null,0.0d) == 0.0d
     *     ofDouble("",1.0d)   == 1.0d
     *     ofDouble("1",0.0d)  == 1.0d
     * </pre>
     *
     * @param number       字符串。
     * @param defaultValue 默认值。
     * @return 字符串对应的双精度浮点型数字。如果字符串无效，将返回默认值。
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
     * 大数字的双精度数值。
     * <p>
     * 简单的示例如下：
     * <pre>
     *   ofDouble(null)                     == 0.0d
     *   ofDouble(BigDecimal.valueOf(1.0d)) == 1.0d
     * </pre>
     *
     * @param number 大数字。
     * @return 大数字对应的双精度浮点型数字。如果大数字无效，将默认返回 0.0d 值。
     */
    public static double ofDouble(BigDecimal number) {
        return ofDouble(number, 0.0d);
    }

    /**
     * 大数字的双精度数值——支持自定义默认值。
     * <p>
     * 简单的示例如下：
     * <pre>
     *   ofDouble(null, 1.1d)                     == 1.1d
     *   ofDouble(BigDecimal.valueOf(1.0d), 1.1d) == 1.0d
     * </pre>
     *
     * @param number       大数字。
     * @param defaultValue 默认值。
     * @return 大数字对应的双精度浮点型数字。如果大数字无效，将返回默认值。
     */
    public static double ofDouble(BigDecimal number, double defaultValue) {
        return number == null ? defaultValue : number.doubleValue();
    }

    /**
     * 字符串的字节值。
     * <p>
     * 简单的示例如下：
     * <pre>
     *   ofByte(null) ==  (byte)0
     *   ofByte("")   ==  (byte)0
     *   ofByte("1")  ==  (byte)1
     * </pre>
     *
     * @param number 字符串。
     * @return 字符串对应的字节数字。如果字符串无效，将默认返回 (byte)0 值。
     */
    public static byte ofByte(String number) {
        return ofByte(number, (byte) 0);
    }

    /**
     * 字符串的字节值——支持自定义默认值。
     * <p>
     * 简单的示例如下：
     * <pre>
     *   ofByte(null, 1) ==  (byte)1
     *   ofByte("", 1)   ==  (byte)1
     *   ofByte("1", 0)  ==  (byte)1
     * </pre>
     *
     * @param number       字符串。
     * @param defaultValue 默认值。
     * @return 字符串对应的字节数字。如果字符串无效，将返回默认值。
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
     * 字符串的短整型值。
     * <p>
     * 简单的示例如下：
     * <pre>
     *   ofShort(null) ==  0
     *   ofShort("")   ==  0
     *   ofShort("1")  ==  1
     * </pre>
     *
     * @param number 字符串。
     * @return 字符串对应的短整型数字。如果字符串无效，将返回默认的 0 值。
     */
    public static short ofShort(String number) {
        return ofShort(number, (short) 0);
    }

    /**
     * 字符串的短整型值——支持自定义默认值。
     * <p>
     * 简单的示例如下：
     * <pre>
     *   ofShort(null, 1) ==  1
     *   ofShort("", 1)   ==  1
     *   ofShort("1", 0)  ==  1
     * </pre>
     *
     * @param number       字符串。
     * @param defaultValue 默认值。
     * @return 字符串对应的短整型数字。如果字符串无效，将返回默认值。
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
