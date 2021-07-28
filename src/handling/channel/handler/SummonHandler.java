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
import java.util.ArrayList;
import java.util.Iterator;

import client.ISkill;
import client.MapleBuffStat;
import client.MapleClient;
import client.MapleCharacter;
import client.SkillFactory;
import client.SummonSkillEntry;
import client.status.MonsterStatusEffect;
import client.anticheat.CheatingOffense;
import client.status.MonsterStatus;
import java.lang.ref.WeakReference;
import java.util.Map;
import server.MapleStatEffect;
import server.AutobanManager;
import server.Timer.CloneTimer;
import server.movement.LifeMovementFragment;
import server.life.MapleMonster;
import server.life.SummonAttackEntry;
import server.maps.MapleMap;
import server.maps.MapleSummon;
import server.maps.MapleMapObject;
import server.maps.MapleMapObjectType;
import server.maps.SummonMovementType;
import tools.MaplePacketCreator;
import tools.packet.MobPacket;
import tools.data.input.SeekableLittleEndianAccessor;

public class SummonHandler {

    public static final void MoveDragon(final SeekableLittleEndianAccessor slea, final MapleCharacter chr) {
        slea.skip(8); //POS
        final List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 5);
        if (chr != null && chr.getDragon() != null) {
            final Point pos = chr.getDragon().getPosition();
            MovementParse.updatePosition(res, chr.getDragon(), 0);
            if (!chr.isHidden()) {
                chr.getMap().broadcastMessage(chr, MaplePacketCreator.moveDragon(chr.getDragon(), pos, res), chr.getPosition());
            }

            WeakReference<MapleCharacter>[] clones = chr.getClones();
            for (int i = 0; i < clones.length; i++) {
                if (clones[i].get() != null) {
                    final MapleMap map = chr.getMap();
                    final MapleCharacter clone = clones[i].get();
                    final List<LifeMovementFragment> res3 = new ArrayList<LifeMovementFragment>(res);
                    CloneTimer.getInstance().schedule(new Runnable() {

                        public void run() {
                            try {
                                if (clone.getMap() == map && clone.getDragon() != null) {
                                    final Point startPos = clone.getDragon().getPosition();
                                    MovementParse.updatePosition(res3, clone.getDragon(), 0);

                                    if (!clone.isHidden()) {
                                        map.broadcastMessage(clone, MaplePacketCreator.moveDragon(clone.getDragon(), startPos, res3), clone.getPosition());
                                    }

                                }
                            } catch (Exception e) {
                                //very rarely swallowed
                            }
                        }
                    }, 500 * i + 500);
                }
            }
        }
    }

    public static final void MoveSummon(final SeekableLittleEndianAccessor slea, final MapleCharacter chr) {
        final int oid = slea.readInt();
        Point startPos = new Point(slea.readShort(), slea.readShort());
        List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 4);
//        slea.skip(4); //startPOS
//        final List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 4);
        if (chr == null) {
            return;
        }
        for (MapleSummon sum : chr.getSummons().values()) {
            if (sum.getObjectId() == oid && sum.getMovementType() != SummonMovementType.STATIONARY) {
                final Point pos = sum.getPosition();
                MovementParse.updatePosition(res, sum, 0);
                if (!sum.isChangedMap()) {
                    chr.getMap().broadcastMessage(chr, MaplePacketCreator.moveSummon(chr.getId(), oid, startPos, res), sum.getPosition());
                }
                break;
            }
        }
    }

    public static final void DamageSummon(final SeekableLittleEndianAccessor slea, final MapleCharacter chr) {
      
        int skillid = slea.readInt();
         int unkByte = slea.readByte();
         int damage = slea.readInt();
         int monsterIdFrom = slea.readInt();
        //       slea.readByte(); // stance


        final Iterator<MapleSummon> iter = chr.getSummons().values().iterator();
        if (SkillFactory.getSkill(skillid) != null) {
            MapleSummon summon;
            while (iter.hasNext()) {
                summon = iter.next();
                if (summon.isPuppet() && summon.getOwnerId() == chr.getId()) { //We can only have one puppet(AFAIK O.O) so this check is safe.
                    summon.addHP((short) -damage);
                    if (summon.getHP() <= 0) {
                        chr.cancelEffectFromBuffStat(MapleBuffStat.PUPPET);
                    }
                    chr.getMap().broadcastMessage(chr, MaplePacketCreator.damageSummon(chr.getId(), skillid, damage, unkByte, monsterIdFrom), summon.getPosition());
                    break;
                }
            }
        }
    }

    public static void SummonAttack(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null || !chr.isAlive()) {
            return;
        }
        final MapleMap map = chr.getMap();
        final MapleMapObject obj = map.getMapObject(slea.readInt(), MapleMapObjectType.SUMMON);
        if (obj == null) {
            return;
        }
        final MapleSummon summon = (MapleSummon) obj;
        if (summon.getOwnerId() != chr.getId() || summon.getSkillLevel() <= 0) {
            return;
        }
        final SummonSkillEntry sse = SkillFactory.getSummonData(summon.getSkill());
        if (sse == null) {
            return;
        }
        slea.skip(8);//1
        int tick = slea.readInt();
        chr.updateTick(tick);
        summon.CheckSummonAttackFrequency(chr, tick);
        slea.skip(8);//2
        final byte animation = slea.readByte();
        slea.skip(8);//3
        final byte numAttacked = slea.readByte();
        if (numAttacked > sse.mobCount) {
            chr.getCheatTracker().registerOffense(CheatingOffense.召唤兽攻击怪物数量异常);
            //AutobanManager.getInstance().autoban(c, "Attacking more monster that summon can do (Skillid : "+summon.getSkill()+" Count : " + numAttacked + ", allowed : " + sse.mobCount + ")");
            return;
        }
        //slea.skip(8); //some pos stuff
        final List<SummonAttackEntry> allDamage = new ArrayList<SummonAttackEntry>();
        chr.getCheatTracker().checkSummonAttack();

        for (int i = 0; i < numAttacked; i++) {
            final MapleMonster mob = map.getMonsterByOid(slea.readInt());

            if (mob == null) {
                continue;
            }
            if (chr.getPosition().distanceSq(mob.getPosition()) > 400000.0) {
                chr.getCheatTracker().registerOffense(CheatingOffense.召唤兽攻击范围过大);
            }
            slea.skip(14); // who knows
            final int damage = slea.readInt();
            allDamage.add(new SummonAttackEntry(mob, damage));
        }
        if (!summon.isChangedMap()) {
        //    map.broadcastMessage(chr, MaplePacketCreator.summonAttack(summon.getOwnerId(), summon.getObjectId(), animation, allDamage), summon.getPosition());
        }
        final ISkill summonSkill = SkillFactory.getSkill(summon.getSkill());
        final MapleStatEffect summonEffect = summonSkill.getEffect(summon.getSkillLevel());

        if (summonEffect == null) {
            return;
        }
        for (SummonAttackEntry attackEntry : allDamage) {
            final int toDamage = attackEntry.getDamage();
            final MapleMonster mob = attackEntry.getMonster();

            if (toDamage > 0 && summonEffect.getMonsterStati().size() > 0) {
                if (summonEffect.makeChanceResult()) {
                    for (Map.Entry<MonsterStatus, Integer> z : summonEffect.getMonsterStati().entrySet()) {
                        mob.applyStatus(chr, new MonsterStatusEffect(z.getKey(), z.getValue(), summonSkill.getId(), null, false), summonEffect.isPoison(), 4000, false);
                    }
                }
            }
            if (chr.isGM() || toDamage < 120000) {
                mob.damage(chr, toDamage, true);
                chr.checkMonsterAggro(mob);
                if (!mob.isAlive()) {
                    chr.getClient().getSession().write(MobPacket.killMonster(mob.getObjectId(), 1));
                }
            } else {
            //    AutobanManager.getInstance().autoban(c, "High Summon Damage (" + toDamage + " to " + attackEntry.getMonster().getId() + ")");
                // TODO : Check player's stat for damage checking.
            }
        }
        if (summon.isGaviota()) {
            chr.getMap().broadcastMessage(MaplePacketCreator.removeSummon(summon, true));
            chr.getMap().removeMapObject(summon);
            chr.removeVisibleMapObject(summon);
            chr.cancelEffectFromBuffStat(MapleBuffStat.SUMMON);
          //  chr.cancelEffectFromBuffStat(MapleBuffStat.REAPER);
            //TODO: Multi Summoning, must do something about hack buffstat
        }
    }
}
