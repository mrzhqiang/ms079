package com.github.mrzhqiang.maplestory.wz.element.data;

/**
 * 矢量数据。
 */
public final class Vector {

    /**
     * 空的矢量，表示原点坐标。设置为常量避免内存占用。
     */
    private static final Vector EMPTY = Vector.of(0, 0);

    /**
     * X 坐标。
     */
    public final int x;
    /**
     * Y 坐标。
     */
    public final int y;

    public static Vector of(int x, int y) {
        return new Vector(x, y);
    }

    public static Vector of(Vector vector) {
        return new Vector(vector);
    }

    public static Vector empty() {
        return EMPTY;
    }

    /**
     * @param x X 坐标。
     * @param y Y 坐标。
     */
    private Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param vector 矢量，表示一组 x/y 坐标。
     */
    private Vector(Vector vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    /**
     * Returns the square of the distance from this
     * <code>Point2D</code> to a specified point.
     *
     * @param px the X coordinate of the specified point to be measured
     *           against this <code>Point2D</code>
     * @param py the Y coordinate of the specified point to be measured
     *           against this <code>Point2D</code>
     * @return the square of the distance between this
     * <code>Point2D</code> and the specified point.
     * @since copy from java\awt\geom\Point2D.java
     */
    public double distanceSq(double px, double py) {
        px -= x;
        py -= y;
        return (px * px + py * py);
    }

    /**
     * Returns the square of the distance from this
     * <code>Point2D</code> to a specified <code>Point2D</code>.
     *
     * @param pt the specified point to be measured
     *           against this <code>Point2D</code>
     * @return the square of the distance between this
     * <code>Point2D</code> to a specified <code>Point2D</code>.
     * @since copy from java\awt\geom\Point2D.java
     */
    public double distanceSq(Vector pt) {
        double px = pt.x - this.x;
        double py = pt.y - this.y;
        return (px * px + py * py);
    }

    public Vector plusX(int x) {
        return Vector.of(this.x + x, this.y);
    }

    public Vector minusX(int x) {
        return plusX(-x);
    }

    public Vector plusY(int y) {
        return Vector.of(this.x, this.y + y);
    }

    public Vector minusY(int y) {
        return plusY(-y);
    }

    public Vector plus(int x, int y) {
        return Vector.of(this.x + x, this.y + y);
    }

    public Vector minus(int x, int y) {
        return Vector.of(this.x - x, this.y - y);
    }

    public Vector plus(Vector vector) {
        return Vector.of(this.x + vector.x, this.y + vector.y);
    }

    public Vector minus(Vector vector) {
        return Vector.of(this.x - vector.x, this.y - vector.y);
    }

}
