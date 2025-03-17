package server.maps;

import com.github.mrzhqiang.maplestory.domain.query.QDWzCustomLife;
import com.github.mrzhqiang.maplestory.util.Numbers;
import com.github.mrzhqiang.maplestory.wz.WzData;
import com.github.mrzhqiang.maplestory.wz.WzDirectory;
import com.github.mrzhqiang.maplestory.wz.WzElement;
import com.github.mrzhqiang.maplestory.wz.WzFile;
import com.github.mrzhqiang.maplestory.wz.element.Elements;
import com.github.mrzhqiang.maplestory.wz.element.ImgdirElement;
import com.github.mrzhqiang.maplestory.wz.element.data.Rectangle;
import com.github.mrzhqiang.maplestory.wz.element.data.Vector;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.MaplePortal;
import server.PortalFactory;
import server.life.AbstractLoadedMapleLife;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleNPC;
import server.maps.MapleNodes.MapleNodeInfo;
import server.maps.MapleNodes.MaplePlatform;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class MapleMapFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleMapFactory.class);

    private final Map<Integer, MapleMap> maps = new HashMap<>();
    private final Map<Integer, Integer> DeStorymaps = new HashMap<Integer, Integer>() {

        {
            // put(230000000, 0);// 水下世界
            // put(211000000, 0);//冰封雪域
            //put(200082200, 0);//通天塔底下1层
            // put(221000000, 0);//地球防御本部
            // put(222000000, 0);//童话村
            // put(240000000, 0);//神木村
            //put(260000000, 0);//阿里安特
            //put(261000000, 0);//玛家提亚
            //put(250000000, 0);//武陵
            //  put(541010000, 0);//幽灵船
            //  put(240010501, 0);//祭司之林


        }
    };
    private final Map<Integer, MapleMap> instanceMap = new HashMap<>();
    private static final Map<Integer, MapleNodes> MAP_INFOS_CACHED = new HashMap<>();
    private static final Map<Integer, List<AbstractLoadedMapleLife>> customLife = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock(true);
    private int channel;

    public final MapleMap getMap(final int mapid) {
        return getMap(mapid, true, true, true);
    }

    //backwards-compatible
    public final MapleMap getMap(final int mapid, final boolean respawns, final boolean npcs) {
        return getMap(mapid, respawns, npcs, true);
    }

    public final MapleMap getMap(final int mapid, final boolean respawns, final boolean npcs, final boolean reactors) {
        Integer omapid = mapid;
        MapleMap cachedMap = maps.get(omapid);
        if (cachedMap != null) {
            return cachedMap;
        }

        lock.lock();
        try {
            if (DeStorymaps.get(omapid) != null) {
                return null;
            }
            cachedMap = maps.get(omapid);
            if (cachedMap != null) {
                return cachedMap;
            }

            return WzData.MAP.directory()
                    .findDir("Map")
                    .flatMap(dir->dir.findDir( String.format("Map%s", mapid / 100000000)))
                    .flatMap(dir-> dir.findFile( String.format("%s.img",  getPadingMapName(mapid))))
                    .map(WzFile::content)
                    .map(element -> element.findByName("info/link")
                            .map(Elements::ofInt)
                            .map(this::getMapName)
                            .flatMap(mapName -> WzData.MAP.directory().findFile(mapName))
                            .map(WzFile::content)
                            .orElse(element))
                    .map(element -> createMapleMap(omapid, respawns, npcs, reactors, element))
                    .orElse(null);
        } finally {
            lock.unlock();
        }
    }

    private MapleFoothold createMapleFoothold(Rectangle bound, WzElement<?> element) {
        int x1 = Elements.findInt(element, "x1");
        int y1 = Elements.findInt(element, "y1");
        Vector p1 = Vector.of(x1, y1);
        int x2 = Elements.findInt(element, "x2");
        int y2 = Elements.findInt(element, "y2");
        Vector p2 = Vector.of(x2, y2);
        int id = Numbers.ofInt(element.name());
        MapleFoothold foothold = new MapleFoothold(p1, p2, id);
        foothold.setPrev(Elements.findInt(element, "prev").shortValue());
        foothold.setNext(Elements.findInt(element, "next").shortValue());
        if (x1 < bound.left.x) {
            bound.left = Vector.of(x1, bound.left.y);
        }
        if (y1 < bound.left.y) {
            bound.left = Vector.of(bound.left.x, y1);
        }
        if (x2 > bound.right.x) {
            bound.right = Vector.of(x1, bound.right.y);
        }
        if (y2 > bound.right.y) {
            bound.right = Vector.of(bound.right.x, y2);
        }
        return foothold;
    }

    public void HealMap(int mapid) {
        synchronized (this.maps) {
            this.DeStorymaps.remove(mapid);
        }
    }

    public boolean destroyMap(int mapid, boolean Remove) {
        synchronized (this.maps) {
            if (this.maps.containsKey(mapid)) {
                if (Remove) {
                    this.DeStorymaps.put(mapid, 0);
                    this.maps.remove(mapid);
                }
                return this.maps.remove(mapid) != null;

            }
        }
        return false;
    }

    public boolean destroyMap(int mapid) {
        return destroyMap(mapid, false);
    }

    public MapleMap getInstanceMap(final int instanceid) {
        return instanceMap.get(instanceid);
    }

    public void removeInstanceMap(final int instanceid) {
        if (isInstanceMapLoaded(instanceid)) {
            getInstanceMap(instanceid).checkStates("");
            instanceMap.remove(instanceid);
        }
    }

    public void removeMap(final int instanceid) {
        if (isMapLoaded(instanceid)) {
            getMap(instanceid).checkStates("");
            maps.remove(instanceid);
        }
    }

    public MapleMap CreateInstanceMap(int mapid, boolean respawns, boolean npcs, boolean reactors, int instanceid) {
        MapleMap mapleMap = instanceMap.get(instanceid);
        if (mapleMap != null) {
            return mapleMap;
        }

        return WzData.MAP.directory().findDir("Map")
                .flatMap(dir->dir.findDir( String.format("Map%s", mapid / 100000000)))
                .flatMap(dir-> dir.findFile( String.format("%s.img",  getPadingMapName(mapid))))
                .map(WzFile::content)
                .map(mapData -> mapData.findByName("info/link")
                        .map(Elements::ofInt)
                        .map(this::getMapName)
                        .flatMap(mapName -> WzData.MAP.directory().findFile(mapName))
                        .map(WzFile::content)
                        .orElse(mapData))
                .map(mapData -> createMapleMap(mapid, respawns, npcs, reactors, mapData))
                .orElse(null);
    }

    private MapleMap createMapleMap(int mapid, boolean respawns, boolean npcs, boolean reactors, ImgdirElement mapData) {
        float monsterRate = 0;
        if (respawns) {
            monsterRate = Elements.findFloat(mapData, "info/mobRate");
        }
        int returnMap = Elements.findInt(mapData, "info/returnMap");
        MapleMap map = new MapleMap(mapid, channel, returnMap, monsterRate);
        PortalFactory portalFactory = new PortalFactory();
        mapData.findByName("portal").map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(element -> {
                    int type = Elements.findInt(element, "pt");
                    MaplePortal pt = portalFactory.makePortal(type, element);
                    map.addPortal(pt);
                }));
        Rectangle bound = Rectangle.empty();
        List<MapleFoothold> allFootholds = mapData.findByName("foothold").map(WzElement::childrenStream)
                .map(stream -> stream.flatMap(WzElement::childrenStream))
                .map(stream -> stream.flatMap(WzElement::childrenStream))
                .map(stream -> stream.map(element -> createMapleFoothold(bound, element))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        MapleFootholdTree fTree = new MapleFootholdTree(bound.left, bound.right);
        for (MapleFoothold foothold : allFootholds) {
            fTree.insert(foothold);
        }
        map.setFootholds(fTree);
        int bossId = Elements.findInt(mapData, "info/timeMob/id");
        String msg = Elements.findString(mapData, "info/timeMob/message", null);
        mapData.findByName("life").map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(lifeChildren -> {
                    String type = Elements.findString(lifeChildren, "type");
                    if (npcs || !type.equals("n")) {
                        String id = Elements.findString(lifeChildren, "id");
                        AbstractLoadedMapleLife life = loadLife(lifeChildren, id, type, mapid);
                        if (life != null) {
                            if (life instanceof MapleMonster) {
                                MapleMonster mob = (MapleMonster) life;
                                int mobTime = Elements.findInt(lifeChildren, "mobTime");
                                byte team = Elements.findInt(lifeChildren, "team", -1).byteValue();
                                String message = mob.getId() == bossId ? msg : null;
                                map.addMonsterSpawn(mob, mobTime, team, message);
                            } else {
                                map.addMapObject(life);
                            }
                        }
                    }
                }));
        List<AbstractLoadedMapleLife> custom = customLife.get(mapid);
        if (custom != null) {
            for (AbstractLoadedMapleLife n : custom) {
                switch (n.getCType()) {
                    case "n":
                        map.addMapObject(n);
                        break;
                    case "m":
                        final MapleMonster monster = (MapleMonster) n;
                        map.addMonsterSpawn(monster, n.getMTime(), (byte) -1, null);
                        break;
                }
            }
        }
        addAreaBossSpawn(map);
        short createMobInterval = Elements.findInt(mapData, "info/createMobInterval", 9000).shortValue();
        map.setCreateMobInterval(createMobInterval);
        map.loadMonsterRate(true);
        map.setNodes(loadNodes(mapid, mapData));

        if (reactors) {
            mapData.findByName("reactor").map(WzElement::childrenStream)
                    .ifPresent(stream -> stream.forEach(reactorChildren -> {
                        String id = Elements.findString(reactorChildren, "id");
                        if (!Strings.isNullOrEmpty(id)) {
                            byte direction = Elements.findInt(reactorChildren, "f").byteValue();
                            map.spawnReactor(loadReactor(reactorChildren, id, direction));
                        }
                    }));
        }

        try {
            String mapPath = getMapStringName(mapid);
            map.setMapName(Elements.findString(mapData, mapPath + "/mapName"));
            map.setStreetName(Elements.findString(mapData, mapPath + "/streetName"));
        } catch (Exception e) {
            map.setMapName("");
            map.setStreetName("");
        }
        map.setClock(mapData.findByName("clock").isPresent()); //clock was changed in wz to have x,y,width,height
        map.setEverlast(Elements.findInt(mapData, "info/everlast") > 0);
        map.setTown(Elements.findInt(mapData, "info/town") > 0);
        map.setSoaring(Elements.findInt(mapData, "info/needSkillForFly") > 0);
        map.setPersonalShop(Elements.findInt(mapData, "info/personalShop") > 0);
        map.setForceMove(Elements.findInt(mapData, "info/lvForceMove"));
        map.setHPDec(Elements.findInt(mapData, "info/decHP"));
        map.setHPDecInterval(Elements.findInt(mapData, "info/decHPInterval", 10000));
        map.setHPDecProtect(Elements.findInt(mapData, "info/protectItem"));
        map.setForcedReturnMap(Elements.findInt(mapData, "info/forcedReturn", 999999999));
        map.setTimeLimit(Elements.findInt(mapData, "info/timeLimit", -1));
        map.setFieldLimit(Elements.findInt(mapData, "info/fieldLimit"));
        map.setFieldType(Elements.findInt(mapData, "info/fieldType"));
        map.setFirstUserEnter(Elements.findString(mapData, "info/onFirstUserEnter"));
        map.setUserEnter(Elements.findString(mapData, "info/onUserEnter"));
        map.setRecoveryRate(Elements.findInt(mapData, "info/recovery", 1));
        map.setFixedMob(Elements.findInt(mapData, "info/fixedMobCapacity"));
        map.setConsumeItemCoolTime(Elements.findInt(mapData, "info/consumeItemCoolTime"));
        map.setOnUserEnter(Elements.findString(mapData, "info/onUserEnter", String.valueOf(mapid)));
        maps.put(mapid, map);
        return map;
    }

    public int getLoadedMaps() {
        return maps.size();
    }

    public boolean isMapLoaded(int mapId) {
        return maps.containsKey(mapId);
    }

    public boolean isInstanceMapLoaded(int instanceid) {
        return instanceMap.containsKey(instanceid);
    }

    public void clearLoadedMap() {
        maps.clear();
    }

    public Collection<MapleMap> getAllMaps() {
        return maps.values();
    }

    public Collection<MapleMap> getAllInstanceMaps() {
        return instanceMap.values();
    }

    private static AbstractLoadedMapleLife loadLife(int id, int f, boolean hide, int fh, int cy, int rx0, int rx1, int x, int y, String type, int mtime) {
        final AbstractLoadedMapleLife myLife = MapleLifeFactory.getLife(id, type);
        if (myLife == null) {
            LOGGER.error("载入 npc " + id + " 异常...");
            return null;
        }
        myLife.setCy(cy);
        myLife.setF(f);
        myLife.setFh(fh);
        myLife.setRx0(rx0);
        myLife.setRx1(rx1);
        myLife.setPosition(Vector.of(x, y));
        myLife.setHide(hide);
        myLife.setMTime(mtime);
        myLife.setCType(type);
        return myLife;
    }

    private AbstractLoadedMapleLife loadLife(WzElement<?> life, String id, String type, int mapid) {
        int[] pb_map = {910000000};
        int[] pb_npc = {9310059, 9310022};
        AbstractLoadedMapleLife myLife = MapleLifeFactory.getLife(Integer.parseInt(id), type);
        if (myLife == null) {
            return null;
        }
        for (int j : pb_map) {
            if (mapid == j) {
                for (int k : pb_npc) {
                    if (Integer.parseInt(id) == k) {
                        return null;
                    }
                }
            }
        }
        myLife.setCy(Elements.findInt(life, "cy"));
        life.findByName("f").ifPresent(element -> myLife.setF(Elements.ofInt(element)));
        myLife.setFh(Elements.findInt(life, "fh"));
        myLife.setRx0(Elements.findInt(life, "rx0"));
        myLife.setRx1(Elements.findInt(life, "rx1"));
        myLife.setPosition(Vector.of(Elements.findInt(life, "x"), Elements.findInt(life, "y")));

        if (Elements.findInt(life, "hide") == 1 && myLife instanceof MapleNPC) {
            myLife.setHide(true);
        }
        return myLife;
    }

    private MapleReactor loadReactor(WzElement<?> reactor, String id, byte FacingDirection) {
        MapleReactorStats stats = MapleReactorFactory.getReactor(Numbers.ofInt(id));
        MapleReactor myReactor = new MapleReactor(stats, Numbers.ofInt(id));

        stats.setFacingDirection(FacingDirection);
        int x = Elements.findInt(reactor, "x");
        int y = Elements.findInt(reactor, "y");
        myReactor.setPosition(Vector.of(x, y));
        int reactorTime = Elements.findInt(reactor, "reactorTime") * 1000;
        myReactor.setDelay(reactorTime);
        myReactor.setState((byte) 0);
        String name = Elements.findString(reactor, "name");
        myReactor.setName(name);

        return myReactor;
    }

    private String getMapName(int mapid) {
        return String.format("Map/Map%s/%s.img", mapid / 100000000, getPadingMapName(mapid));
    }

    private String getPadingMapName(int mapid) {
        return Strings.padStart(String.valueOf(mapid), 9, '0');
    }

    private String getMapStringName(int mapid) {
        StringBuilder builder = new StringBuilder();
        if (mapid < 100000000) {
            builder.append("maple");
        } else if ((mapid >= 100000000 && mapid < 200000000) || mapid / 100000 == 5540) {
            builder.append("victoria");
        } else if (mapid >= 200000000 && mapid < 300000000) {
            builder.append("ossyria");
        } else if (mapid >= 300000000 && mapid < 400000000) {
            builder.append("elin");
        } else if (mapid >= 500000000 && mapid < 510000000) {
            builder.append("thai");
        } else if (mapid >= 540000000 && mapid < 600000000) {
            builder.append("SG");
        } else if (mapid >= 600000000 && mapid < 620000000) {
            builder.append("MasteriaGL");
        } else if ((mapid >= 670000000 && mapid < 677000000) || (mapid >= 678000000 && mapid < 682000000)) {
            builder.append("global");
        } else if (mapid >= 677000000 && mapid < 678000000) {
            builder.append("Episode1GL");
        } else if (mapid >= 682000000 && mapid < 683000000) {
            builder.append("HalloweenGL");
        } else if (mapid >= 683000000 && mapid < 684000000) {
            builder.append("event");
        } else if (mapid >= 684000000 && mapid < 685000000) {
            builder.append("event_5th");
        } else if (mapid >= 700000000 && mapid < 700000300) {
            builder.append("wedding");
        } else if (mapid >= 701000000 && mapid < 701020000) {
            builder.append("china");
        } else if (mapid >= 800000000 && mapid < 900000000) {
            builder.append("jp");
        } else if (mapid >= 700000000 && mapid < 782000002) {
            builder.append("chinese");
        } else {
            builder.append("etc");
        }
        builder.append("/");
        builder.append(mapid);

        return builder.toString();
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public static void loadCustomLife() {
        new QDWzCustomLife().findEach(it -> {
            int mapid = it.getMid();
            AbstractLoadedMapleLife myLife = loadLife(it.getDataId(), it.getF(),
                    it.getHide() > 0, it.getFh(), it.getCy(), it.getRx0(), it.getRx1(), it.getX(), it.getY(), it.getType(), it.getMobTime());
            if (myLife == null) {
                return;
            }
            List<AbstractLoadedMapleLife> entries = customLife.get(mapid);
            List<AbstractLoadedMapleLife> collections = new ArrayList<>();
            if (entries == null) {
                collections.add(myLife);
                customLife.put(mapid, collections);
            } else {
                collections.addAll(entries); //re-add
                collections.add(myLife);
                customLife.put(mapid, collections);
            }
        });

    }

    private void addAreaBossSpawn(final MapleMap map) {
        int monsterid = -1;
        int mobtime = -1;
        String msg = null;
        Vector pos1 = null, pos2 = null, pos3 = null;

        switch (map.getId()) {
            case 104000400: // Mano
                mobtime = 2700;
                monsterid = 2220000;
                msg = "红蜗牛王出现了";
                pos1 = Vector.of(439, 185);
                pos2 = Vector.of(301, -85);
                pos3 = Vector.of(107, -355);
                break;
            case 101030404: // Stumpy
                mobtime = 2700;
                monsterid = 3220000;
                msg = "树妖王出现了";
                pos1 = Vector.of(867, 1282);
                pos2 = Vector.of(810, 1570);
                pos3 = Vector.of(838, 2197);
                break;
            case 110040000: // King Clang
                mobtime = 1200;
                monsterid = 5220001;
                msg = "巨居蟹出现了";
                pos1 = Vector.of(-355, 179);
                pos2 = Vector.of(-1283, -113);
                pos3 = Vector.of(-571, -593);
                break;
            case 250010304: // Tae Roon
                mobtime = 2100;
                monsterid = 7220000;
                msg = "流浪熊出现了";
                pos1 = Vector.of(-210, 33);
                pos2 = Vector.of(-234, 393);
                pos3 = Vector.of(-654, 33);
                break;
            case 200010300: // Eliza
                mobtime = 1200;
                monsterid = 8220000;
                msg = "艾利杰出现了";
                pos1 = Vector.of(665, 83);
                pos2 = Vector.of(672, -217);
                pos3 = Vector.of(-123, -217);
                break;
            case 250010503: // Ghost Priest
                mobtime = 1800;
                monsterid = 7220002;
                msg = "喵仙怪人出现了";
                pos1 = Vector.of(-303, 543);
                pos2 = Vector.of(227, 543);
                pos3 = Vector.of(719, 543);
                break;
            case 222010310: // Old Fox
                mobtime = 2700;
                monsterid = 7220001;
                msg = "九尾妖狐出现了";
                pos1 = Vector.of(-169, -147);
                pos2 = Vector.of(-517, 93);
                pos3 = Vector.of(247, 93);
                break;
            case 107000300: // Dale
                mobtime = 1800;
                monsterid = 6220000;
                msg = "沼泽巨鳄出现了";
                pos1 = Vector.of(710, 118);
                pos2 = Vector.of(95, 119);
                pos3 = Vector.of(-535, 120);
                break;
            case 100040105: // Faust
                mobtime = 1800;
                monsterid = 5220002;
                msg = "浮士德出现了";
                pos1 = Vector.of(1000, 278);
                pos2 = Vector.of(557, 278);
                pos3 = Vector.of(95, 278);
                break;
            case 100040106: // Faust
                mobtime = 1800;
                monsterid = 5220002;
                msg = "浮士德出现了";
                pos1 = Vector.of(1000, 278);
                pos2 = Vector.of(557, 278);
                pos3 = Vector.of(95, 278);
                break;
            case 220050100: // Timer
                mobtime = 1500;
                monsterid = 5220003;
                msg = "提莫出现了";
                pos1 = Vector.of(-467, 1032);
                pos2 = Vector.of(532, 1032);
                pos3 = Vector.of(-47, 1032);
                break;
            case 221040301: // Jeno
                mobtime = 2400;
                monsterid = 6220001;
                msg = "朱诺出现了";
                pos1 = Vector.of(-4134, 416);
                pos2 = Vector.of(-4283, 776);
                pos3 = Vector.of(-3292, 776);
                break;
            case 240040401: // Lev
                mobtime = 7200;
                monsterid = 8220003;
                msg = "大海兽出现了";
                pos1 = Vector.of(-15, 2481);
                pos2 = Vector.of(127, 1634);
                pos3 = Vector.of(159, 1142);
                break;
            case 260010201: // Dewu
                mobtime = 3600;
                monsterid = 3220001;
                msg = "大宇出现了";
                pos1 = Vector.of(-215, 275);
                pos2 = Vector.of(298, 275);
                pos3 = Vector.of(592, 275);
                break;
            case 261030000: // Chimera
                mobtime = 2700;
                monsterid = 8220002;
                msg = "吉米拉出现了";
                pos1 = Vector.of(-1094, -405);
                pos2 = Vector.of(-772, -116);
                pos3 = Vector.of(-108, 181);
                break;
            case 230020100: // Sherp
                mobtime = 2700;
                monsterid = 4220000;
                msg = "歇尔夫出现了";
                pos1 = Vector.of(-291, -20);
                pos2 = Vector.of(-272, -500);
                pos3 = Vector.of(-462, 640);
                break;
            /*
             * case 910000000: // FM if (channel == 5 || channel == 7) { mobtime
             * = 3600; monsterid = 9420015; msg = "NooNoo has appeared out of
             * anger, it seems that NooNoo is stuffed with Christmas gifts!";
             * pos1 = new Point(498, 4); pos2 = new Point(498, 4); pos3 = new
             * Point(498, 4); } break;
             */
            /*
             * case 209080000: // Happyvile mobtime = 2700; monsterid = 9400708;
             * pos1 = new Point(-115, 154); pos2 = new Point(-115, 154); pos3 =
             * new Point(-115, 154); break;
             */
            /*
             * case 677000001: mobtime = 60; monsterid = 9400612; msg = "Marbas
             * has appeared."; pos1 = new Point(99, 60); pos2 = new Point(99,
             * 60); pos3 = new Point(99, 60); break; case 677000003: mobtime =
             * 60; monsterid = 9400610; msg = "Amdusias has appeared."; pos1 =
             * new Point(6, 35); pos2 = new Point(6, 35); pos3 = new Point(6,
             * 35); break; case 677000005: mobtime = 60; monsterid = 9400609;
             * msg = "Andras has appeared."; pos1 = new Point(-277, 78); //on
             * the spawnpoint pos2 = new Point(547, 86); //bottom of right
             * ladder pos3 = new Point(-347, 80); //bottom of left ladder break;
             * case 677000007: mobtime = 60; monsterid = 9400611; msg = "Crocell
             * has appeared."; pos1 = new Point(117, 73); pos2 = new Point(117,
             * 73); pos3 = new Point(117, 73); break; case 677000009: mobtime =
             * 60; monsterid = 9400613; msg = "Valefor has appeared."; pos1 =
             * new Point(85, 66); pos2 = new Point(85, 66); pos3 = new Point(85,
             * 66); break;
             */
            default:
                return;
        }
        if (monsterid > 0) {
            map.addAreaMonsterSpawn(
                    MapleLifeFactory.getMonster(monsterid),
                    pos1, pos2, pos3,
                    mobtime,
                    msg);
        }
    }

    private MapleNodes loadNodes(int mapid, WzElement<?> mapData) {
        MapleNodes nodeInfo = MAP_INFOS_CACHED.get(mapid);
        if (nodeInfo != null) {
            return nodeInfo;
        }

        MapleNodes newNodes = new MapleNodes(mapid);

        mapData.findByName("nodeInfo").map(WzElement::childrenStream)
                .ifPresent(stream -> {
                    stream.forEach(element -> {
                        try {
                            newNodes.setNodeStart(Elements.findInt(element, "start"));
                            newNodes.setNodeEnd(Elements.findInt(element, "end"));
                            List<Integer> edges = element.findByName("edge").map(WzElement::childrenStream)
                                    .map(edgeStream -> edgeStream.map(it -> Elements.ofInt(it, -1))
                                            .collect(Collectors.toList()))
                                    .orElse(Collections.emptyList());
                            MapleNodeInfo mni = new MapleNodeInfo(Numbers.ofInt(element.name()),
                                    Elements.findInt(element, "key"),
                                    Elements.findInt(element, "x"),
                                    Elements.findInt(element, "y"),
                                    Elements.findInt(element, "attr"), edges);
                            newNodes.addNode(mni);
                        } catch (NumberFormatException e) {
                            LOGGER.error("数字格式化失败！", e);
                        } //start, end, edgeInfo = we dont need it
                    });
                    newNodes.sortNodes();
                });
        for (int i = 1; i <= 7; i++) {
            mapData.findByName(i + "obj").map(WzElement::childrenStream)
                    .ifPresent(stream -> stream.forEach(element -> {
                        int snCount = Elements.findInt(element, "SN_count");
                        if (snCount <= 0) {
                            return;
                        }
                        String name = Elements.findString(element, "name");
                        if (Strings.isNullOrEmpty(name)) {
                            return;
                        }
                        int speed = Elements.findInt(element, "speed");
                        if (speed <= 0) {
                            return;
                        }
                        List<Integer> SN = Lists.newArrayListWithCapacity(snCount);
                        for (int x = 0; x < snCount; x++) {
                            SN.add(Elements.findInt(element, "SN" + x));
                        }
                        MaplePlatform mni = new MaplePlatform(name,
                                Elements.findInt(element, "start", 2),
                                speed,
                                Elements.findInt(element, "x1"),
                                Elements.findInt(element, "y1"),
                                Elements.findInt(element, "x2"),
                                Elements.findInt(element, "y2"),
                                Elements.findInt(element, "r"),
                                SN);
                        newNodes.addPlatform(mni);
                    }));
        }
        mapData.findByName("area").map(WzElement::childrenStream)
                .map(stream -> stream.map(element -> {
                    int x1 = Elements.findInt(element, "x1");
                    int y1 = Elements.findInt(element, "y1");
                    int x2 = Elements.findInt(element, "x2");
                    int y2 = Elements.findInt(element, "y2");
                    return Rectangle.of(x1, y1, (x2 - x1), (y2 - y1));
                }).collect(Collectors.toList()))
                .ifPresent(rectangles -> rectangles.stream()
                        .map(Rectangle::toJavaRectangle)
                        .forEach(newNodes::addMapleArea));
        mapData.findByName("monsterCarnival/mobGenPos").map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(element -> {
                    int x = Elements.findInt(element, "x");
                    int y = Elements.findInt(element, "y");
                    int fh = Elements.findInt(element, "fh");
                    int cy = Elements.findInt(element, "cy");
                    int team = Elements.findInt(element, "team", -1);
                    newNodes.addMonsterPoint(x, y, fh, cy, team);
                }));
        mapData.findByName("monsterCarnival/mob").map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(element -> {
                    int id = Elements.findInt(element, "id");
                    int spendCP = Elements.findInt(element, "spendCP");
                    newNodes.addMobSpawn(id, spendCP);
                }));
        mapData.findByName("monsterCarnival/guardianGenPos").map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(element -> {
                    int x = Elements.findInt(element, "x");
                    int y = Elements.findInt(element, "y");
                    int team = Elements.findInt(element, "team", -1);
                    newNodes.addGuardianSpawn(Vector.of(x, y), team);
                }));
        mapData.findByName("monsterCarnival/skill").map(WzElement::childrenStream)
                .ifPresent(stream -> stream.forEach(element ->
                        newNodes.addSkillId(Elements.ofInt(element))));
        MAP_INFOS_CACHED.put(mapid, newNodes);
        return newNodes;
    }
}
