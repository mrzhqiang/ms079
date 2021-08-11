package KinMS.PvP;

import client.ISkill;
import client.MapleBuffStat;
import client.MapleCharacter;
import client.MapleClient;
import client.PlayerStats;
import client.Skill;
import client.SkillFactory;
import handling.channel.ChannelServer;
import handling.channel.handler.AttackInfo;
import java.awt.Point;
import java.awt.Rectangle;
import server.MapleStatEffect;
import server.Randomizer;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.maps.MapleMap;
import tools.MaplePacketCreator;

public class MaplePvp {

    private static PvpAttackInfo parsePvpAttack(AttackInfo attack, MapleCharacter player, MapleStatEffect effect) {
        PvpAttackInfo ret = new PvpAttackInfo();
        double maxdamage = player.getLevel() + 100.0D;
        int skillId = attack.skill;
        ret.skillId = skillId;
        ret.critRate = 5;
        ret.ignoreDef = 0;
        ret.skillDamage = 100;
        ret.mobCount = 1;
        ret.attackCount = 1;
        int pvpRange = attack.isCloseRangeAttack ? 35 : 70;
        ret.facingLeft = (attack.animation < 0);
        System.out.println("PVP伤害检查-A");
        if ((skillId != 0) && (effect != null)) {
            System.out.println("PVP伤害检查-C");
            ret.skillDamage = (effect.getDamage());
            ret.mobCount = Math.max(1, effect.getMobCount());
            ret.attackCount = Math.max(effect.getBulletCount(), effect.getAttackCount());
            ret.box = effect.calculateBoundingBox(player.getTruePosition(), ret.facingLeft, pvpRange);
            System.out.println("PVP伤害检查-D");
        } else {
            System.out.println("PVP伤害检查-E");
            ret.box = calculateBoundingBox(player.getTruePosition(), ret.facingLeft, pvpRange);
            System.out.println("PVP伤害检查-F");
        }
        System.out.println("PVP伤害检查-G");
        boolean mirror = (player.getBuffedValue(MapleBuffStat.SHADOWPARTNER) != null);
        ret.attackCount *= (mirror ? 2 : 1);
        maxdamage *= ret.skillDamage / 100.0D;
        ret.maxDamage = (maxdamage * ret.attackCount);
        System.out.println("PVP伤害检查-H");
        if (player.isGM()) {
            player.dropMessage(6, "Pvp伤害解析 - 最大攻击: " + maxdamage + " 数量: " + ret.mobCount + " 次数: " + ret.attackCount + " 爆击: " + ret.critRate + " 无视: " + ret.ignoreDef + " 技能伤害: " + ret.skillDamage);
        }
        return ret;
    }

    private static Rectangle calculateBoundingBox(Point posFrom, boolean facingLeft, int range) {
        Point lt = new Point(-70, -30);
        Point rb = new Point(-10, 0);
        Point myrb;
        Point mylt;
        if (facingLeft) {
            mylt = new Point(lt.x + posFrom.x - range, lt.y + posFrom.y);
            myrb = new Point(rb.x + posFrom.x, rb.y + posFrom.y);
        } else {
            myrb = new Point(lt.x * -1 + posFrom.x + range, rb.y + posFrom.y);
            mylt = new Point(rb.x * -1 + posFrom.x, lt.y + posFrom.y);
        }
        return new Rectangle(mylt.x, mylt.y, myrb.x - mylt.x, myrb.y - mylt.y);
    }

    public static boolean inArea(MapleCharacter chr) {
        for (Rectangle rect : chr.getMap().getAreas()) {
            if (rect.contains(chr.getTruePosition())) {
                return true;
            }
        }
        return false;
    }

    private static void monsterBomb(MapleCharacter player, MapleCharacter attacked, MapleMap map, PvpAttackInfo attack) {
        System.out.println("PVP伤害检查-L");
        if ((player == null) || (attacked == null) || (map == null)) {
            return;
        }
        System.out.println("PVP伤害检查-M");
        double maxDamage = attack.maxDamage;
        boolean isCritDamage = false;

        if (player.getLevel() > attacked.getLevel() + 10) {
            maxDamage *= 1.05D;
        } else if (player.getLevel() < attacked.getLevel() - 10) {
            maxDamage /= 1.05D;
        } else if (player.getLevel() > attacked.getLevel() + 20) {
            maxDamage *= 1.1D;
        } else if (player.getLevel() < attacked.getLevel() - 20) {
            maxDamage /= 1.1D;
        } else if (player.getLevel() > attacked.getLevel() + 30) {
            maxDamage *= 1.15D;
        } else if (player.getLevel() < attacked.getLevel() - 30) {
            maxDamage /= 1.15D;
        }

        System.out.println("PVP伤害检查-N");
        if (Randomizer.nextInt(100) < attack.critRate) {
            maxDamage *= 1.5D;
            isCritDamage = true;
        }
        System.out.println("PVP伤害检查-O");
        int attackedDamage = (int) Math.floor(Math.random() * ((int) maxDamage * 0.35D) + (int) maxDamage * 0.65D);
        int MAX_PVP_DAMAGE = (int) (player.getStat().getLimitBreak(player) / 100.0D);
        int MIN_PVP_DAMAGE = 100;
        if (attackedDamage > MAX_PVP_DAMAGE) {
            attackedDamage = MAX_PVP_DAMAGE;
        }
        if (attackedDamage < MIN_PVP_DAMAGE) {
            attackedDamage = MIN_PVP_DAMAGE;
        }
        System.out.println("PVP伤害检查-P");
        int hploss = attackedDamage;
        int mploss = 0;
        System.out.println("PVP伤害检查-Q:" + attackedDamage);
        if (attackedDamage > 0) {
            System.out.println("PVP伤害检查-R");
            if (attacked.getBuffedValue(MapleBuffStat.MAGIC_GUARD) != null) {
                System.out.println("PVP伤害检查-S");
                mploss = (int) (attackedDamage * (attacked.getBuffedValue(MapleBuffStat.MAGIC_GUARD).doubleValue() / 100.0D));
                hploss -= mploss;
                if (attacked.getBuffedValue(MapleBuffStat.INFINITY) != null) {
                    mploss = 0;
                } else if (mploss > attacked.getStat().getMp()) {
                    mploss = attacked.getStat().getMp();
                    hploss -= mploss;
                }
                attacked.addMPHP(-hploss, -mploss);
                System.out.println("PVP伤害检查-T");
            } else {
                System.out.println("PVP伤害检查-U");
                attacked.addHP(-hploss);
                System.out.println("PVP伤害检查-V");
            }
        }
        System.out.println("PVP伤害检查-W");
        MapleMonster pvpMob = MapleLifeFactory.getMonster(9400711);
        map.spawnMonsterOnGroundBelow(pvpMob, attacked.getPosition());
        map.broadcastMessage(MaplePacketCreator.PVPdamagePlayer(attacked.getId(), 2, pvpMob.getId(), hploss));
        System.out.println("PVP伤害检查-X");
        if (isCritDamage) {
            player.dropMessage(6, "你对玩家 " + attacked.getName() + " 造成了 " + hploss + " 点爆击伤害! 对方血量: " + attacked.getStat().getHp() + "/" + attacked.getStat().getCurrentMaxHp());
            attacked.dropMessage(6, "玩家 " + player.getName() + " 对你造成了 " + hploss + " 点爆击伤害!");
        } else {
            player.dropTopMsg("你对玩家 " + attacked.getName() + " 造成了 " + hploss + " 点伤害! 对方血量: " + attacked.getStat().getHp() + "/" + attacked.getStat().getCurrentMaxHp());
            attacked.dropTopMsg("玩家 " + player.getName() + " 对你造成了 " + hploss + " 点伤害!");
        }
        System.out.println("PVP伤害检查-Y");
        map.killMonster(pvpMob, player, false, false, (byte) 1);

        System.out.println("PVP伤害检查-Z");
        if ((attacked.getStat().getHp() <= 0) && (!attacked.isAlive())) {
            int expReward = attacked.getLevel() * 10 * (attacked.getLevel() / player.getLevel());
            int gpReward = (int) Math.floor(Math.random() * 10.0D + 10.0D);
            //    if (player.getPvpKills() * 0.25D >= player.getPvpDeaths()) {
            //        expReward *= 2;
            //    }
            player.gainExp(expReward, true, false, true);
            //    player.gainPvpKill();
            player.dropMessage(6, "你击败了玩家 " + attacked.getName() + "!! ");
            //  int pvpVictory = attacked.getPvpVictory();
            //  attacked.gainPvpDeath();
            attacked.dropMessage(6, player.getName() + " 将你击败!");
            //  byte[] packet = MaplePacketCreator.spouseMessage(10, "[Pvp] 玩家 " + player.getName() + " 终结了 " + attacked.getName() + "");
            // byte[] packet = MaplePacketCreator.spouseMessage(10, "[Pvp] 玩家 " + player.getName() + " 终结了 " + attacked.getName() + " 的 " + pvpVictory + " 连斩。");

        }
    }

    public static synchronized void doPvP(MapleCharacter player, MapleMap map, AttackInfo attack, MapleStatEffect effect) {
        System.out.println("PVP伤害检查");
        PvpAttackInfo pvpAttack = parsePvpAttack(attack, player, effect);
        System.out.println("PVP伤害检查-I");
        int mobCount = 0;
        for (MapleCharacter attacked : player.getMap().getCharactersIntersect(pvpAttack.box)) {
            System.out.println("PVP伤害检查-J");
            if ((attacked.getId() != player.getId()) && (attacked.isAlive()) && (!attacked.isHidden()) && (mobCount < pvpAttack.mobCount)) {
                mobCount++;
                System.out.println("PVP伤害检查-K");
                monsterBomb(player, attacked, map, pvpAttack);
            }
        }
    }

    public static synchronized void doPartyPvP(MapleCharacter player, MapleMap map, AttackInfo attack, MapleStatEffect effect) {
        PvpAttackInfo pvpAttack = parsePvpAttack(attack, player, effect);
        int mobCount = 0;
        for (MapleCharacter attacked : player.getMap().getCharactersIntersect(pvpAttack.box)) {
            if ((attacked.getId() != player.getId()) && (attacked.isAlive()) && (!attacked.isHidden()) && ((player.getParty() == null) || (player.getParty() != attacked.getParty())) && (mobCount < pvpAttack.mobCount)) {
                mobCount++;
                monsterBomb(player, attacked, map, pvpAttack);
            }
        }
    }

    public static synchronized void doGuildPvP(MapleCharacter player, MapleMap map, AttackInfo attack, MapleStatEffect effect) {
        PvpAttackInfo pvpAttack = parsePvpAttack(attack, player, effect);
        int mobCount = 0;
        for (MapleCharacter attacked : player.getMap().getCharactersIntersect(pvpAttack.box)) {
            if ((attacked.getId() != player.getId()) && (attacked.isAlive()) && (!attacked.isHidden()) && ((player.getGuildId() == 0) || (player.getGuildId() != attacked.getGuildId())) && (mobCount < pvpAttack.mobCount)) {
                mobCount++;
                monsterBomb(player, attacked, map, pvpAttack);
            }
        }
    }

}
