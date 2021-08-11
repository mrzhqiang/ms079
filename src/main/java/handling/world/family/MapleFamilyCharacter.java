/*
 This file is part of the ZeroFusion MapleStory Server
 Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
 Matthias Butz <matze@odinms.de>
 Jan Christian Meyer <vimes@odinms.de>
 ZeroFusion organized by "RMZero213" <RMZero213@hotmail.com>

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

import client.MapleCharacter;
import handling.world.World;
import java.util.ArrayList;
import java.util.List;

public class MapleFamilyCharacter implements java.io.Serializable {

    public static final long serialVersionUID = 2058609046116597760L;
    private int level, id, channel = -1, jobid, familyid, seniorid, currentrep, totalrep, junior1, junior2;
    private boolean online;
    private String name;
    private List<Integer> pedigree = new ArrayList<Integer>(); //recalculate
    private int descendants = 0;

	// either read from active character...
    // if it's online
    public MapleFamilyCharacter(MapleCharacter c, int fid, int sid, int j1, int j2) {
        name = c.getName();
        level = c.getLevel();
        id = c.getId();
        channel = c.getClient().getChannel();
        jobid = c.getJob();
        familyid = fid;
        junior1 = j1;
        junior2 = j2;
        seniorid = sid;
        currentrep = c.getCurrentRep();
        totalrep = c.getTotalRep();
        online = true;
    }

    // or we could just read from the database
    public MapleFamilyCharacter(int _id, int _lv, String _name, int _channel, int _job, int _fid, int _sid, int _jr1, int _jr2, int _crep, int _trep, boolean _on) {
        level = _lv;
        id = _id;
        name = _name;
        if (_on) {
            channel = _channel;
        }
        jobid = _job;
        online = _on;
        familyid = _fid;
        seniorid = _sid;
        currentrep = _crep;
        totalrep = _trep;
        junior1 = _jr1;
        junior2 = _jr2;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int l) {
        level = l;
    }

    public int getId() {
        return id;
    }

    public void setChannel(int ch) {
        channel = ch;
    }

    public int getChannel() {
        return channel;
    }

    public int getJobId() {
        return jobid;
    }

    public void setJobId(int job) {
        jobid = job;
    }

    public int getCurrentRep() {
        return currentrep;
    }

    public void setCurrentRep(int cr) {
        this.currentrep = cr;
    }

    public int getTotalRep() {
        return totalrep;
    }

    public void setTotalRep(int tr) {
        this.totalrep = tr;
    }

    public int getJunior1() {
        return junior1;
    }

    public int getJunior2() {
        return junior2;
    }

    public void setJunior1(int trs) {
        junior1 = trs;
    }

    public void setJunior2(int trs) {
        junior2 = trs;
    }

    public int getSeniorId() {
        return seniorid;
    }

    public void setSeniorId(int si) {
        this.seniorid = si;
    }

    public int getFamilyId() {
        return familyid;
    }

    public void setFamilyId(int fi) {
        this.familyid = fi;
    }

    public boolean isOnline() {
        return online;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MapleFamilyCharacter)) {
            return false;
        }

        MapleFamilyCharacter o = (MapleFamilyCharacter) other;
        return (o.getId() == id && o.getName().equals(name));
    }

    public void setOnline(boolean f) {
        online = f;
    }

    public List<MapleFamilyCharacter> getAllJuniors(MapleFamily fam) { //to be used scarcely
        List<MapleFamilyCharacter> ret = new ArrayList<MapleFamilyCharacter>();
        ret.add(this);
        if (junior1 > 0) {
            MapleFamilyCharacter chr = fam.getMFC(junior1);
            if (chr != null) {
                ret.addAll(chr.getAllJuniors(fam));
			//} else {
                //	junior1 = 0;
            }
        }
        if (junior2 > 0) {
            MapleFamilyCharacter chr = fam.getMFC(junior2);
            if (chr != null) {
                ret.addAll(chr.getAllJuniors(fam));
			//} else {
                //	junior2 = 0;
            }
        }
        return ret;
    }

    public List<MapleFamilyCharacter> getOnlineJuniors(MapleFamily fam) { //to be used scarcely
        List<MapleFamilyCharacter> ret = new ArrayList<MapleFamilyCharacter>();
        ret.add(this);
        if (junior1 > 0) {
            MapleFamilyCharacter chr = fam.getMFC(junior1);
            if (chr != null) {
                if (chr.isOnline()) {
                    ret.add(chr);
                }
                if (chr.getJunior1() > 0) {
                    MapleFamilyCharacter chr2 = fam.getMFC(chr.getJunior1());
                    if (chr2 != null && chr2.isOnline()) {
                        ret.add(chr2);
                    }
                }
                if (chr.getJunior2() > 0) {
                    MapleFamilyCharacter chr2 = fam.getMFC(chr.getJunior2());
                    if (chr2 != null && chr2.isOnline()) {
                        ret.add(chr2);
                    }
                }
			//} else {
                //	junior1 = 0;
            }
        }
        if (junior2 > 0) {
            MapleFamilyCharacter chr = fam.getMFC(junior2);
            if (chr != null) {
                if (chr.isOnline()) {
                    ret.add(chr);
                }
                if (chr.getJunior1() > 0) {
                    MapleFamilyCharacter chr2 = fam.getMFC(chr.getJunior1());
                    if (chr2 != null && chr2.isOnline()) {
                        ret.add(chr2);
                    }
                }
                if (chr.getJunior2() > 0) {
                    MapleFamilyCharacter chr2 = fam.getMFC(chr.getJunior2());
                    if (chr2 != null && chr2.isOnline()) {
                        ret.add(chr2);
                    }
                }
			//} else {
                //	junior2 = 0;
            }
        }
        return ret;
    }

    public List<Integer> getPedigree() {
        return pedigree;
    }

    public void resetPedigree(MapleFamily fam) { //not in order
        pedigree = new ArrayList<Integer>();
        pedigree.add(id); //lol
        if (seniorid > 0) {
            MapleFamilyCharacter chr = fam.getMFC(seniorid);
            if (chr != null) {
                pedigree.add(seniorid);
                if (chr.getSeniorId() > 0) {
                    pedigree.add(chr.getSeniorId());
                }
                if (chr.getJunior1() > 0 && chr.getJunior1() != id) {
                    pedigree.add(chr.getJunior1());
                } else if (chr.getJunior2() > 0 && chr.getJunior2() != id) {
                    pedigree.add(chr.getJunior2());
                }
			//} else {
                //	seniorid = 0;
            }
        }
        if (junior1 > 0) {
            MapleFamilyCharacter chr = fam.getMFC(junior1);
            if (chr != null) {
                pedigree.add(junior1);
                if (chr.getJunior1() > 0) {
                    pedigree.add(chr.getJunior1());
                }
                if (chr.getJunior2() > 0) {
                    pedigree.add(chr.getJunior2());
                }
			//} else {
                //	junior1 = 0;
            }
        }
        if (junior2 > 0) {
            MapleFamilyCharacter chr = fam.getMFC(junior2);
            if (chr != null) {
                pedigree.add(junior2);
                if (chr.getJunior1() > 0) {
                    pedigree.add(chr.getJunior1());
                }
                if (chr.getJunior2() > 0) {
                    pedigree.add(chr.getJunior2());
                }
			//} else {
                //	junior2 = 0;
            }
        }

    }

    public int getDescendants() {
        return descendants;
    }

    public int resetDescendants(MapleFamily fam) { //advisable to only start this with leader. resets EVERYONE
        //recursion.
        descendants = 0;
        if (junior1 > 0) {
            MapleFamilyCharacter chr = fam.getMFC(junior1);
            if (chr != null) {
                descendants += 1 + chr.resetDescendants(fam);
            }
        }
        if (junior2 > 0) {
            MapleFamilyCharacter chr = fam.getMFC(junior2);
            if (chr != null) {
                descendants += 1 + chr.resetDescendants(fam);
            }
        }
        return descendants;
    }

    public int resetGenerations(MapleFamily fam) { //advisable to only start this with leader. resets EVERYONE
        //recursion. this field is NOT stored so please be advised
        int descendants1 = 0, descendants2 = 0;
        if (junior1 > 0) {
            MapleFamilyCharacter chr = fam.getMFC(junior1);
            if (chr != null) {
                descendants1 = chr.resetGenerations(fam);
            }
        }
        if (junior2 > 0) {
            MapleFamilyCharacter chr = fam.getMFC(junior2);
            if (chr != null) {
                descendants2 = chr.resetGenerations(fam);
            }
        }
        int ret = Math.max(descendants1, descendants2);
        return ret + (ret > 0 ? 1 : 0);
    }

    public int getNoJuniors() {
        int ret = 0;
        if (junior1 > 0) {
            ret++;
        }
        if (junior2 > 0) {
            ret++;
        }
        return ret;
    }
}
