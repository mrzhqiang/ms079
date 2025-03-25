package com.github.mrzhqiang.maplestory.util;

import client.MapleBuffStat;
import client.MapleDisease;
import client.status.MonsterStatus;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import server.MapleStatEffect;
import tools.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 特效辅助工具。
 */
public final class Effects {
    private Effects() {
        // no instances
    }

    /**
     * wz 特效定义。
     *
     * @param id    特效编码。
     * @param level wz 特效信息。
     * @return 特效定义。
     */
    public static MapleStatEffect ofItem(int id, WzElement<?> level) {
        MapleStatEffect effect = ofSkill(id, false, level);
        effect.setSkill(false);

        if (!effect.isSkill() && effect.getDuration() > -1) {
            effect.setOverTime(true);
        }
        return effect;
    }

    public static MapleStatEffect ofSkill(int id, boolean overTime, WzElement<?> level) {
        MapleStatEffect effect = new MapleStatEffect();
        effect.setSourceId(id);
        effect.setSkill(true);
        effect.setLevel(Elements.findInt(level, "level"));
        effect.setDuration(Elements.findInt(level, "time", -1));
        effect.setHp(Elements.findInt(level, "hp").shortValue());
        effect.setMp(Elements.findInt(level, "mp").shortValue());
        effect.setMpCon(Elements.findInt(level, "mpCon").shortValue());
        effect.setHpCon(Elements.findInt(level, "hpCon").shortValue());
        effect.setProp(Elements.findInt(level, "prop").shortValue());
        effect.setCooldown(Elements.findInt(level, "cooltime"));
        effect.setMorphId(Elements.findInt(level, "morph"));
        effect.setMobCount(Elements.findInt(level, "mobCount").byteValue());
        // fixme item 相关的设置，怕出问题，先全部在这边实现，待以后测试

        effect.setHpR(Elements.findInt(level, "hpR") / 100.0);
        effect.setMpR(Elements.findInt(level, "mpR") / 100.0);
        effect.setMhpR(Elements.findInt(level, "mhpR").byteValue());
        effect.setMmpR(Elements.findInt(level, "mmpR").byteValue());
        effect.setExpinc(Elements.findInt(level, "expinc"));
        effect.setCp(Elements.findInt(level, "cp"));
        effect.setNuffSkill(Elements.findInt(level, "nuffSkill"));

        // todo 将 id 转为名字
        switch (id) {
            case 1100002:
            case 1100003:
            case 1200002:
            case 1200003:
            case 1300002:
            case 1300003:
            case 3100001:
            case 3200001:
            case 11101002:
            case 13101002:
                effect.setMobCount((byte) 6);
                break;
        }

        // items have their times stored in ms, of course
        effect.setDuration(effect.getDuration() * 1000);
        overTime = overTime || effect.isMorph() || effect.isPirateMorph() || effect.isFinalAttack();
        effect.setOverTime(overTime);

        effect.setMastery(Elements.findInt(level, "mastery").byteValue());
        effect.setWatk(Elements.findInt(level, "pad").shortValue());
        effect.setWdef(Elements.findInt(level, "pdd").shortValue());
        effect.setMatk(Elements.findInt(level, "mad").shortValue());
        effect.setMdef(Elements.findInt(level, "mdd").shortValue());
        effect.setEhp(Elements.findInt(level, "emhp").shortValue());
        effect.setEmp(Elements.findInt(level, "emmp").shortValue());
        effect.setEwatk(Elements.findInt(level, "epad").shortValue());
        effect.setEwdef(Elements.findInt(level, "epdd").shortValue());
        effect.setEmdef(Elements.findInt(level, "emdd").shortValue());
        effect.setAcc(Elements.findInt(level, "acc").shortValue());
        effect.setAvoid(Elements.findInt(level, "eva").shortValue());
        effect.setSpeed(Elements.findInt(level, "speed").shortValue());
        effect.setJump(Elements.findInt(level, "jump").shortValue());
        effect.setExpBuff(Elements.findInt(level, "expBuff"));
        effect.setCashup(Elements.findInt(level, "cashBuff"));
        effect.setItemup(Elements.findInt(level, "itemupbyitem"));
        effect.setMesoup(Elements.findInt(level, "mesoupbyitem"));
        effect.setBerserk(Elements.findInt(level, "berserk"));
        effect.setBerserk2(Elements.findInt(level, "berserk2"));
        effect.setBooster(Elements.findInt(level, "booster"));
        effect.setIllusion(Elements.findInt(level, "illusion"));

        List<MapleDisease> cure = new ArrayList<>(5);
        if (Elements.findInt(level, "poison") > 0) {
            cure.add(MapleDisease.POISON);
        }
        if (Elements.findInt(level, "seal") > 0) {
            cure.add(MapleDisease.SEAL);
        }
        if (Elements.findInt(level, "darkness") > 0) {
            cure.add(MapleDisease.DARKNESS);
        }
        if (Elements.findInt(level, "weakness") > 0) {
            cure.add(MapleDisease.WEAKEN);
        }
        if (Elements.findInt(level, "curse") > 0) {
            cure.add(MapleDisease.CURSE);
        }
        effect.setCureDebuffs(cure);

        effect.setLt(Elements.findVector(level, "lt"));
        effect.setRb(Elements.findVector(level, "rb"));

        effect.setX(Elements.findInt(level, "x"));
        effect.setY(Elements.findInt(level, "y"));
        effect.setZ(Elements.findInt(level, "z"));
        effect.setDamage(Elements.findInt(level, "damage", 100).shortValue());
        effect.setAttackCount(Elements.findInt(level, "attackCount", 1).byteValue());
        effect.setBulletCount(Elements.findInt(level, "bulletCount", 1).byteValue());
        effect.setBulletConsume(Elements.findInt(level, "bulletConsume"));
        effect.setMoneyCon(Elements.findInt(level, "moneyCon"));
        effect.setItemCon(Elements.findInt(level, "itemCon"));
        effect.setItemConNo(Elements.findInt(level, "itemConNo"));
        effect.setMoveTo(Elements.findInt(level, "moveTo", -1));

        List<Pair<MapleBuffStat, Integer>> statups = Lists.newArrayList();
        Map<MonsterStatus, Integer> monsterStatus = Maps.newEnumMap(MonsterStatus.class);

        if (effect.isOverTime() && effect.getSummonMovementType() == null) {
            addBuffStatPairToListIfNotZero(statups, effect);
        }

        handleBySkillId(effect);
        handleBuffStatPairList(effect, statups);
        handleMonsterStatusMap(effect, monsterStatus);

        if (effect.isMonsterRiding()) {
            statups.add(new Pair<>(MapleBuffStat.骑兽技能, 1));
        }
        if (effect.isMorph() || effect.isPirateMorph()) {
            statups.add(new Pair<>(MapleBuffStat.MORPH, effect.getMorph()));
        }
        effect.setMonsterStatus(monsterStatus);
//        statups.trimToSize();
        effect.setStatups(statups);

        return effect;
    }

    public static void addBuffStatPairToListIfNotZero(List<Pair<MapleBuffStat, Integer>> statups, MapleStatEffect ret) {
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.WATK, (int) ret.getWatk());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.WDEF, (int) ret.getWdef());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.MATK, (int) ret.getMatk());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.MDEF, (int) ret.getMdef());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ACC, (int) ret.getAcc());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.AVOID, (int) ret.getAvoid());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.速度, (int) ret.getSpeed());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.跳跃, (int) ret.getJump());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.MAXHP, (int) ret.getMhpR());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.MAXMP, (int) ret.getMmpR());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.EXPRATE, ret.getExpBuff()); // EXP
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ACASH_RATE, ret.getCashup()); // custom
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.DROP_RATE, ret.getItemup() * 200); // defaults to 2x
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.MESO_RATE, ret.getMesoup() * 200); // defaults to 2x
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.BERSERK_FURY, ret.getBerserk2());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.PYRAMID_PQ, ret.getBerserk());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.BOOSTER, ret.getBooster());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ILLUSION, ret.getIllusion());

        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ENHANCED_WATK, (int) ret.getEwatk());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ENHANCED_WDEF, (int) ret.getEwdef());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ENHANCED_MDEF, (int) ret.getEmdef());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ENHANCED_MAXHP, (int) ret.getEhp());
        addBuffStatPairToListIfNotZero(statups, MapleBuffStat.ENHANCED_MAXMP, (int) ret.getEmp());// 原版本是 ehp
    }

    private static void addBuffStatPairToListIfNotZero(List<Pair<MapleBuffStat, Integer>> list,
                                                       MapleBuffStat buffstat, Integer val) {
        if (val != 0) {
            list.add(new Pair<>(buffstat, val));
        }
    }

    public static void handleBySkillId(MapleStatEffect effect) {
        if (effect == null) {
            return;
        }

        switch (effect.getSourceId()) {
            case 35120000:
            case 35001002: //TEMP. mech
                effect.setDuration(60 * 120 * 1000);
                break;
            case 9001004: // hide
                effect.setDuration(60 * 120 * 1000);
                break;
            case 5211006: // 导航
            case 5220011: // 导航辅助
            case 22151002: //079不存在
                effect.setDuration(60 * 120000);
                break;
            case 1311006: //dragon roar
                effect.setHpR(-effect.getX() / 100.0);
                break;
            case 4341002:
                effect.setDuration(60 * 1000);
                effect.setHpR(-effect.getX() / 100.0);
                break;
            case 4331003:
                effect.setDuration(60 * 1000);
                break;
            case 2201004: // cold beam
            case 2211002: // ice strike
            case 3211003: // blizzard
            case 2211006: // il elemental compo
            case 2221007: // Blizzard
            case 5211005: // Ice Splitter
            case 2121006: // Paralyze
            case 21120006: // Tempest
            case 22121000:
                effect.setDuration(effect.getDuration() * 2); // freezing skills are a little strange
                break;
            case 1026: // Soaring
            case 10001026: // Soaring
            case 20001026: // Soaring
            case 20011026: // Soaring
            case 30001026:
                effect.setDuration(60 * 120 * 1000); //because it seems to dispel asap.
                break;
            case 32001003: //dark aura
            case 32120000:
                effect.setDuration(60 * 120 * 1000); //because it seems to dispel asap.
                break;
            case 32101002: //blue aura
            case 32110000:
                effect.setDuration(60 * 120 * 1000); //because it seems to dispel asap.
                break;
            case 32101003: //yellow aura
            case 32120001:
                effect.setDuration(60 * 120 * 1000); //because it seems to dispel asap.
                break;
            case 35101007: //perfect armor
                effect.setDuration(60 * 120 * 1000);
                break;
            case 35121006: //satellite safety
                effect.setDuration(60 * 120 * 1000);
                break;
            case 35001001: //flame
            case 35101009:
            case 35111007: //TEMP
                //pre-bb = 35111007,
                effect.setDuration(8000);
                break;
            case 35121013:
                //case 35111004: //siege
            case 35101002: //TEMP
                effect.setDuration(5000);
                break;
            case 35121005: //missile
                effect.setDuration(60 * 120 * 1000);
                break;
            default:
                break;
        }
    }

    public static void handleBuffStatPairList(MapleStatEffect effect, List<Pair<MapleBuffStat, Integer>> statups) {
        if (effect == null || statups == null) {
            return;
        }

        if (effect.isSkill()) { // hack because we can't get from the datafile...
            switch (effect.getSourceId()) {
                case 2001002: // magic guard
                case 12001001:
                case 22111001:
                    statups.add(new Pair<>(MapleBuffStat.MAGIC_GUARD, effect.getX()));
                    break;
                case 2301003: // invincible
                    statups.add(new Pair<>(MapleBuffStat.INVINCIBLE, effect.getX()));
                    break;
                case 35120000:
                case 9001004: // hide
                    statups.add(new Pair<>(MapleBuffStat.DARKSIGHT, effect.getX()));
                    break;
                case 13101006: // Wind Walk
                case 4001003: // darksight
                case 14001003: // cygnus ds
                case 4330001:
                case 30001001: //resist beginner hide
                    statups.add(new Pair<>(MapleBuffStat.DARKSIGHT, effect.getX()));
                    break;
                case 4211003: // pickpocket
                    statups.add(new Pair<>(MapleBuffStat.PICKPOCKET, effect.getX()));
                    break;
                case 4211005: // mesoguard
                    statups.add(new Pair<>(MapleBuffStat.MESOGUARD, effect.getX()));
                    break;
                case 4111001: // mesoup
                    statups.add(new Pair<>(MapleBuffStat.MESOUP, effect.getX()));
                    break;
                case 4111002: // shadowpartner
                case 14111000: // cygnus
                    statups.add(new Pair<>(MapleBuffStat.SHADOWPARTNER, effect.getX()));
                    break;
                case 11101002: // All Final attack
                case 13101002:
                    statups.add(new Pair<>(MapleBuffStat.FINALATTACK, effect.getX()));
                    break;
                case 3101004: // soul arrow
                case 3201004:
                case 2311002: // mystic door - hacked buff icon
                case 13101003:
                case 33101003:
                case 8001:
                case 10008001:
                case 20008001:
                case 20018001:
                case 30008001:
                    statups.add(new Pair<>(MapleBuffStat.SOULARROW, effect.getX()));
                    break;
                case 1211006: // wk charges
                case 1211003:
                case 1211004:
                case 1211005:
                case 1211008:
                case 1211007:
                case 1221003:
                case 1221004:
                case 11111007:
                case 21111005:
                case 15101006:
                    statups.add(new Pair<>(MapleBuffStat.属性攻击, effect.getX()));
                    break;
                case 12101005:
                case 22121001: // Elemental Reset
                    statups.add(new Pair<>(MapleBuffStat.ELEMENT_RESET, effect.getX()));
                    break;
                case 3121008:
                    statups.add(new Pair<>(MapleBuffStat.CONCENTRATE, effect.getX()));
                    break;
                case 5110001: // Energy Charge
                case 15100004:
                    statups.add(new Pair<>(MapleBuffStat.ENERGY_CHARGE, 0));
                    break;
                case 1101005: // booster
                case 1101004:
                case 1201005:
                case 1201004:
                case 1301005:
                case 1301004:
                case 3101002:
                case 3201002:
                case 4101003:
                case 4201002:
                case 2111005: // spell booster, do these work the same?
                case 2211005:
                case 5101006:
                case 5201003:
                case 11101001:
                case 12101004:
                case 13101001:
                case 14101002:
                case 15101002:
                case 21001003: // Aran - Pole Arm Booster
                case 22141002: // Magic Booster
                case 4301002:
                case 32101005:
                case 33001003:
                case 35101006:
                case 35001003: //TEMP.BOOSTER
                    statups.add(new Pair<>(MapleBuffStat.BOOSTER, effect.getX()));
                    break;
                case 5121009://极速领域
                case 15111005:
                    statups.add(new Pair<>(MapleBuffStat.极速领域, effect.getX()));
                    break;
                case 5001005://改为疾驰
                    statups.add(new Pair<>(MapleBuffStat.疾驰移动, effect.getX()));
                    statups.add(new Pair<>(MapleBuffStat.疾驰跳跃, effect.getY())); //always 0 but its there
                    break;
                case 15001003:
                    statups.add(new Pair<>(MapleBuffStat.疾驰移动, effect.getX()));
                    statups.add(new Pair<>(MapleBuffStat.疾驰跳跃, effect.getY()));
                    break;
                case 1101007: // 剑客伤害反击
                case 1201007: // 准骑士伤害反击
                case 21101003://战神抗压
                    statups.add(new Pair<>(MapleBuffStat.伤害反击, effect.getX()));
                    break;
                case 32111004: //conversion
                    statups.add(new Pair<>(MapleBuffStat.CONVERSION, effect.getX()));
                    break;
                case 1301007: // hyper body
                case 9001008:
                case 8003:
                case 10008003:
                case 20008003:
                case 20018003:
                case 30008003:
                    statups.add(new Pair<>(MapleBuffStat.MAXHP, effect.getX()));
                    statups.add(new Pair<>(MapleBuffStat.MAXMP, effect.getY()));
                    break;
                case 1001: // recovery
                    statups.add(new Pair<>(MapleBuffStat.RECOVERY, effect.getX()));
                    break;
                case 1111002: // combo
                case 11111001: // combo
                    statups.add(new Pair<>(MapleBuffStat.COMBO, 1));
                    break;
                case 21120007: //combo barrier
                    statups.add(new Pair<>(MapleBuffStat.COMBO_BARRIER, effect.getX()));
                    break;
                case 5211006: // 导航
                case 5220011: // 导航辅助
                case 22151002: //079不存在
                    statups.add(new Pair<>(MapleBuffStat.HOMING_BEACON, effect.getX()));
                    break;
                case 1011: // Berserk fury
                case 10001011:
                case 20001011:
                case 20011011:
                case 30001011:
                    statups.add(new Pair<>(MapleBuffStat.BERSERK_FURY, 1));
                    break;
                case 1010:
                case 10001010:// Invincible Barrier
                case 20001010:
                case 20011010:
                case 30001010:
                    statups.add(new Pair<>(MapleBuffStat.DIVINE_BODY, 1));
                    break;
                case 1311006: //dragon roar
                    statups.add(new Pair<>(MapleBuffStat.DRAGON_ROAR, effect.getY()));
                    break;
                case 4341007:
                    statups.add(new Pair<>(MapleBuffStat.THORNS, effect.getX() << 8 | effect.getY()));
                    break;
                case 4341002:
                    statups.add(new Pair<>(MapleBuffStat.FINAL_CUT, effect.getY()));
                    break;
                case 4331002:
                    statups.add(new Pair<>(MapleBuffStat.MIRROR_IMAGE, effect.getX()));
                    break;
                case 4331003:
                    statups.add(new Pair<>(MapleBuffStat.OWL_SPIRIT, effect.getY()));
                    break;
                case 1311008: // dragon blood
                    statups.add(new Pair<>(MapleBuffStat.DRAGONBLOOD, effect.getX()));
                    break;
                case 1121000: // maple warrior, all classes
                case 1221000:
                case 1321000:
                case 2121000:
                case 2221000:
                case 2321000:
                case 3121000:
                case 3221000:
                case 4121000:
                case 4221000:
                case 5121000:
                case 5221000:
                case 21121000: // Aran - Maple Warrior
                case 22171000:
                case 4341000:
                case 32121007:
                case 33121007:
                case 35121007:
                    statups.add(new Pair<>(MapleBuffStat.MAPLE_WARRIOR, effect.getX()));
                    break;
                case 15111006: //spark
                    statups.add(new Pair<>(MapleBuffStat.SPARK, effect.getX()));
                    break;
                case 3121002: // sharp eyes bow master
                case 3221002: // sharp eyes marksmen
                case 33121004:
                case 8002:
                case 10008002:
                case 20008002:
                case 20018002:
                case 30008002:
                    statups.add(new Pair<>(MapleBuffStat.SHARP_EYES, effect.getX() << 8 | effect.getY()));
                    break;
                case 22151003: //magic resistance
                    statups.add(new Pair<>(MapleBuffStat.MAGIC_RESISTANCE, effect.getX()));
                    break;
                /* case 21101003: // 抗压
                    statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.POWERGUARD, effect.getX()));
                    break;*/
                case 21000000: // Aran Combo
                    statups.add(new Pair<>(MapleBuffStat.ARAN_COMBO, 100));
                    break;
                case 21100005: // Combo Drain
                case 32101004:
                    statups.add(new Pair<>(MapleBuffStat.COMBO_DRAIN, effect.getX()));
                    break;
                case 21111001: // Smart Knockback
                    statups.add(new Pair<>(MapleBuffStat.SMART_KNOCKBACK, effect.getX()));
                    break;
                case 22131001: //magic shield
                    statups.add(new Pair<>(MapleBuffStat.MAGIC_SHIELD, effect.getX()));
                    break;
                case 22181003: //soul stone
                    statups.add(new Pair<>(MapleBuffStat.SOUL_STONE, 1));
                    break;
                case 32111006:
                    statups.add(new Pair<>(MapleBuffStat.REAPER, 1));
                    break;
                case 4341006:
                case 3111002: // puppet ranger
                case 3211002: // puppet sniper
                case 13111004: // puppet cygnus
                case 5211001: // Pirate octopus summon
                case 5220002: // wrath of the octopi
                case 33111003:
                    statups.add(new Pair<>(MapleBuffStat.PUPPET, 1));
                    break;
                case 3211005: // golden eagle
                case 3111005: // golden hawk
                case 33111005:
                case 35111002:
                    statups.add(new Pair<>(MapleBuffStat.SUMMON, 1));
                    break;
                case 3221005: // frostprey
                case 2121005: // elquines
                    statups.add(new Pair<>(MapleBuffStat.SUMMON, 1));
                    break;
                case 2311006: // summon dragon
                case 3121006: // phoenix
                case 2221005: // ifrit
                case 2321003: // bahamut
                case 1321007: // Beholder
                case 5211002: // Pirate bird summon
                case 11001004:
                case 12001004:
                case 12111004: // Itrit
                case 13001004:
                case 14001005:
                case 15001004:
                    statups.add(new Pair<>(MapleBuffStat.SUMMON, 1));
                    break;
                case 2311003: // hs
                case 9001002: // GM hs
                    statups.add(new Pair<>(MapleBuffStat.HOLY_SYMBOL, effect.getX()));
                    break;
                case 2211004: // il seal
                case 2111004: // fp seal
                case 12111002: // cygnus seal
                    break;
                case 4111003: // shadow web
                case 14111001:
                    break;
                case 4121006: // spirit claw
                    statups.add(new Pair<>(MapleBuffStat.SPIRIT_CLAW, 0));
                    break;
                case 2121004:
                case 2221004:
                case 2321004: // Infinity
                    statups.add(new Pair<>(MapleBuffStat.INFINITY, effect.getX()));
                    break;
                case 1121002:
                case 1221002:
                case 1321002: // Stance
                case 21121003: // Aran - Freezing Posture
                case 32121005:
                    statups.add(new Pair<>(MapleBuffStat.STANCE, (int) effect.getProp()));
                    break;
                case 1005: // Echo of Hero
                case 10001005: // Cygnus Echo
                case 20001005: // Aran
                case 20011005: // Evan
                case 30001005:
                    statups.add(new Pair<>(MapleBuffStat.ECHO_OF_HERO, effect.getX()));
                    break;
                case 1026: // Soaring
                case 10001026: // Soaring
                case 20001026: // Soaring
                case 20011026: // Soaring
                case 30001026:
                    statups.add(new Pair<>(MapleBuffStat.SOARING, 1));
                    break;
                case 2121002: // mana reflection
                case 2221002:
                case 2321002:
                    statups.add(new Pair<>(MapleBuffStat.MANA_REFLECTION, 1));
                    break;
                case 2321005: // holy shield
                    statups.add(new Pair<>(MapleBuffStat.HOLY_SHIELD, effect.getX()));
                    break;
                case 3121007: // Hamstring
                    statups.add(new Pair<>(MapleBuffStat.HAMSTRING, effect.getX()));
                    break;
                case 3221006: // Blind
                case 33111004:
                    statups.add(new Pair<>(MapleBuffStat.BLIND, effect.getX()));
                    break;
                case 33121006: //feline berserk
                    statups.add(new Pair<>(MapleBuffStat.MAXHP, effect.getX()));
                    statups.add(new Pair<>(MapleBuffStat.WATK, effect.getY()));//temp
                    //statups.add(new Pair<MapleBuffStat, Integer>(MapleBuffStat.DASH_SPEED, effect.z));
                    break;
                case 32001003: //dark aura
                case 32120000:
                    statups.add(new Pair<>(MapleBuffStat.DARK_AURA, effect.getX()));
                    break;
                case 32101002: //blue aura
                case 32110000:
                    statups.add(new Pair<>(MapleBuffStat.BLUE_AURA, effect.getX()));
                    break;
                case 32101003: //yellow aura
                case 32120001:
                    statups.add(new Pair<>(MapleBuffStat.YELLOW_AURA, effect.getX()));
                    break;
                case 33101004: //it's raining mines
                    statups.add(new Pair<>(MapleBuffStat.RAINING_MINES, effect.getX())); //getX()?
                    break;
                case 35101007: //perfect armor
                    statups.add(new Pair<>(MapleBuffStat.PERFECT_ARMOR, effect.getX()));
                    break;
                case 35121006: //satellite safety
                    statups.add(new Pair<>(MapleBuffStat.SATELLITESAFE_PROC, effect.getX()));
                    statups.add(new Pair<>(MapleBuffStat.SATELLITESAFE_ABSORB, effect.getY()));
                    break;
                case 35001001: //flame
                case 35101009:
                case 35111007: //TEMP
                    //pre-bb = 35111007,
                    statups.add(new Pair<>(MapleBuffStat.MECH_CHANGE, (int) effect.getLevel())); //ya wtf
                    break;
                case 35121013:
                    //case 35111004: //siege
                case 35101002: //TEMP
                    statups.add(new Pair<>(MapleBuffStat.MECH_CHANGE, (int) effect.getLevel())); //ya wtf
                    break;
                case 35121005: //missile
                    statups.add(new Pair<>(MapleBuffStat.MECH_CHANGE, (int) effect.getLevel())); //ya wtf
                    break;
                default:
                    break;
            }
        }
    }

    public static void handleMonsterStatusMap(MapleStatEffect effect, Map<MonsterStatus, Integer> monsterStatus) {
        if (effect == null || monsterStatus == null) {
            return;
        }

        if (effect.isSkill()) { // hack because we can't get from the datafile...
            switch (effect.getSourceId()) {
                case 4001002: // disorder
                case 14001002: // cygnus disorder
                    monsterStatus.put(MonsterStatus.物攻, effect.getX());
                    monsterStatus.put(MonsterStatus.物防, effect.getY());
                    break;
                case 5221009: //- 心灵控制 - 诱惑怪物，使怪物在一定时间内攻击其他怪物。
                    monsterStatus.put(MonsterStatus.心灵控制, 1);
                    break;
                case 1201006: // threaten
                    monsterStatus.put(MonsterStatus.物攻, effect.getX());
                    monsterStatus.put(MonsterStatus.物防, effect.getY());
                    break;
                case 1211002: // charged blow
                case 1111008: // shout
                case 4211002: // assaulter
                case 3101005: // arrow bomb
                case 1111005: // coma: sword
                case 1111006: // coma: axe
                case 4221007: // boomerang step
                case 5101002: // Backspin Blow
                case 5101003: // Double Uppercut
                case 5121004: // Demolition
                case 5121005: // Snatch
                case 5121007: // Barrage
                case 5201004: // pirate blank shot
                case 4121008: // Ninja Storm
                case 22151001:
                case 4201004: //steal, new
                case 33101001:
                case 33101002:
                case 32111010:
                case 32121004:
                case 33111002:
                case 33121002:
                case 35101003:
                case 35111015:
                case 5111002: //energy blast
                case 15101005:
                case 4331005:
                    monsterStatus.put(MonsterStatus.眩晕, 1);
                    break;
                case 4321002:
                    monsterStatus.put(MonsterStatus.恐慌, 1);
                    break;
                case 4221003:
                case 4121003:
                case 33121005:
                    monsterStatus.put(MonsterStatus.挑衅, effect.getX());
                    monsterStatus.put(MonsterStatus.魔防, effect.getX());
                    monsterStatus.put(MonsterStatus.物防, effect.getX());
                    break;
                case 2201004: // cold beam
                case 2211002: // ice strike
                case 3211003: // blizzard
                case 2211006: // il elemental compo
                case 2221007: // Blizzard
                case 5211005: // Ice Splitter
                case 2121006: // Paralyze
                case 21120006: // Tempest
                case 22121000:
                    monsterStatus.put(MonsterStatus.冻结, 1);
                    break;
                case 2101003: // fp slow
                case 2201003: // il slow
                case 12101001:
                case 22141003: // Slow
                    monsterStatus.put(MonsterStatus.速度, effect.getX());
                    break;
                case 2101005: // poison breath
                case 2111006: // fp elemental compo
                case 2121003: // ice demon
                case 2221003: // fire demon
                case 3111003: //inferno, new
                case 22161002: //phantom imprint
                    monsterStatus.put(MonsterStatus.中毒, 1);
                    break;
                case 4121004: // - 忍者伏击 - [最高等级 : 30]\n给一定范围内的敌人持续的伤害.一次不能攻击6只以上,HP不掉到1以下.\n必要技能 : #c假动作等级5以上#
                case 4221004: //- 忍者伏击 - 躲藏的同伴突然出现在一定时间内持续攻击敌人.\n一次无法攻击6只以上,HP不会掉到1以下.\n必要技能 : #c假动作 5级 以上#
                    monsterStatus.put(MonsterStatus.忍者伏击, (int) effect.getDamage());
                    break;
                case 2311005://- 巫毒术 - [最高等级:30]\n使周围怪物变成蜗牛,攻击力和移动速度均下降。对BOSS无效,最多变化6个怪物
                    monsterStatus.put(MonsterStatus.巫毒, 1);
                    break;
                case 3211005: // golden eagle
                case 3111005: // golden hawk
                case 33111005:
                case 35111002:
                    monsterStatus.put(MonsterStatus.眩晕, 1);
                    break;
                case 3221005: // frostprey
                case 2121005: // elquines
                    monsterStatus.put(MonsterStatus.冻结, 1);
                    break;
                case 2211004: // il seal
                case 2111004: // fp seal
                case 12111002: // cygnus seal
                    monsterStatus.put(MonsterStatus.封印, 1);
                    break;
                case 4111003: // shadow web
                case 14111001:
                    monsterStatus.put(MonsterStatus.影网, 1);
                    break;
                case 3121007: // Hamstring
                    monsterStatus.put(MonsterStatus.速度, effect.getX());
                    break;
                case 3221006: // Blind
                case 33111004:
                    monsterStatus.put(MonsterStatus.命中, effect.getX());
                    break;
                default:
                    break;
            }
        }
    }

}
