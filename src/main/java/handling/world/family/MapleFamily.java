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
package handling.world.family;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import client.MapleCharacter;
import database.DatabaseConnection;
import handling.MaplePacket;
import handling.world.World;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import tools.MaplePacketCreator;
import tools.packet.FamilyPacket;

public class MapleFamily implements java.io.Serializable {

    public static enum FCOp {

        NONE, DISBAND;
    }
    public static final long serialVersionUID = 6322150443228168192L;
    //does not need to be in order :) CID -> MFC
    private final Map<Integer, MapleFamilyCharacter> members = new ConcurrentHashMap<Integer, MapleFamilyCharacter>();
    private String leadername = null, notice;
    private int id, leaderid, generations = 0;
    private boolean proper = true, bDirty = false, changed = false;

    public MapleFamily(final int fid) {
        super();

        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM families WHERE familyid = ?");
            ps.setInt(1, fid);
            ResultSet rs = ps.executeQuery();

            if (!rs.first()) {
                rs.close();
                ps.close();
                id = -1;
                return;
            }
            id = fid;
            leaderid = rs.getInt("leaderid");
            notice = rs.getString("notice");
            rs.close();
            ps.close();
            //does not need to be in any order
            ps = con.prepareStatement("SELECT id, name, level, job, seniorid, junior1, junior2, currentrep, totalrep FROM characters WHERE familyid = ?");
            ps.setInt(1, fid);
            rs = ps.executeQuery();
            while (rs.next()) {
                if (rs.getInt("id") == leaderid) {
                    leadername = rs.getString("name");
                }
                members.put(rs.getInt("id"), new MapleFamilyCharacter(rs.getInt("id"), rs.getShort("level"), rs.getString("name"), (byte) -1, rs.getInt("job"), fid, rs.getInt("seniorid"), rs.getInt("junior1"), rs.getInt("junior2"), rs.getInt("currentrep"), rs.getInt("totalrep"), false));
            }
            rs.close();
            ps.close();

            if (leadername == null || members.size() < 2) {
                System.err.println("Leader " + leaderid + " isn't in family " + id + ".  Impossible... family is disbanding.");
            //    writeToDB(true);
                proper = false;
                return;
            }
            //upon startup, load all the seniorid/junior1/junior2 that aren't in this family
            for (MapleFamilyCharacter mfc : members.values()) { //just in case
                if (mfc.getJunior1() > 0 && (getMFC(mfc.getJunior1()) == null || mfc.getId() == mfc.getJunior1())) {
                    mfc.setJunior1(0);
                }
                if (mfc.getJunior2() > 0 && (getMFC(mfc.getJunior2()) == null || mfc.getId() == mfc.getJunior2() || mfc.getJunior1() == mfc.getJunior2())) {
                    mfc.setJunior2(0);
                }
                if (mfc.getSeniorId() > 0 && (getMFC(mfc.getSeniorId()) == null || mfc.getId() == mfc.getSeniorId())) {
                    mfc.setSeniorId(0);
                }
                if (mfc.getJunior2() > 0 && mfc.getJunior1() <= 0) {
                    mfc.setJunior1(mfc.getJunior2());
                    mfc.setJunior2(0);
                }
                if (mfc.getJunior1() > 0) {
                    MapleFamilyCharacter mfc2 = getMFC(mfc.getJunior1());
                    if (mfc2.getJunior1() == mfc.getId()) {
                        mfc2.setJunior1(0);
                    }
                    if (mfc2.getJunior2() == mfc.getId()) {
                        mfc2.setJunior2(0);
                    }
                    if (mfc2.getSeniorId() != mfc.getId()) {
                        mfc2.setSeniorId(mfc.getId());
                    }
                }
                if (mfc.getJunior2() > 0) {
                    MapleFamilyCharacter mfc2 = getMFC(mfc.getJunior2());
                    if (mfc2.getJunior1() == mfc.getId()) {
                        mfc2.setJunior1(0);
                    }
                    if (mfc2.getJunior2() == mfc.getId()) {
                        mfc2.setJunior2(0);
                    }
                    if (mfc2.getSeniorId() != mfc.getId()) {
                        mfc2.setSeniorId(mfc.getId());
                    }
                }
            }
            resetPedigree();
            resetDescendants(); //set
            resetGens(); //set
        } catch (SQLException se) {
            System.err.println("unable to read family information from sql");
            se.printStackTrace();
        }
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
        MapleFamilyCharacter mfc = getMFC(leaderid);
        if (mfc != null) {
            generations = mfc.resetGenerations(this);
        }
        bDirty = true;
    }

    public void resetDescendants() { //not stored here, but rather in the MFC
        MapleFamilyCharacter mfc = getMFC(leaderid);
        if (mfc != null) {
            mfc.resetDescendants(this);
        }
        bDirty = true;
    }

    public boolean isProper() {
        return proper;
    }

    public static final Collection<MapleFamily> loadAll() {
        final Collection<MapleFamily> ret = new ArrayList<MapleFamily>();
        MapleFamily g;
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT familyid FROM families");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                g = new MapleFamily(rs.getInt("familyid"));
                if (g.getId() > 0) {
                    ret.add(g);
                }
            }
            rs.close();
            ps.close();
        } catch (SQLException se) {
            System.err.println("unable to read family information from sql");
            se.printStackTrace();
        }
        return ret;
    }

    public final void writeToDB(final boolean bDisband) {
        try {
            Connection con = DatabaseConnection.getConnection();
            if (!bDisband) {
                if (changed) {
                    PreparedStatement ps = con.prepareStatement("UPDATE families SET notice = ? WHERE familyid = ?");
                    ps.setString(1, notice);
                    ps.setInt(2, id);
                    ps.execute();
                    ps.close();
                }
                changed = false;
            } else {
                //members is less than 2, this shall be executed
               /*
                 * if (leadername == null || members.size() < 2) {
                 * broadcast(null, -1, FCOp.DISBAND, null);
                 }
                 */

                PreparedStatement ps = con.prepareStatement("DELETE FROM families WHERE familyid = ?");
                ps.setInt(1, id);
                ps.execute();
                ps.close();
            }
        } catch (SQLException se) {
            System.err.println("Error saving family to SQL");
            se.printStackTrace();
        }
    }

    public final int getId() {
        return id;
    }

    public final int getLeaderId() {
        return leaderid;
    }

    public final String getNotice() {
        if (notice == null) {
            return "";
        }
        return notice;
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
                broadcast(FamilyPacket.familyLoggedIn(online, mgc.getName()), cid, mgc.getId() == leaderid ? null : mgc.getPedigree());
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
        final MapleFamilyCharacter ret = new MapleFamilyCharacter(mc, id, seniorid, junior1, junior2);
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
        if (mgc.getId() == leaderid && !skipLeader) {
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
                this.broadcast(MaplePacketCreator.sendLevelup(true, mgc.getLevel(), mgc.getName()), mgc.getId(), mgc.getId() == leaderid ? null : member.getPedigree());
            }
            if (old_job != mgc.getJob()) {
                this.broadcast(MaplePacketCreator.sendJobup(true, mgc.getJob(), mgc.getName()), mgc.getId(), mgc.getId() == leaderid ? null : member.getPedigree());
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
        try {
            java.sql.Connection con = DatabaseConnection.getConnection();
            java.sql.PreparedStatement ps = con.prepareStatement("UPDATE characters SET familyid = ?, seniorid = ?, junior1 = ?, junior2 = ?, currentrep = ?, totalrep = ? WHERE id = ?");
            ps.setInt(1, familyid);
            ps.setInt(2, seniorid);
            ps.setInt(3, junior1);
            ps.setInt(4, junior2);
            ps.setInt(5, currentrep);
            ps.setInt(6, totalrep);
            ps.setInt(7, cid);
            ps.execute();
            ps.close();
        } catch (SQLException se) {
            System.out.println("SQLException: " + se.getLocalizedMessage());
            se.printStackTrace();
        }
    }

    public static int createFamily(int leaderId) {
        try {
            Connection con = DatabaseConnection.getConnection();

            PreparedStatement ps = con.prepareStatement("INSERT INTO families (`leaderid`) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, leaderId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (!rs.next()) {
                rs.close();
                ps.close();
                return 0;
            }
            int ret = rs.getInt(1);
            rs.close();
            ps.close();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
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
        this.notice = notice;
    }
}
