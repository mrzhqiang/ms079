package tools;

import com.google.common.base.Objects;

import java.io.Serializable;

/**
 * 三合一包装类。
 */
public final class Triple<L, M, R> implements Serializable {

    private static final long serialVersionUID = 9179541993413739999L;

    private final L left;
    private final M mid;
    private final R right;

    private Triple(L left, M mid, R right) {
        this.left = left;
        this.mid = mid;
        this.right = right;
    }

    public static <L, M, R> Triple<L, M, R> of(L left, M mid, R right) {
        return new Triple<>(left, mid, right);
    }

    public L getLeft() {
        return this.left;
    }

    public M getMid() {
        return this.mid;
    }

    public R getRight() {
        return this.right;
    }

    public String toString() {
        return this.left.toString() + ":" + this.mid.toString() + ":" + this.right.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple<?, ?, ?> triple = (Triple<?, ?, ?>) o;
        return Objects.equal(left, triple.left)
                && Objects.equal(mid, triple.mid)
                && Objects.equal(right, triple.right);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(left, mid, right);
    }
}