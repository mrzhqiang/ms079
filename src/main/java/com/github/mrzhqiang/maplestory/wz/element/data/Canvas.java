package com.github.mrzhqiang.maplestory.wz.element.data;

import com.github.mrzhqiang.maplestory.util.Numbers;
import com.google.common.base.Preconditions;
import org.jsoup.nodes.Element;

/**
 * 画布数据。
 */
public final class Canvas {

    /**
     * 宽度的键名。
     */
    private static final String WIDTH_KEY_NAME = "width";
    /**
     * 高度的键名。
     */
    private static final String HEIGHT_KEY_NAME = "height";

    /**
     * 宽度。
     */
    private final int width;
    /**
     * 高度。
     */
    private final int height;

    public static Canvas of(int width, int height) {
        return new Canvas(width, height);
    }

    public static Canvas of(Element source) {
        Preconditions.checkNotNull(source, "source == null");
        int width = Numbers.ofInt(source.attr(WIDTH_KEY_NAME));
        int height = Numbers.ofInt(source.attr(HEIGHT_KEY_NAME));
        return new Canvas(width, height);
    }

    private Canvas(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * @return 画布宽度。
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return 画布高度。
     */
    public int getHeight() {
        return height;
    }
}
