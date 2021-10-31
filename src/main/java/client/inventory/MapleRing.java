package client.inventory;

import client.MapleCharacter;

import java.sql.SQLException;
import java.io.Serializable;

import com.github.mrzhqiang.maplestory.domain.DRing;
import com.github.mrzhqiang.maplestory.domain.query.QDRing;

import java.util.Comparator;
import server.MapleInventoryManipulator;

public class MapleRing implements Serializable {

    private static final long serialVersionUID = 9179541993413738579L;

    public final DRing ring;

    private int ringId;
    private boolean equipped = false;

    private MapleRing(int id, DRing ring) {
        this.ringId = id;
        this.ring = ring;
    }

    public static Equip loadFromDb(IItem ring) {
        DRing one = new QDRing().id.eq(ring.getUniqueId()).findOne();
        if (one == null) {
            return null;
        }

        MapleRing ret = new MapleRing(ring.getItemId(), one);
        ret.setEquipped(false);
        return new Equip(ring.getItemId(), ring.getPosition(), ring.getUniqueId(), ring.getFlag());
    }

    public static MapleRing loadFromDb(int ringId) {
        return loadFromDb(ringId, false);
    }

    public static MapleRing loadFromDb(int ringId, boolean equipped) {
        DRing one = new QDRing().id.eq(ringId).findOne();
        MapleRing ret = null;
        if (one != null) {
            ret = new MapleRing(ringId, one);
            ret.setEquipped(equipped);
        }
        return ret;
    }

    public static void addToDB(int itemid, MapleCharacter chr, String player, int id, int[] ringId) throws SQLException {
        DRing ring = new DRing();
        ring.id = ringId[0];
        ring.itemid = itemid;
        ring.partnerChrId = chr.getId();
        ring.partnername = chr.getName();
        ring.partnerRingId = ringId[1];
        ring.save();

        ring = new DRing();
        ring.id = ringId[1];
        ring.itemid = itemid;
        ring.partnerChrId = id;
        ring.partnername = player;
        ring.partnerRingId = ringId[0];
        ring.save();
    }

    public static int createRing(int itemid, MapleCharacter partner1, String partner2, String msg, int id2, int sn) {
        try {
            if (partner1 == null) {
                return -2;
            } else if (id2 <= 0) {
                return -1;
            }
            return makeRing(itemid, partner1, partner2, id2, msg, sn);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }

    public static int makeRing(int itemid, MapleCharacter partner1, String partner2, int id2, String msg, int sn) { //return partner1 the id
        int[] ringID = {MapleInventoryIdentifier.getInstance(), MapleInventoryIdentifier.getInstance()};
        //[1] = partner1, [0] = partner2
        try {
            addToDB(itemid, partner1, partner2, id2, ringID);
        } catch (SQLException mslcve) {
            return 0;
        }
        MapleInventoryManipulator.addRing(partner1, itemid, ringID[1], sn);
        partner1.getCashInventory().gift(id2, partner1.getName(), msg, sn, ringID[0]);
        return 1;
    }

    public int getRingId() {
        return ringId;
    }

    public int getItemId() {
        return ring.itemid;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    public String getPartnerName() {
        return ring.partnername;
    }

    public void setPartnerName(String partnerName) {
        this.ring.partnername = partnerName;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MapleRing) {
            return ((MapleRing) o).getRingId() == getRingId();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + this.ringId;
        return hash;
    }

    public static void removeRingFromDb(MapleCharacter player) {
        DRing one = new QDRing().partnerChrId.eq(player.getId()).findOne();
        if (one == null) {
            return;
        }

        new QDRing().id.eq(one.id).or().id.eq(one.partnerRingId).delete();
    }

    public static class RingComparator implements Comparator<MapleRing>, Serializable {

        @Override
        public int compare(MapleRing o1, MapleRing o2) {
            if (o1.ringId < o2.ringId) {
                return -1;
            } else if (o1.ringId == o2.ringId) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
