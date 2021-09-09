package com.github.mrzhqiang.maplestory.wz.element.data;

/**
 * 画布数据。
 */
public final class Canvas {

    private final int width;
    private final int height;

    /**
     * @param width  宽度。
     * @param height 高度。
     */
    public Canvas(int width, int height) {
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
