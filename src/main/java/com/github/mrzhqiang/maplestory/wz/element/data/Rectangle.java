package com.github.mrzhqiang.maplestory.wz.element.data;

/**
 * 矩形，长方形。
 */
public final class Rectangle {

    private static final Rectangle EMPTY = Rectangle.of(Vector.empty(), Vector.empty());

    /**
     * 矩形左边的起始坐标。
     * <p>
     * 一般来说，在 Canvas 中，以左上角为左边的起始坐标。
     */
    public Vector left;
    /**
     * 矩形右边的起始坐标。
     * <p>
     * 一般来说，在 Canvas 中，以右下角为右边的起始坐标。
     */
    public Vector right;

    public static Rectangle empty() {
        return EMPTY;
    }

    public static Rectangle of(int x1, int y1, int x2, int y2) {
        return new Rectangle(x1, y1, x2, y2);
    }

    public static Rectangle of(Vector left, Vector right) {
        return new Rectangle(left, right);
    }

    private Rectangle(int x1, int y1, int x2, int y2) {
        this.left = Vector.of(x1, y1);
        this.right = Vector.of(x2, y2);
    }

    private Rectangle(Vector left, Vector right) {
        this.left = left;
        this.right = right;
    }

    public int width() {
        return right.x - left.x;
    }

    public int height() {
        return right.y - left.y;
    }

    public java.awt.Rectangle toJavaRectangle() {
        return new java.awt.Rectangle(this.left.x, this.left.y, width(), height());
    }
}
