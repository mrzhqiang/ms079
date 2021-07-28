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
package server.life;

import client.inventory.Equip;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledFuture;

import constants.GameConstants;
import client.inventory.IItem;
import client.ISkill;
import client.inventory.Item;
import client.MapleDisease;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.inventory.MapleInventoryType;
import client.MapleClient;
import handling.channel.ChannelServer;
import client.SkillFactory;
import client.status.MonsterStatus;
import client.status.MonsterStatusEffect;
import constants.ServerConstants;
import handling.MaplePacket;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import java.awt.Point;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import scripting.EventInstanceManager;
import server.MapleItemInformationProvider;
import server.MapleStatEffect;
import server.Randomizer;
import server.Timer.MobTimer;
import server.maps.MapScriptMethods;
import server.maps.MapleMap;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import tools.ConcurrentEnumMap;
import tools.Pair;
import tools.MaplePacketCreator;
import tools.packet.MobPacket;

public class MapleMonster extends AbstractLoadedMapleLife {

    private MapleMonsterStats stats;
    private OverrideMonsterStats ostats = null;
    private long hp;
    private int mp;
    private byte venom_counter, carnivalTeam;
    private MapleMap map;
    private WeakReference<MapleMonster> sponge = new WeakReference<MapleMonster>(null);
    private int linkoid = 0, lastNode = -1, lastNodeController = -1, highestDamageChar = 0; // Just a reference for monster EXP distribution after dead
    private WeakReference<MapleCharacter> controller = new WeakReference<MapleCharacter>(null);
    private boolean fake, dropsDisabled, controllerHasAggro, controllerKnowsAboutAggro;
    private final Collection<AttackerEntry> attackers = new LinkedList<AttackerEntry>();
    private EventInstanceManager eventInstance;
    private MonsterListener listener = null;
    private MaplePacket reflectpack = null, nodepack = null;
    private final Map<MonsterStatus, MonsterStatusEffect> stati = new ConcurrentEnumMap<MonsterStatus, MonsterStatusEffect>(MonsterStatus.class);
    private Map<Integer, Long> usedSkills;
    private int stolen = -1; //monster can only be stolen ONCE
    private ScheduledFuture<?> dropItemSchedule;
    private boolean shouldDropItem = false;
    private final LinkedList<MonsterStatusEffect> poisons = new LinkedList<MonsterStatusEffect>();
    private final ReentrantReadWriteLock poisonsLock = new ReentrantReadWriteLock();

    public MapleMonster(final int id, final MapleMonsterStats stats) {
        super(id);
        initWithStats(stats);
    }

    public MapleMonster(final MapleMonster monster) {
        super(monster);
        initWithStats(monster.stats);
    }

    private final void initWithStats(final MapleMonsterStats stats) {
        setStance(5);
        this.stats = stats;
        hp = stats.getHp();
        mp = stats.getMp();
        venom_counter = 0;
//	showdown = 100;
        carnivalTeam = -1;
        fake = false;
        dropsDisabled = false;

        if (stats.getNoSkills() > 0) {
            usedSkills = new HashMap<Integer, Long>();
        }
    }

    public final MapleMonsterStats getStats() {
        return stats;
    }

    public final void disableDrops() {
        this.dropsDisabled = true;
    }

    public final boolean dropsDisabled() {
        return dropsDisabled;
    }

    public final void setSponge(final MapleMonster mob) {
        sponge = new WeakReference<MapleMonster>(mob);
    }

    public final void setMap(final MapleMap map) {
        this.map = map;
        startDropItemSchedule();
    }

    public final long getHp() {
        return hp;
    }

    public final void setHp(long hp) {
        this.hp = hp;
    }

    public final long getMobMaxHp() {
        if (ostats != null) {
            return ostats.getHp();
        }
        return stats.getHp();
    }

    public final int getMp() {
        return mp;
    }

    public final void setMp(int mp) {
        if (mp < 0) {
            mp = 0;
        }
        this.mp = mp;
    }

    public final int getMobMaxMp() {
        if (ostats != null) {
            return ostats.getMp();
        }
        return stats.getMp();
    }

    public final int getMobExp() {
        if (ostats != null) {
            return ostats.getExp();
        }
        return stats.getExp();
    }

    public final void setOverrideStats(final OverrideMonsterStats ostats) {
        this.ostats = ostats;
        this.hp = ostats.getHp();
        this.mp = ostats.getMp();
    }

    public final MapleMonster getSponge() {
        return sponge.get();
    }

    public final byte getVenomMulti() {
        return venom_counter;
    }

    public final void setVenomMulti(final byte venom_counter) {
        this.venom_counter = venom_counter;
    }

    public final void damage(final MapleCharacter from, final long damage, final boolean updateAttackTime) {
        damage(from, damage, updateAttackTime, 0);
    }

    public final void damage(final MapleCharacter from, final long damage, final boolean updateAttackTime, final int lastSkill) {
        if (from == null || damage <= 0 || !isAlive()) {
            return;
        }
        AttackerEntry attacker = null;

        if (from.getParty() != null) {
            attacker = new PartyAttackerEntry(from.getParty().getId(), map.getChannel());
        } else {
            attacker = new SingleAttackerEntry(from, map.getChannel());
        }
        boolean replaced = false;
        for (final AttackerEntry aentry : attackers) {
            if (aentry.equals(attacker)) {
                attacker = aentry;
                replaced = true;
                break;
            }
        }
        if (!replaced) {
            attackers.add(attacker);
        }
        final long rDamage = Math.max(0, Math.min(damage, hp));
        attacker.addDamage(from, rDamage, updateAttackTime);

        if (stats.getSelfD() != -1) {
            hp -= rDamage;
            if (hp > 0) {
                if (hp < stats.getSelfDHp()) { // HP is below the selfd level
                    map.killMonster(this, from, false, false, stats.getSelfD(), lastSkill);
                } else { // Show HP
                    for (final AttackerEntry mattacker : attackers) {
                        for (final AttackingMapleCharacter cattacker : mattacker.getAttackers()) {
                            if (cattacker.getAttacker().getMap() == from.getMap()) { // current attacker is on the map of the monster
                                if (cattacker.getLastAttackTime() >= System.currentTimeMillis() - 4000) {
                                    cattacker.getAttacker().getClient().getSession().write(MobPacket.showMonsterHP(getObjectId(), (int) Math.ceil((hp * 100.0) / getMobMaxHp())));
                                }
                            }
                        }
                    }
                }
            } else { // Character killed it without explosing :(
                map.killMonster(this, from, true, false, (byte) 1, lastSkill);
            }
        } else {
            if (sponge.get() != null) {
                if (sponge.get().hp > 0) { // If it's still alive, dont want double/triple rewards
                    // Sponge are always in the same map, so we can use this.map
                    // The only mob that uses sponge are PB/HT
                    sponge.get().hp -= rDamage;

                    if (sponge.get().hp <= 0) {
                        map.broadcastMessage(MobPacket.showBossHP(((MapleMonster) this.sponge.get()).getId(), -1L, ((MapleMonster) this.sponge.get()).getMobMaxHp()));
                        map.killMonster(sponge.get(), from, true, false, (byte) 1, lastSkill);
                    } else {
                        map.broadcastMessage(MobPacket.showBossHP(sponge.get()));
                    }
                }
            }
            if (hp > 0) {
                hp -= rDamage;
                if (eventInstance != null) {
                    eventInstance.monsterDamaged(from, this, (int) rDamage);
                } else {
                    final EventInstanceManager em = from.getEventInstance();
                    if (em != null) {
                        em.monsterDamaged(from, this, (int) rDamage);
                    }
                }
                if (sponge.get() == null && hp > 0) {
                    switch (stats.getHPDisplayType()) {
                        case 0:
                            map.broadcastMessage(MobPacket.showBossHP(this), this.getPosition());
                            break;

                        case 1:
                            map.broadcastMessage(from, MobPacket.damageFriendlyMob(this, damage, true), false);
                            //   map.broadcastMessage(MobPacket.damageFriendlyMob(this, damage, true));
                            // map.broadcastMessage(MobPacket.showMonsterHP(getObjectId(), (int) Math.ceil((hp * 100.0) / getMobMaxHp())));
                            break;
                        case -1:
                        case 2:
                            int oid = getObjectId();
                            double percent = (hp * 100.0) / getMobMaxHp();
                            int show = (int) Math.ceil(percent);
                            map.broadcastMessage(MobPacket.showMonsterHP(getObjectId(), (int) Math.ceil((hp * 100.0) / getMobMaxHp())));
                            from.mulung_EnergyModify(true);
                            break;
                        case 3:
                            for (final AttackerEntry mattacker : attackers) {
                                for (final AttackingMapleCharacter cattacker : mattacker.getAttackers()) {
                                    if (cattacker.getAttacker().getMap() == from.getMap()) { // current attacker is on the map of the monster
                                        if (cattacker.getLastAttackTime() >= System.currentTimeMillis() - 4000) {
                                            cattacker.getAttacker().getClient().getSession().write(MobPacket.showMonsterHP(getObjectId(), (int) Math.ceil((hp * 100.0) / getMobMaxHp())));
                                        }
                                    }
                                }
                            }
                            break;
                        default:
                            System.out.println(stats.isBoss() + " " + stats.getHPDisplayType());
                            break;
                    }
                }
                if (hp <= 0) {
                    if (this.stats.getHPDisplayType() == 0 || this.stats.getHPDisplayType() == -1) {
                        this.map.broadcastMessage(MobPacket.showBossHP(getId(), -1L, getMobMaxHp()), this.getPosition());
                    }
                    map.killMonster(this, from, true, false, (byte) 1, lastSkill);
                }
            }
        }
        startDropItemSchedule();
    }

    public final void heal(int hp, int mp, final boolean broadcast) {
        final long TotalHP = getHp() + hp;
        final int TotalMP = getMp() + mp;

        if (TotalHP >= getMobMaxHp()) {
            setHp(getMobMaxHp());
        } else {
            setHp(TotalHP);
        }
        if (TotalMP >= getMp()) {
            setMp(getMp());
        } else {
            setMp(TotalMP);
        }
        if (broadcast) {
            map.broadcastMessage(MobPacket.healMonster(getObjectId(), hp));
        } else if (sponge.get() != null) { // else if, since only sponge doesn't broadcast
            sponge.get().hp += hp;
        }
    }

    private final void giveExpToCharacter(final MapleCharacter attacker, int exp, final boolean highestDamage, final int numExpSharers, final byte pty, final byte Class_Bonus_EXP_PERCENT, final byte Premium_Bonus_EXP_PERCENT, final int lastskillID) {
        if (highestDamage) {
            if (eventInstance != null) {
                eventInstance.monsterKilled(attacker, this);
            } else {
                final EventInstanceManager em = attacker.getEventInstance();
                if (em != null) {
                    em.monsterKilled(attacker, this);
                }
            }
            highestDamageChar = attacker.getId();
        }
        if (exp > 0) {
            final MonsterStatusEffect mse = stati.get(MonsterStatus.挑衅);
            if (mse != null) {
                exp += (int) (exp * (mse.getX() / 100.0));
            }
            final Integer holySymbol = attacker.getBuffedValue(MapleBuffStat.HOLY_SYMBOL);
            if (holySymbol != null) {
                if (numExpSharers == 1) {
                    exp *= 1.0 + (holySymbol.doubleValue() / 500.0);
                } else {
                    exp *= 1.0 + (holySymbol.doubleValue() / 100.0);
                }
            }
            if (attacker.hasDisease(MapleDisease.CURSE)) {
                exp /= 2;
            }
            exp *= attacker.getEXPMod() * (int) (attacker.getStat().expBuff / 100.0);
            exp = (int) Math.min(Integer.MAX_VALUE, exp * (attacker.getLevel() < 10 ? GameConstants.getExpRate_Below10(attacker.getJob()) : ChannelServer.getInstance(map.getChannel()).getExpRate()));
            //do this last just incase someone has a 2x exp card and its set to max value
            int Class_Bonus_EXP = 0;
            if (Class_Bonus_EXP_PERCENT > 0) {
                Class_Bonus_EXP = (int) ((exp / 100.0) * Class_Bonus_EXP_PERCENT);
            }
            int Premium_Bonus_EXP = 0;
            if (Premium_Bonus_EXP_PERCENT > 0) {
                Premium_Bonus_EXP = (int) ((exp / 100.0) * Premium_Bonus_EXP_PERCENT);
            }
            int Equipment_Bonus_EXP = (int) ((exp / 100.0) * attacker.getStat().equipmentBonusExp);
            if (attacker.getStat().equippedFairy) {
                Equipment_Bonus_EXP += (int) ((exp / 100.0) * attacker.getFairyExp());
            }
            int wedding_EXP = 0;
            if (attacker.getMarriageId() > 0 && attacker.getMap().getCharacterById_InMap(attacker.getMarriageId()) != null) {
                wedding_EXP += (exp / 100.0d) * 10.0d;
            }
            attacker.gainExpMonster(exp, true, highestDamage, pty, wedding_EXP, Class_Bonus_EXP, Equipment_Bonus_EXP, Premium_Bonus_EXP);
            // attacker.increaseEquipExp(exp);
        }
        attacker.mobKilled(getId(), lastskillID);
    }

    public final int killBy(final MapleCharacter killer, final int lastSkill) {
        int totalBaseExp = getMobExp();
        AttackerEntry highest = null;
        long highdamage = 0;
        for (final AttackerEntry attackEntry : attackers) {
            if (attackEntry.getDamage() > highdamage) {
                highest = attackEntry;
                highdamage = attackEntry.getDamage();
            }
        }
        int baseExp;
        for (final AttackerEntry attackEntry : attackers) {
            baseExp = (int) Math.ceil(totalBaseExp * ((double) attackEntry.getDamage() / getMobMaxHp()));
            attackEntry.killedMob(getMap(), baseExp, attackEntry == highest, lastSkill);
        }
        final MapleCharacter controll = controller.get();
        if (controll != null) { // this can/should only happen when a hidden gm attacks the monster
            controll.getClient().getSession().write(MobPacket.stopControllingMonster(getObjectId()));
            controll.stopControllingMonster(this);
        }
        //int achievement = 0;

        switch (getId()) {
            case 9400121:
            //achievement = 12;
            //break;
            case 8500002:
            //achievement = 13;
            //break;
            case 8510000:
            case 8520000:
            //achievement = 14;
            //break;
            default:
                break;
        }

        /*
         * if (achievement != 0) { if (killer != null && killer.getParty() !=
         * null) { for (MaplePartyCharacter mp : killer.getParty().getMembers())
         * { final MapleCharacter mpc =
         * killer.getMap().getCharacterById(mp.getId()); if (mpc != null) {
         * mpc.finishAchievement(achievement); } } } else if (killer != null) {
         * killer.finishAchievement(achievement); } } if (killer != null &&
         * stats.isBoss()) { killer.finishAchievement(18); }
         */
        spawnRevives(getMap());
        if (eventInstance != null) {
            eventInstance.unregisterMonster(this);
            eventInstance = null;
        }
        if (killer != null && killer.getPyramidSubway() != null) {
            killer.getPyramidSubway().onKill(killer);
        }
        MapleMonster oldSponge = getSponge();
        sponge = new WeakReference<MapleMonster>(null);
        if (oldSponge != null && oldSponge.isAlive()) {
            boolean set = true;
            for (MapleMapObject mon : map.getAllMonstersThreadsafe()) {
                MapleMonster mons = (MapleMonster) mon;
                if (mons.getObjectId() != oldSponge.getObjectId() && mons.getObjectId() != this.getObjectId() && (mons.getSponge() == oldSponge || mons.getLinkOid() == oldSponge.getObjectId())) { //sponge was this, please update
                    set = false;
                    break;
                }
            }
            if (set) { //all sponge monsters are dead, please kill off the sponge
                map.killMonster(oldSponge, killer, true, false, (byte) 1);
            }
        }

        nodepack = null;
        reflectpack = null;
        stati.clear();
        //attackers.clear();
        cancelDropItem();
        if (listener != null) {
            listener.monsterKilled();
        }
        int v1 = highestDamageChar;
        this.highestDamageChar = 0; //reset so we dont kill twice
        return v1;
    }

    public final void spawnRevives(final MapleMap map) {
        final List<Integer> toSpawn = stats.getRevives();

        if (toSpawn == null) {
            return;
        }
        MapleMonster spongy = null;
        switch (getId()) {
            case 8810118:
            case 8810119:
            case 8810120:
            case 8810121: //must update sponges
                for (final int i : toSpawn) {
                    final MapleMonster mob = MapleLifeFactory.getMonster(i);

                    mob.setPosition(getPosition());
                    if (eventInstance != null) {
                        eventInstance.registerMonster(mob);
                    }
                    if (dropsDisabled()) {
                        mob.disableDrops();
                    }
                    switch (mob.getId()) {
                        case 8810119:
                        case 8810120:
                        case 8810121:
                        case 8810122:
                            spongy = mob;
                            break;
                    }
                }
                if (spongy != null) {
                    map.spawnRevives(spongy, this.getObjectId());
                    for (MapleMapObject mon : map.getAllMonstersThreadsafe()) {
                        MapleMonster mons = (MapleMonster) mon;
                        if (mons.getObjectId() != spongy.getObjectId() && (mons.getSponge() == this || mons.getLinkOid() == this.getObjectId())) { //sponge was this, please update
                            mons.setSponge(spongy);
                            mons.setLinkOid(spongy.getObjectId());
                        }
                    }
                }
                break;
            case 8810026:
            case 8810130:
            case 8820008:
            case 8820009:
            case 8820010:
            case 8820011:
            case 8820012:
            case 8820013: {
                final List<MapleMonster> mobs = new ArrayList<MapleMonster>();

                for (final int i : toSpawn) {
                    final MapleMonster mob = MapleLifeFactory.getMonster(i);

                    mob.setPosition(getPosition());
                    if (eventInstance != null) {
                        eventInstance.registerMonster(mob);
                    }
                    if (dropsDisabled()) {
                        mob.disableDrops();
                    }
                    switch (mob.getId()) {
                        case 8810018: // Horntail Sponge
                        case 8810118:
                        case 8820009: // PinkBeanSponge0
                        case 8820010: // PinkBeanSponge1
                        case 8820011: // PinkBeanSponge2
                        case 8820012: // PinkBeanSponge3
                        case 8820013: // PinkBeanSponge4
                        case 8820014: // PinkBeanSponge5
                            spongy = mob;
                            break;
                        default:
                            mobs.add(mob);
                            break;
                    }
                }
                if (spongy != null) {
                    map.spawnRevives(spongy, this.getObjectId());

                    for (final MapleMonster i : mobs) {
                        i.setSponge(spongy);
                        map.spawnRevives(i, this.getObjectId());
                    }
                }
                break;
            }
            default: {
                for (final int i : toSpawn) {
                    final MapleMonster mob = MapleLifeFactory.getMonster(i);

                    if (eventInstance != null) {
                        eventInstance.registerMonster(mob);
                    }
                    mob.setPosition(getPosition());
                    if (dropsDisabled()) {
                        mob.disableDrops();
                    }
                    map.spawnRevives(mob, this.getObjectId());

                    if (mob.getId() == 9300216) {
                        map.broadcastMessage(MaplePacketCreator.environmentChange("Dojang/clear", 4));
                        map.broadcastMessage(MaplePacketCreator.environmentChange("dojang/end/clear", 3));
                    }
                }
                break;
            }
        }
    }

    public final boolean isAlive() {
        return hp > 0;
    }

    public final void setCarnivalTeam(final byte team) {
        carnivalTeam = team;
    }

    public final byte getCarnivalTeam() {
        return carnivalTeam;
    }

    public final MapleCharacter getController() {
        return controller.get();
    }

    public final void setController(final MapleCharacter controller) {
        this.controller = new WeakReference<MapleCharacter>(controller);
    }

    public final void switchController(final MapleCharacter newController, final boolean immediateAggro) {
        final MapleCharacter controllers = getController();
        if (controllers == newController) {
            return;
        } else if (controllers != null) {
            controllers.stopControllingMonster(this);
            controllers.getClient().getSession().write(MobPacket.stopControllingMonster(getObjectId()));
        }
        newController.controlMonster(this, immediateAggro);
        setController(newController);
        if (immediateAggro) {
            setControllerHasAggro(true);
        }
        setControllerKnowsAboutAggro(false);
        if (getId() == 9300275 && map.getId() >= 921120100 && map.getId() < 921120500) { //shammos
            if (lastNodeController != -1 && lastNodeController != newController.getId()) { //new controller, please re update
                resetShammos(newController.getClient());
            } else {
                setLastNodeController(newController.getId());
            }
        }
    }

    public final void resetShammos(MapleClient c) {
        map.killAllMonsters(true);
        map.broadcastMessage(MaplePacketCreator.serverNotice(5, "A player has moved too far from Shammos. Shammos is going back to the start."));
        for (MapleCharacter chr : map.getCharactersThreadsafe()) {
            chr.changeMap(chr.getMap(), chr.getMap().getPortal(0));
        }
        //屏蔽地图动画
        // MapScriptMethods.startScript_FirstUser(c, "shammos_Fenter");
    }

    public final void addListener(final MonsterListener listener) {
        this.listener = listener;
    }

    public final boolean isControllerHasAggro() {
        return controllerHasAggro;
    }

    public final void setControllerHasAggro(final boolean controllerHasAggro) {
        this.controllerHasAggro = controllerHasAggro;
    }

    public final boolean isControllerKnowsAboutAggro() {
        return controllerKnowsAboutAggro;
    }

    public final void setControllerKnowsAboutAggro(final boolean controllerKnowsAboutAggro) {
        this.controllerKnowsAboutAggro = controllerKnowsAboutAggro;
    }

    @Override
    public final void sendSpawnData(final MapleClient client) {
        if (!isAlive()) {
            return;
        }
        client.getSession().write(MobPacket.spawnMonster(this, (lastNode >= 0 ? -2 : -1), fake ? 0xfc : (lastNode >= 0 ? 12 : 0), 0));
        if (reflectpack != null) {
            client.getSession().write(reflectpack);
        }
        if (lastNode >= 0) {
            //   client.getSession().write(MaplePacketCreator.getNodeProperties(this, map));
            if (getId() == 9300275 && map.getId() >= 921120100 && map.getId() < 921120500) { //shammos
                if (lastNodeController != -1) { //new controller, please re update. sendSpawn only comes when you get too far then come back anyway
                    resetShammos(client);
                } else {
                    setLastNodeController(client.getPlayer().getId());
                }
            }
        }
    }

    @Override
    public final void sendDestroyData(final MapleClient client) {
        if (lastNode == -1) {
            client.getSession().write(MobPacket.killMonster(getObjectId(), 0));
        }
        if (getId() == 9300275 && map.getId() >= 921120100 && map.getId() < 921120500) { //shammos
            resetShammos(client);
        }
    }

    @Override
    public final String toString() {
        final StringBuilder sb = new StringBuilder();

        sb.append(stats.getName());
        sb.append("(");
        sb.append(getId());
        sb.append(") (等級 ");
        sb.append(stats.getLevel());
        sb.append(") 在 (X");
        sb.append(getPosition().x);
        sb.append("/ Y");
        sb.append(getPosition().y);
        sb.append(") 座標 ");
        sb.append(getHp());
        sb.append("/ ");
        sb.append(getMobMaxHp());
        sb.append("血量, ");
        sb.append(getMp());
        sb.append("/ ");
        sb.append(getMobMaxMp());
        sb.append(" 魔力, 反應堆: ");
        sb.append(getObjectId());
        sb.append(" || 仇恨目標 : ");
        final MapleCharacter chr = controller.get();
        sb.append(chr != null ? chr.getName() : "無");

        return sb.toString();
    }

    @Override
    public final MapleMapObjectType getType() {
        return MapleMapObjectType.MONSTER;
    }

    public final EventInstanceManager getEventInstance() {
        return eventInstance;
    }

    public final void setEventInstance(final EventInstanceManager eventInstance) {
        this.eventInstance = eventInstance;
    }

    public final int getStatusSourceID(final MonsterStatus status) {
        final MonsterStatusEffect effect = stati.get(status);
        if (effect != null) {
            return effect.getSkill();
        }
        return -1;
    }

    public final ElementalEffectiveness getEffectiveness(final Element e) {
        if (stati.size() > 0 && stati.get(MonsterStatus.巫毒) != null) {
            return ElementalEffectiveness.正常; // like blue snails
        }
        return stats.getEffectiveness(e);
    }

    //玩家给怪物上BUFF处理
    public final void applyStatus(final MapleCharacter from, final MonsterStatusEffect status, final boolean poison, final long duration, final boolean venom) {
        applyStatus(from, status, poison, duration, venom, null);
    }

    //玩家给怪物上BUFF处理
    public final void applyStatus(final MapleCharacter from, final MonsterStatusEffect status, final boolean poison, final long duration, final boolean venom, final MapleStatEffect effect) {
        applyStatus(from, status, poison, duration, venom, true, effect);
    }

    //玩家给怪物上BUFF处理
    public final void applyStatus(final MapleCharacter from, final MonsterStatusEffect status, final boolean poison, final long duration, final boolean venom, final boolean checkboss, final MapleStatEffect effect) {
        if (!isAlive()) {
            return;
        }
        
        if (from.hasGmLevel(5)) {
            from.dropMessage(6, "怪物: " + getId() + " 状态: " + status.getStati().name() + " 中毒: " + poison + " 持续时间: " + duration);
        }
        ISkill skilz = SkillFactory.getSkill(status.getSkill());
        if (skilz != null) {
            switch (stats.getEffectiveness(skilz.getElement())) {
                case 免疫:
                case 增强:
                    return;
                case 正常:
                case 虚弱:
                    break;
                default:
                    return;
            }
        }
        //ElementalEffectiveness effectiveness = null;
        // compos don't have an elemental (they have 2 - so we have to hack here...)
        final int statusSkill = status.getSkill();
        switch (statusSkill) {
            //case 2121003:// - 火凤球 - 火凤球用火柱环绕怪物,给予持续伤害。被火凤球抓住的人冰系抗性降低。
            case 2111006: { //火毒合击
                switch (stats.getEffectiveness(Element.POISON)) {
                    case 免疫:
                    case 增强:
                        return;
                }
                break;
            }
            case 14111006: {//毒炸弹
                switch (stats.getEffectiveness(Element.POISON)) {
                    case 免疫:
                    case 增强:
                        return;
                }
                break;
            }
            //case 2221003:// - 冰凤球 - 冻结多数怪物,给予持续伤害.被冰凤球抓住的人火系抗性降低.
            //case 2211002:
            case 2211006: { //冰雷合击
                switch (stats.getEffectiveness(Element.ICE)) {
                    case 免疫:
                    case 增强:
                        return;
                }
                break;
            }
            case 4120005://隐士的武器用毒液
            case 4220005://侠盗的武器用毒液
            case 14110004: {//夜行者的武器用毒液
                switch (stats.getEffectiveness(Element.POISON)) {
                    case 虚弱:
                        //case 免疫:
                        //case 增强:
                        return;
                }
                break;
            }
        }

        if (poison && getHp() <= 1) {//新增判断
            return;
        }

        final MonsterStatus stat = status.getStati();

        if (stats.isNoDoom() && stat == MonsterStatus.巫毒) {
            return;
        }

        if (stats.isBoss()) {
            
            if (stat == MonsterStatus.眩晕 || stat == MonsterStatus.速度) {
                return;
            }
            if (checkboss && stat != (MonsterStatus.忍者伏击) && stat != (MonsterStatus.物攻) && stat != (MonsterStatus.中毒) && stat != (MonsterStatus.烈焰喷射) && stat != (MonsterStatus.恐慌) && stat != (MonsterStatus.魔击无效)) {
                return;
            }
        }

        final MonsterStatusEffect oldEffect = stati.get(stat);
        if (oldEffect != null) {
            stati.remove(stat);
            if (oldEffect.getStati() == null) {
                oldEffect.cancelTask();
                oldEffect.cancelPoisonSchedule();
            }
        }
        final MobTimer timerManager = MobTimer.getInstance();
        final Runnable cancelTask = new Runnable() {

            @Override
            public final void run() {
                cancelStatus(stat);
            }
        };

        if (poison && getHp() > 1) {
            if (poison) {
                int poisonLevel = from.getSkillLevel(status.getSkill());
                int poisonDamage = Math.min(Short.MAX_VALUE, (int) (getMobMaxHp() / (70.0 - poisonLevel) + 0.999));
                status.setValue(MonsterStatus.中毒, Integer.valueOf(poisonDamage));
                status.setPoisonSchedule(timerManager.register(new PoisonTask(poisonDamage, from, status, cancelTask, false), 1000, 1000));
                // final int poisonDamage = (int) Math.min(Short.MAX_VALUE, (long) (getMobMaxHp() / (70.0 - from.getSkillLevel(status.getSkill())) + 0.999));
                // status.setValue(MonsterStatus.中毒, poisonDamage);
                // status.setPoisonSchedule(timerManager.register(new PoisonTask(poisonDamage, from, status, cancelTask, false), 1000, 1000));
            } else if (venom) {
                int poisonLevel = 0;
                int matk = 0;

                switch (from.getJob()) {
                    case 412:
                        poisonLevel = from.getSkillLevel(SkillFactory.getSkill(4120005));
                        if (poisonLevel <= 0) {
                            return;
                        }
                        matk = SkillFactory.getSkill(4120005).getEffect(poisonLevel).getMatk();
                        break;
                    case 422:
                        poisonLevel = from.getSkillLevel(SkillFactory.getSkill(4220005));
                        if (poisonLevel <= 0) {
                            return;
                        }
                        matk = SkillFactory.getSkill(4220005).getEffect(poisonLevel).getMatk();
                        break;
                    case 1411:
                    case 1412:
                        poisonLevel = from.getSkillLevel(SkillFactory.getSkill(14110004));
                        if (poisonLevel <= 0) {
                            return;
                        }
                        matk = SkillFactory.getSkill(14110004).getEffect(poisonLevel).getMatk();
                        break;
                    case 434:
                        poisonLevel = from.getSkillLevel(SkillFactory.getSkill(4340001));
                        if (poisonLevel <= 0) {
                            return;
                        }
                        matk = SkillFactory.getSkill(4340001).getEffect(poisonLevel).getMatk();
                        break;
                    default:
                        return; // Hack, using venom without the job required
                }
                final int luk = from.getStat().getLuk();
                final int maxDmg = (int) Math.ceil(Math.min(Short.MAX_VALUE, 0.2 * luk * matk));
                final int minDmg = (int) Math.ceil(Math.min(Short.MAX_VALUE, 0.1 * luk * matk));
                int gap = maxDmg - minDmg;
                if (gap == 0) {
                    gap = 1;
                }
                int poisonDamage = 0;
                for (int i = 0; i < getVenomMulti(); i++) {
                    poisonDamage = poisonDamage + (Randomizer.nextInt(gap) + minDmg);
                }
                poisonDamage = Math.min(Short.MAX_VALUE, poisonDamage);
                status.setValue(MonsterStatus.中毒, poisonDamage);
                status.setPoisonSchedule(timerManager.register(new PoisonTask(poisonDamage, from, status, cancelTask, false), 1000, 1000));

            } else if (statusSkill == 4111003 || statusSkill == 14111001) { // shadow web
                status.setPoisonSchedule(timerManager.schedule(new PoisonTask((int) (getMobMaxHp() / 50.0 + 0.999), from, status, cancelTask, true), 3500));

            } else if (statusSkill == 4121004 || statusSkill == 4221004) {
                final int damage = (from.getStat().getStr() + from.getStat().getLuk()) * 2 * (60 / 100);
                status.setPoisonSchedule(timerManager.register(new PoisonTask(damage, from, status, cancelTask, false), 1000, 1000));
            }

            stati.put(stat, status);
            map.broadcastMessage(MobPacket.applyMonsterStatus(getObjectId(), status), getPosition());
            if (getController() != null && !getController().isMapObjectVisible(this)) {
                getController().getClient().getSession().write(MobPacket.applyMonsterStatus(getObjectId(), status));
            }
            int aniTime = 0;
            if (skilz != null) {
                aniTime = skilz.getAnimationTime();
            }
            ScheduledFuture<?> schedule = timerManager.schedule(cancelTask, duration + aniTime);
            status.setCancelTask(schedule);
        }
    }

    /*
     * 玩家给怪物上BUFF
     */
    //public final void applyStatus(final MapleCharacter from, final MonsterStatusEffect status, final boolean poison, final long duration, final boolean venom) {
    //    applyStatus(from, status, poison, duration, venom, true, null);
    //}

    /*
     * 玩家给怪物上BUFF
     */
 /*public final void applyStatus(final MapleCharacter from, final MonsterStatusEffect status, final boolean poison, long duration, final boolean venom, final boolean checkboss, MapleStatEffect effect) {
        if (!isAlive()) {//死亡状态返回
            return;
        }
        if (from.isGM()) {
            from.dropMessage(5, "怪物: " + getId() + " 状态: " + status.getStati().name() + " 中毒: " + poison + " 持续时间: " + duration);
        }
        ISkill skilz = SkillFactory.getSkill(status.getSkill());
        if (skilz != null) {
            switch (stats.getEffectiveness(skilz.getElement())) {
                case 免疫:
                case 增强:
                    return;
                case 正常:
                case 虚弱:
                    break;
                default:
                    return;
            }
        }
        // compos don't have an elemental (they have 2 - so we have to hack here...)
        final int statusSkill = status.getSkill();
        switch (statusSkill) {
            case 2121003:// - 火凤球 - 火凤球用火柱环绕怪物,给予持续伤害。被火凤球抓住的人冰系抗性降低。
            case 2111006: {//- 火毒合击 - [最高等级:30]\n混合火和毒属性的魔法攻击一个怪物,一定的几率使怪物中毒。
                switch (stats.getEffectiveness(Element.POISON)) {//毒
                    case 免疫:
                    case 增强:
                        return;
                }
                break;
            }
            case 2221003:// - 冰凤球 - 冻结多数怪物,给予持续伤害.被冰凤球抓住的人火系抗性降低.
            case 2211006: { //- 冰雷合击 - [最高等级:30]\n混合冰和雷属性的魔法攻击一个怪物。一定的几率怪物被冻结。
                switch (stats.getEffectiveness(Element.ICE)) {//冰
                    case 免疫:
                    case 增强:
                        return;
                }
                break;
            }
            case 4120005://- 标飞武器用毒液 - 在飞镖上涂抹毒药攻击敌人使它一定几率中毒受持续伤害.最多可重复3次,敌人的HP不会掉落1以下.
            case 4220005://- 刀飞武器用毒液 - 在短剑上涂抹毒药攻击敌人,使它一定的几率陷入中毒状态受持续伤害.最多可重复3次,敌人的HP不会掉到1以下.
            case 14110004: {//14110004 - 夜行者武器用毒液 - [最高等级：20]\n在标枪上涂毒，进行攻击。有一定概率使敌人中毒，造成伤害。最多重复3次，敌人的HP不会掉落到1以下。
                switch (stats.getEffectiveness(Element.POISON)) {
                    //case 虚弱:
                    case 免疫:
                    case 增强:
                        return;
                }
                break;
            }
        }
        if (duration >= 2000000000) {//持续时间
            duration = 5000; //teleport master
        }
        final MonsterStatus stat = status.getStati();
        if (stats.isNoDoom() && stat == MonsterStatus.巫毒) {//巫毒
            return;
        }

        if (stats.isBoss()) {
            if (stat == MonsterStatus.眩晕 || stat == MonsterStatus.速度) {
                return;
            }
            if (checkboss && stat != (MonsterStatus.忍者伏击) && stat != (MonsterStatus.物攻) && stat != (MonsterStatus.中毒) && stat != (MonsterStatus.烈焰喷射) && stat != (MonsterStatus.恐慌) && stat != (MonsterStatus.魔击无效)) {
                return;
            }
        }
        if (stats.isFriendly() || isFake()) {
            if (stat == MonsterStatus.眩晕 || stat == MonsterStatus.速度 || stat == MonsterStatus.中毒 || stat == MonsterStatus.烈焰喷射) {
                return;
            }
        }
        if ((stat == MonsterStatus.烈焰喷射 || stat == MonsterStatus.中毒) && effect == null) {
            return;
        }
        if (stati.containsKey(stat)) {
            cancelStatus(stat);
        }
        if (stat == MonsterStatus.中毒 || stat == MonsterStatus.烈焰喷射) {
            poisonsLock.readLock().lock();
            try {
                for (MonsterStatusEffect mse : poisons) {
                    if (mse != null && (mse.getSkill() == effect.getSourceId()) || mse.getSkill() == GameConstants.getLinkedAttackSkill(effect.getSourceId()) || GameConstants.getLinkedAttackSkill(mse.getSkill()) == effect.getSourceId()) {
                        return;
                    }
                }
            } finally {
                poisonsLock.readLock().unlock();
            }
        }
        if (poison && getHp() > 1 && effect != null) {
            duration = Math.max(duration, effect.getDOTTime() * 1000);
        }
        duration += from.getStat().dotTime * 1000;
        long aniTime = duration;
        if (skilz != null) {
            aniTime += skilz.getAnimationTime();
        }
        status.setCancelTask(aniTime);

        if (poison && getHp() > 1) {
            status.setValue(status.getStati(), Integer.valueOf((int) ((effect.getDOT() + from.getStat().dot + from.getStat().getDamageIncrease(effect.getSourceId())) * from.getStat().getCurrentMaxBaseDamage() / 100.0)));
            int dam = Integer.valueOf((int) (aniTime / 1000 * status.getX() / 2));
            status.setPoisonSchedule(dam, from);
            if (dam > 0) {
                if (dam >= hp) {
                    dam = (int) (hp - 1);
                }
                damage(from, dam, false);
            }
            // final int poisonDamage = (int) Math.min(Short.MAX_VALUE, (long) (getMobMaxHp() / (70.0 - from.getSkillLevel(status.getSkill())) + 0.999));
            // status.setValue(MonsterStatus.中毒, Integer.valueOf(poisonDamage));
            // status.setPoisonSchedule(timerManager.register(new PoisonTask(poisonDamage, from, status, cancelTask, false), 1000, 1000));
        } else if (venom) {
            int poisonLevel = 0;
            int matk = 0;

            switch (from.getJob()) {
                case 412:
                    poisonLevel = from.getSkillLevel(SkillFactory.getSkill(4120005));
                    if (poisonLevel <= 0) {
                        return;
                    }
                    matk = SkillFactory.getSkill(4120005).getEffect(poisonLevel).getMatk();
                    break;
                case 422:
                    poisonLevel = from.getSkillLevel(SkillFactory.getSkill(4220005));
                    if (poisonLevel <= 0) {
                        return;
                    }
                    matk = SkillFactory.getSkill(4220005).getEffect(poisonLevel).getMatk();
                    break;
                case 1411:
                case 1412:
                    poisonLevel = from.getSkillLevel(SkillFactory.getSkill(14110004));
                    if (poisonLevel <= 0) {
                        return;
                    }
                    matk = SkillFactory.getSkill(14110004).getEffect(poisonLevel).getMatk();
                    break;
                case 434:
                    poisonLevel = from.getSkillLevel(SkillFactory.getSkill(4340001));
                    if (poisonLevel <= 0) {
                        return;
                    }
                    matk = SkillFactory.getSkill(4340001).getEffect(poisonLevel).getMatk();
                    break;
                default:
                    return; // Hack, using venom without the job required
            }
            final int luk = from.getStat().getLuk();
            final int maxDmg = (int) Math.ceil(Math.min(Short.MAX_VALUE, 0.2 * luk * matk));
            final int minDmg = (int) Math.ceil(Math.min(Short.MAX_VALUE, 0.1 * luk * matk));
            int gap = maxDmg - minDmg;
            if (gap == 0) {
                gap = 1;
            }
            int poisonDamage = 0;
            for (int i = 0; i < getVenomMulti(); i++) {
                poisonDamage = poisonDamage + (Randomizer.nextInt(gap) + minDmg);
            }
            poisonDamage = Math.min(Short.MAX_VALUE, poisonDamage);
            status.setValue(MonsterStatus.中毒, Integer.valueOf(poisonDamage));
            status.setPoisonSchedule(timerManager.register(new PoisonTask(poisonDamage, from, status, cancelTask, false), 1000, 1000));

        } else if (statusSkill == 4111003 || statusSkill == 14111001) { // shadow web
            status.setPoisonSchedule(timerManager.schedule(new PoisonTask((int) (getMobMaxHp() / 50.0 + 0.999), from, status, cancelTask, true), 3500));

        } else if (statusSkill == 4121004 || statusSkill == 4221004) {
            final int damage = (from.getStat().getStr() + from.getStat().getLuk()) * 2 * (60 / 100);
            status.setPoisonSchedule(timerManager.register(new PoisonTask(damage, from, status, cancelTask, false), 1000, 1000));
        }

        stati.put(stat, status);
        map.broadcastMessage(MobPacket.applyMonsterStatus(getObjectId(), status), getPosition());
        if (getController() != null && !getController().isMapObjectVisible(this)) {
            getController().getClient().getSession().write(MobPacket.applyMonsterStatus(getObjectId(), status));
        }
        int aniTime = 0;
        if (skilz != null) {
            aniTime = skilz.getAnimationTime();
        }
        ScheduledFuture<?> schedule = timerManager.schedule(cancelTask, duration + aniTime);
        status.setCancelTask(schedule);
    }*/
    public final void dispelSkill(final MobSkill skillId) {
        List<MonsterStatus> toCancel = new ArrayList<MonsterStatus>();
        for (Entry<MonsterStatus, MonsterStatusEffect> effects : stati.entrySet()) {
            if (effects.getValue().getMobSkill() != null && effects.getValue().getMobSkill().getSkillId() == skillId.getSkillId()) { //not checking for level.
                toCancel.add(effects.getKey());
            }
        }
        for (MonsterStatus stat : toCancel) {
            cancelStatus(stat);
        }
    }

    public final void applyMonsterBuff(final Map<MonsterStatus, Integer> effect, final int skillId, final long duration, final MobSkill skill, final List<Integer> reflection) {
        MobTimer timerManager = MobTimer.getInstance();
        final Runnable cancelTask = new Runnable() {

            @Override
            public final void run() {
                if (reflection.size() > 0) {
                    MapleMonster.this.reflectpack = null;
                }
                if (isAlive()) {
                    for (MonsterStatus z : effect.keySet()) {
                        cancelStatus(z);
                    }
                }
            }
        };
        for (Entry<MonsterStatus, Integer> z : effect.entrySet()) {
            final MonsterStatusEffect effectz = new MonsterStatusEffect(z.getKey(), z.getValue(), 0, skill, true);
            stati.put(z.getKey(), effectz);
        }
        if (reflection.size() > 0) {
            this.reflectpack = MobPacket.applyMonsterStatus(getObjectId(), effect, reflection, skill);
            map.broadcastMessage(reflectpack, getPosition());
            if (getController() != null && !getController().isMapObjectVisible(this)) {
                getController().getClient().getSession().write(this.reflectpack);
            }
        } else {
            for (Entry<MonsterStatus, Integer> z : effect.entrySet()) {
                map.broadcastMessage(MobPacket.applyMonsterStatus(getObjectId(), z.getKey(), z.getValue(), skill), getPosition());
                if (getController() != null && !getController().isMapObjectVisible(this)) {
                    getController().getClient().getSession().write(MobPacket.applyMonsterStatus(getObjectId(), z.getKey(), z.getValue(), skill));
                }
            }
        }
        timerManager.schedule(cancelTask, duration);
    }

    public final void setTempEffectiveness(final Element e, final long milli) {
        stats.setEffectiveness(e, ElementalEffectiveness.虚弱);
        MobTimer.getInstance().schedule(new Runnable() {

            public void run() {
                stats.removeEffectiveness(e);
            }
        }, milli);
    }

    public final boolean isBuffed(final MonsterStatus status) {
        return stati.containsKey(status);
    }

    public final MonsterStatusEffect getBuff(final MonsterStatus status) {
        return stati.get(status);
    }

    public final void setFake(final boolean fake) {
        this.fake = fake;
    }

    public final boolean isFake() {
        return fake;
    }

    public final MapleMap getMap() {
        return map;
    }

    public final List<Pair<Integer, Integer>> getSkills() {
        return stats.getSkills();
    }

    public final boolean hasSkill(final int skillId, final int level) {
        return stats.hasSkill(skillId, level);
    }

    public final long getLastSkillUsed(final int skillId) {
        if (usedSkills.containsKey(skillId)) {
            return usedSkills.get(skillId);
        }
        return 0;
    }

    public final void setLastSkillUsed(final int skillId, final long now, final long cooltime) {
        switch (skillId) {
            case 140:
                usedSkills.put(skillId, now + (cooltime * 2));
                usedSkills.put(141, now);
                break;
            case 141:
                usedSkills.put(skillId, now + (cooltime * 2));
                usedSkills.put(140, now + cooltime);
                break;
            default:
                usedSkills.put(skillId, now + cooltime);
                break;
        }
    }

    public final byte getNoSkills() {
        return stats.getNoSkills();
    }

    public final boolean isFirstAttack() {
        return stats.isFirstAttack();
    }

    public final int getBuffToGive() {
        return stats.getBuffToGive();
    }

    private final class PoisonTask implements Runnable {

        private final int poisonDamage;
        private final MapleCharacter chr;
        private final MonsterStatusEffect status;
        private final Runnable cancelTask;
        private final boolean shadowWeb;
        private final MapleMap map;

        private PoisonTask(final int poisonDamage, final MapleCharacter chr, final MonsterStatusEffect status, final Runnable cancelTask, final boolean shadowWeb) {
            this.poisonDamage = poisonDamage;
            this.chr = chr;
            this.status = status;
            this.cancelTask = cancelTask;
            this.shadowWeb = shadowWeb;
            this.map = chr.getMap();
        }

        @Override
        public void run() {
            long damage = poisonDamage;
            if (damage >= hp) {
                damage = hp - 1;
                if (!shadowWeb) {
                    cancelTask.run();
                    status.cancelTask();
                }
            }
            if (hp > 1 && damage > 0) {
                damage(chr, damage, false);
                if (shadowWeb) {
                    map.broadcastMessage(MobPacket.damageMonster(getObjectId(), damage), getPosition());
                }
            }
        }
    }

    private static class AttackingMapleCharacter {

        private MapleCharacter attacker;
        private long lastAttackTime;

        public AttackingMapleCharacter(final MapleCharacter attacker, final long lastAttackTime) {
            super();
            this.attacker = attacker;
            this.lastAttackTime = lastAttackTime;
        }

        public final long getLastAttackTime() {
            return lastAttackTime;
        }

        public final void setLastAttackTime(final long lastAttackTime) {
            this.lastAttackTime = lastAttackTime;
        }

        public final MapleCharacter getAttacker() {
            return attacker;
        }
    }

    private interface AttackerEntry {

        List<AttackingMapleCharacter> getAttackers();

        public void addDamage(MapleCharacter from, long damage, boolean updateAttackTime);

        public long getDamage();

        public boolean contains(MapleCharacter chr);

        public void killedMob(MapleMap map, int baseExp, boolean mostDamage, int lastSkill);
    }

    private final class SingleAttackerEntry implements AttackerEntry {

        private long damage = 0;
        private int chrid;
        private long lastAttackTime;
        private int channel;

        public SingleAttackerEntry(final MapleCharacter from, final int cserv) {
            this.chrid = from.getId();
            this.channel = cserv;
        }

        @Override
        public void addDamage(final MapleCharacter from, final long damage, final boolean updateAttackTime) {
            if (chrid == from.getId()) {
                this.damage += damage;
                if (updateAttackTime) {
                    lastAttackTime = System.currentTimeMillis();
                }
            }
        }

        @Override
        public final List<AttackingMapleCharacter> getAttackers() {
            final MapleCharacter chr = map.getCharacterById(chrid);
            if (chr != null) {
                return Collections.singletonList(new AttackingMapleCharacter(chr, lastAttackTime));
            } else {
                return Collections.emptyList();
            }
        }

        @Override
        public boolean contains(final MapleCharacter chr) {
            return chrid == chr.getId();
        }

        @Override
        public long getDamage() {
            return damage;
        }

        @Override
        public void killedMob(final MapleMap map, final int baseExp, final boolean mostDamage, final int lastSkill) {
            final MapleCharacter chr = map.getCharacterById(chrid);
            if (chr != null && chr.isAlive()) {
                giveExpToCharacter(chr, baseExp, mostDamage, 1, (byte) 0, (byte) 0, (byte) 0, lastSkill);
            }
        }

        @Override
        public int hashCode() {
            return chrid;
        }

        @Override
        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final SingleAttackerEntry other = (SingleAttackerEntry) obj;
            return chrid == other.chrid;
        }
    }

    private static final class ExpMap {

        public final int exp;
        public final byte ptysize;
        public final byte Class_Bonus_EXP;
        public final byte Premium_Bonus_EXP;

        public ExpMap(final int exp, final byte ptysize, final byte Class_Bonus_EXP, final byte Premium_Bonus_EXP) {
            super();
            this.exp = exp;
            this.ptysize = ptysize;
            this.Class_Bonus_EXP = Class_Bonus_EXP;
            this.Premium_Bonus_EXP = Premium_Bonus_EXP;
        }
    }

    private static final class OnePartyAttacker {

        public MapleParty lastKnownParty;
        public long damage;
        public long lastAttackTime;

        public OnePartyAttacker(final MapleParty lastKnownParty, final long damage) {
            super();
            this.lastKnownParty = lastKnownParty;
            this.damage = damage;
            this.lastAttackTime = System.currentTimeMillis();
        }
    }

    private class PartyAttackerEntry implements AttackerEntry {

        private long totDamage;
        private final Map<Integer, OnePartyAttacker> attackers = new HashMap<Integer, OnePartyAttacker>(6);
        private int partyid;
        private int channel;

        public PartyAttackerEntry(final int partyid, final int cserv) {
            this.partyid = partyid;
            this.channel = cserv;
        }

        public List<AttackingMapleCharacter> getAttackers() {
            final List<AttackingMapleCharacter> ret = new ArrayList<AttackingMapleCharacter>(attackers.size());
            for (final Entry<Integer, OnePartyAttacker> entry : attackers.entrySet()) {
                final MapleCharacter chr = map.getCharacterById(entry.getKey());
                if (chr != null) {
                    ret.add(new AttackingMapleCharacter(chr, entry.getValue().lastAttackTime));
                }
            }
            return ret;
        }

        private final Map<MapleCharacter, OnePartyAttacker> resolveAttackers() {
            final Map<MapleCharacter, OnePartyAttacker> ret = new HashMap<MapleCharacter, OnePartyAttacker>(attackers.size());
            for (final Entry<Integer, OnePartyAttacker> aentry : attackers.entrySet()) {
                final MapleCharacter chr = map.getCharacterById(aentry.getKey());
                if (chr != null) {
                    ret.put(chr, aentry.getValue());
                }
            }
            return ret;
        }

        @Override
        public final boolean contains(final MapleCharacter chr) {
            return attackers.containsKey(chr.getId());
        }

        @Override
        public final long getDamage() {
            return totDamage;
        }

        public void addDamage(final MapleCharacter from, final long damage, final boolean updateAttackTime) {
            final OnePartyAttacker oldPartyAttacker = attackers.get(from.getId());
            if (oldPartyAttacker != null) {
                oldPartyAttacker.damage += damage;
                oldPartyAttacker.lastKnownParty = from.getParty();
                if (updateAttackTime) {
                    oldPartyAttacker.lastAttackTime = System.currentTimeMillis();
                }
            } else {
                // TODO actually this causes wrong behaviour when the party changes between attacks
                // only the last setup will get exp - but otherwise we'd have to store the full party
                // constellation for every attack/everytime it changes, might be wanted/needed in the
                // future but not now
                final OnePartyAttacker onePartyAttacker = new OnePartyAttacker(from.getParty(), damage);
                attackers.put(from.getId(), onePartyAttacker);
                if (!updateAttackTime) {
                    onePartyAttacker.lastAttackTime = 0;
                }
            }
            totDamage += damage;
        }

        @Override
        public final void killedMob(final MapleMap map, final int baseExp, final boolean mostDamage, final int lastSkill) {
            MapleCharacter pchr, highest = null;
            long iDamage, highestDamage = 0;
            int iexp = 0;
            MapleParty party;
            double averagePartyLevel, expWeight, levelMod, innerBaseExp, expFraction;
            List<MapleCharacter> expApplicable;
            final Map<MapleCharacter, ExpMap> expMap = new HashMap<MapleCharacter, ExpMap>(6);
            byte Class_Bonus_EXP;
            byte Premium_Bonus_EXP;
            byte added_partyinc = 0;

            for (final Entry<MapleCharacter, OnePartyAttacker> attacker : resolveAttackers().entrySet()) {
                party = attacker.getValue().lastKnownParty;
                averagePartyLevel = 0;

                Class_Bonus_EXP = 0;
                Premium_Bonus_EXP = 0;
                expApplicable = new ArrayList<MapleCharacter>();
                for (final MaplePartyCharacter partychar : party.getMembers()) {
                    if (attacker.getKey().getLevel() - partychar.getLevel() <= 5 || stats.getLevel() - partychar.getLevel() <= 5) {
                        pchr = map.getCharacterById(partychar.getId());
                        if (pchr != null) {
                            if (pchr.isAlive() && pchr.getMap() == map) {
                                expApplicable.add(pchr);
                                averagePartyLevel += pchr.getLevel();

                                if (Class_Bonus_EXP == 0) {
                                    Class_Bonus_EXP = ServerConstants.Class_Bonus_EXP(pchr.getJob());
                                }
                                if (pchr.getStat().equippedWelcomeBackRing && Premium_Bonus_EXP == 0) {
                                    Premium_Bonus_EXP = 80;
                                }
                                if (pchr.getStat().hasPartyBonus && added_partyinc < 4) {
                                    added_partyinc++;
                                }
                            }
                        }
                    }
                }
                /*
                 * if (expApplicable.size() > 1) { averagePartyLevel /=
                 * expApplicable.size(); } else { Class_Bonus_EXP = 0; //no
                 * class bonus if not in a party. }
                 */

                iDamage = attacker.getValue().damage;
                if (iDamage > highestDamage) {
                    highest = attacker.getKey();
                    highestDamage = iDamage;
                }
                innerBaseExp = baseExp * ((double) iDamage / totDamage);
                //  expFraction = innerBaseExp / (expApplicable.size() + 1);
                double expBonus = 1.0;
                if (expApplicable.size() > 1) {
                    expBonus = 1.10 + 0.05 * expApplicable.size();
                    averagePartyLevel /= expApplicable.size();
                }
                expFraction = (innerBaseExp * expBonus) / (expApplicable.size() + 1);

                for (MapleCharacter expReceiver : expApplicable) {
                    Integer oexp = expMap.get(expReceiver) == null ? 0 : expMap.get(expReceiver).exp;
                    if (oexp == null) {
                        iexp = 0;
                    } else {
                        iexp = oexp.intValue();
                    }
                    expWeight = (expReceiver == attacker.getKey() ? 2.0 : 1.0);
                    levelMod = expReceiver.getLevel() / averagePartyLevel;
                    if (levelMod > 1.0 || this.attackers.containsKey(expReceiver.getId())) {
                        levelMod = 1.0;
                    }
                    iexp += (int) Math.round(expFraction * expWeight * levelMod);
                    expMap.put(expReceiver, new ExpMap(iexp, (byte) (expApplicable.size() + added_partyinc), Class_Bonus_EXP, Premium_Bonus_EXP));

                }
                /*
                 * for (final MapleCharacter expReceiver : expApplicable) { iexp
                 * = expMap.get(expReceiver) == null ? 0 :
                 * expMap.get(expReceiver).exp; expWeight = (expReceiver ==
                 * attacker.getKey() ? 2.0 : 0.3); //hopefully this is correct
                 * o.o -/+0.4 levelMod = expReceiver.getLevel() /
                 * averagePartyLevel * 0.4D; // levelMod =
                 * expReceiver.getLevel() / averagePartyLevel; // if (levelMod >
                 * 1.0 || attackers.containsKey(expReceiver.getId())) { ///
                 * levelMod = 1.0; // } iexp += (int)
                 * Math.round(((((MapleCharacter) attacker.getKey()).getId() ==
                 * expReceiver.getId() ? 0.6D : 0.0D) + levelMod) *
                 * innerBaseExp); //iexp += (int) Math.round(expWeight *
                 * innerBaseExp); expMap.put(expReceiver, new ExpMap(iexp,
                 * (byte) (expApplicable.size() + added_partyinc),
                 * Class_Bonus_EXP, Premium_Bonus_EXP)); }
                 */
            }
            ExpMap expmap;
            for (final Entry<MapleCharacter, ExpMap> expReceiver : expMap.entrySet()) {
                expmap = expReceiver.getValue();
                giveExpToCharacter(expReceiver.getKey(), expmap.exp, mostDamage ? expReceiver.getKey() == highest : false, expMap.size(), expmap.ptysize, expmap.Class_Bonus_EXP, expmap.Premium_Bonus_EXP, lastSkill);
            }
        }

        @Override
        public final int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + partyid;
            return result;
        }

        @Override
        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final PartyAttackerEntry other = (PartyAttackerEntry) obj;
            if (partyid != other.partyid) {
                return false;
            }
            return true;
        }
    }

    public int getLinkOid() {
        return linkoid;
    }

    public void setLinkOid(int lo) {
        this.linkoid = lo;
    }

    public final Map<MonsterStatus, MonsterStatusEffect> getStati() {
        return stati;
    }

    public void addEmpty() {
        stati.put(MonsterStatus.模糊领域, new MonsterStatusEffect(MonsterStatus.模糊领域, 0, 0, null, false));
        stati.put(MonsterStatus.SUMMON, new MonsterStatusEffect(MonsterStatus.SUMMON, 0, 0, null, false));
    }

    public final int getStolen() {
        return stolen;
    }

    public final void setStolen(final int s) {
        this.stolen = s;
    }

    public final void handleSteal(MapleCharacter chr) {
        double showdown = 100.0;
        final MonsterStatusEffect mse = getBuff(MonsterStatus.挑衅);
        if (mse != null) {
            showdown += mse.getX();
        }

        ISkill steal = SkillFactory.getSkill(4201004);
        final int level = chr.getSkillLevel(steal), chServerrate = ChannelServer.getInstance(chr.getClient().getChannel()).getDropRate();
        if (level > 0 && !getStats().isBoss() && stolen == -1 && steal.getEffect(level).makeChanceResult()) {
            final MapleMonsterInformationProvider mi = MapleMonsterInformationProvider.getInstance();
            final List<MonsterDropEntry> de = mi.retrieveDrop(getId());
            if (de == null) {
                stolen = 0;
                return;
            }
            final List<MonsterDropEntry> dropEntry = new ArrayList<>(de);
            Collections.shuffle(dropEntry);
            IItem idrop;
            for (MonsterDropEntry d : dropEntry) {
                if (d.itemId > 0 && d.questid == 0 && d.itemId / 10000 != 238 && Randomizer.nextInt(999999) < (int) (10 * d.chance * chServerrate * chr.getDropMod() * (chr.getStat().dropBuff / 100.0) * (showdown / 100.0))) { //kinda op
                    if (GameConstants.getInventoryType(d.itemId) == MapleInventoryType.EQUIP) {
                        Equip eq = (Equip) MapleItemInformationProvider.getInstance().getEquipById(d.itemId);
                        idrop = MapleItemInformationProvider.getInstance().randomizeStats(eq);
                    } else {
                        idrop = new Item(d.itemId, (byte) 0, (short) (d.Maximum != 1 ? Randomizer.nextInt(d.Maximum - d.Minimum) + d.Minimum : 1), (byte) 0);
                    }
                    stolen = d.itemId;
                    map.spawnMobDrop(idrop, map.calcDropPos(getPosition(), getTruePosition()), this, chr, (byte) 0, (short) 0);
                    break;
                }
            }
        } else {
            stolen = 0; //failed once, may not go again
        }
    }

    public final void setLastNode(final int lastNode) {
        this.lastNode = lastNode;
    }

    public final int getLastNode() {
        return lastNode;
    }

    public final void setLastNodeController(final int lastNode) {
        this.lastNodeController = lastNode;
    }

    public final int getLastNodeController() {
        return lastNodeController;
    }

    public final void cancelStatus(final MonsterStatus stat) {
        final MonsterStatusEffect mse = stati.get(stat);
        if (mse == null || !isAlive()) {
            return;
        }
        mse.cancelPoisonSchedule();
        map.broadcastMessage(MobPacket.cancelMonsterStatus(getObjectId(), stat), getPosition());
        if (getController() != null && !getController().isMapObjectVisible(MapleMonster.this)) {
            getController().getClient().getSession().write(MobPacket.cancelMonsterStatus(getObjectId(), stat));
        }
        stati.remove(stat);
        setVenomMulti((byte) 0);
    }

    public final void cancelDropItem() {
        if (dropItemSchedule != null) {
            dropItemSchedule.cancel(false);
            dropItemSchedule = null;
        }
    }

    public final void startDropItemSchedule() {
        cancelDropItem();
        if (stats.getDropItemPeriod() <= 0 || !isAlive()) {
            return;
        }
        final int itemId;
        switch (getId()) {
            case 9300061:
                itemId = 4001101;
                break;
            case 9300102:
                itemId = 4031507;
                break;
            default: //until we find out ... what other mobs use this and how to get the ITEMID
                return;
        }
        shouldDropItem = false;
        dropItemSchedule = MobTimer.getInstance().register(new Runnable() {

            public void run() {
                if (isAlive() && map != null) {
                    if (shouldDropItem) {
                        map.spawnAutoDrop(itemId, getPosition());
                    } else {
                        shouldDropItem = true;
                    }
                }
            }
        }, stats.getDropItemPeriod() * 1000);
    }

    public MaplePacket getNodePacket() {
        return nodepack;
    }

    public void setNodePacket(final MaplePacket np) {
        this.nodepack = np;
    }

    public final void killed() {
        if (listener != null) {
            listener.monsterKilled();
        }
        listener = null;
    }

    public final void changeLevel(final int newLevel, boolean pqMob) {
        this.ostats = new ChangeableStats(stats, newLevel, pqMob);
        this.hp = ostats.getHp();
        this.mp = ostats.getMp();
    }
}
