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

import java.awt.Point;
import java.util.List;

import client.inventory.IItem;
import client.ISkill;
import client.SkillFactory;
import client.SkillMacro;
import constants.GameConstants;
import client.inventory.MapleInventoryType;
import client.MapleBuffStat;
import client.MapleClient;
import client.MapleCharacter;
import client.PlayerStats;
import client.anticheat.CheatingOffense;
import constants.MapConstants;
import handling.channel.ChannelServer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import server.AutobanManager;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.MapleStatEffect;
import server.MaplePortal;
import server.Randomizer;
import server.Timer.CloneTimer;
import server.events.MapleSnowball.MapleSnowballs;
import server.life.MapleMonster;
import server.life.MobAttackInfo;
import server.life.MobAttackInfoFactory;
import server.life.MobSkill;
import server.life.MobSkillFactory;
import server.maps.MapleMap;
import server.maps.FieldLimitType;
import server.maps.MapleMapObjectType;
import server.movement.LifeMovementFragment;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;
import tools.packet.MobPacket;
import tools.packet.MTSCSPacket;
import tools.data.input.SeekableLittleEndianAccessor;

public class PlayerHandler {

    private static boolean isFinisher(final int skillid) {
        switch (skillid) {
            case 1111003:
            case 1111004:
            case 1111005:
            case 1111006:
            case 11111002:
            case 11111003:
                return true;
        }
        return false;
    }

    public static void ChangeMonsterBookCover(final int bookid, final MapleClient c, final MapleCharacter chr) {
        if (bookid == 0 || GameConstants.isMonsterCard(bookid)) {
            chr.setMonsterBookCover(bookid);
            chr.getMonsterBook().updateCard(c, bookid);
        }
    }

    public static void ChangeSkillMacro(final SeekableLittleEndianAccessor slea, final MapleCharacter chr) {
        final int num = slea.readByte();
        String name;
        int shout, skill1, skill2, skill3;
        SkillMacro macro;

        for (int i = 0; i < num; i++) {
            name = slea.readMapleAsciiString();
            shout = slea.readByte();
            skill1 = slea.readInt();
            skill2 = slea.readInt();
            skill3 = slea.readInt();

            macro = new SkillMacro(skill1, skill2, skill3, name, shout, i);
            chr.updateMacros(i, macro);
        }
    }

    public static final void ChangeKeymap(final SeekableLittleEndianAccessor slea, final MapleCharacter chr) {

        if (slea.available() > 8 && chr != null) { // else = pet auto pot
            chr.updateTick(slea.readInt());
            final int numChanges = slea.readInt();

            for (int i = 0; i < numChanges; i++) {
                chr.changeKeybinding(slea.readInt(), slea.readByte(), slea.readInt());
            }
        } else if (chr != null) {
            int type = slea.readInt();
            int data = slea.readInt();
            switch (type) {
                case 1:
                    if (data <= 0) {
                        chr.getQuestRemove(MapleQuest.getInstance(122221));
                    } else {
                        chr.getQuestNAdd(MapleQuest.getInstance(122221)).setCustomData(String.valueOf(data));
                    }
                    break;
                case 2:
                    if (data <= 0) {
                        chr.getQuestRemove(MapleQuest.getInstance(122222));
                    } else {
                        chr.getQuestNAdd(MapleQuest.getInstance(122222)).setCustomData(String.valueOf(data));
                    }
                    break;
                case 3:
                    if (data <= 0) {
                        chr.getQuestRemove(MapleQuest.getInstance(122224));
                    } else {
                        chr.getQuestNAdd(MapleQuest.getInstance(122224)).setCustomData(String.valueOf(data));
                    }
            }
        }
    }

    public static final void UseChair(final int itemId, final MapleClient c, final MapleCharacter chr) {

        if (chr == null) {
            return;
        }
        final IItem toUse = chr.getInventory(MapleInventoryType.SETUP).findById(itemId);

        if (toUse == null) {
            chr.getCheatTracker().registerOffense(CheatingOffense.使用不存在道具, Integer.toString(itemId));
            return;
        }
        if (itemId == 3011000) {
            boolean haz = false;
            for (IItem item : c.getPlayer().getInventory(MapleInventoryType.CASH).list()) {
                if (item.getItemId() == 5340000) {
                    haz = true;
                    chr.startFishingTask(true);
                } else if (item.getItemId() == 5340001) {
                    haz = false;
                    chr.startFishingTask(true);
                    break;
                }
            }
            if (haz) {
                chr.startFishingTask(false);
            }
        }
        chr.setChair(itemId);
        chr.getMap().broadcastMessage(chr, MaplePacketCreator.showChair(chr.getId(), itemId), false);
        c.getSession().write(MaplePacketCreator.enableActions());
    }

    public static final void CancelChair(final short id, final MapleClient c, final MapleCharacter chr) {
        if (id == -1) { // Cancel Chair
            if (chr.getChair() == 3011000) {
                chr.cancelFishingTask();
            }
            chr.setChair(0);
            c.getSession().write(MaplePacketCreator.cancelChair(-1));
            chr.getMap().broadcastMessage(chr, MaplePacketCreator.showChair(chr.getId(), 0), false);
        } else { // Use In-Map Chair
            chr.setChair(id);
            c.getSession().write(MaplePacketCreator.cancelChair(id));
        }
    }

    /*
     * 使用缩地石
     */
    public static final void TrockAddMap(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final byte addrem = slea.readByte();
        final byte vip = slea.readByte();

        if (vip == 1) {
            if (addrem == 0) {
                chr.deleteFromRocks(slea.readInt());
            } else if (addrem == 1) {
                if (!FieldLimitType.VipRock.check(chr.getMap().getFieldLimit())) {
                    chr.addRockMap();
                } else {
                    chr.dropMessage(1, "你可能不能添加此地图.");
                }
            }
        } else if (addrem == 0) {
            chr.deleteFromRegRocks(slea.readInt());
        } else if (addrem == 1) {
            if (!FieldLimitType.VipRock.check(chr.getMap().getFieldLimit())) {
                chr.addRegRockMap();
            } else {
                chr.dropMessage(1, "你可能不能添加此地图.");
            }
        }
        c.getSession().write(MTSCSPacket.getTrockRefresh(chr, vip == 1, addrem == 3));
    }

    public static final void CharInfoRequest(final int objectid, final MapleClient c, final MapleCharacter chr) {
        if (c.getPlayer() == null || c.getPlayer().getMap() == null) {
            return;
        }
        final MapleCharacter player = c.getPlayer().getMap().getCharacterById(objectid);
        c.getSession().write(MaplePacketCreator.enableActions());
        if (player != null && !player.isClone()) {
            if (!player.isGM() || c.getPlayer().isGM()) {
                c.getSession().write(MaplePacketCreator.charInfo(player, c.getPlayer().getId() == objectid));
            }
        }
    }

    public static final void TakeDamage(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        //System.out.println(slea.toString());
        chr.updateTick(slea.readInt());
        final byte type = slea.readByte(); //-4 is mist, -3 and -2 are map damage.
        slea.skip(1); // Element - 0x00 = elementless, 0x01 = ice, 0x02 = fire, 0x03 = lightning
        int damage = slea.readInt();

        int oid = 0;
        int monsteridfrom = 0;
        int reflect = 0;
        byte direction = 0;
        int pos_x = 0;
        int pos_y = 0;
        int fake = 0;
        int mpattack = 0;
        boolean is_pg = false;
        boolean isDeadlyAttack = false;
        MapleMonster attacker = null;
        if (chr == null || chr.isHidden() || chr.getMap() == null) {
            return;
        }

        if (chr.isGM() && chr.isInvincible()) {
            return;
        }
        final PlayerStats stats = chr.getStat();
        if (type != -2 && type != -3 && type != -4) { // Not map damage
            monsteridfrom = slea.readInt();
            oid = slea.readInt();
            attacker = chr.getMap().getMonsterByOid(oid);
            direction = slea.readByte();

            if (attacker == null) {
                return;
            }
            if (type != -1) { // Bump damage
                final MobAttackInfo attackInfo = MobAttackInfoFactory.getInstance().getMobAttackInfo(attacker, type);
                if (attackInfo != null) {
                    if (attackInfo.isDeadlyAttack()) {
                        isDeadlyAttack = true;
                        mpattack = stats.getMp() - 1;
                    } else {
                        mpattack += attackInfo.getMpBurn();
                    }
                    final MobSkill skill = MobSkillFactory.getMobSkill(attackInfo.getDiseaseSkill(), attackInfo.getDiseaseLevel());
                    if (skill != null && (damage == -1 || damage > 0)) {
                        skill.applyEffect(chr, attacker, false);
                    }
                    attacker.setMp(attacker.getMp() - attackInfo.getMpCon());
                }
            }
        }

        if (damage == -1) {
            fake = 4020002 + ((chr.getJob() / 10 - 40) * 100000);
        } else if (damage < -1 || damage > 60000) {
            AutobanManager.getInstance().addPoints(c, 1000, 60000, "Taking abnormal amounts of damge from " + monsteridfrom + ": " + damage);
            return;
        }
        chr.getCheatTracker().checkTakeDamage(damage);

        if (damage > 0) {
            chr.getCheatTracker().setAttacksWithoutHit(false);

            if (chr.getBuffedValue(MapleBuffStat.MORPH) != null) {
                chr.cancelMorphs();
            }
            if (slea.available() == 3) {
                byte level = slea.readByte();
                if (level > 0) {
                    final MobSkill skill = MobSkillFactory.getMobSkill(slea.readShort(), level);
                    if (skill != null) {
                        skill.applyEffect(chr, attacker, false);
                    }
                }
            }
            if (type != -2 && type != -3 && type != -4) {
                final int bouncedam_ = (Randomizer.nextInt(100) < chr.getStat().DAMreflect_rate ? chr.getStat().DAMreflect : 0) + (type == -1 && chr.getBuffedValue(MapleBuffStat.伤害反击) != null ? chr.getBuffedValue(MapleBuffStat.伤害反击) : 0) + (type == -1 && chr.getBuffedValue(MapleBuffStat.PERFECT_ARMOR) != null ? chr.getBuffedValue(MapleBuffStat.PERFECT_ARMOR) : 0);
                // final boolean bouncedam_A = chr.getBuffedValue(MapleBuffStat.BODY_PRESSURE) != null;
                if (bouncedam_ > 0 && attacker != null) {
                    long bouncedamage = (long) (damage * bouncedam_ / 100);
                    bouncedamage = Math.min(bouncedamage, attacker.getMobMaxHp() / 10);
                    attacker.damage(chr, bouncedamage, true);
                    damage -= bouncedamage;
                    chr.getMap().broadcastMessage(chr, MobPacket.damageMonster(oid, bouncedamage), chr.getPosition());
                    is_pg = true;
                }
            }
            if (type != -1 && type != -2 && type != -3 && type != -4) {
                switch (chr.getJob()) {
                    case 112: {
                        final ISkill skill = SkillFactory.getSkill(1120004);
                        if (chr.getSkillLevel(skill) > 0) {
                            damage = (int) ((skill.getEffect(chr.getSkillLevel(skill)).getX() / 1000.0) * damage);
                        }
                        break;
                    }
                    case 122: {
                        final ISkill skill = SkillFactory.getSkill(1220005);
                        if (chr.getSkillLevel(skill) > 0) {
                            damage = (int) ((skill.getEffect(chr.getSkillLevel(skill)).getX() / 1000.0) * damage);
                        }
                        break;
                    }
                    case 132: {
                        final ISkill skill = SkillFactory.getSkill(1320005);
                        if (chr.getSkillLevel(skill) > 0) {
                            damage = (int) ((skill.getEffect(chr.getSkillLevel(skill)).getX() / 1000.0) * damage);
                        }
                        break;
                    }
                }
            }
            if (damage == -1) {
                Integer pguard = chr.getBuffedValue(MapleBuffStat.伤害反击);//伤害反击
                if (pguard != null) {
                    attacker = (MapleMonster) chr.getMap().getMapObject(oid, null);
                    if (attacker != null) {//攻击者
                        int 反击伤害 = (int) (damage * (pguard.doubleValue() / 100.0D));
                        反击伤害 = (int) Math.min(反击伤害, attacker.getMobMaxHp() / 10);
                        attacker.damage(chr, damage, true);
                        damage -= 反击伤害;
                        chr.getMap().broadcastMessage(chr, MobPacket.damageMonster(oid, damage), chr.getPosition());
                        chr.checkMonsterAggro(attacker);
                        c.getPlayer().setHp(c.getPlayer().getHp() - damage);
                    }
                }
            }
            Integer 战神抗压 = chr.getBuffedValue(MapleBuffStat.伤害反击);//伤害反击
            if ((战神抗压 != null) && (damage > 0)) { //抗压的wz为 x = 5<怪物变弱几率>  damage为伤害正常值
                attacker = (MapleMonster) chr.getMap().getMapObject(oid, MapleMapObjectType.MONSTER);
                if (attacker != null) { //攻击者
                    ISkill 抗压 = SkillFactory.getSkill(21101003); //抗压
                    int 抗压伤害 = (int) ((抗压.getEffect(chr.getSkillLevel(21101003)).getDamage() / 100.0) * damage);
                    attacker.damage(chr, 抗压伤害, true);
                    damage -= 抗压伤害;
                    chr.getMap().broadcastMessage(chr, MobPacket.damageMonster(oid, 抗压伤害), chr.getPosition());
                    chr.checkMonsterAggro(attacker);
                    c.getPlayer().setHp(c.getPlayer().getHp() - damage);
                }
            }
            final MapleStatEffect magicShield = chr.getStatForBuff(MapleBuffStat.MAGIC_SHIELD);
            if (magicShield != null) {
                damage -= (int) ((magicShield.getX() / 100.0) * damage);
            }
            final MapleStatEffect blueAura = chr.getStatForBuff(MapleBuffStat.BLUE_AURA);
            if (blueAura != null) {
                damage -= (int) ((blueAura.getY() / 100.0) * damage);
            }
            if (chr.getBuffedValue(MapleBuffStat.SATELLITESAFE_PROC) != null && chr.getBuffedValue(MapleBuffStat.SATELLITESAFE_ABSORB) != null) {
                double buff = chr.getBuffedValue(MapleBuffStat.SATELLITESAFE_PROC).doubleValue();
                double buffz = chr.getBuffedValue(MapleBuffStat.SATELLITESAFE_ABSORB).doubleValue();
                if ((int) ((buff / 100.0) * chr.getStat().getMaxHp()) <= damage) {
                    damage -= (int) ((buffz / 100.0) * damage);
                    chr.cancelEffectFromBuffStat(MapleBuffStat.SUMMON);
                    chr.cancelEffectFromBuffStat(MapleBuffStat.REAPER);
                }
            }
            if (chr.getBuffedValue(MapleBuffStat.MAGIC_GUARD) != null) {
                int hploss = 0, mploss = 0;
                if (isDeadlyAttack) {
                    if (stats.getHp() > 1) {
                        hploss = stats.getHp() - 1;
                    }
                    if (stats.getMp() > 1) {
                        mploss = stats.getMp() - 1;
                    }
                    if (chr.getBuffedValue(MapleBuffStat.INFINITY) != null) {
                        mploss = 0;
                    }
                    chr.addMPHP(-hploss, -mploss);
                    //} else if (mpattack > 0) {
                    //    chr.addMPHP(-damage, -mpattack);
                } else {
                    mploss = (int) (damage * (chr.getBuffedValue(MapleBuffStat.MAGIC_GUARD).doubleValue() / 100.0)) + mpattack;
                    hploss = damage - mploss;
                    if (chr.getBuffedValue(MapleBuffStat.INFINITY) != null) {
                        mploss = 0;
                    } else if (mploss > stats.getMp()) {
                        mploss = stats.getMp();
                        hploss = damage - mploss + mpattack;
                    }
                    chr.addMPHP(-hploss, -mploss);
                }

            } else if (chr.getBuffedValue(MapleBuffStat.MESOGUARD) != null) {
                damage = (damage % 2 == 0) ? damage / 2 : (damage / 2 + 1);

                final int mesoloss = (int) (damage * (chr.getBuffedValue(MapleBuffStat.MESOGUARD).doubleValue() / 100.0));
                if (chr.getMeso() < mesoloss) {
                    chr.gainMeso(-chr.getMeso(), false);
                    chr.cancelBuffStats(MapleBuffStat.MESOGUARD);
                } else {
                    chr.gainMeso(-mesoloss, false);
                }
                if (isDeadlyAttack && stats.getMp() > 1) {
                    mpattack = stats.getMp() - 1;
                }
                chr.addMPHP(-damage, -mpattack);
            } else if (isDeadlyAttack) {
                chr.addMPHP(stats.getHp() > 1 ? -(stats.getHp() - 1) : 0, stats.getMp() > 1 ? -(stats.getMp() - 1) : 0);
            } else {
                chr.addMPHP(-damage, -mpattack);
            }
            chr.handleBattleshipHP(-damage);
        }
        if (!chr.isHidden()) {
            chr.getMap().broadcastMessage(chr, MaplePacketCreator.damagePlayer(type, monsteridfrom, chr.getId(), damage, fake, direction, reflect, is_pg, oid, pos_x, pos_y), false);
        }
    }

    public static final void AranCombo(final MapleClient c, final MapleCharacter chr) {
        if (chr != null && chr.getJob() >= 2000 && chr.getJob() <= 2112) {
            short combo = chr.getCombo();
            final long curr = System.currentTimeMillis();

            if (combo > 0 && (curr - chr.getLastCombo()) > 7000) {
                // Official MS timing is 3.5 seconds, so 7 seconds should be safe.
                //chr.getCheatTracker().registerOffense(CheatingOffense.ARAN_COMBO_HACK);
                combo = 0;
                final ISkill skill = SkillFactory.getSkill(21000000);
                final ISkill skillA = SkillFactory.getSkill(21110000);
                if (combo <= 1 && skill != null) {
                    //   chr.cancelEffect(skill.getEffect(1), false, -1);
                    SkillFactory.getSkill(21000000).getEffect((short) 0).applyComboBuff(chr, (short) 0);

                }
                if (combo <= 1 && skillA != null) {
                    //  chr.cancelEffect(skillA.getEffect(1), false, -1);
                    SkillFactory.getSkill(21110000).getEffect((short) 0).applyComboBuffA(chr, (short) 0);
                }
            }
            if (combo < 30000) {
                combo++;
            }
            chr.setLastCombo(curr);
            chr.setCombo(combo);

            switch (combo) { // Hackish method xD
                case 10:
                case 20:
                case 30:
                case 40:
                case 50:
                case 60:
                case 70:
                case 80:
                case 90:
                case 100:

                    if (chr.getSkillLevel(21000000) >= (combo / 10)) {
                        SkillFactory.getSkill(21000000).getEffect(combo / 10).applyComboBuff(chr, combo);
                    }
                    if (chr.getSkillLevel(21110000) >= (combo / 10)) {
                        SkillFactory.getSkill(21110000).getEffect(combo / 10).applyComboBuffA(chr, combo);
                    }
                    break;
            }
            c.getSession().write(MaplePacketCreator.testCombo(combo));
            chr.setLastCombo(curr);
        }
    }

    public static final void UseItemEffect(SeekableLittleEndianAccessor slea, MapleClient c, MapleCharacter chr) {
        System.out.println("啊啊啊啊AA");
        int itemId = slea.readInt();
        if (itemId == 4270000) {
            System.out.println("ss");
        }
        IItem toUse;
        if ((itemId == 4290001) || (itemId == 4290000)) //倒霉鸡  金鸡  效果
        {
            toUse = c.getPlayer().getInventory(MapleInventoryType.ETC).findById(itemId);
        } else {
            toUse = c.getPlayer().getInventory(MapleInventoryType.CASH).findById(itemId);
        }
        if ((itemId != 0) && (toUse == null)) {
            return;
        }
        c.getPlayer().setItemEffect(itemId);
        c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.itemEffect(c.getPlayer().getId(), itemId), false);

        /* final IItem toUse = chr.getInventory(MapleInventoryType.CASH).findById(itemId);
        if (toUse == null || toUse.getItemId() != itemId || toUse.getQuantity() < 1) {
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        if (itemId != 5510000) {
            chr.setItemEffect(itemId);
        }
        byte flag = toUse.getFlag();
        chr.getMap().broadcastMessage(chr, MaplePacketCreator.itemEffects(chr.getId(), itemId), false);
        if (ItemFlag.KARMA_EQ.check(flag)) {
            toUse.setFlag((byte) (flag - ItemFlag.KARMA_EQ.getValue()));
            c.getSession().write(MaplePacketCreator.getCharInfo(chr));
            chr.getMap().removePlayer(chr);
            chr.getMap().addPlayer(chr);
             c.getSession().write(MaplePacketCreator.updateSpecialItemUse_(toUse, GameConstants.getInventoryType(toUse.getItemId()).getType()));
        } else if (ItemFlag.KARMA_USE.check(flag)) {
            toUse.setFlag((byte) (flag - ItemFlag.KARMA_USE.getValue()));
            c.getSession().write(MaplePacketCreator.getCharInfo(chr));
            chr.getMap().removePlayer(chr);
            chr.getMap().addPlayer(chr);
             c.getSession().write(MaplePacketCreator.updateSpecialItemUse_(toUse, GameConstants.getInventoryType(toUse.getItemId()).getType()));
        }*/
    }

    public static final void CancelItemEffect(final int id, final MapleCharacter chr) {
        chr.cancelEffect(MapleItemInformationProvider.getInstance().getItemEffect(-id), false, -1);
    }

    public static final void CancelBuffHandler(final int sourceid, final MapleCharacter chr) {//取消BUFF处理
        if (chr == null) {
            return;
        }
        final ISkill skill = SkillFactory.getSkill1(sourceid);
        if (skill.isChargeSkill()) {
            chr.setKeyDownSkill_Time(0);
            chr.getMap().broadcastMessage(chr, MaplePacketCreator.skillCancel(chr, sourceid), false);
        } else {
            chr.cancelEffect(skill.getEffect(1), false, -1);
        }
    }

    public static final void SkillEffect(final SeekableLittleEndianAccessor slea, final MapleCharacter chr) {
        final int skillId = slea.readInt();
        final byte level = slea.readByte();
        final byte flags = slea.readByte();
        final byte speed = slea.readByte();
        final byte unk = slea.readByte(); // Added on v.82

        final ISkill skill = SkillFactory.getSkill(skillId);
        if (chr == null) {
            return;
        }
        final int skilllevel_serv = chr.getSkillLevel(skill);

        if (skilllevel_serv > 0 && skilllevel_serv == level && skill.isChargeSkill()) {
            chr.setKeyDownSkill_Time(System.currentTimeMillis());
            chr.getMap().broadcastMessage(chr, MaplePacketCreator.skillEffect(chr, skillId, level, flags, speed, unk), false);
        }
    }

    public static final void SpecialMove(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null || !chr.isAlive() || chr.getMap() == null) {
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        slea.skip(4); // Old X and Y
        final int skillid = slea.readInt();
        final int skillLevel = slea.readByte();
        final ISkill skill = SkillFactory.getSkill(skillid);

        if (chr.getSkillLevel(skill) <= 0 || chr.getSkillLevel(skill) != skillLevel) {
            if (!GameConstants.isMulungSkill(skillid) && !GameConstants.isPyramidSkill(skillid)) {
                //  c.getSession().close();
                return;
            }
            if (GameConstants.isMulungSkill(skillid)) {
                if (chr.getMapId() / 10000 != 92502) {
                    //AutobanManager.getInstance().autoban(c, "Using Mu Lung dojo skill out of dojo maps.");
                    return;
                } else {
                    chr.mulung_EnergyModify(false);
                }
            } else if (GameConstants.isPyramidSkill(skillid)) {
                if (chr.getMapId() / 10000 != 92602) {
                    //AutobanManager.getInstance().autoban(c, "Using Pyramid skill out of pyramid maps.");
                    return;
                }
            }
        }
        final MapleStatEffect effect = skill.getEffect(chr.getSkillLevel(GameConstants.getLinkedAranSkill(skillid)));

        if (effect.getCooldown() > 0 && !chr.isGM()) {
            if (chr.skillisCooling(skillid)) {
                c.getSession().write(MaplePacketCreator.enableActions());
                return;
            }
            if (skillid != 5221006) { // Battleship
                c.getSession().write(MaplePacketCreator.skillCooldown(skillid, effect.getCooldown()));
                chr.addCooldown(skillid, System.currentTimeMillis(), effect.getCooldown() * 1000);
            }
        }
        //chr.checkFollow(); //not msea-like but ALEX'S WISHES
        switch (skillid) {
            case 1121001:
            case 1221001:
            case 1321001:
            case 9001020: // GM magnet
                final byte number_of_mobs = slea.readByte();
                slea.skip(3);
                for (int i = 0; i < number_of_mobs; i++) {
                    int mobId = slea.readInt();

                    final MapleMonster mob = chr.getMap().getMonsterByOid(mobId);
                    if (mob != null) {
//			chr.getMap().broadcastMessage(chr, MaplePacketCreator.showMagnet(mobId, slea.readByte()), chr.getPosition());
                        mob.switchController(chr, mob.isControllerHasAggro());
                    }
                }
                chr.getMap().broadcastMessage(chr, MaplePacketCreator.showBuffeffect(chr.getId(), skillid, 1, slea.readByte()), chr.getPosition());
                c.getSession().write(MaplePacketCreator.enableActions());
                break;
            default:
                Point pos = null;
                if (slea.available() == 7 || skill.getId() == 3111002 || skill.getId() == 3211002) {
                    pos = slea.readPos();
                }

                if (effect.isMagicDoor()) { // Mystic Door
                    if (!FieldLimitType.MysticDoor.check(chr.getMap().getFieldLimit())) {
                        effect.applyTo(c.getPlayer(), pos);
                    } else {
                        c.getSession().write(MaplePacketCreator.enableActions());
                    }

                } else {
                    final int mountid = MapleStatEffect.parseMountInfo(c.getPlayer(), skill.getId());
                    if (mountid != 0 && mountid != GameConstants.getMountItem(skill.getId()) && !c.getPlayer().isGM() && c.getPlayer().getBuffedValue(MapleBuffStat.骑兽技能) == null && c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -118) == null) {
                        //if (!GameConstants.isMountItemAvailable(mountid, c.getPlayer().getJob())) {
                        //    c.getSession().write(MaplePacketCreator.enableActions());
                        //    return;
                        // }
                    }
                    effect.applyTo(c.getPlayer(), pos);
                }
                break;
        }
    }

    public static final void closeRangeAttack(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr, final boolean energy) {
        if (chr == null || (energy && chr.getBuffedValue(MapleBuffStat.ENERGY_CHARGE) == null && chr.getBuffedValue(MapleBuffStat.伤害反击) == null && !GameConstants.isKOC(chr.getJob()))) {
            return;
        }
        if (!chr.isAlive() || chr.getMap() == null) {
            chr.getCheatTracker().registerOffense(CheatingOffense.人物死亡攻击);
            return;
        }
        final AttackInfo attack = DamageParse.Modify_AttackCrit(DamageParse.parseDmgM(slea, chr), chr, 1);
        final boolean mirror = chr.getBuffedValue(MapleBuffStat.MIRROR_IMAGE) != null;
        Integer 夜行者隐身 = chr.getBuffedValue(MapleBuffStat.DARKSIGHT);
        double maxdamage = chr.getStat().getCurrentMaxBaseDamage();
        int attackCount = (chr.getJob() >= 430 && chr.getJob() <= 434 ? 2 : 1), skillLevel = 0;
        MapleStatEffect effect = null;
        ISkill skill = null;
        if ((attack.skill == 21100004) || (attack.skill == 21100005) || (attack.skill == 21110003) || (attack.skill == 21110004) || (attack.skill == 21120006) || (attack.skill == 21120007)) {
            chr.setCombo((byte) 1);
        }
        if (夜行者隐身 != null) {
            if (attack.skill != 14100005) {
                chr.dropMessage("夜行者隐身状态除非使用驱逐技能。其他技能均无效！");
                return;
            }
        }
        if (attack.skill != 0) {
            skill = SkillFactory.getSkill(GameConstants.getLinkedAranSkill(attack.skill));
            skillLevel = chr.getSkillLevel(skill);
            effect = attack.getAttackEffect(chr, skillLevel, skill);
            if (effect == null) {
                return;
            }
            maxdamage *= effect.getDamage() / 100.0;
            attackCount = effect.getAttackCount();

            if (effect.getCooldown() > 0 && !chr.isGM()) {
                if (chr.skillisCooling(attack.skill)) {
                    c.getSession().write(MaplePacketCreator.enableActions());
                    return;
                }
                c.getSession().write(MaplePacketCreator.skillCooldown(attack.skill, effect.getCooldown()));
                chr.addCooldown(attack.skill, System.currentTimeMillis(), effect.getCooldown() * 1000);
            }
        }
        attackCount *= (mirror ? 2 : 1);
        if (!energy) {
            if ((chr.getMapId() == 109060000 || chr.getMapId() == 109060002 || chr.getMapId() == 109060004) && attack.skill == 0) {
                MapleSnowballs.hitSnowball(chr);
            }
            // handle combo orbconsume
            int numFinisherOrbs = 0;
            final Integer comboBuff = chr.getBuffedValue(MapleBuffStat.COMBO);

            if (isFinisher(attack.skill)) { // finisher
                if (comboBuff != null) {
                    numFinisherOrbs = comboBuff.intValue() - 1;
                }
                chr.handleOrbconsume();

            } else if (attack.targets > 0 && comboBuff != null) {
                // handle combo orbgain
                switch (chr.getJob()) {
                    case 111:
                    case 112:
                    case 1110:
                    case 1111:
                        if (attack.skill != 1111008) { // shout should not give orbs
                            chr.handleOrbgain();
                        }
                        break;
                }
            }
            switch (chr.getJob()) {
                case 511:
                case 512: {
                    //   chr.handleEnergyCharge(5110001, 100);
                    chr.handleEnergyCharge(5110001, attack.targets * attack.hits);
                    //System.out.println("获取能量A："+ attack.targets * attack.hits);
                    //chr.handleEnergyChargeGain();
                    break;
                }
                case 1510:
                case 1511:
                case 1512: {
                    //  chr.handleEnergyChargeGain();
                    //    chr.handleEnergyCharge(15100004, 100);
                    chr.handleEnergyCharge(15100004, attack.targets * attack.hits);
                    break;
                }
            }
            // handle sacrifice hp loss
            //after BIG BANG, TEMP
            if (attack.targets > 0 && attack.skill == 1211002) { // handle charged blow
                final int advcharge_level = chr.getSkillLevel(SkillFactory.getSkill(1220010));
                if (advcharge_level > 0) {
                    if (!SkillFactory.getSkill(1220010).getEffect(advcharge_level).makeChanceResult()) {
                        chr.cancelEffectFromBuffStat(MapleBuffStat.属性攻击);
                        chr.cancelEffectFromBuffStat(MapleBuffStat.LIGHTNING_CHARGE);
                    }
                } else {
                    chr.cancelEffectFromBuffStat(MapleBuffStat.属性攻击);
                    chr.cancelEffectFromBuffStat(MapleBuffStat.LIGHTNING_CHARGE);
                }
            }

            if (numFinisherOrbs > 0) {
                maxdamage *= numFinisherOrbs;
            } else if (comboBuff != null) {
                ISkill combo;
                if (c.getPlayer().getJob() == 1110 || c.getPlayer().getJob() == 1111) {
                    combo = SkillFactory.getSkill(11111001);
                } else {
                    combo = SkillFactory.getSkill(1111002);
                }
                if (c.getPlayer().getSkillLevel(combo) > 0) {
                    maxdamage *= 1.0 + (combo.getEffect(c.getPlayer().getSkillLevel(combo)).getDamage() / 100.0 - 1.0) * (comboBuff.intValue() - 1);
                }
            }

            if (isFinisher(attack.skill)) {
                if (numFinisherOrbs == 0) {
                    return;
                }
                maxdamage = 199999; // FIXME reenable damage calculation for finishers
            }
        }
        chr.checkFollow();
        chr.getMap().broadcastMessage(chr, MaplePacketCreator.closeRangeAttack(chr.getId(), attack.tbyte, attack.skill, skillLevel, attack.display, attack.animation, attack.speed, attack.allDamage, energy, chr.getLevel(), chr.getStat().passive_mastery(), attack.unk, attack.charge), chr.getPosition());
        DamageParse.applyAttack(attack, skill, c.getPlayer(), attackCount, maxdamage, effect, mirror ? AttackType.NON_RANGED_WITH_MIRROR : AttackType.NON_RANGED, null);
        WeakReference<MapleCharacter>[] clones = chr.getClones();
        for (int i = 0; i < clones.length; i++) {
            if (clones[i].get() != null) {
                final MapleCharacter clone = clones[i].get();
                final ISkill skil2 = skill;
                final int skillLevel2 = skillLevel;
                final int attackCount2 = attackCount;
                final double maxdamage2 = maxdamage;
                final MapleStatEffect eff2 = effect;
                final AttackInfo attack2 = DamageParse.DivideAttack(attack, chr.isGM() ? 1 : 4);
                CloneTimer.getInstance().schedule(new Runnable() {

                    public void run() {
                        clone.getMap().broadcastMessage(MaplePacketCreator.closeRangeAttack(clone.getId(), attack2.tbyte, attack2.skill, skillLevel2, attack2.display, attack2.animation, attack2.speed, attack2.allDamage, energy, clone.getLevel(), clone.getStat().passive_mastery(), attack2.unk, attack2.charge));
                        DamageParse.applyAttack(attack2, skil2, chr, attackCount2, maxdamage2, eff2, mirror ? AttackType.NON_RANGED_WITH_MIRROR : AttackType.NON_RANGED, null);
                    }
                }, 500 * i + 500);
            }
        }
    }

    public static final void rangedAttack(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null) {
            return;
        }
        if (!chr.isAlive() || chr.getMap() == null) {
            chr.getCheatTracker().registerOffense(CheatingOffense.人物死亡攻击);
            return;
        }
        final AttackInfo attack = DamageParse.Modify_AttackCrit(DamageParse.parseDmgR(slea, chr), chr, 2);
        Integer 夜行者隐身 = chr.getBuffedValue(MapleBuffStat.DARKSIGHT);
        int bulletCount = 1, skillLevel = 0;
        MapleStatEffect effect = null;
        ISkill skill = null;
        if (夜行者隐身 != null) {
            if (attack.skill != 14100005) {
                chr.dropMessage("夜行者隐身状态除非使用驱逐技能。其他技能均无效！");
                return;
            }
        }
        //无效
        if (chr.getSkillLevel(SkillFactory.getSkill(14100005)) > 0 && chr.getBuffedValue(MapleBuffStat.DARKSIGHT) != null) {
            chr.cancelEffectFromBuffStat(MapleBuffStat.DARKSIGHT);//有效
            chr.cancelBuffStats(MapleBuffStat.DARKSIGHT);//有效
        }
        if (attack.skill != 0) {
            skill = SkillFactory.getSkill(GameConstants.getLinkedAranSkill(attack.skill));
            skillLevel = chr.getSkillLevel(skill);
            effect = attack.getAttackEffect(chr, skillLevel, skill);
            if (effect == null) {
                return;
            }

            switch (attack.skill) {
                case 13111007:
                case 21120006:
                case 21110004: // Ranged but uses attackcount instead
                case 14101006: // Vampure
                    bulletCount = effect.getAttackCount();
                    break;
                default:
                    bulletCount = effect.getBulletCount();
                    break;
            }
            if (effect.getCooldown() > 0 && !chr.isGM()) {
                if (chr.skillisCooling(attack.skill)) {
                    c.getSession().write(MaplePacketCreator.enableActions());
                    return;
                }
                c.getSession().write(MaplePacketCreator.skillCooldown(attack.skill, effect.getCooldown()));
                chr.addCooldown(attack.skill, System.currentTimeMillis(), effect.getCooldown() * 1000);
            }
        }
        final Integer ShadowPartner = chr.getBuffedValue(MapleBuffStat.SHADOWPARTNER);
        if (ShadowPartner != null) {
            bulletCount *= 2;
        }
        int projectile = 0, visProjectile = 0;
        if (attack.AOE != 0 && chr.getBuffedValue(MapleBuffStat.SOULARROW) == null && attack.skill != 4111004) {
            if (chr.getInventory(MapleInventoryType.USE).getItem(attack.slot) == null) {
                return;
            }
            projectile = chr.getInventory(MapleInventoryType.USE).getItem(attack.slot).getItemId();

            if (attack.csstar > 0) {
                if (chr.getInventory(MapleInventoryType.CASH).getItem(attack.csstar) == null) {
                    return;
                }
                visProjectile = chr.getInventory(MapleInventoryType.CASH).getItem(attack.csstar).getItemId();
            } else {
                visProjectile = projectile;
            }
            // Handle bulletcount
            if (chr.getBuffedValue(MapleBuffStat.SPIRIT_CLAW) == null) {
                int bulletConsume = bulletCount;
                if (effect != null && effect.getBulletConsume() != 0) {
                    bulletConsume = effect.getBulletConsume() * (ShadowPartner != null ? 2 : 1);
                }
                if (!MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, projectile, bulletConsume, false, true)) {
                    chr.dropMessage(5, "You do not have enough arrows/bullets/stars.");
                    return;
                }
            }
        }
        double basedamage;
        int projectileWatk = 0;
        if (projectile != 0) {
            projectileWatk = MapleItemInformationProvider.getInstance().getWatkForProjectile(projectile);
        }
        final PlayerStats statst = chr.getStat();
        switch (attack.skill) {
            case 4001344: // Lucky Seven
            case 4121007: // Triple Throw
            case 14001004: // Lucky seven
            case 14111005: // Triple Throw
                basedamage = (float) ((float) ((statst.getTotalLuk() * 5.0f) * (statst.getTotalWatk() + projectileWatk)) / 100);
                break;
            case 4111004: // Shadow Meso
//		basedamage = ((effect.getMoneyCon() * 10) / 100) * effect.getProb(); // Not sure
                basedamage = 13000;
                break;
            default:
                if (projectileWatk != 0) {
                    basedamage = statst.calculateMaxBaseDamage(statst.getTotalWatk() + projectileWatk);
                } else {
                    basedamage = statst.getCurrentMaxBaseDamage();
                }
                switch (attack.skill) {
                    case 3101005: // arrowbomb is hardcore like that
                        basedamage *= effect.getX() / 100.0;
                        break;
                }
                break;
        }
        if (effect != null) {
            basedamage *= effect.getDamage() / 100.0;

            int money = effect.getMoneyCon();
            if (money != 0) {
                if (money > chr.getMeso()) {
                    money = chr.getMeso();
                }
                chr.gainMeso(-money, false);
            }
        }

        chr.checkFollow();
        chr.getMap().broadcastMessage(chr, MaplePacketCreator.rangedAttack(chr.getId(), attack.tbyte, attack.skill, skillLevel, attack.display, attack.animation, attack.speed, visProjectile, attack.allDamage, attack.position, chr.getLevel(), chr.getStat().passive_mastery(), attack.unk), chr.getPosition());
        DamageParse.applyAttack(attack, skill, chr, bulletCount, basedamage, effect, ShadowPartner != null ? AttackType.RANGED_WITH_SHADOWPARTNER : AttackType.RANGED, null);

        WeakReference<MapleCharacter>[] clones = chr.getClones();
        for (int i = 0; i < clones.length; i++) {
            if (clones[i].get() != null) {
                final MapleCharacter clone = clones[i].get();
                final ISkill skil2 = skill;
                final MapleStatEffect eff2 = effect;
                final double basedamage2 = basedamage;
                final int bulletCount2 = bulletCount;
                final int visProjectile2 = visProjectile;
                final int skillLevel2 = skillLevel;
                final AttackInfo attack2 = DamageParse.DivideAttack(attack, chr.isGM() ? 1 : 4);
                CloneTimer.getInstance().schedule(new Runnable() {

                    public void run() {
                        clone.getMap().broadcastMessage(MaplePacketCreator.rangedAttack(clone.getId(), attack2.tbyte, attack2.skill, skillLevel2, attack2.display, attack2.animation, attack2.speed, visProjectile2, attack2.allDamage, attack2.position, clone.getLevel(), clone.getStat().passive_mastery(), attack2.unk));
                        DamageParse.applyAttack(attack2, skil2, chr, bulletCount2, basedamage2, eff2, AttackType.RANGED, null);
                    }
                }, 500 * i + 500);
            }
        }
    }

    public static final void MagicDamage(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null) {
            return;
        }
        if (!chr.isAlive() || chr.getMap() == null) {
            chr.getCheatTracker().registerOffense(CheatingOffense.人物死亡攻击);
            return;
        }
        Integer 夜行者隐身 = chr.getBuffedValue(MapleBuffStat.DARKSIGHT);
        final AttackInfo attack = DamageParse.Modify_AttackCrit(DamageParse.parseDmgMa(slea, chr), chr, 3);
        final ISkill skill = SkillFactory.getSkill(GameConstants.getLinkedAranSkill(attack.skill));
        final int skillLevel = chr.getSkillLevel(skill);
        final MapleStatEffect effect = attack.getAttackEffect(chr, skillLevel, skill);
        if (effect == null) {
            return;
        }
        if (夜行者隐身 != null) {
            if (attack.skill != 14100005) {
                chr.dropMessage("夜行者隐身状态除非使用驱逐技能。其他技能均无效！");
                return;
            }
        }
        if (effect.getCooldown() > 0 && !chr.isGM()) {
            if (chr.skillisCooling(attack.skill)) {
                c.getSession().write(MaplePacketCreator.enableActions());
                return;
            }
            c.getSession().write(MaplePacketCreator.skillCooldown(attack.skill, effect.getCooldown()));
            chr.addCooldown(attack.skill, System.currentTimeMillis(), effect.getCooldown() * 1000);
        }
        chr.checkFollow();
        chr.getMap().broadcastMessage(chr, MaplePacketCreator.magicAttack(chr.getId(), attack.tbyte, attack.skill, skillLevel, attack.display, attack.animation, attack.speed, attack.allDamage, attack.charge, chr.getLevel(), attack.unk), chr.getPosition());
        DamageParse.applyAttackMagic(attack, skill, c.getPlayer(), effect, null);
        WeakReference<MapleCharacter>[] clones = chr.getClones();
        for (int i = 0; i < clones.length; i++) {
            if (clones[i].get() != null) {
                final MapleCharacter clone = clones[i].get();
                final ISkill skil2 = skill;
                final MapleStatEffect eff2 = effect;
                final int skillLevel2 = skillLevel;
                final AttackInfo attack2 = DamageParse.DivideAttack(attack, chr.isGM() ? 1 : 4);
                CloneTimer.getInstance().schedule(new Runnable() {

                    public void run() {
                        //if (attack.skill != 22121000 && attack.skill != 22151001) {
                        clone.getMap().broadcastMessage(MaplePacketCreator.magicAttack(clone.getId(), attack2.tbyte, attack2.skill, skillLevel2, attack2.display, attack2.animation, attack2.speed, attack2.allDamage, attack2.charge, clone.getLevel(), attack2.unk));
                        //}
                        DamageParse.applyAttackMagic(attack2, skil2, chr, eff2, null);
                    }
                }, 500 * i + 500);
            }
        }
    }

    public static final void DropMeso(final int meso, final MapleCharacter chr) {
        if (!chr.isAlive() || (meso < 10 || meso > 50000) || (meso > chr.getMeso())) {
            chr.getClient().getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        chr.gainMeso(-meso, false, true);
        chr.getMap().spawnMesoDrop(meso, chr.getPosition(), chr, chr, true, (byte) 0);
        chr.getCheatTracker().checkDrop(true);
    }

    public static final void ChangeEmotion(final int emote, final MapleCharacter chr) {
        if (emote > 7) {
            final int emoteid = 5159992 + emote;
            final MapleInventoryType type = GameConstants.getInventoryType(emoteid);
            if (chr.getInventory(type).findById(emoteid) == null) {
                chr.getCheatTracker().registerOffense(CheatingOffense.使用不存在道具, Integer.toString(emoteid));
                return;
            }
        }
        if (emote > 0 && chr != null && chr.getMap() != null) { //O_o
            chr.getMap().broadcastMessage(chr, MaplePacketCreator.facialExpression(chr, emote), false);
            WeakReference<MapleCharacter>[] clones = chr.getClones();
            for (int i = 0; i < clones.length; i++) {
                if (clones[i].get() != null) {
                    final MapleCharacter clone = clones[i].get();
                    CloneTimer.getInstance().schedule(new Runnable() {

                        public void run() {
                            clone.getMap().broadcastMessage(MaplePacketCreator.facialExpression(clone, emote));
                        }
                    }, 500 * i + 500);
                }
            }
        }
    }

    public static final void Heal(final SeekableLittleEndianAccessor slea, final MapleCharacter chr) {
        if (chr == null) {
            return;
        }
        chr.updateTick(slea.readInt());
        /*
         * if (slea.available() >= 8) { slea.skip(4); }
         */

        int healHP = slea.readShort();
        int healMP = slea.readShort();
        final PlayerStats stats = chr.getStat();
        int check_hp = (int) stats.getHealHP();
        int check_mp = (int) stats.getHealMP();

        if (stats.getHp() <= 0) {
            return;
        }
        if (chr.canHP()) {
            if (healHP != 0) {
                if (chr.getChair() != 0) {
                    check_hp += 150;
                }
                if (healHP > check_hp * 2 && healHP > 20) {
                    chr.getCheatTracker().registerOffense(CheatingOffense.回复过多HP, String.valueOf(healHP) + " 服务器:" + check_hp);
                    //  healHP = check_hp;
                }
                chr.addHP(healHP);
            }
        }
        if (chr.canMP()) {
            if (healMP != 0) {
                if (healMP > check_mp * 2 && healMP > 20) {
                    chr.getCheatTracker().registerOffense(CheatingOffense.回复过多MP, String.valueOf(healMP) + "服务器:" + check_mp);
                    //  healMP = check_mp;
                }
                chr.addMP(healMP);
            }
        }

    }

    public static final void MovePlayer(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
//	slea.skip(5); // unknown
        if (chr == null) {
            return;
        }
        final Point Original_Pos = chr.getPosition(); // 4 bytes Added on v.80 MSEA
        slea.skip(33);

        /**
         *
         * FF FF 01 FF FF FF FF FF FF FF FF 2A 8E 66 CB 8E 7D 17 FC 4A BF D5 CE
         * B4 FE D7 00 0B 00 B4 FE D7 00 00 00 00 00 71 00 04 96 00 00 B7 FE D7
         * 00 54 00 00 00 71 00 02 3C 00 00 BB FE D7 00 0C 00 00 00 71 00 04 5A
         * 00 14 0C 00 00 00 04 00 00 00 BB FE D6 00 0C 00 00 00 71 00 04 00 00
         * 02 8A 02 00 00 06 00 00 00 CE FE D7 00 44 01 00 00 00 00 06 1E 00 00
         * EA FE D7 00 84 00 00 00 71 00 04 78 00 00 EA FE D6 00 84 00 00 00 71
         * 00 08 00 00 02 77 FF F2 FE 06 00 00 00 E1 FE C9 00 77 FF 6A FF 00 00
         * 06 3C 00 11 00 00 40 04 00 00 00 00 00 B4 FE C9 00 EA FE D7 00 Now:
         * D7 00 44 01 00 00 00 00 06 1E 00 00 EA FE D7 00 84 00 00 00 71 00 04
         * 78 00 00 EA FE D6 00 84 00 00 00 71 00 08 00 00 02 77 FF F2 FE 06 00
         * 00 00 E1 FE C9 00 77 FF 6A FF 00 00 06 3C 00 11 00 00 40 04 00 00 00
         * 00 00 B4 FE C9 00 EA FE D7 00
         */
        // log.trace("Movement command received: unk1 {} unk2 {}", new Object[] { unk1, unk2 });
        List<LifeMovementFragment> res;
        try {
            res = MovementParse.parseMovement(slea, 1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("AIOBE Type1:\n" + slea.toString(true));
            return;
        }

        if (res != null && c.getPlayer().getMap() != null) { // TODO more validation of input data
            if (slea.available() < 13 || slea.available() > 26) {
                System.out.println("slea.available != 13-26 (movement parsing error)\n" + slea.toString(true));
                return;
            }
            final List<LifeMovementFragment> res2 = new ArrayList<LifeMovementFragment>(res);
            final MapleMap map = c.getPlayer().getMap();

            if (chr.isHidden()) {
                chr.setLastRes(res2);
                c.getPlayer().getMap().broadcastGMMessage(chr, MaplePacketCreator.movePlayer(chr.getId(), res, Original_Pos), false);
            } else {
                c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.movePlayer(chr.getId(), res, Original_Pos), false);
            }

//	    if (chr.isHidden()) {
//		chr.setLastRes(res2);
//	    } else { //original POS? or end POS?
//		map.broadcastMessage(chr, MaplePacketCreator.movePlayer(chr.getId(), res, Original_Pos), false);
//	    }
            MovementParse.updatePosition(res, chr, 0);
            final Point pos = chr.getPosition();
            map.movePlayer(chr, pos);
            if (chr.getFollowId() > 0 && chr.isFollowOn() && chr.isFollowInitiator()) {
                final MapleCharacter fol = map.getCharacterById(chr.getFollowId());
                if (fol != null) {
                    final Point original_pos = fol.getPosition();
                    // fol.getClient().getSession().write(MaplePacketCreator.moveFollow(Original_Pos, original_pos, pos, res));
                    MovementParse.updatePosition(res, fol, 0);
                    // map.broadcastMessage(fol, MaplePacketCreator.movePlayer(fol.getId(), res, original_pos), false);
                } else {
                    chr.checkFollow();
                }
            }
            WeakReference<MapleCharacter>[] clones = chr.getClones();
            for (int i = 0; i < clones.length; i++) {
                if (clones[i].get() != null) {
                    final MapleCharacter clone = clones[i].get();
                    final List<LifeMovementFragment> res3 = new ArrayList<LifeMovementFragment>(res2);
                    CloneTimer.getInstance().schedule(new Runnable() {

                        public void run() {
                            try {
                                if (clone.getMap() == map) {
                                    if (clone.isHidden()) {
                                        clone.setLastRes(res3);
                                    } else {
                                        map.broadcastMessage(clone, MaplePacketCreator.movePlayer(clone.getId(), res3, Original_Pos), false);
                                    }
                                    MovementParse.updatePosition(res3, clone, 0);
                                    map.movePlayer(clone, pos);
                                }
                            } catch (Exception e) {
                                //very rarely swallowed
                            }
                        }
                    }, 500 * i + 500);
                }
            }
            int count = c.getPlayer().getFallCounter();
            try {
                if (map.getFootholds().findBelow(c.getPlayer().getPosition()) == null && c.getPlayer().getPosition().y > c.getPlayer().getOldPosition().y && c.getPlayer().getPosition().x == c.getPlayer().getOldPosition().x) {
                    if (count > 10) {
                        c.getPlayer().changeMap(map, map.getPortal(0));
                        c.getPlayer().setFallCounter(0);
                    } else {
                        c.getPlayer().setFallCounter(++count);
                    }
                } else if (count > 0) {
                    c.getPlayer().setFallCounter(0);
                }
            } catch (Exception e) {
            }
            c.getPlayer().setOldPosition(new Point(c.getPlayer().getPosition()));
        }
    }

    //更新玩家，顺便保存数据
    public static final void UpdateHandler(final SeekableLittleEndianAccessor mplew, final MapleClient c, final MapleCharacter chr) {
        boolean autoSave = true;
        if (!autoSave || chr == null || chr.getMap() == null) {
            return;
        }
        if (chr.getCheatTracker().canSaveDB()) {
            long startTime = System.currentTimeMillis();
            chr.saveToDB(false, false);
            if (chr.isAdmin()) {
                long endTime = System.currentTimeMillis() - startTime;
                String msg = "更新保存玩家数据 耗时: " + endTime + " 毫秒 ==> " + endTime / 1000 + " 秒..";
                c.getSession().write(MaplePacketCreator.yellowChat(msg));
            }
        } else if (chr.isAdmin()) {
            String msg = "更新保存玩家数据 离下次保存还有: " + chr.getCheatTracker().getlastSaveTime() + " 秒..";
            c.getSession().write(MaplePacketCreator.yellowChat(msg));
        }
    }

    public static final void ChangeMapSpecial(final SeekableLittleEndianAccessor slea, final String portal_name, final MapleClient c, final MapleCharacter chr) {
        slea.readShort();
        final MaplePortal portal = chr.getMap().getPortal(portal_name);
//	slea.skip(2);

        if (portal != null) {
            portal.enterPortal(c);
        } else {
            c.getSession().write(MaplePacketCreator.enableActions());
        }
    }

    public static final void ChangeMap(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        long startTime = System.currentTimeMillis();
        if (chr == null || chr.getMap() == null) {
            return;
        }
        if (slea.available() == 0L) {
            String[] socket = c.getChannelServer().getIP().split(":");
            chr.saveToDB(false, false);
            //chr.setInCS(false);
            c.getChannelServer().removePlayer(c.getPlayer());
            c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION);
            try {
                c.getSession().write(MaplePacketCreator.getChannelChange(c, Integer.parseInt(socket[1])));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return;
        }
        if (slea.available() != 0) {
//            slea.skip(6); //D3 75 00 00 00 00
            slea.readByte(); // 1 = from dying 2 = regular portals
            int targetid = slea.readInt(); // FF FF FF FF
            if (targetid == 0) {
                targetid = 1000000;
            }
            final MaplePortal portal = chr.getMap().getPortal(slea.readMapleAsciiString());
            /*
             * if (slea.available() >= 7) { chr.updateTick(slea.readInt()); }
             */
            //  slea.skip(1);
            final boolean wheel = slea.readShort() > 0 && !MapConstants.isEventMap(chr.getMapId()) && chr.haveItem(5510000, 1, false, true);

            if (targetid != -1 && !chr.isAlive()) {
                chr.setStance(0);
                if (chr.getEventInstance() != null && chr.getEventInstance().revivePlayer(chr) && chr.isAlive()) {
                    return;
                }
                if (chr.getPyramidSubway() != null) {
                    chr.getStat().setHp((short) 50);
                    chr.getPyramidSubway().fail(chr);
                    return;
                }
                if (!wheel) {
                    chr.getStat().setHp((short) 50);
                    MapleMap to = chr.getMap().getReturnMap();
                    /*if (to == null) {//修复死亡不回城
                        chr.setHp(50);
                        chr.updateSingleStat(MapleStat.HP, 50);
                        c.getSession().write(MaplePacketCreator.enableActions());
                        return;
                    }*/
                    chr.changeMap(to, to.getPortal(0));
                } else {
                    c.getSession().write(MTSCSPacket.useWheel((byte) (chr.getInventory(MapleInventoryType.CASH).countById(5510000) - 1)));
                    chr.getStat().setHp(((chr.getStat().getMaxHp() / 100) * 40));
                    MapleInventoryManipulator.removeById(c, MapleInventoryType.CASH, 5510000, 1, true, false);

                    final MapleMap to = chr.getMap();
                    chr.changeMap(to, to.getPortal(0));
                }
            } else if (targetid != -1 && chr.isGM()) {
                final MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
                if (to != null && to.getPortal(0) != null) {
                    chr.changeMap(to, to.getPortal(0));
                }

            } else if (targetid != -1 && !chr.isGM()) {
                final int divi = chr.getMapId() / 100;
                if (divi == 9130401) { // Only allow warp if player is already in Intro map, or else = hack

                    if (targetid == 130000000 || targetid / 100 == 9130401) { // Cygnus introduction
                        final MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
                        chr.changeMap(to, to.getPortal(0));
                    }
                } else if (divi == 9140900) { // Aran Introduction
                    if (targetid == 914090011 || targetid == 914090012 || targetid == 914090013 || targetid == 140090000) {
                        final MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
                        chr.changeMap(to, to.getPortal(0));
                    }
                } else if (divi == 9140901 && targetid == 140000000) {
                    //c.getSession().write(UIPacket.IntroDisableUI(false));
                    //   c.getSession().write(UIPacket.IntroLock(false));
                    c.getSession().write(MaplePacketCreator.enableActions());
                    final MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
                    chr.changeMap(to, to.getPortal(0));
                } else if (divi == 9140902 && (targetid == 140030000 || targetid == 140000000)) { //thing is. dont really know which one!
                    //  c.getSession().write(UIPacket.IntroDisableUI(false));
                    //   c.getSession().write(UIPacket.IntroLock(false));
                    c.getSession().write(MaplePacketCreator.enableActions());
                    final MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
                    chr.changeMap(to, to.getPortal(0));
                } else if (divi == 9000900 && targetid / 100 == 9000900 && targetid > chr.getMapId()) {
                    final MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
                    chr.changeMap(to, to.getPortal(0));
                } else if (divi / 1000 == 9000 && targetid / 100000 == 9000) {
                    if (targetid < 900090000 || targetid > 900090004) { //1 movie
                        //      c.getSession().write(UIPacket.IntroDisableUI(false));
                        //      c.getSession().write(UIPacket.IntroLock(false));
                        c.getSession().write(MaplePacketCreator.enableActions());
                    }
                    final MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
                    chr.changeMap(to, to.getPortal(0));
                } else if (divi / 10 == 1020 && targetid == 1020000) { // Adventurer movie clip Intro
                    //  c.getSession().write(UIPacket.IntroDisableUI(false));
                    //   c.getSession().write(UIPacket.IntroLock(false));
                    c.getSession().write(MaplePacketCreator.enableActions());
                    final MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
                    chr.changeMap(to, to.getPortal(0));

                } else if (chr.getMapId() == 900090101 && targetid == 100030100) {
                    //    c.getSession().write(UIPacket.IntroDisableUI(false));
                    //    c.getSession().write(UIPacket.IntroLock(false));
                    c.getSession().write(MaplePacketCreator.enableActions());
                    final MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
                    chr.changeMap(to, to.getPortal(0));
                } else if (chr.getMapId() == 2010000 && targetid == 104000000) {
                    //   c.getSession().write(UIPacket.IntroDisableUI(false));
                    //    c.getSession().write(UIPacket.IntroLock(false));
                    c.getSession().write(MaplePacketCreator.enableActions());
                    final MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
                    chr.changeMap(to, to.getPortal(0));
                } else if (chr.getMapId() == 106020001 || chr.getMapId() == 106020502) {
                    if (targetid == (chr.getMapId() - 1)) {
                        //     c.getSession().write(UIPacket.IntroDisableUI(false));
                        //     c.getSession().write(UIPacket.IntroLock(false));
                        c.getSession().write(MaplePacketCreator.enableActions());
                        final MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
                        chr.changeMap(to, to.getPortal(0));
                    }
                } else if (chr.getMapId() == 0 && targetid == 10000) {
                    //  c.getSession().write(UIPacket.IntroDisableUI(false));
                    //  c.getSession().write(UIPacket.IntroLock(false));
                    c.getSession().write(MaplePacketCreator.enableActions());
                    final MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(targetid);
                    chr.changeMap(to, to.getPortal(0));
                }
            } else if (portal != null) {
                portal.enterPortal(c);
            } else {
                c.getSession().write(MaplePacketCreator.enableActions());
            }
        }
        if (chr.isAdmin()) {
            long endTime = System.currentTimeMillis() - startTime;
            String msg = "切换地图完成 耗时" + endTime + " 毫秒 ==> " + endTime / 1000L + " 秒..";
            c.getSession().write(MaplePacketCreator.yellowChat(msg));
        }
    }

    public static final void InnerPortal(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null) {
            return;
        }
        final MaplePortal portal = chr.getMap().getPortal(slea.readMapleAsciiString());
        final Point Original_Pos = chr.getPosition();
        final int toX = slea.readShort();
        final int toY = slea.readShort();
//	slea.readShort(); // Original X pos
//	slea.readShort(); // Original Y pos

        if (portal == null) {
            return;
        } else if (portal.getPosition().distanceSq(chr.getPosition()) > 22500) {
            chr.getCheatTracker().registerOffense(CheatingOffense.使用过远传送点);
        }
        chr.getMap().movePlayer(chr, new Point(toX, toY));
        chr.checkFollow();
    }

    public static final void snowBall(SeekableLittleEndianAccessor slea, MapleClient c) {
        //B2 00
        //01 [team]
        //00 00 [unknown]
        //89 [position]
        //01 [stage]
        c.getSession().write(MaplePacketCreator.enableActions());
        //empty, we do this in closerange
    }

    public static final void leftKnockBack(SeekableLittleEndianAccessor slea, final MapleClient c) {
        // D1 00 86 01 47 01
        if (c.getPlayer().getMapId() / 10000 == 10906) { //must be in snowball map or else its like infinite FJ
            c.getSession().write(MaplePacketCreator.leftKnockBack());
            c.getSession().write(MaplePacketCreator.enableActions());
        }
    }

    public static void Rabbit(SeekableLittleEndianAccessor slea, final MapleClient c) {
        int arrackfrom = slea.readInt();
        int damage = slea.readInt() + 100;
        int attackto = slea.readInt();
        MapleMonster mob = c.getPlayer().getMap().getMonsterByOid(attackto);
        if (mob != null && mob.getHp() > 0) {
            mob.damage(c.getPlayer(), (long) damage, true);
        }
    }
    /*
     * public static final void UpdateFkCharMessages(final
     * SeekableLittleEndianAccessor slea, final MapleClient c, final
     * MapleCharacter chr) { int type = slea.readByte();
     * //chr.UpdateCharMessageZone(); //c.getPlayer().setcharmessage(s); // if
     * (type == 0) { // 角色訊息 /*String
     *///int charmessage = slea.readMapleAsciiString();
    //c.getPlayer().setcharmessage(charmessage);
    //MapleCharacter.UpdateCharMessageZone();
    //chr.UpdateCharMessageZone();
    //System.err.println("SetCharMessage");
    /*
     * } else if (type == 1) { // 表情 int expression = slea.readByte();
     * c.getPlayer().setexpression(expression);
     * System.err.println("Expression"); } else if (type == 2) { // 生日及星座 int
     * blood = slea.readByte(); int month = slea.readByte(); int day =
     * slea.readByte(); int constellation = slea.readByte();
     * c.getPlayer().setblood(blood); c.getPlayer().setmonth(month);
     * c.getPlayer().setday(day); c.getPlayer().setconstellation(constellation);
     * System.err.println("Constellation"); }
     */
    //}
    /*
     * public String getcharmessage(final MapleClient c) {
     *
     * return c.getPlayer().getcharmessage();
     *
     * }
     * public void setcharmessage(final MapleClient c, String s) {
     * c.getPlayer().setcharmessage(s);
     * c.getSession().write(MaplePacketCreator.updateBeans(c.getPlayer().getId(),
     * s)); }
     */
}
