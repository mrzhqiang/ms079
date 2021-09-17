package server.maps;

import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MapleFootholdTree {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleFootholdTree.class);

    private MapleFootholdTree nw = null;
    private MapleFootholdTree ne = null;
    private MapleFootholdTree sw = null;
    private MapleFootholdTree se = null;
    private List<MapleFoothold> footholds = new LinkedList<>();
    private Vector p1;
    private Vector p2;
    private Vector center;
    private int depth = 0;
    private static final byte maxDepth = 8;
    private int maxDropX;
    private int minDropX;

    public MapleFootholdTree(final Vector p1, final Vector p2) {
        this.p1 = p1;
        this.p2 = p2;
        center = Vector.of((p2.x - p1.x) / 2, (p2.y - p1.y) / 2);
    }

    public MapleFootholdTree(final Vector p1, final Vector p2, final int depth) {
        this.p1 = p1;
        this.p2 = p2;
        this.depth = depth;
        center = Vector.of((p2.x - p1.x) / 2, (p2.y - p1.y) / 2);
    }

    public final void insert(final MapleFoothold f) {
        if (depth == 0) {
            if (f.getX1() > maxDropX) {
                maxDropX = f.getX1();
            }
            if (f.getX1() < minDropX) {
                minDropX = f.getX1();
            }
            if (f.getX2() > maxDropX) {
                maxDropX = f.getX2();
            }
            if (f.getX2() < minDropX) {
                minDropX = f.getX2();
            }
        }
        if (/*footholds.size() == 0 || */depth == maxDepth || (f.getX1() >= p1.x && f.getX2() <= p2.x && f.getY1() >= p1.y && f.getY2() <= p2.y)) {
            footholds.add(f);
        } else {
            if (nw == null) {
                nw = new MapleFootholdTree(p1, center, depth + 1);
                ne = new MapleFootholdTree(Vector.of(center.x, p1.y), Vector.of(p2.x, center.y), depth + 1);
                sw = new MapleFootholdTree(Vector.of(p1.x, center.y), Vector.of(center.x, p2.y), depth + 1);
                se = new MapleFootholdTree(center, p2, depth + 1);
            }
            if (f.getX2() <= center.x && f.getY2() <= center.y) {
                nw.insert(f);
            } else if (f.getX1() > center.x && f.getY2() <= center.y) {
                ne.insert(f);
            } else if (f.getX2() <= center.x && f.getY1() > center.y) {
                sw.insert(f);
            } else {
                se.insert(f);
            }
        }
    }

    private List<MapleFoothold> getRelevants(Vector p) {
        return getRelevants(p, new LinkedList<>());
    }

    private List<MapleFoothold> getRelevants(Vector p, List<MapleFoothold> list) {
        list.addAll(footholds);
        if (nw != null) {
            if (p.x <= center.x && p.y <= center.y) {
                nw.getRelevants(p, list);
            } else if (p.x > center.x && p.y <= center.y) {
                ne.getRelevants(p, list);
            } else if (p.x <= center.x && p.y > center.y) {
                sw.getRelevants(p, list);
            } else {
                se.getRelevants(p, list);
            }
        }
        return list;
    }

    private final MapleFoothold findWallR(final Vector p1, final Vector p2) {
        MapleFoothold ret;
        for (final MapleFoothold f : footholds) {
            //if (f.isWall()) LOGGER.debug(f.getX1() + " " + f.getX2());
            if (f.isWall() && f.getX1() >= p1.x && f.getX1() <= p2.x && f.getY1() >= p1.y && f.getY2() <= p1.y) {
                return f;
            }
        }
        if (nw != null) {
            if (p1.x <= center.x && p1.y <= center.y) {
                ret = nw.findWallR(p1, p2);
                if (ret != null) {
                    return ret;
                }
            }
            if ((p1.x > center.x || p2.x > center.x) && p1.y <= center.y) {
                ret = ne.findWallR(p1, p2);
                if (ret != null) {
                    return ret;
                }
            }
            if (p1.x <= center.x && p1.y > center.y) {
                ret = sw.findWallR(p1, p2);
                if (ret != null) {
                    return ret;
                }
            }
            if ((p1.x > center.x || p2.x > center.x) && p1.y > center.y) {
                ret = se.findWallR(p1, p2);
                if (ret != null) {
                    return ret;
                }
            }
        }
        return null;
    }

    public final MapleFoothold findWall(final Vector p1, final Vector p2) {
        if (p1.y != p2.y) {
            throw new IllegalArgumentException();
        }
        return findWallR(p1, p2);
    }

    // To be refined, still inaccurate :(
    public final boolean checkRelevantFH(final short fromx, final short fromy, final short tox, final short toy) {
        MapleFoothold fhdata = null;
        for (final MapleFoothold fh : footholds) { // From
            if (fh.getX1() <= fromx && fh.getX2() >= fromx && fh.getY1() <= fromy && fh.getY2() >= fromy) { // monster pos is within
                fhdata = fh;
                break;
            }
        }
        for (final MapleFoothold fh2 : footholds) { // To
            if (fh2.getX1() <= tox && fh2.getX2() >= tox && fh2.getY1() <= toy && fh2.getY2() >= toy) { // monster pos is within
                if (!(fhdata.getId() == fh2.getId() || fh2.getId() == fhdata.getNext() || fh2.getId() == fhdata.getPrev())) {
                    LOGGER.debug("Couldn't find the correct pos for next/prev");
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public final MapleFoothold findBelow(final Vector p) {
        List<MapleFoothold> relevants = getRelevants(p);
        // find fhs with matching x coordinates
        List<MapleFoothold> xMatches = new LinkedList<>();
        for (MapleFoothold fh : relevants) {
            if (fh.getX1() <= p.x && fh.getX2() >= p.x) {
                if (fh.getX1() == fh.getX2()) {
                    continue;
                }
                xMatches.add(fh);
            }
        }
        Collections.sort(xMatches);//这句话报错了。怪物移动的
        for (MapleFoothold fh : xMatches) {
            if (!fh.isWall() && fh.getY1() != fh.getY2()) {
                int calcY;
                double s1 = Math.abs(fh.getY2() - fh.getY1());
                double s2 = Math.abs(fh.getX2() - fh.getX1());
                double s4 = Math.abs(p.x - fh.getX1());
                double alpha = Math.atan(s2 / s1);
                double beta = Math.atan(s1 / s2);
                double s5 = Math.cos(alpha) * (s4 / Math.cos(beta));
                if (fh.getY2() < fh.getY1()) {
                    calcY = fh.getY1() - (int) s5;
                } else {
                    calcY = fh.getY1() + (int) s5;
                }
                if (calcY >= p.y) {
                    return fh;
                }
            } else if (!fh.isWall()) {
                if (fh.getY1() >= p.y) {
                    return fh;
                }
            }
        }
        return null;
    }

    public final int getX1() {
        return p1.x;
    }

    public final int getX2() {
        return p2.x;
    }

    public final int getY1() {
        return p1.y;
    }

    public final int getY2() {
        return p2.y;
    }

    public final int getMaxDropX() {
        return maxDropX;
    }

    public final int getMinDropX() {
        return minDropX;
    }
}
