package tools;

import java.io.Serializable;

public class Triple<E, F, G>
        implements Serializable {

    private static final long serialVersionUID = 9179541993413739999L;
    public E left;
    public F mid;
    public G right;

    public Triple(E left, F mid, G right) {
        this.left = left;
        this.mid = mid;
        this.right = right;
    }

    public E getLeft() {
        return this.left;
    }

    public F getMid() {
        return this.mid;
    }

    public G getRight() {
        return this.right;
    }

    public String toString() {
        return this.left.toString() + ":" + this.mid.toString() + ":" + this.right.toString();
    }

    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = 31 * result + (this.left == null ? 0 : this.left.hashCode());
        result = 31 * result + (this.mid == null ? 0 : this.mid.hashCode());
        result = 31 * result + (this.right == null ? 0 : this.right.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Triple other = (Triple) obj;
        if (this.left == null) {
            if (other.left != null) {
                return false;
            }
        } else if (!this.left.equals(other.left)) {
            return false;
        }
        if (this.mid == null) {
            if (other.mid != null) {
                return false;
            }
        } else if (!this.mid.equals(other.mid)) {
            return false;
        }
        if (this.right == null) {
            if (other.right != null) {
                return false;
            }
        } else if (!this.right.equals(other.right)) {
            return false;
        }
        return true;
    }
}