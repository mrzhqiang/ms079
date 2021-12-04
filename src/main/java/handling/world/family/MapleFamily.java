package handling.world.family;

import client.MapleCharacter;
import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.DFamily;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDFamily;
import com.google.common.base.Strings;
import handling.MaplePacket;
import handling.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.MaplePacketCreator;
import tools.packet.FamilyPacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class MapleFamily implements java.io.Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleFamily.class);

    public enum FCOp {
        NONE, DISBAND;
    }

    public static final long serialVersionUID = 6322150443228168192L;

    //does not need to be in order :) CID -> MFC
    private final Map<Integer, MapleFamilyCharacter> members = new ConcurrentHashMap<>();
    private String leadername = null;
    private int id, generations = 0;
    private boolean proper = true, bDirty = false, changed = false;

    private final DFamily family;

    public MapleFamily(DFamily family) {
        this.family = family;
        family.getMembers().forEach(character -> {
            MapleFamilyCharacter familyCharacter = new MapleFamilyCharacter(character, -1, false);
            members.put(character.getId(), familyCharacter);
        });
        members.forEach((integer, mapleFamilyCharacter) -> {
            DCharacter character = mapleFamilyCharacter.character;
            // mfc.getJunior1() > 0 && (getMFC(mfc.getJunior1()) == null || mfc.getId() == mfc.getJunior1())
            DCharacter junior1 = character.getJunior1();
            if (junior1 != null
                    && (!members.containsKey(junior1.getId()) || character.equals(junior1))) {
                character.setJunior1(null);
            }
            // mfc.getJunior2() > 0 && (getMFC(mfc.getJunior2()) == null || mfc.getId() == mfc.getJunior2() || mfc.getJunior1() == mfc.getJunior2())
            DCharacter junior2 = character.getJunior2();
            if (junior2 != null
                    && (!members.containsKey(junior2.getId()) || character.equals(junior2) || junior2.equals(junior1))) {
                character.setJunior2(null);
            }
            // mfc.getSeniorId() > 0 && (getMFC(mfc.getSeniorId()) == null || mfc.getId() == mfc.getSeniorId())
            DCharacter senior = character.getSenior();
            if (senior != null
                    && (members.containsKey(senior.getId())) || character.equals(senior)) {
                character.setSenior(null);
            }
            if (junior2 != null && junior1 == null) {
                character.setJunior1(junior2);
                character.setJunior2(null);
            }
            if (junior1 != null) {
                if (character.equals(junior1.getJunior1())) {
                    junior1.setJunior1(null);
                }
                if (character.equals(junior1.getJunior2())) {
                    junior1.setJunior2(null);
                }
                if (!character.equals(junior1.getSenior())) {
                    junior1.setSenior(character);
                }
            }
            if (junior2 != null) {
                if (character.equals(junior2.getJunior1())) {
                    junior2.setJunior1(null);
                }
                if (character.equals(junior2.getJunior2())) {
                    junior2.setJunior2(null);
                }
                if (!character.equals(junior2.getSenior())) {
                    junior2.setSenior(character);
                }
            }
        });
        resetPedigree();
        resetDescendants();
        resetGens();
    }

    public int getGens() {
        return generations;
    }

    public void resetPedigree() {
        for (MapleFamilyCharacter mfc : members.values()) {
            mfc.resetPedigree(this);
        }
        bDirty = true;
    }

    public void resetGens() {
        MapleFamilyCharacter mfc = getMFC(family.getLeader().getId());
        if (mfc != null) {
            generations = mfc.resetGenerations(this);
        }
        bDirty = true;
    }

    public void resetDescendants() { //not stored here, but rather in the MFC
        MapleFamilyCharacter mfc = getMFC(family.getLeader().getId());
        if (mfc != null) {
            mfc.resetDescendants(this);
        }
        bDirty = true;
    }

    public boolean isProper() {
        return proper;
    }

    public static List<MapleFamily> loadAll() {
        return new QDFamily().findStream()
                .map(MapleFamily::new)
                .collect(Collectors.toList());
    }

    public final void writeToDB(final boolean bDisband) {
        if (!bDisband) {
            if (changed) {
                family.save();
            }
            changed = false;
        } else {
            family.delete();
        }
    }

    public final int getId() {
        return id;
    }

    public final int getLeaderId() {
        return family.getLeader().getId();
    }

    public final String getNotice() {
        return Strings.nullToEmpty(family.getNotice());
    }

    public final String getLeaderName() {
        return leadername;
    }

    public final void broadcast(final MaplePacket packet, List<Integer> cids) {
        broadcast(packet, -1, FCOp.NONE, cids);
    }

    public final void broadcast(final MaplePacket packet, final int exception, List<Integer> cids) {
        broadcast(packet, exception, FCOp.NONE, cids);
    }

    public final void broadcast(final MaplePacket packet, final int exceptionId, final FCOp bcop, List<Integer> cids) {
        //passing null to cids will ensure all
        buildNotifications();
        if (members.size() < 2) {
            bDirty = true;
            return;
        }
        for (MapleFamilyCharacter mgc : members.values()) {
            if (cids == null || cids.contains(mgc.getId())) {
                if (bcop == FCOp.DISBAND) {
                    if (mgc.isOnline()) {
                        World.Family.setFamily(0, 0, 0, 0, mgc.getCurrentRep(), mgc.getTotalRep(), mgc.getId());
                    } else {
                        setOfflineFamilyStatus(0, 0, 0, 0, mgc.getCurrentRep(), mgc.getTotalRep(), mgc.getId());
                    }
                } else if (mgc.isOnline() && mgc.getId() != exceptionId) {
                    World.Broadcast.sendFamilyPacket(mgc.getId(), packet, exceptionId, id);
                }
            }
        }

    }

    private final void buildNotifications() {
        if (!bDirty) {
            return;
        }
        final Iterator<Entry<Integer, MapleFamilyCharacter>> toRemove = members.entrySet().iterator();
        while (toRemove.hasNext()) {
            MapleFamilyCharacter mfc = toRemove.next().getValue();
            if (mfc.getJunior1() > 0 && getMFC(mfc.getJunior1()) == null) {
                mfc.setJunior1(0);
            }
            if (mfc.getJunior2() > 0 && getMFC(mfc.getJunior2()) == null) {
                mfc.setJunior2(0);
            }
            if (mfc.getSeniorId() > 0 && getMFC(mfc.getSeniorId()) == null) {
                mfc.setSeniorId(0);
            }
            if (mfc.getFamilyId() != id) {
                toRemove.remove();
                continue;
            }
        }
        if (members.size() < 2 && World.Family.getFamily(id) != null) {
            World.Family.disbandFamily(id); //disband us.
        }
        bDirty = false;
    }

    public final void setOnline(final int cid, final boolean online, final int channel) {
        final MapleFamilyCharacter mgc = getMFC(cid);
        if (mgc != null && mgc.getFamilyId() == id) {
            if (mgc.isOnline() != online) {
                broadcast(FamilyPacket.familyLoggedIn(online, mgc.getName()), cid, mgc.getId() == family.getLeader().getId() ? null : mgc.getPedigree());
            }
            mgc.setOnline(online);
            mgc.setChannel((byte) channel);
        }
        bDirty = true; // member formation has changed, update notifications
    }

    public final int setRep(final int cid, int addrep, final int oldLevel) {
        final MapleFamilyCharacter mgc = getMFC(cid);
        if (mgc != null && mgc.getFamilyId() == id) {
            if (oldLevel > mgc.getLevel()) {
                addrep /= 2; //:D
            }
            //mgc.setCurrentRep(mgc.getCurrentRep()+addrep);
            //mgc.setTotalRep(mgc.getTotalRep()+addrep);
            if (mgc.isOnline()) {
                List<Integer> dummy = new ArrayList<Integer>();
                dummy.add(mgc.getId());
                broadcast(FamilyPacket.changeRep(addrep), -1, dummy);
                World.Family.setFamily(id, mgc.getSeniorId(), mgc.getJunior1(), mgc.getJunior2(), mgc.getCurrentRep() + addrep, mgc.getTotalRep() + addrep, mgc.getId());
            } else {
                setOfflineFamilyStatus(id, mgc.getSeniorId(), mgc.getJunior1(), mgc.getJunior2(), mgc.getCurrentRep() + addrep, mgc.getTotalRep() + addrep, mgc.getId());
            }
            return mgc.getSeniorId();
        }
        return 0;
    }

    public final MapleFamilyCharacter addFamilyMemberInfo(final MapleCharacter mc, final int seniorid, final int junior1, final int junior2) {
        final MapleFamilyCharacter ret = new MapleFamilyCharacter(mc.character, mc.getClient().getChannel(), true);
        members.put(mc.getId(), ret);
        ret.resetPedigree(this);
        bDirty = true;
        List<Integer> toRemove = new ArrayList<Integer>();
        for (int i = 0; i < ret.getPedigree().size(); i++) {
            if (ret.getPedigree().get(i) == ret.getId()) {
                continue;
            }
            MapleFamilyCharacter mfc = getMFC(ret.getPedigree().get(i));
            if (mfc == null) {
                toRemove.add(i);
            } else {
                mfc.resetPedigree(this);
            }
        }
        for (int i : toRemove) {
            ret.getPedigree().remove(i);
        }
        return ret;
    }

    public final int addFamilyMember(final MapleFamilyCharacter mgc) {
        mgc.setFamilyId(id);
        members.put(mgc.getId(), mgc);
        mgc.resetPedigree(this);
        bDirty = true;
        for (int i : mgc.getPedigree()) {
            getMFC(i).resetPedigree(this);
        }
        return 1;
    }

    public final void leaveFamily(final int id) {
        leaveFamily(getMFC(id), true);
    }

    /*
     * public final void leaveFamily(final int id) { leaveFamily(getMFC(id),
     * true);
     }
     */

    /*
     * public final void leaveFamily(final MapleFamilyCharacter mgc, final
     * boolean skipLeader) { bDirty = true; if (mgc.getId() == leaderid &&
     * !skipLeader) { //disband leadername = null; //to disband family
     * completely World.Family.disbandFamily(id); } else { //we also have to
     * update anyone below us
     *
     * if (mgc.getJunior1() > 0) { MapleFamilyCharacter j =
     * getMFC(mgc.getJunior1()); if (j != null) { j.setSeniorId(0);
     * splitFamily(mgc.getJunior1()); } } if (mgc.getJunior2() > 0) {
     * MapleFamilyCharacter j = getMFC(mgc.getJunior2()); if (j != null) {
     * j.setSeniorId(0); splitFamily(mgc.getJunior2()); } } if
     * (mgc.getSeniorId() > 0) { MapleFamilyCharacter mfc =
     * getMFC(mgc.getSeniorId()); if (mfc != null) { if (mfc.getJunior1() ==
     * mgc.getId()) { mfc.setJunior1(0); } else { mfc.setJunior2(0); } } }
     * List<Integer> dummy = new ArrayList<Integer>(); dummy.add(mgc.getId());
     * broadcast(null, -1, FCOp.DISBAND, dummy); resetPedigree(); //ex but eh }
     * members.remove(mgc.getId()); bDirty = true;
     }
     */
    /*
     * public final void leaveFamily(final MapleFamilyCharacter mgc, final
     * boolean skipLeader) { bDirty = true; if (mgc.getId() == leaderid &&
     * !skipLeader) { //disband leadername = null; //to disband family
     * completely World.Family.disbandFamily(id); } else { //we also have to
     * update anyone below us if (mgc.getJunior1() > 0) {
     * splitFamily(mgc.getJunior1()); //junior1 makes his own family } if
     * (mgc.getJunior2() > 0) { splitFamily(mgc.getJunior2()); //junior2 makes
     * his own family } List<Integer> dummy = new ArrayList<Integer>();
     * dummy.add(mgc.getId()); broadcast(null, -1, FCOp.DISBAND, dummy);
     * resetPedigree(); //ex but eh } members.remove(mgc.getId()); } public
     * final void setNotice(final String notice) { this.notice = notice;
     }
     */
    public final void leaveFamily(final MapleFamilyCharacter mgc, final boolean skipLeader) {
        bDirty = true;
        if (mgc.getId() == family.getLeader().getId() && !skipLeader) {
            //disband
            leadername = null; //to disband family completely
            World.Family.disbandFamily(id);
        } else {
            //we also have to update anyone below us

            if (mgc.getJunior1() > 0) {
                MapleFamilyCharacter j = getMFC(mgc.getJunior1());
                if (j != null) {
                    j.setSeniorId(0);
                    //splitFamily(mgc.getJunior1());
                    splitFamily(j.getId(), j); //junior1 makes his own family
                }
            }
            if (mgc.getJunior2() > 0) {
                MapleFamilyCharacter j = getMFC(mgc.getJunior2());
                if (j != null) {
                    j.setSeniorId(0);
                    //splitFamily(mgc.getJunior2());
                    splitFamily(j.getId(), j); //junior1 makes his own family
                }
            }
            if (mgc.getSeniorId() > 0) {
                MapleFamilyCharacter mfc = getMFC(mgc.getSeniorId());
                if (mfc != null) {
                    if (mfc.getJunior1() == mgc.getId()) {
                        mfc.setJunior1(0);
                    } else {
                        mfc.setJunior2(0);
                    }
                }
            }
            List<Integer> dummy = new ArrayList<Integer>();
            dummy.add(mgc.getId());
            broadcast(null, -1, FCOp.DISBAND, dummy);
            resetPedigree(); //ex but eh
        }
        members.remove(mgc.getId());
        bDirty = true;
    }

    public final void memberLevelJobUpdate(final MapleCharacter mgc) {
        final MapleFamilyCharacter member = getMFC(mgc.getId());
        if (member != null) {
            int old_level = member.getLevel();
            int old_job = member.getJobId();
            member.setJobId(mgc.getJob());
            member.setLevel((short) mgc.getLevel());
            if (old_level != mgc.getLevel()) {
                this.broadcast(MaplePacketCreator.sendLevelup(true, mgc.getLevel(), mgc.getName()), mgc.getId(), mgc.getId() == family.getLeader().getId() ? null : member.getPedigree());
            }
            if (old_job != mgc.getJob()) {
                this.broadcast(MaplePacketCreator.sendJobup(true, mgc.getJob(), mgc.getName()), mgc.getId(), mgc.getId() == family.getLeader().getId() ? null : member.getPedigree());
            }
        }
    }

    public final void disbandFamily() {
        writeToDB(true);
    }

    public final MapleFamilyCharacter getMFC(final int cid) {
        return members.get(cid);
    }

    public int getMemberSize() {
        return members.size();
    }

    public static void setOfflineFamilyStatus(int familyid, int seniorid, int junior1, int junior2, int currentrep, int totalrep, int cid) {
        DCharacter one = new QDCharacter().id.eq(cid).findOne();
        if (one != null) {
            one.setFamily(new QDFamily().id.eq(familyid).findOne());
            one.setSenior(new QDCharacter().id.eq(seniorid).findOne());
            one.setJunior1(new QDCharacter().id.eq(junior1).findOne());
            one.setJunior1(new QDCharacter().id.eq(junior2).findOne());
            one.setCurrentRep(currentrep);
            one.setTotalRep(totalrep);
            one.save();
        }
    }

    public static int createFamily(int leaderId) {
        DFamily family = new DFamily();
        family.setLeader(new QDCharacter().id.eq(leaderId).findOne());
        family.save();
        return family.getId();
    }

    public static void mergeFamily(MapleFamily newfam, MapleFamily oldfam) {
        //happens when someone in newfam juniors LEADER in oldfam
        //update all the members.
        for (MapleFamilyCharacter mgc : oldfam.members.values()) {
            mgc.setFamilyId(newfam.getId());
            if (mgc.isOnline()) {
                World.Family.setFamily(newfam.getId(), mgc.getSeniorId(), mgc.getJunior1(), mgc.getJunior2(), mgc.getCurrentRep(), mgc.getTotalRep(), mgc.getId());
            } else {
                setOfflineFamilyStatus(newfam.getId(), mgc.getSeniorId(), mgc.getJunior1(), mgc.getJunior2(), mgc.getCurrentRep(), mgc.getTotalRep(), mgc.getId());
            }
            newfam.members.put(mgc.getId(), mgc); //reset pedigree after
            newfam.setOnline(mgc.getId(), mgc.isOnline(), mgc.getChannel());
        }
        newfam.resetPedigree();
        //do not reset characters, so leadername is fine
        World.Family.disbandFamily(oldfam.getId()); //and remove it
    }

    /*
     * //return disbanded or not. public boolean splitFamily(int splitId,
     * MapleFamilyCharacter def) { //toSplit = initiator who either broke off
     * with their junior/senior, splitId is the ID of the one broken off //if
     * it's junior, splitId will be the new leaderID, if its senior it's toSplit
     * thats the new leader //happens when someone in fam breaks off with anyone
     * else, either junior/senior //update all the members. MapleFamilyCharacter
     * leader = getMFC(splitId); if (leader == null) { leader = def; if (leader
     * == null) { return false; } } try { List<MapleFamilyCharacter> all =
     * leader.getAllJuniors(this); //leader is included in this collection if
     * (all.size() <= 1) { //but if leader is the only person, then we're done
     * leaveFamily(leader, false); return true; } final int newId =
     * createFamily(leader.getId()); if (newId <= 0) { return false; } for
     * (MapleFamilyCharacter mgc : all) { // need it for sql
     * mgc.setFamilyId(newId); setOfflineFamilyStatus(newId, mgc.getSeniorId(),
     * mgc.getJunior1(), mgc.getJunior2(), mgc.getCurrentRep(),
     * mgc.getTotalRep(), mgc.getId()); members.remove(mgc.getId()); //clean
     * remove } final MapleFamily newfam = World.Family.getFamily(newId); for
     * (MapleFamilyCharacter mgc : all) { if (mgc.isOnline()) { //NOW we change
     * the char info World.Family.setFamily(newId, mgc.getSeniorId(),
     * mgc.getJunior1(), mgc.getJunior2(), mgc.getCurrentRep(),
     * mgc.getTotalRep(), mgc.getId()); } newfam.setOnline(mgc.getId(),
     * mgc.isOnline(), mgc.getChannel()); } } finally { if (members.size() <= 1)
     * { //only one person is left :| World.Family.disbandFamily(id); //disband
     * us. return true; } } bDirty = true; return false;
     }
     */
    //return disbanded or not.
    public boolean splitFamily(int splitId, MapleFamilyCharacter def) {
        //toSplit = initiator who either broke off with their junior/senior, splitId is the ID of the one broken off
        //if it's junior, splitId will be the new leaderID, if its senior it's toSplit thats the new leader
        //happens when someone in fam breaks off with anyone else, either junior/senior
        //update all the members.
        MapleFamilyCharacter leader = getMFC(splitId);
        if (leader == null) {
            leader = def;
            if (leader == null) {
                return false;
            }
        }
        try {
            List<MapleFamilyCharacter> all = leader.getAllJuniors(this); //leader is included in this collection
            if (all.size() <= 1) { //but if leader is the only person, then we're done
                leaveFamily(leader, false);
                return true;
            }
            final int newId = createFamily(leader.getId());
            if (newId <= 0) {
                return false;
            }
            for (MapleFamilyCharacter mgc : all) {
                // need it for sql
                mgc.setFamilyId(newId);
                setOfflineFamilyStatus(newId, mgc.getSeniorId(), mgc.getJunior1(), mgc.getJunior2(), mgc.getCurrentRep(), mgc.getTotalRep(), mgc.getId());
                members.remove(mgc.getId()); //clean remove
            }
            final MapleFamily newfam = World.Family.getFamily(newId);
            for (MapleFamilyCharacter mgc : all) {
                if (mgc.isOnline()) { //NOW we change the char info
                    World.Family.setFamily(newId, mgc.getSeniorId(), mgc.getJunior1(), mgc.getJunior2(), mgc.getCurrentRep(), mgc.getTotalRep(), mgc.getId());
                }
                newfam.setOnline(mgc.getId(), mgc.isOnline(), mgc.getChannel());
            }
        } finally {
            if (members.size() <= 1) { //only one person is left :|
                World.Family.disbandFamily(id); //disband us.
                return true;
            }
        }
        bDirty = true;
        return false;
    }

    public final void setNotice(final String notice) {
        this.changed = true;
        this.family.setNotice(notice);
    }
}
