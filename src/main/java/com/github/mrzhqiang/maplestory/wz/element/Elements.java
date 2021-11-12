package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.element.data.Canvas;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import com.google.common.base.Preconditions;

import java.util.Objects;

/**
 * wz 元素辅助工具。
 */
public final class Elements {
    private Elements() {
        // no instance
    }

    public static String ofString(WzElement<?> element) {
        Preconditions.checkNotNull(element, "element == null");
        if (element instanceof StringElement) {
            return ((StringElement) element).value();
        }
        return Objects.toString(element.value());
    }

    public static String ofString(WzElement<?> element, String defaultValue) {
        if (element == null) {
            return defaultValue;
        }
        if (Objects.isNull(element.value())) {
            return defaultValue;
        }
        if (element instanceof StringElement) {
            return ((StringElement) element).value();
        }
        return Objects.toString(element.value(), defaultValue);
    }

    public static String findString(WzElement<?> element, String name) {
        return findString(element, name, "");
    }

    public static String findString(WzElement<?> element, String name, String defaultValue) {
        Preconditions.checkNotNull(element, "element == null");
        return element.findByName(name).map(Elements::ofString).orElse(defaultValue);
    }

    public static Integer ofInt(WzElement<?> element) {
        Preconditions.checkNotNull(element, "element == null");
        if (element instanceof IntElement) {
            return ((IntElement) element).value();
        }
        return Numbers.ofInt(ofString(element));
    }

    public static Integer ofInt(WzElement<?> element, Integer defaultValue) {
        if (element == null) {
            return defaultValue;
        }

        if (element instanceof IntElement) {
            return ((IntElement) element).value();
        }
        return Numbers.ofInt(ofString(element, String.valueOf(defaultValue)));
    }

    public static Integer findInt(WzElement<?> element, String name) {
        return findInt(element, name, 0);
    }

    public static Integer findInt(WzElement<?> element, String name, int defaultValue) {
        Preconditions.checkNotNull(element, "element == null");
        return element.findByName(name).map(Elements::ofInt).orElse(defaultValue);
    }

    public static Short ofShort(WzElement<?> element) {
        Preconditions.checkNotNull(element, "element == null");
        if (element instanceof ShortElement) {
            return ((ShortElement) element).value();
        }
        return Numbers.ofShort(ofString(element));
    }

    public static Short findShort(WzElement<?> element, String name) {
        return findShort(element, name, (short) 0);
    }

    public static Short findShort(WzElement<?> element, String name, Short defaultValue) {
        Preconditions.checkNotNull(element, "element == null");
        return element.findByName(name).map(Elements::ofShort).orElse(defaultValue);
    }

    public static Float ofFloat(WzElement<?> element) {
        Preconditions.checkNotNull(element, "element == null");
        if (element instanceof FloatElement) {
            return ((FloatElement) element).value();
        }
        return Numbers.ofFloat(ofString(element));
    }

    public static Float findFloat(WzElement<?> element, String name) {
        return findFloat(element, name, 0.f);
    }

    public static Float findFloat(WzElement<?> element, String name, Float defaultValue) {
        Preconditions.checkNotNull(element, "element == null");
        return element.findByName(name).map(Elements::ofFloat).orElse(defaultValue);
    }

    public static Double ofDouble(WzElement<?> element) {
        Preconditions.checkNotNull(element, "element == null");
        if (element instanceof DoubleElement) {
            return ((DoubleElement) element).value();
        }
        return Numbers.ofDouble(ofString(element));
    }

    public static Double findDouble(WzElement<?> element, String name) {
        return findDouble(element, name, 0.D);
    }

    public static Double findDouble(WzElement<?> element, String name, Double defaultValue) {
        Preconditions.checkNotNull(element, "element == null");
        return element.findByName(name).map(Elements::ofDouble).orElse(defaultValue);
    }

    public static Canvas ofCanvas(WzElement<?> element) {
        Preconditions.checkNotNull(element, "element == null");
        if (element instanceof CanvasElement) {
            return ((CanvasElement) element).value();
        }
        return null;
    }

    @SuppressWarnings("unused")
    public static Canvas findCanvas(WzElement<?> element, String name) {
        return findCanvas(element, name, null);
    }

    public static Canvas findCanvas(WzElement<?> element, String name, Canvas defaultValue) {
        Preconditions.checkNotNull(element, "element == null");
        return element.findByName(name).map(Elements::ofCanvas).orElse(defaultValue);
    }

    public static Vector ofVector(WzElement<?> element) {
        Preconditions.checkNotNull(element, "element == null");
        if (element instanceof VectorElement) {
            return ((VectorElement) element).value();
        }
        return null;
    }

    public static Vector findVector(WzElement<?> element, String name) {
        return findVector(element, name, null);
    }

    public static Vector findVector(WzElement<?> element, String name, Vector defaultValue) {
        Preconditions.checkNotNull(element, "element == null");
        return element.findByName(name).map(Elements::ofVector).orElse(defaultValue);
    }
}
