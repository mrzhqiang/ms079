package server.maps;

import com.github.mrzhqiang.maplestory.wz.element.data.Vector;

public class MapleFoothold implements Comparable<MapleFoothold> {

    private Vector p1;
    private Vector p2;
    private int id;
    private short next, prev;

    public MapleFoothold(Vector p1, Vector p2, int id) {
        this.p1 = p1;
        this.p2 = p2;
        this.id = id;
    }

    public boolean isWall() {
        return p1.x == p2.x;
    }

    public int getX1() {
        return p1.x;
    }

    public int getX2() {
        return p2.x;
    }

    public int getY1() {
        return p1.y;
    }

    public int getY2() {
        return p2.y;
    }

    public int compareTo(MapleFoothold o) {
        MapleFoothold other = (MapleFoothold) o;
        if (p2.y < other.getY1()) {
            return -1;
        } else if (p1.y > other.getY2()) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MapleFoothold)) {
            return false;
        }
        final MapleFoothold oth = (MapleFoothold) o;
        return oth.getY1() == p1.y && oth.getY2() == p2.y && oth.getX1() == p1.x && oth.getX2() == p2.x && id == oth.getId();
    }

    public int getId() {
        return id;
    }

    public short getNext() {
        return next;
    }

    public void setNext(short next) {
        this.next = next;
    }

    public short getPrev() {
        return prev;
    }

    public void setPrev(short prev) {
        this.prev = prev;
    }
}
