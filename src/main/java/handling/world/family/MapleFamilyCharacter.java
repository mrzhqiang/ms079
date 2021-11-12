package handling.world.family;

import com.github.mrzhqiang.maplestory.domain.DCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDCharacter;
import com.github.mrzhqiang.maplestory.domain.query.QDFamily;

import java.util.ArrayList;
import java.util.List;

public class MapleFamilyCharacter implements java.io.Serializable {

    public static final long serialVersionUID = 2058609046116597760L;

    private int channel = -1;
    private List<Integer> pedigree = new ArrayList<>();
    private int descendants = 0;

    private boolean online;

    public final DCharacter character;

    public MapleFamilyCharacter(DCharacter character, int channel, boolean online) {
        this.character = character;
        this.channel = channel;
        this.online = online;
    }

    public int getLevel() {
        return character.level;
    }

    public void setLevel(int l) {
        character.level = l;
    }

    public int getId() {
        return character.id;
    }

    public void setChannel(int ch) {
        channel = ch;
    }

    public int getChannel() {
        return channel;
    }

    public int getJobId() {
        return character.job;
    }

    public void setJobId(int job) {
        character.job = job;
    }

    public int getCurrentRep() {
        return character.currentRep;
    }

    public void setCurrentRep(int cr) {
        character.currentRep = cr;
    }

    public int getTotalRep() {
        return character.totalRep;
    }

    public void setTotalRep(int tr) {
        character.totalRep = tr;
    }

    public int getJunior1() {
        return character.junior1.id;
    }

    public int getJunior2() {
        return character.junior2.id;
    }

    public void setJunior1(int trs) {
        character.junior1 = new QDCharacter().id.eq(trs).findOne();
    }

    public void setJunior2(int trs) {
        character.junior2 = new QDCharacter().id.eq(trs).findOne();
    }

    public int getSeniorId() {
        return character.senior.id;
    }

    public void setSeniorId(int si) {
        character.senior = new QDCharacter().id.eq(si).findOne();
    }

    public int getFamilyId() {
        return character.family != null ? character.family.id : 0;
    }

    public void setFamilyId(int fi) {
        character.family = new QDFamily().id.eq(fi).findOne();
    }

    public boolean isOnline() {
        return online;
    }

    public String getName() {
        return character.name;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof MapleFamilyCharacter)) {
            return false;
        }

        MapleFamilyCharacter o = (MapleFamilyCharacter) other;
        return (o.getId() == character.id && o.getName().equals(character.name));
    }

    public void setOnline(boolean f) {
        online = f;
    }

    public List<MapleFamilyCharacter> getAllJuniors(MapleFamily fam) {
        List<MapleFamilyCharacter> ret = new ArrayList<>();
        ret.add(this);
        if (character.junior1 != null) {
            MapleFamilyCharacter chr = fam.getMFC(character.junior1.id);
            if (chr != null) {
                ret.addAll(chr.getAllJuniors(fam));
                //} else {
                //	junior1 = 0;
            }
        }
        if (character.junior2 != null) {
            MapleFamilyCharacter chr = fam.getMFC(character.junior2.id);
            if (chr != null) {
                ret.addAll(chr.getAllJuniors(fam));
                //} else {
                //	junior2 = 0;
            }
        }
        return ret;
    }

    public List<MapleFamilyCharacter> getOnlineJuniors(MapleFamily fam) {
        List<MapleFamilyCharacter> ret = new ArrayList<>();
        ret.add(this);
        if (character.junior1 != null) {
            MapleFamilyCharacter chr = fam.getMFC(character.junior1.id);
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
        if (character.junior2 != null) {
            MapleFamilyCharacter chr = fam.getMFC(character.junior2.id);
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

    public void resetPedigree(MapleFamily fam) {
        pedigree = new ArrayList<>();
        pedigree.add(character.id); //lol
        if (character.senior != null) {
            MapleFamilyCharacter chr = fam.getMFC(character.senior.id);
            if (chr != null) {
                pedigree.add(character.senior.id);
                if (chr.getSeniorId() > 0) {
                    pedigree.add(chr.getSeniorId());
                }
                if (chr.getJunior1() > 0 && chr.getJunior1() != character.id) {
                    pedigree.add(chr.getJunior1());
                } else if (chr.getJunior2() > 0 && chr.getJunior2() != character.id) {
                    pedigree.add(chr.getJunior2());
                }
                //} else {
                //	seniorid = 0;
            }
        }
        if (character.junior1 != null) {
            MapleFamilyCharacter chr = fam.getMFC(character.junior1.id);
            if (chr != null) {
                pedigree.add(character.junior1.id);
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
        if (character.junior2 != null) {
            MapleFamilyCharacter chr = fam.getMFC(character.junior2.id);
            if (chr != null) {
                pedigree.add(character.junior2.id);
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
        if (character.junior1 != null) {
            MapleFamilyCharacter chr = fam.getMFC(character.junior1.id);
            if (chr != null) {
                descendants += 1 + chr.resetDescendants(fam);
            }
        }
        if (character.junior2 != null) {
            MapleFamilyCharacter chr = fam.getMFC(character.junior2.id);
            if (chr != null) {
                descendants += 1 + chr.resetDescendants(fam);
            }
        }
        return descendants;
    }

    public int resetGenerations(MapleFamily fam) { //advisable to only start this with leader. resets EVERYONE
        //recursion. this field is NOT stored so please be advised
        int descendants1 = 0, descendants2 = 0;
        if (character.junior1 != null) {
            MapleFamilyCharacter chr = fam.getMFC(character.junior1.id);
            if (chr != null) {
                descendants1 = chr.resetGenerations(fam);
            }
        }
        if (character.junior2 != null) {
            MapleFamilyCharacter chr = fam.getMFC(character.junior2.id);
            if (chr != null) {
                descendants2 = chr.resetGenerations(fam);
            }
        }
        int ret = Math.max(descendants1, descendants2);
        return ret + (ret > 0 ? 1 : 0);
    }

    public int getNoJuniors() {
        int ret = 0;
        if (character.junior1 != null) {
            ret++;
        }
        if (character.junior2 != null) {
            ret++;
        }
        return ret;
    }
}
