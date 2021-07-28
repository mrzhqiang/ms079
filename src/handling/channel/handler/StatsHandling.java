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
package handling.channel.handler;

import constants.GameConstants;
import java.util.ArrayList;
import java.util.List;

import client.ISkill;
import client.MapleClient;
import client.MapleCharacter;
import client.MapleStat;
import client.PlayerStats;
import client.SkillFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.AutobanManager;
import server.Randomizer;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.data.input.SeekableLittleEndianAccessor;

public class StatsHandling {

    private static Logger log = LoggerFactory.getLogger(StatsHandling.class);
    
    public static final void DistributeAP(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final List<Pair<MapleStat, Integer>> statupdate = new ArrayList<Pair<MapleStat, Integer>>(2);
        c.getSession().write(MaplePacketCreator.updatePlayerStats(statupdate, true, chr.getJob()));
        chr.updateTick(slea.readInt());

        final PlayerStats stat = chr.getStat();
        final int job = chr.getJob();
        if (chr.getRemainingAp() > 0) {
            switch (slea.readInt()) {
                case 256: // Str
                    if (stat.getStr() >= 999) {
                        return;
                    }
                    stat.setStr((short) (stat.getStr() + 1));
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.STR, (int) stat.getStr()));
                    break;
                case 512: // Dex
                    if (stat.getDex() >= 999) {
                        return;
                    }
                    stat.setDex((short) (stat.getDex() + 1));
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.DEX, (int) stat.getDex()));
                    break;
                case 1024: // Int
                    if (stat.getInt() >= 999) {
                        return;
                    }
                    stat.setInt((short) (stat.getInt() + 1));
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.INT, (int) stat.getInt()));
                    break;
                case 2048: // Luk
                    if (stat.getLuk() >= 999) {
                        return;
                    }
                    stat.setLuk((short) (stat.getLuk() + 1));
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.LUK, (int) stat.getLuk()));
                    break;
                case 8192: // HP
                    short maxhp = stat.getMaxHp();
                    if (chr.getHpApUsed() >= 10000 || maxhp >= 30000) {
                        return;
                    }
                    if (job == 0) { // Beginner
                        maxhp += Randomizer.rand(8, 12);
                    } else if ((job >= 100 && job <= 132) || (job >= 3200 && job <= 3212)) { // Warrior
                        ISkill improvingMaxHP = SkillFactory.getSkill(1000001);
                        int improvingMaxHPLevel = c.getPlayer().getSkillLevel(improvingMaxHP);
                        maxhp += Randomizer.rand(20, 25);
                        if (improvingMaxHPLevel >= 1) {
                            maxhp += improvingMaxHP.getEffect(improvingMaxHPLevel).getX();
                        }
                    } else if ((job >= 200 && job <= 232) || (GameConstants.isEvan(job))) { // Magician
                        maxhp += Randomizer.rand(10, 20);
                    } else if ((job >= 300 && job <= 322) || (job >= 400 && job <= 434) || (job >= 1300 && job <= 1312) || (job >= 1400 && job <= 1412) || (job >= 3300 && job <= 3312)) { // Bowman
                        maxhp += Randomizer.rand(16, 20);
                    } else if ((job >= 500 && job <= 522) || (job >= 3500 && job <= 3512)) { // Pirate
                        ISkill improvingMaxHP = SkillFactory.getSkill(5100000);
                        int improvingMaxHPLevel = c.getPlayer().getSkillLevel(improvingMaxHP);
                        maxhp += Randomizer.rand(18, 22);
                        if (improvingMaxHPLevel >= 1) {
                            maxhp += improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                        }
                    } else if (job >= 1500 && job <= 1512) { // Pirate
                        ISkill improvingMaxHP = SkillFactory.getSkill(15100000);
                        int improvingMaxHPLevel = c.getPlayer().getSkillLevel(improvingMaxHP);
                        maxhp += Randomizer.rand(18, 22);
                        if (improvingMaxHPLevel >= 1) {
                            maxhp += improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                        }
                    } else if (job >= 1100 && job <= 1112) { // Soul Master
                        ISkill improvingMaxHP = SkillFactory.getSkill(11000000);
                        int improvingMaxHPLevel = c.getPlayer().getSkillLevel(improvingMaxHP);
                        maxhp += Randomizer.rand(36, 42);
                        if (improvingMaxHPLevel >= 1) {
                            maxhp += improvingMaxHP.getEffect(improvingMaxHPLevel).getY();
                        }
                    } else if (job >= 1200 && job <= 1212) { // Flame Wizard
                        maxhp += Randomizer.rand(15, 21);
                    } else if (job >= 2000 && job <= 2112) { // Aran
                        maxhp += Randomizer.rand(40, 50);
                    } else { // GameMaster
                        maxhp += Randomizer.rand(50, 100);
                    }
                    maxhp = (short) Math.min(30000, Math.abs(maxhp));
                    chr.setHpApUsed((short) (chr.getHpApUsed() + 1));
                    stat.setMaxHp(maxhp);
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.MAXHP, (int) maxhp));
                    break;
                case 32768: // MP
                    short maxmp = stat.getMaxMp();
                    short Int = (short) ((short) (stat.getInt() / 10) - 10);
                    if (Int < 0) {
                        Int = 0;
                    }
                    if (chr.getHpApUsed() >= 10000 || stat.getMaxMp() >= 30000) {
                        return;
                    }
                    if (job == 0) { // Beginner
                        maxmp += Randomizer.rand(6, 8);
                    } else if (job >= 100 && job <= 132) { // Warrior
                        maxmp += Randomizer.rand(2, 4);
                    } else if ((job >= 200 && job <= 232) || (GameConstants.isEvan(job)) || (job >= 3200 && job <= 3212)) { // Magician
                        ISkill improvingMaxMP = SkillFactory.getSkill(2000001);
                        int improvingMaxMPLevel = c.getPlayer().getSkillLevel(improvingMaxMP);
                        maxmp += Randomizer.rand(18, 20);
                        if (improvingMaxMPLevel >= 1) {
                            maxmp += improvingMaxMP.getEffect(improvingMaxMPLevel).getY() * 2;
                        }
                    } else if ((job >= 300 && job <= 322) || (job >= 400 && job <= 434) || (job >= 500 && job <= 522) || (job >= 3200 && job <= 3212) || (job >= 3500 && job <= 3512) || (job >= 1300 && job <= 1312) || (job >= 1400 && job <= 1412) || (job >= 1500 && job <= 1512)) { // Bowman
                        maxmp += Randomizer.rand(10, 12);
                    } else if (job >= 1100 && job <= 1112) { // Soul Master
                        maxmp += Randomizer.rand(6, 9);
                    } else if (job >= 1200 && job <= 1212) { // Flame Wizard
                        ISkill improvingMaxMP = SkillFactory.getSkill(12000000);
                        int improvingMaxMPLevel = c.getPlayer().getSkillLevel(improvingMaxMP);
                        maxmp += Randomizer.rand(18, 20);
                        if (improvingMaxMPLevel >= 1) {
                            maxmp += improvingMaxMP.getEffect(improvingMaxMPLevel).getY() * 2;
                        }
                    } else if (job >= 2000 && job <= 2112) { // Aran
                        maxmp += Randomizer.rand(6, 9);
                    } else { // GameMaster
                        maxmp += Randomizer.rand(50, 100);
                    }
                    maxmp = (short) (maxmp + Int);
                    maxmp = (short) Math.min(30000, Math.abs(maxmp));
                    chr.setHpApUsed((short) (chr.getHpApUsed() + 1));
                    stat.setMaxMp(maxmp);
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.MAXMP, (int) maxmp));
                    break;
                default:
                    c.getSession().write(MaplePacketCreator.updatePlayerStats(MaplePacketCreator.EMPTY_STATUPDATE, true, chr.getJob()));
                    return;
            }
            chr.setRemainingAp((short) (chr.getRemainingAp() - 1));
            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.AVAILABLEAP, (int) chr.getRemainingAp()));
            c.getSession().write(MaplePacketCreator.updatePlayerStats(statupdate, true, chr.getJob()));
        }
    }

    public static void DistributeSP(final int skillid, final MapleClient c, final MapleCharacter chr) {
        boolean isBeginnerSkill = false;
        final int remainingSp;

        switch (skillid) {
            case 1000:
            case 1001:
            case 1002: {
                final int snailsLevel = chr.getSkillLevel(SkillFactory.getSkill(1000));
                final int recoveryLevel = chr.getSkillLevel(SkillFactory.getSkill(1001));
                final int nimbleFeetLevel = chr.getSkillLevel(SkillFactory.getSkill(1002));
                remainingSp = Math.min((chr.getLevel() - 1), 6) - snailsLevel - recoveryLevel - nimbleFeetLevel;
                isBeginnerSkill = true;
                break;
            }
            case 10001000:
            case 10001001:
            case 10001002: {
                final int snailsLevel = chr.getSkillLevel(SkillFactory.getSkill(10001000));
                final int recoveryLevel = chr.getSkillLevel(SkillFactory.getSkill(10001001));
                final int nimbleFeetLevel = chr.getSkillLevel(SkillFactory.getSkill(10001002));
                remainingSp = Math.min((chr.getLevel() - 1), 6) - snailsLevel - recoveryLevel - nimbleFeetLevel;
                isBeginnerSkill = true;
                break;
            }
            case 20001000:
            case 20001001:
            case 20001002: {
                final int snailsLevel = chr.getSkillLevel(SkillFactory.getSkill(20001000));
                final int recoveryLevel = chr.getSkillLevel(SkillFactory.getSkill(20001001));
                final int nimbleFeetLevel = chr.getSkillLevel(SkillFactory.getSkill(20001002));
                remainingSp = Math.min((chr.getLevel() - 1), 6) - snailsLevel - recoveryLevel - nimbleFeetLevel;
                isBeginnerSkill = true;
                break;
            }
            case 20011000:
            case 20011001:
            case 20011002: {
                final int snailsLevel = chr.getSkillLevel(SkillFactory.getSkill(20011000));
                final int recoveryLevel = chr.getSkillLevel(SkillFactory.getSkill(20011001));
                final int nimbleFeetLevel = chr.getSkillLevel(SkillFactory.getSkill(20011002));
                remainingSp = Math.min((chr.getLevel() - 1), 6) - snailsLevel - recoveryLevel - nimbleFeetLevel;
                isBeginnerSkill = true;
                break;
            }
            case 30001000:
            case 30001001:
            case 30000002: {
                final int snailsLevel = chr.getSkillLevel(SkillFactory.getSkill(30001000));
                final int recoveryLevel = chr.getSkillLevel(SkillFactory.getSkill(30001001));
                final int nimbleFeetLevel = chr.getSkillLevel(SkillFactory.getSkill(30000002));
                remainingSp = Math.min((chr.getLevel() - 1), 9) - snailsLevel - recoveryLevel - nimbleFeetLevel;
                isBeginnerSkill = true; //resist can max ALL THREE
                break;
            }
            default: {
                remainingSp = chr.getRemainingSp(GameConstants.getSkillBookForSkill(skillid));
                break;
            }
        }
        final ISkill skill = SkillFactory.getSkill(skillid);

        if (skill.hasRequiredSkill()) {
            if (chr.getSkillLevel(SkillFactory.getSkill(skill.getRequiredSkillId())) < skill.getRequiredSkillLevel()) {
//                AutobanManager.getInstance().addPoints(c, 1000, 0, "Trying to learn a skill without the required skill (" + skillid + ")");
                return;
            }
        }
        final int maxlevel = skill.isFourthJob() ? chr.getMasterLevel(skill) : skill.getMaxLevel();
        final int curLevel = chr.getSkillLevel(skill);

        if (skill.isInvisible() && chr.getSkillLevel(skill) == 0) {
            if ((skill.isFourthJob() && chr.getMasterLevel(skill) == 0) || (!skill.isFourthJob() && maxlevel < 10 && !isBeginnerSkill)) {
                //AutobanManager.getInstance().addPoints(c, 1000, 0, "Illegal distribution of SP to invisible skills (" + skillid + ")");
                return;
            }
        }

        for (int i : GameConstants.blockedSkills) {
            if (skill.getId() == i) {
                chr.dropMessage(1, "你可能不会增加这个技能.");
                return;
            }
        }

        if ((remainingSp > 0 && curLevel + 1 <= maxlevel) && (skill.canBeLearnedBy(chr.getJob()) || isBeginnerSkill)) {
            if (!isBeginnerSkill) {
                final int skillbook = GameConstants.getSkillBookForSkill(skillid);
                chr.setRemainingSp(chr.getRemainingSp(skillbook) - 1, skillbook);
            }
            chr.updateSingleStat(MapleStat.AVAILABLESP, chr.getRemainingSp());
           // c.getSession().write(MaplePacketCreator.updateSp(chr, false));
            chr.changeSkillLevel(skill, (byte) (curLevel + 1), chr.getMasterLevel(skill));
        } else if (!skill.canBeLearnedBy(chr.getJob())) {
//            AutobanManager.getInstance().addPoints(c, 1000, 0, "Trying to learn a skill for a different job (" + skillid + ")");
            return;
        }
    }

    public static final void AutoAssignAP(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
         List statupdate = new ArrayList(2);
     //   c.getSession().write(MaplePacketCreator.updatePlayerStats(statupdate, 0));
        c.getSession().write(MaplePacketCreator.updatePlayerStats(statupdate, true, chr.getJob()));
        final PlayerStats playerst = chr.getStat();
        slea.readInt();
        int count = slea.readInt();
        for (int i = 0; i < count; i++) {
            int update = slea.readInt();
            int updatenumber = slea.readInt();
            if (chr.getRemainingAp() >= updatenumber) {
                switch (update) {
                    case 256:
                        if (playerst.getStr() + updatenumber >= 30000) {
                            return;
                        }
                        playerst.setStr((short) (playerst.getStr() + updatenumber));
                        statupdate.add(new Pair(MapleStat.STR, Integer.valueOf(playerst.getStr())));
                        break;
                    case 512:
                        if (playerst.getDex() + updatenumber >= 30000) {
                            return;
                        }
                        playerst.setDex((short) (playerst.getDex() + updatenumber));
                        statupdate.add(new Pair(MapleStat.DEX, Integer.valueOf(playerst.getDex())));
                        break;
                    case 1024:
                        if (playerst.getInt() + updatenumber >= 30000) {
                            return;
                        }
                        playerst.setInt((short) (playerst.getInt() + updatenumber));
                        statupdate.add(new Pair(MapleStat.INT, Integer.valueOf(playerst.getInt())));
                        break;
                    case 2048:
                        if (playerst.getLuk() + updatenumber >= 30000) {
                            return;
                        }
                        playerst.setLuk((short) (playerst.getLuk() + updatenumber));
                        statupdate.add(new Pair(MapleStat.LUK, Integer.valueOf(playerst.getLuk())));
                        break;
                    default:
                      //  c.getSession().write(MaplePacketCreator.updatePlayerStats(MaplePacketCreator.EMPTY_STATUPDATE, 0));
                    c.getSession().write(MaplePacketCreator.updatePlayerStats(MaplePacketCreator.EMPTY_STATUPDATE, true, chr.getJob()));
                        return;
                }
                chr.setRemainingAp((short) (chr.getRemainingAp() - updatenumber));
            } else {
                log.info("[h4x] Player {} is distributing AP to {} without having any", chr.getName(), Integer.valueOf(update));
            }
        }
       // statupdate.add(new Pair(MapleStat.AVAILABLEAP, Integer.valueOf(chr.getRemainingAp())));
        statupdate.add(new Pair<MapleStat, Integer>(MapleStat.AVAILABLEAP, (int) chr.getRemainingAp()));
        c.getSession().write(MaplePacketCreator.updatePlayerStats(statupdate, true, chr.getJob()));
     //   c.getSession().write(MaplePacketCreator.updatePlayerStats(statupdate, 0));
        
       /* chr.updateTick(slea.readInt());
        slea.skip(4);
        if (slea.available() < 16) {
            System.out.println("AutoAssignAP : \n" + slea.toString(true));
            FileoutputUtil.log(FileoutputUtil.PacketEx_Log, "AutoAssignAP : \n" + slea.toString(true));
            return;
        }
        final int PrimaryStat = slea.readInt();
        final int amount = slea.readInt();
        final int SecondaryStat = slea.readInt();
        final int amount2 = slea.readInt();
        if (amount < 0 || amount2 < 0) {
            return;
        }

        final PlayerStats playerst = chr.getStat();

        List<Pair<MapleStat, Integer>> statupdate = new ArrayList<Pair<MapleStat, Integer>>(2);
        c.getSession().write(MaplePacketCreator.updatePlayerStats(statupdate, true, chr.getJob()));

        if (chr.getRemainingAp() == amount + amount2) {
            switch (PrimaryStat) {
                case 64: // Str
                    if (playerst.getStr() + amount > 999) {
                        return;
                    }
                    playerst.setStr((short) (playerst.getStr() + amount));
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.STR, (int) playerst.getStr()));
                    break;
                case 128: // Dex
                    if (playerst.getDex() + amount > 999) {
                        return;
                    }
                    playerst.setDex((short) (playerst.getDex() + amount));
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.DEX, (int) playerst.getDex()));
                    break;
                case 256: // Int
                    if (playerst.getInt() + amount > 999) {
                        return;
                    }
                    playerst.setInt((short) (playerst.getInt() + amount));
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.INT, (int) playerst.getInt()));
                    break;
                case 512: // Luk
                    if (playerst.getLuk() + amount > 999) {
                        return;
                    }
                    playerst.setLuk((short) (playerst.getLuk() + amount));
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.LUK, (int) playerst.getLuk()));
                    break;
                default:
                    c.getSession().write(MaplePacketCreator.updatePlayerStats(MaplePacketCreator.EMPTY_STATUPDATE, true, chr.getJob()));
                    return;
            }
            switch (SecondaryStat) {
                case 64: // Str
                    if (playerst.getStr() + amount2 > 999) {
                        return;
                    }
                    playerst.setStr((short) (playerst.getStr() + amount2));
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.STR, (int) playerst.getStr()));
                    break;
                case 128: // Dex
                    if (playerst.getDex() + amount2 > 999) {
                        return;
                    }
                    playerst.setDex((short) (playerst.getDex() + amount2));
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.DEX, (int) playerst.getDex()));
                    break;
                case 256: // Int
                    if (playerst.getInt() + amount2 > 999) {
                        return;
                    }
                    playerst.setInt((short) (playerst.getInt() + amount2));
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.INT, (int) playerst.getInt()));
                    break;
                case 512: // Luk
                    if (playerst.getLuk() + amount2 > 999) {
                        return;
                    }
                    playerst.setLuk((short) (playerst.getLuk() + amount2));
                    statupdate.add(new Pair<MapleStat, Integer>(MapleStat.LUK, (int) playerst.getLuk()));
                    break;
                default:
                    c.getSession().write(MaplePacketCreator.updatePlayerStats(MaplePacketCreator.EMPTY_STATUPDATE, true, chr.getJob()));
                    return;
            }
            chr.setRemainingAp((short) (chr.getRemainingAp() - (amount + amount2)));
            statupdate.add(new Pair<MapleStat, Integer>(MapleStat.AVAILABLEAP, (int) chr.getRemainingAp()));
            c.getSession().write(MaplePacketCreator.updatePlayerStats(statupdate, true, chr.getJob()));
        }*/
    }
}
