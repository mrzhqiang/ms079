/*
 This file is part of the OdinMS Maple Story Server
 Copyright (C) 2008 ~ 2010 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU Affero General Public License version 3
 as published by the Free Software Foundation. You may not use, modify
 or distribute this program under any other version of the
 GNU Affero General Public License.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU Affero General Public License for more details.

 You should have received a copy of the GNU Affero General Public License
 along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package server.maps;

import java.awt.Point;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MapleFootholdTree {

    private MapleFootholdTree nw = null;
    private MapleFootholdTree ne = null;
    private MapleFootholdTree sw = null;
    private MapleFootholdTree se = null;
    private List<MapleFoothold> footholds = new LinkedList<MapleFoothold>();
    private Point p1;
    private Point p2;
    private Point center;
    private int depth = 0;
    private static final byte maxDepth = 8;
    private int maxDropX;
    private int minDropX;

    public MapleFootholdTree(final Point p1, final Point p2) {
        this.p1 = p1;
        this.p2 = p2;
        center = new Point((p2.x - p1.x) / 2, (p2.y - p1.y) / 2);
    }

    public MapleFootholdTree(final Point p1, final Point p2, final int depth) {
        this.p1 = p1;
        this.p2 = p2;
        this.depth = depth;
        center = new Point((p2.x - p1.x) / 2, (p2.y - p1.y) / 2);
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
                ne = new MapleFootholdTree(new Point(center.x, p1.y), new Point(p2.x, center.y), depth + 1);
                sw = new MapleFootholdTree(new Point(p1.x, center.y), new Point(center.x, p2.y), depth + 1);
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

    private final List<MapleFoothold> getRelevants(final Point p) {
        return getRelevants(p, new LinkedList<MapleFoothold>());
    }

    private final List<MapleFoothold> getRelevants(final Point p, final List<MapleFoothold> list) {
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

    private final MapleFoothold findWallR(final Point p1, final Point p2) {
        MapleFoothold ret;
        for (final MapleFoothold f : footholds) {
            //if (f.isWall()) System.out.println(f.getX1() + " " + f.getX2());
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

    public final MapleFoothold findWall(final Point p1, final Point p2) {
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
                    System.out.println("Couldn't find the correct pos for next/prev");
                    return false;
                }
                return true;
            }
        }
        return false;
    }

    public final MapleFoothold findBelow(final Point p) {
        final List<MapleFoothold> relevants = getRelevants(p);
        // find fhs with matching x coordinates
        final List<MapleFoothold> xMatches = new LinkedList<MapleFoothold>();
        for (final MapleFoothold fh : relevants) {
            if (fh.getX1() <= p.x && fh.getX2() >= p.x) {
                if (fh.getX1() == fh.getX2()) {
                    continue;
                }
                xMatches.add(fh);
            }
        }
        Collections.sort(xMatches);//这句话报错了。怪物移动的
        for (final MapleFoothold fh : xMatches) {
            if (!fh.isWall() && fh.getY1() != fh.getY2()) {
                int calcY;
                final double s1 = Math.abs(fh.getY2() - fh.getY1());
                final double s2 = Math.abs(fh.getX2() - fh.getX1());
                final double s4 = Math.abs(p.x - fh.getX1());
                final double alpha = Math.atan(s2 / s1);
                final double beta = Math.atan(s1 / s2);
                final double s5 = Math.cos(alpha) * (s4 / Math.cos(beta));
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
