package server.life;

import com.github.mrzhqiang.maplestory.domain.query.QDWzNPCNameData;
import com.github.mrzhqiang.maplestory.util.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzDirectory;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.github.mrzhqiang.maplestory.wz.element.FloatElement;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class MapleLifeFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleLifeFactory.class);

    private static final Map<Integer, String> npcNames = new HashMap<>();
    private static final Map<Integer, MapleMonsterStats> monsterStats = new HashMap<>();
    private static final Map<Integer, Integer> NPCLoc = new HashMap<>();
    private static final Map<Integer, List<Integer>> questCount = new HashMap<>();

    public static AbstractLoadedMapleLife getLife(int id, String type) {
        if (type.equalsIgnoreCase("n")) {
            return getNPC(id);
        } else if (type.equalsIgnoreCase("m")) {
            return getMonster(id);
        } else {
            LOGGER.error("Unknown Life type: " + type + "");
            return null;
        }
    }

    public static int getNPCLocation(int npcid) {
        Integer npc = NPCLoc.get(npcid);
        if (npc != null) {
            return npc;
        }

        Integer map = WzData.ETC.directory().findFile("NpcLocation.img")
                .map(WzFile::content)
                .map(element -> Elements.findInt(element, npcid + "/0"))
                .orElse(-1);
        NPCLoc.put(npcid, map);
        return map;
    }

    public static void loadQuestCounts() {
        if (questCount.size() > 0) {
            return;
        }

        WzData.MOB.directory().findDir("QuestCountGroup")
                .map(WzDirectory::fileStream)
                .ifPresent(stream -> stream.forEach(file -> file.content()
                        .findByName("info")
                        .map(it -> it.childrenStream().map(Elements::ofInt).collect(Collectors.toList()))
                        .ifPresent(integers -> {
                            int id = Numbers.ofInt(file.smallName());
                            questCount.put(id, integers);
                        }))
                );

        new QDWzNPCNameData().orderBy().npc.asc().findEach(it -> npcNames.put(it.getNpc(), it.getName()));
        LOGGER.info("共加载 {} 个NPC.", npcNames.size());
    }

    public static List<Integer> getQuestCount(final int id) {
        return questCount.get(id);
    }

    public static MapleMonster getMonster(int mid) {
        MapleMonsterStats statsCache = monsterStats.get(mid);
        if (statsCache != null) {
            return new MapleMonster(mid, statsCache);
        }

        return WzData.MOB.directory()
                .findFile(Strings.padStart(String.valueOf(mid), 7, '0'))
                .map(WzFile::content)
                .map(element -> {
                    MapleMonsterStats stats = new MapleMonsterStats();
                    stats.setHp(Elements.findInt(element, "info/maxHP"));
                    stats.setMp(Elements.findInt(element, "info/maxMP"));
                    stats.setExp(Elements.findInt(element, "info/exp"));
                    stats.setLevel(Elements.findInt(element, "info/level").shortValue());
                    stats.setRemoveAfter(Elements.findInt(element, "info/removeAfter"));
                    stats.setrareItemDropLevel(Elements.findInt(element, "info/rareItemDropLevel").byteValue());
                    stats.setFixedDamage(Elements.findInt(element, "info/fixedDamage", -1));
                    stats.setOnlyNormalAttack(Elements.findInt(element, "info/onlyNormalAttack") > 0);
                    stats.setBoss(Elements.findInt(element, "info/boss") > 0
                            || mid == 8810018
                            || mid == 9410066
                            || (mid >= 8810118 && mid <= 8810122));
                    stats.setExplosiveReward(Elements.findInt(element, "info/explosiveReward") > 0);
                    stats.setFfaLoot(Elements.findInt(element, "info/publicReward") > 0);
                    stats.setUndead(Elements.findInt(element, "info/undead") > 0
                            || mid == 9700004
                            || mid == 9700009
                            || mid == 9700010);
                    stats.setName(WzData.STRING.directory().findFile("Mob.img")
                            .map(WzFile::content)
                            .map(mob -> Elements.findString(mob, mid + "/name"))
                            .orElse("MISSINGNO"));
                    stats.setBuffToGive(Elements.findInt(element, "info/buff", -1));
                    stats.setFriendly(Elements.findInt(element, "info/damagedByMob") > 0);
                    stats.setNoDoom(Elements.findInt(element, "info/noDoom") > 0);
                    stats.setCP(Elements.findInt(element, "info/getCP").byteValue());
                    stats.setPoint(Elements.findInt(element, "info/point"));
                    stats.setDropItemPeriod(Elements.findInt(element, "info/dropItemPeriod"));
                    stats.setPhysicalDefense(Elements.findInt(element, "info/PDDamage").shortValue());
                    stats.setMagicDefense(Elements.findInt(element, "info/MDDamage").shortValue());
                    stats.setEva(Elements.findInt(element, "info/eva").shortValue());
                    stats.setSelfDHP(Elements.findInt(element, "info/selfDestruction/hp"));
                    stats.setSelfD(Elements.findInt(element, "info/selfDestruction/action", -1).byteValue());
                    element.findByName("info/firstAttack").ifPresent(attack -> {
                        if (attack instanceof FloatElement) {
                            stats.setFirstAttack(Math.round(Elements.ofFloat(attack)) > 0);
                        } else {
                            stats.setFirstAttack(Elements.ofInt(attack) > 0);
                        }
                    });
                    boolean hideHp = Elements.findInt(element, "info/HPgaugeHide") > 0
                            || Elements.findInt(element, "info/hideHP") > 0;
                    if (stats.isBoss() || isDmgSponge(mid)) {
                        if (hideHp) {
                            stats.setTagColor(0);
                            stats.setTagBgColor(0);
                        } else {
                            stats.setTagColor(Elements.findInt(element, "info/hpTagColor"));
                            stats.setTagBgColor(Elements.findInt(element, "info/hpTagBgcolor"));
                        }
                    }
                    element.findByName("info/ban").ifPresent(ban -> {
                        String msg = Elements.findString(ban, "banMsg");
                        int map = Elements.findInt(ban, "banMap/0/field", -1);
                        String portal = Elements.findString(ban, "banMap/0/portal", "sp");
                        stats.setBanishInfo(new BanishInfo(msg, map, portal));
                    });
                    stats.setRevives(element.findByName("info/revive")
                            .map(WzElement::childrenStream)
                            .map(stream -> stream.map(Elements::ofInt).collect(Collectors.toList()))
                            .orElse(Collections.emptyList()));
                    stats.setSkills(element.findByName("info/skill")
                            .map(WzElement::childrenStream)
                            .map(stream -> stream.map(skillChildren -> {
                                        int skill = Elements.findInt(skillChildren, "skill");
                                        int levelInt = Elements.findInt(skillChildren, "level");
                                        return new Pair<>(skill, levelInt);
                                    })
                                    .collect(Collectors.toList()))
                            .orElse(Collections.emptyList()));
                    decodeElementalString(stats, Elements.findString(element, "info/elemAttr"));
                    int link = Elements.findInt(element, "info/link");
                    if (link != 0) {
                        String name = Strings.padStart(String.valueOf(link), 7, '0');
                        WzData.MOB.directory().findFile(name)
                                .map(WzFile::content)
                                .ifPresent(mob -> {
                                    stats.setFly(mob.findByName("fly").isPresent());
                                    stats.setMobile(mob.findByName("move").isPresent());
                                });
                    }
                    byte hpdisplaytype = -1;
                    if (stats.getTagColor() > 0) {
                        hpdisplaytype = 0;
                    } else if (stats.isFriendly()) {
                        hpdisplaytype = 1;
                    } else if (mid >= 9300184 && mid <= 9300215) { // Mulung TC mobs
                        hpdisplaytype = 2;
                    } else if (!stats.isBoss() || mid == 9410066) { // Not boss and dong dong chiang
                        hpdisplaytype = 3;
                    }
                    stats.setHPDisplayType(hpdisplaytype);
                    monsterStats.put(mid, stats);
                    return new MapleMonster(mid, stats);
                }).orElse(null);
    }

    public static void decodeElementalString(MapleMonsterStats stats, String elemAttr) {
        for (int i = 0; i < elemAttr.length(); i += 2) {
            stats.setEffectiveness(
                    Element.getFromChar(elemAttr.charAt(i)),
                    ElementalEffectiveness.getByNumber(Integer.parseInt(String.valueOf(elemAttr.charAt(i + 1)))));
        }
    }

    private static boolean isDmgSponge(final int mid) {
        switch (mid) {
            case 8810018:
            case 8810118:
            case 8810119:
            case 8810120:
            case 8810121:
            case 8810122:
            case 8820009:
            case 8820010:
            case 8820011:
            case 8820012:
            case 8820013:
            case 8820014:
                return true;
        }
        return false;
    }

    public static MapleNPC getNPC(final int nid) {
        String name = npcNames.get(nid);
        if (name != null) {
            return new MapleNPC(nid, name);
        }

        name = WzData.STRING.directory().findFile("Npc.img")
                .map(WzFile::content)
                .map(element -> Elements.findString(element, nid + "/name"))
                .orElse("MISSINGNO");
        if (name.contains("Maple TV")) {
            return null;
        }

        npcNames.put(nid, name);
        return new MapleNPC(nid, name);
    }
}
