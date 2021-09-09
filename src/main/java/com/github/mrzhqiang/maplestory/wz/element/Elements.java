package com.github.mrzhqiang.maplestory.wz.element;

import com.github.mrzhqiang.helper.math.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.element.data.Canvas;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import com.google.common.base.MoreObjects;
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
        return element.value().toString();
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
        return MoreObjects.firstNonNull(element.value().toString(), defaultValue);
    }

    public static String findString(WzElement<?> element, String name) {
        return findString(element, name, "");
    }

    public static String findString(WzElement<?> element, String name, String defaultValue) {
        Preconditions.checkNotNull(element, "element == null");
        return element.findByName(name).map(Elements::ofString).orElse(defaultValue);
    }

    public static int ofInt(WzElement<?> element) {
        Preconditions.checkNotNull(element, "element == null");
        if (element instanceof IntElement) {
            return ((IntElement) element).value();
        }
        return Numbers.ofInt(ofString(element));
    }

    public static int ofInt(WzElement<?> element, int defaultValue) {
        if (element == null) {
            return defaultValue;
        }

        if (element instanceof IntElement) {
            return ((IntElement) element).value();
        }
        return Numbers.ofInt(ofString(element, String.valueOf(defaultValue)));
    }

    public static int findInt(WzElement<?> element, String name) {
        return findInt(element, name, 0);
    }

    public static int findInt(WzElement<?> element, String name, int defaultValue) {
        Preconditions.checkNotNull(element, "element == null");
        return element.findByName(name).map(Elements::ofInt).orElse(defaultValue);
    }

    public static short ofShort(WzElement<?> element) {
        Preconditions.checkNotNull(element, "element == null");
        if (element instanceof ShortElement) {
            return ((ShortElement) element).value();
        }
        return Numbers.ofShort(ofString(element));
    }

    public static short findShort(WzElement<?> element, String name) {
        return findShort(element, name, (short) 0);
    }

    public static short findShort(WzElement<?> element, String name, short defaultValue) {
        Preconditions.checkNotNull(element, "element == null");
        return element.findByName(name).map(Elements::ofShort).orElse(defaultValue);
    }

    public static float ofFloat(WzElement<?> element) {
        Preconditions.checkNotNull(element, "element == null");
        if (element instanceof FloatElement) {
            return ((FloatElement) element).value();
        }
        return Numbers.ofFloat(ofString(element));
    }

    public static float findFloat(WzElement<?> element, String name) {
        return findFloat(element, name, 0.f);
    }

    public static float findFloat(WzElement<?> element, String name, float defaultValue) {
        Preconditions.checkNotNull(element, "element == null");
        return element.findByName(name).map(Elements::ofFloat).orElse(defaultValue);
    }

    public static double ofDouble(WzElement<?> element) {
        Preconditions.checkNotNull(element, "element == null");
        if (element instanceof DoubleElement) {
            return ((DoubleElement) element).value();
        }
        return Numbers.ofDouble(ofString(element));
    }

    public static double findDouble(WzElement<?> element, String name) {
        return findDouble(element, name, 0.f);
    }

    public static double findDouble(WzElement<?> element, String name, double defaultValue) {
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
