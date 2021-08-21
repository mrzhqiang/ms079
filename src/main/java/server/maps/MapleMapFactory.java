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
package server.maps;

import database.DatabaseConnection;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.locks.ReentrantLock;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import server.PortalFactory;
import server.life.AbstractLoadedMapleLife;
import server.life.MapleLifeFactory;
import server.life.MapleMonster;
import server.life.MapleNPC;
import server.maps.MapleNodes.MapleNodeInfo;
import server.maps.MapleNodes.MaplePlatform;
import tools.StringUtil;

public class MapleMapFactory {

    private static final MapleDataProvider source = MapleDataProviderFactory.getDataProvider(new File(MapleDataProviderFactory.wzPath + "/Map.wz"));
    private static final MapleData nameData = MapleDataProviderFactory.getDataProvider(new File(MapleDataProviderFactory.wzPath + "/String.wz")).getData("Map.img");
    private final Map<Integer, MapleMap> maps = new HashMap<Integer, MapleMap>();
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
    private final Map<Integer, MapleMap> instanceMap = new HashMap<Integer, MapleMap>();
    private static final Map<Integer, MapleNodes> mapInfos = new HashMap<Integer, MapleNodes>();
    private static final Map<Integer, List<AbstractLoadedMapleLife>> customLife = new HashMap<>();
    private final ReentrantLock lock = new ReentrantLock(true);
    private int channel;

    public MapleMapFactory(int channel) {
        this.channel = channel;
    }

    public final MapleMap getMap(final int mapid) {
        return getMap(mapid, true, true, true);
    }

    //backwards-compatible
    public final MapleMap getMap(final int mapid, final boolean respawns, final boolean npcs) {
        return getMap(mapid, respawns, npcs, true);
    }

    public final MapleMap getMap(final int mapid, final boolean respawns, final boolean npcs, final boolean reactors) {
        Integer omapid = Integer.valueOf(mapid);
        MapleMap map = maps.get(omapid);
        if (map == null) {
            lock.lock();
            try {
                if (DeStorymaps.get(omapid) != null) {
                    return null;
                }
                map = maps.get(omapid);
                if (map != null) {
                    return map;
                }
                MapleData mapData = source.getData(getMapName(mapid));

                MapleData link = mapData.getChildByPath("info/link");
                if (link != null) {
                    mapData = source.getData(getMapName(MapleDataTool.getIntConvert("info/link", mapData)));
                }

                float monsterRate = 0;
                if (respawns) {
                    MapleData mobRate = mapData.getChildByPath("info/mobRate");
                    if (mobRate != null) {
                        monsterRate = ((Float) mobRate.getData()).floatValue();
                    }
                }
                map = new MapleMap(mapid, channel, MapleDataTool.getInt("info/returnMap", mapData), monsterRate);

                PortalFactory portalFactory = new PortalFactory();
                for (MapleData portal : mapData.getChildByPath("portal")) {
                    map.addPortal(portalFactory.makePortal(MapleDataTool.getInt(portal.getChildByPath("pt")), portal));
                }
                List<MapleFoothold> allFootholds = new LinkedList<MapleFoothold>();
                Point lBound = new Point();
                Point uBound = new Point();
                MapleFoothold fh;

                for (MapleData footRoot : mapData.getChildByPath("foothold")) {
                    for (MapleData footCat : footRoot) {
                        for (MapleData footHold : footCat) {
                            fh = new MapleFoothold(new Point(
                                    MapleDataTool.getInt(footHold.getChildByPath("x1")), MapleDataTool.getInt(footHold.getChildByPath("y1"))), new Point(
                                    MapleDataTool.getInt(footHold.getChildByPath("x2")), MapleDataTool.getInt(footHold.getChildByPath("y2"))), Integer.parseInt(footHold.getName()));
                            fh.setPrev((short) MapleDataTool.getInt(footHold.getChildByPath("prev")));
                            fh.setNext((short) MapleDataTool.getInt(footHold.getChildByPath("next")));

                            if (fh.getX1() < lBound.x) {
                                lBound.x = fh.getX1();
                            }
                            if (fh.getX2() > uBound.x) {
                                uBound.x = fh.getX2();
                            }
                            if (fh.getY1() < lBound.y) {
                                lBound.y = fh.getY1();
                            }
                            if (fh.getY2() > uBound.y) {
                                uBound.y = fh.getY2();
                            }
                            allFootholds.add(fh);
                        }
                    }
                }
                MapleFootholdTree fTree = new MapleFootholdTree(lBound, uBound);
                for (MapleFoothold foothold : allFootholds) {
                    fTree.insert(foothold);
                }
                map.setFootholds(fTree);

                int bossid = -1;
                String msg = null;
                if (mapData.getChildByPath("info/timeMob") != null) {
                    bossid = MapleDataTool.getInt(mapData.getChildByPath("info/timeMob/id"), 0);
                    msg = MapleDataTool.getString(mapData.getChildByPath("info/timeMob/message"), null);
                }
                // load life data (npc, monsters)
                String type;
                AbstractLoadedMapleLife myLife;

                for (MapleData life : mapData.getChildByPath("life")) {
                    type = MapleDataTool.getString(life.getChildByPath("type"));
                    if (npcs || !type.equals("n")) {
                        myLife = loadLife(life, MapleDataTool.getString(life.getChildByPath("id")), type, mapid);
                        if (myLife != null) {
                            if (myLife instanceof MapleMonster) {
                                final MapleMonster mob = (MapleMonster) myLife;
                                map.addMonsterSpawn(mob, MapleDataTool.getInt("mobTime", life, 0), (byte) MapleDataTool.getInt("team", life, -1), mob.getId() == bossid ? msg : null);
                            } else if (myLife != null) {
                                map.addMapObject(myLife);
                            }
                        }
                    }
                }
                final List<AbstractLoadedMapleLife> custom = customLife.get(mapid);
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
                map.setCreateMobInterval((short) MapleDataTool.getInt(mapData.getChildByPath("info/createMobInterval"), 9000));
                map.loadMonsterRate(true);
                map.setNodes(loadNodes(mapid, mapData));

                //load reactor data
                String id;
                if (reactors && mapData.getChildByPath("reactor") != null) {
                    for (MapleData reactor : mapData.getChildByPath("reactor")) {
                        id = MapleDataTool.getString(reactor.getChildByPath("id"));
                        if (id != null) {
                            map.spawnReactor(loadReactor(reactor, id, (byte) MapleDataTool.getInt(reactor.getChildByPath("f"), 0)));
                        }
                    }
                }

                try {
                    map.setMapName(MapleDataTool.getString("mapName", nameData.getChildByPath(getMapStringName(omapid)), ""));
                    map.setStreetName(MapleDataTool.getString("streetName", nameData.getChildByPath(getMapStringName(omapid)), ""));
                } catch (Exception e) {
                    map.setMapName("");
                    map.setStreetName("");
                }
                map.setClock(mapData.getChildByPath("clock") != null); //clock was changed in wz to have x,y,width,height
                map.setEverlast(MapleDataTool.getInt(mapData.getChildByPath("info/everlast"), 0) > 0);
                map.setTown(MapleDataTool.getInt(mapData.getChildByPath("info/town"), 0) > 0);
                map.setSoaring(MapleDataTool.getInt(mapData.getChildByPath("info/needSkillForFly"), 0) > 0);
                map.setPersonalShop(MapleDataTool.getInt(mapData.getChildByPath("info/personalShop"), 0) > 0);
                map.setForceMove(MapleDataTool.getInt(mapData.getChildByPath("info/lvForceMove"), 0));
                map.setHPDec(MapleDataTool.getInt(mapData.getChildByPath("info/decHP"), 0));
                map.setHPDecInterval(MapleDataTool.getInt(mapData.getChildByPath("info/decHPInterval"), 10000));
                map.setHPDecProtect(MapleDataTool.getInt(mapData.getChildByPath("info/protectItem"), 0));
                map.setForcedReturnMap(MapleDataTool.getInt(mapData.getChildByPath("info/forcedReturn"), 999999999));
                map.setTimeLimit(MapleDataTool.getInt(mapData.getChildByPath("info/timeLimit"), -1));
                map.setFieldLimit(MapleDataTool.getInt(mapData.getChildByPath("info/fieldLimit"), 0));
                map.setFieldType(MapleDataTool.getInt(mapData.getChildByPath("info/fieldType"), 0));
                map.setFirstUserEnter(MapleDataTool.getString(mapData.getChildByPath("info/onFirstUserEnter"), ""));
                map.setUserEnter(MapleDataTool.getString(mapData.getChildByPath("info/onUserEnter"), ""));
                map.setRecoveryRate(MapleDataTool.getFloat(mapData.getChildByPath("info/recovery"), 1));
                map.setFixedMob(MapleDataTool.getInt(mapData.getChildByPath("info/fixedMobCapacity"), 0));
                map.setConsumeItemCoolTime(MapleDataTool.getInt(mapData.getChildByPath("info/consumeItemCoolTime"), 0));
                map.setOnUserEnter(MapleDataTool.getString(mapData.getChildByPath("info/onUserEnter"), String.valueOf(mapid)));

                maps.put(omapid, map);
            } finally {
                lock.unlock();
            }
        }
        return map;
    }

    public void HealMap(int mapid) {
        synchronized (this.maps) {
            if (this.DeStorymaps.containsKey(Integer.valueOf(mapid))) {
                this.DeStorymaps.remove(mapid);
            }
        }
    }

    public boolean destroyMap(int mapid, boolean Remove) {
        synchronized (this.maps) {
            if (this.maps.containsKey(Integer.valueOf(mapid))) {
                if (Remove) {
                    this.DeStorymaps.put(mapid, 0);
                    this.maps.remove(mapid);
                }
                return this.maps.remove(Integer.valueOf(mapid)) != null;

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
        if (isInstanceMapLoaded(instanceid)) {
            return getInstanceMap(instanceid);
        }
        MapleData mapData = source.getData(getMapName(mapid));
        MapleData link = mapData.getChildByPath("info/link");
        if (link != null) {
            mapData = source.getData(getMapName(MapleDataTool.getIntConvert("info/link", mapData)));
        }

        float monsterRate = 0;
        if (respawns) {
            MapleData mobRate = mapData.getChildByPath("info/mobRate");
            if (mobRate != null) {
                monsterRate = ((Float) mobRate.getData()).floatValue();
            }
        }
        MapleMap map = new MapleMap(mapid, channel, MapleDataTool.getInt("info/returnMap", mapData), monsterRate);

        PortalFactory portalFactory = new PortalFactory();
        for (MapleData portal : mapData.getChildByPath("portal")) {
            map.addPortal(portalFactory.makePortal(MapleDataTool.getInt(portal.getChildByPath("pt")), portal));
        }
        List<MapleFoothold> allFootholds = new LinkedList<MapleFoothold>();
        Point lBound = new Point();
        Point uBound = new Point();
        for (MapleData footRoot : mapData.getChildByPath("foothold")) {
            for (MapleData footCat : footRoot) {
                for (MapleData footHold : footCat) {
                    MapleFoothold fh = new MapleFoothold(new Point(
                            MapleDataTool.getInt(footHold.getChildByPath("x1")), MapleDataTool.getInt(footHold.getChildByPath("y1"))), new Point(
                            MapleDataTool.getInt(footHold.getChildByPath("x2")), MapleDataTool.getInt(footHold.getChildByPath("y2"))), Integer.parseInt(footHold.getName()));
                    fh.setPrev((short) MapleDataTool.getInt(footHold.getChildByPath("prev")));
                    fh.setNext((short) MapleDataTool.getInt(footHold.getChildByPath("next")));

                    if (fh.getX1() < lBound.x) {
                        lBound.x = fh.getX1();
                    }
                    if (fh.getX2() > uBound.x) {
                        uBound.x = fh.getX2();
                    }
                    if (fh.getY1() < lBound.y) {
                        lBound.y = fh.getY1();
                    }
                    if (fh.getY2() > uBound.y) {
                        uBound.y = fh.getY2();
                    }
                    allFootholds.add(fh);
                }
            }
        }
        MapleFootholdTree fTree = new MapleFootholdTree(lBound, uBound);
        for (MapleFoothold fh : allFootholds) {
            fTree.insert(fh);
        }
        map.setFootholds(fTree);
        int bossid = -1;
        String msg = null;
        if (mapData.getChildByPath("info/timeMob") != null) {
            bossid = MapleDataTool.getInt(mapData.getChildByPath("info/timeMob/id"), 0);
            msg = MapleDataTool.getString(mapData.getChildByPath("info/timeMob/message"), null);
        }
        // pvp = player vs player
        // load life data (npc, monsters)
        String type;
        AbstractLoadedMapleLife myLife;

        for (MapleData life : mapData.getChildByPath("life")) {
            type = MapleDataTool.getString(life.getChildByPath("type"));
            if (npcs || !type.equals("n")) {
                myLife = loadLife(life, MapleDataTool.getString(life.getChildByPath("id")), type, mapid);
                if (myLife != null) {
                    if (myLife instanceof MapleMonster) {
                        final MapleMonster mob = (MapleMonster) myLife;
                        map.addMonsterSpawn(mob,
                                MapleDataTool.getInt("mobTime", life, 0),
                                (byte) MapleDataTool.getInt("team", life, -1),
                                mob.getId() == bossid ? msg : null);

                    } else {
                        map.addMapObject(myLife);
                    }
                }
            }
        }
        addAreaBossSpawn(map);
        map.setCreateMobInterval((short) MapleDataTool.getInt(mapData.getChildByPath("info/createMobInterval"), 9000));
        map.loadMonsterRate(true);
        map.setNodes(loadNodes(mapid, mapData));

        //load reactor data
        String id;
        if (reactors && mapData.getChildByPath("reactor") != null) {
            for (MapleData reactor : mapData.getChildByPath("reactor")) {
                id = MapleDataTool.getString(reactor.getChildByPath("id"));
                if (id != null) {
                    map.spawnReactor(loadReactor(reactor, id, (byte) MapleDataTool.getInt(reactor.getChildByPath("f"), 0)));
                }
            }
        }
        try {
            map.setMapName(MapleDataTool.getString("mapName", nameData.getChildByPath(getMapStringName(mapid)), ""));
            map.setStreetName(MapleDataTool.getString("streetName", nameData.getChildByPath(getMapStringName(mapid)), ""));
        } catch (Exception e) {
            map.setMapName("");
            map.setStreetName("");
        }
        map.setClock(MapleDataTool.getInt(mapData.getChildByPath("info/clock"), 0) > 0);
        map.setEverlast(MapleDataTool.getInt(mapData.getChildByPath("info/everlast"), 0) > 0);
        map.setTown(MapleDataTool.getInt(mapData.getChildByPath("info/town"), 0) > 0);
        map.setSoaring(MapleDataTool.getInt(mapData.getChildByPath("info/needSkillForFly"), 0) > 0);
        map.setForceMove(MapleDataTool.getInt(mapData.getChildByPath("info/lvForceMove"), 0));
        map.setHPDec(MapleDataTool.getInt(mapData.getChildByPath("info/decHP"), 0));
        map.setHPDecInterval(MapleDataTool.getInt(mapData.getChildByPath("info/decHPInterval"), 10000));
        map.setHPDecProtect(MapleDataTool.getInt(mapData.getChildByPath("info/protectItem"), 0));
        map.setForcedReturnMap(MapleDataTool.getInt(mapData.getChildByPath("info/forcedReturn"), 999999999));
        map.setTimeLimit(MapleDataTool.getInt(mapData.getChildByPath("info/timeLimit"), -1));
        map.setFieldType(MapleDataTool.getInt(mapData.getChildByPath("info/fieldType"), 0));
        map.setFieldLimit(MapleDataTool.getInt(mapData.getChildByPath("info/fieldLimit"), 0));
        map.setFirstUserEnter(MapleDataTool.getString(mapData.getChildByPath("info/onFirstUserEnter"), ""));
        map.setUserEnter(MapleDataTool.getString(mapData.getChildByPath("info/onUserEnter"), ""));
        map.setRecoveryRate(MapleDataTool.getFloat(mapData.getChildByPath("info/recovery"), 1));
        map.setFixedMob(MapleDataTool.getInt(mapData.getChildByPath("info/fixedMobCapacity"), 0));
        map.setConsumeItemCoolTime(MapleDataTool.getInt(mapData.getChildByPath("info/consumeItemCoolTime"), 0));
        map.setOnUserEnter(MapleDataTool.getString(mapData.getChildByPath("info/onUserEnter"), String.valueOf(mapid)));

        instanceMap.put(instanceid, map);
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
            System.err.println("载入 npc " + id + " 异常...");
            return null;
        }
        myLife.setCy(cy);
        myLife.setF(f);
        myLife.setFh(fh);
        myLife.setRx0(rx0);
        myLife.setRx1(rx1);
        myLife.setPosition(new Point(x, y));
        myLife.setHide(hide);
        myLife.setMTime(mtime);
        myLife.setCType(type);
        return myLife;
    }

    private AbstractLoadedMapleLife loadLife(MapleData life, String id, String type, int mapid) {
        int[] pb_map = {910000000};
        int[] pb_npc = {9310059, 9310022};
        AbstractLoadedMapleLife myLife = MapleLifeFactory.getLife(Integer.parseInt(id), type);
        if (myLife == null) {
            return null;
        }
        for (int m = 0; m < pb_map.length; m++) {
            if (mapid == pb_map[m]) {
                for (int i = 0; i < pb_npc.length; i++) {
                    if (Integer.parseInt(id) == pb_npc[i]) {
                        return null;
                    }
                }
            }
        }
        myLife.setCy(MapleDataTool.getInt(life.getChildByPath("cy")));
        MapleData dF = life.getChildByPath("f");
        if (dF != null) {
            myLife.setF(MapleDataTool.getInt(dF));
        }
        myLife.setFh(MapleDataTool.getInt(life.getChildByPath("fh")));
        myLife.setRx0(MapleDataTool.getInt(life.getChildByPath("rx0")));
        myLife.setRx1(MapleDataTool.getInt(life.getChildByPath("rx1")));
        myLife.setPosition(new Point(MapleDataTool.getInt(life.getChildByPath("x")), MapleDataTool.getInt(life.getChildByPath("y"))));

        if (MapleDataTool.getInt("hide", life, 0) == 1 && myLife instanceof MapleNPC) {
            myLife.setHide(true);
//		} else if (hide > 1) {
//			System.err.println("Hide > 1 ("+ hide +")");
        }
        return myLife;
    }

    private final MapleReactor loadReactor(final MapleData reactor, final String id, final byte FacingDirection) {
        final MapleReactorStats stats = MapleReactorFactory.getReactor(Integer.parseInt(id));
        final MapleReactor myReactor = new MapleReactor(stats, Integer.parseInt(id));

        stats.setFacingDirection(FacingDirection);
        myReactor.setPosition(new Point(MapleDataTool.getInt(reactor.getChildByPath("x")), MapleDataTool.getInt(reactor.getChildByPath("y"))));
        myReactor.setDelay(MapleDataTool.getInt(reactor.getChildByPath("reactorTime")) * 1000);
        myReactor.setState((byte) 0);
        myReactor.setName(MapleDataTool.getString(reactor.getChildByPath("name"), ""));

        return myReactor;
    }

    private String getMapName(int mapid) {
        String mapName = StringUtil.getLeftPaddedStr(Integer.toString(mapid), '0', 9);
        StringBuilder builder = new StringBuilder("Map/Map");
        builder.append(mapid / 100000000);
        builder.append("/");
        builder.append(mapName);
        builder.append(".img");

        mapName = builder.toString();
        return mapName;
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
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            try (java.sql.PreparedStatement ps = con.prepareStatement("SELECT * FROM `wz_customlife`"); ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    final int mapid = rs.getInt("mid");
                    final AbstractLoadedMapleLife myLife = loadLife(rs.getInt("dataid"), rs.getInt("f"), rs.getByte("hide") > 0, rs.getInt("fh"), rs.getInt("cy"), rs.getInt("rx0"), rs.getInt("rx1"), rs.getInt("x"), rs.getInt("y"), rs.getString("type"), rs.getInt("mobtime"));
                    if (myLife == null) {
                        continue;
                    }
                    final List<AbstractLoadedMapleLife> entries = customLife.get(mapid);
                    final List<AbstractLoadedMapleLife> collections = new ArrayList<>();
                    if (entries == null) {
                        collections.add(myLife);
                        customLife.put(mapid, collections);
                    } else {
                        collections.addAll(entries); //re-add
                        collections.add(myLife);
                        customLife.put(mapid, collections);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading custom life..." + e);
        }
    }

    private void addAreaBossSpawn(final MapleMap map) {
        int monsterid = -1;
        int mobtime = -1;
        String msg = null;
        Point pos1 = null, pos2 = null, pos3 = null;

        switch (map.getId()) {
            case 104000400: // Mano
                mobtime = 2700;
                monsterid = 2220000;
                msg = "红蜗牛王出现了";
                pos1 = new Point(439, 185);
                pos2 = new Point(301, -85);
                pos3 = new Point(107, -355);
                break;
            case 101030404: // Stumpy
                mobtime = 2700;
                monsterid = 3220000;
                msg = "树妖王出现了";
                pos1 = new Point(867, 1282);
                pos2 = new Point(810, 1570);
                pos3 = new Point(838, 2197);
                break;
            case 110040000: // King Clang
                mobtime = 1200;
                monsterid = 5220001;
                msg = "巨居蟹出现了";
                pos1 = new Point(-355, 179);
                pos2 = new Point(-1283, -113);
                pos3 = new Point(-571, -593);
                break;
            case 250010304: // Tae Roon
                mobtime = 2100;
                monsterid = 7220000;
                msg = "流浪熊出现了";
                pos1 = new Point(-210, 33);
                pos2 = new Point(-234, 393);
                pos3 = new Point(-654, 33);
                break;
            case 200010300: // Eliza
                mobtime = 1200;
                monsterid = 8220000;
                msg = "艾利杰出现了";
                pos1 = new Point(665, 83);
                pos2 = new Point(672, -217);
                pos3 = new Point(-123, -217);
                break;
            case 250010503: // Ghost Priest
                mobtime = 1800;
                monsterid = 7220002;
                msg = "喵仙怪人出现了";
                pos1 = new Point(-303, 543);
                pos2 = new Point(227, 543);
                pos3 = new Point(719, 543);
                break;
            case 222010310: // Old Fox
                mobtime = 2700;
                monsterid = 7220001;
                msg = "九尾妖狐出现了";
                pos1 = new Point(-169, -147);
                pos2 = new Point(-517, 93);
                pos3 = new Point(247, 93);
                break;
            case 107000300: // Dale
                mobtime = 1800;
                monsterid = 6220000;
                msg = "沼泽巨鳄出现了";
                pos1 = new Point(710, 118);
                pos2 = new Point(95, 119);
                pos3 = new Point(-535, 120);
                break;
            case 100040105: // Faust
                mobtime = 1800;
                monsterid = 5220002;
                msg = "浮士德出现了";
                pos1 = new Point(1000, 278);
                pos2 = new Point(557, 278);
                pos3 = new Point(95, 278);
                break;
            case 100040106: // Faust
                mobtime = 1800;
                monsterid = 5220002;
                msg = "浮士德出现了";
                pos1 = new Point(1000, 278);
                pos2 = new Point(557, 278);
                pos3 = new Point(95, 278);
                break;
            case 220050100: // Timer
                mobtime = 1500;
                monsterid = 5220003;
                msg = "提莫出现了";
                pos1 = new Point(-467, 1032);
                pos2 = new Point(532, 1032);
                pos3 = new Point(-47, 1032);
                break;
            case 221040301: // Jeno
                mobtime = 2400;
                monsterid = 6220001;
                msg = "朱诺出现了";
                pos1 = new Point(-4134, 416);
                pos2 = new Point(-4283, 776);
                pos3 = new Point(-3292, 776);
                break;
            case 240040401: // Lev
                mobtime = 7200;
                monsterid = 8220003;
                msg = "大海兽出现了";
                pos1 = new Point(-15, 2481);
                pos2 = new Point(127, 1634);
                pos3 = new Point(159, 1142);
                break;
            case 260010201: // Dewu
                mobtime = 3600;
                monsterid = 3220001;
                msg = "大宇出现了";
                pos1 = new Point(-215, 275);
                pos2 = new Point(298, 275);
                pos3 = new Point(592, 275);
                break;
            case 261030000: // Chimera
                mobtime = 2700;
                monsterid = 8220002;
                msg = "吉米拉出现了";
                pos1 = new Point(-1094, -405);
                pos2 = new Point(-772, -116);
                pos3 = new Point(-108, 181);
                break;
            case 230020100: // Sherp
                mobtime = 2700;
                monsterid = 4220000;
                msg = "歇尔夫出现了";
                pos1 = new Point(-291, -20);
                pos2 = new Point(-272, -500);
                pos3 = new Point(-462, 640);
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

    private MapleNodes loadNodes(final int mapid, final MapleData mapData) {
        MapleNodes nodeInfo = mapInfos.get(mapid);
        if (nodeInfo == null) {
            nodeInfo = new MapleNodes(mapid);
            if (mapData.getChildByPath("nodeInfo") != null) {
                for (MapleData node : mapData.getChildByPath("nodeInfo")) {
                    try {
                        if (node.getName().equals("start")) {
                            nodeInfo.setNodeStart(MapleDataTool.getInt(node, 0));
                            continue;
                        } else if (node.getName().equals("end")) {
                            nodeInfo.setNodeEnd(MapleDataTool.getInt(node, 0));
                            continue;
                        }
                        List<Integer> edges = new ArrayList<Integer>();
                        if (node.getChildByPath("edge") != null) {
                            for (MapleData edge : node.getChildByPath("edge")) {
                                edges.add(MapleDataTool.getInt(edge, -1));
                            }
                        }
                        final MapleNodeInfo mni = new MapleNodeInfo(
                                Integer.parseInt(node.getName()),
                                MapleDataTool.getIntConvert("key", node, 0),
                                MapleDataTool.getIntConvert("x", node, 0),
                                MapleDataTool.getIntConvert("y", node, 0),
                                MapleDataTool.getIntConvert("attr", node, 0), edges);
                        nodeInfo.addNode(mni);
                    } catch (NumberFormatException e) {
                    } //start, end, edgeInfo = we dont need it
                }
                nodeInfo.sortNodes();
            }
            for (int i = 1; i <= 7; i++) {
                if (mapData.getChildByPath(String.valueOf(i)) != null && mapData.getChildByPath(i + "/obj") != null) {
                    for (MapleData node : mapData.getChildByPath(i + "/obj")) {
                        int sn_count = MapleDataTool.getIntConvert("SN_count", node, 0);
                        String name = MapleDataTool.getString("name", node, "");
                        int speed = MapleDataTool.getIntConvert("speed", node, 0);
                        if (sn_count <= 0 || speed <= 0 || name.equals("")) {
                            continue;
                        }
                        final List<Integer> SN = new ArrayList<Integer>();
                        for (int x = 0; x < sn_count; x++) {
                            SN.add(MapleDataTool.getIntConvert("SN" + x, node, 0));
                        }
                        final MaplePlatform mni = new MaplePlatform(
                                name, MapleDataTool.getIntConvert("start", node, 2), speed,
                                MapleDataTool.getIntConvert("x1", node, 0),
                                MapleDataTool.getIntConvert("y1", node, 0),
                                MapleDataTool.getIntConvert("x2", node, 0),
                                MapleDataTool.getIntConvert("y2", node, 0),
                                MapleDataTool.getIntConvert("r", node, 0), SN);
                        nodeInfo.addPlatform(mni);
                    }
                }
            }
            // load areas (EG PQ platforms)
            if (mapData.getChildByPath("area") != null) {
                int x1, y1, x2, y2;
                Rectangle mapArea;
                for (MapleData area : mapData.getChildByPath("area")) {
                    x1 = MapleDataTool.getInt(area.getChildByPath("x1"));
                    y1 = MapleDataTool.getInt(area.getChildByPath("y1"));
                    x2 = MapleDataTool.getInt(area.getChildByPath("x2"));
                    y2 = MapleDataTool.getInt(area.getChildByPath("y2"));
                    mapArea = new Rectangle(x1, y1, (x2 - x1), (y2 - y1));
                    nodeInfo.addMapleArea(mapArea);
                }
            }
            if (mapData.getChildByPath("monsterCarnival") != null) {
                final MapleData mc = mapData.getChildByPath("monsterCarnival");
                if (mc.getChildByPath("mobGenPos") != null) {
                    for (MapleData area : mc.getChildByPath("mobGenPos")) {
                        nodeInfo.addMonsterPoint(MapleDataTool.getInt(area.getChildByPath("x")),
                                MapleDataTool.getInt(area.getChildByPath("y")),
                                MapleDataTool.getInt(area.getChildByPath("fh")),
                                MapleDataTool.getInt(area.getChildByPath("cy")),
                                MapleDataTool.getInt("team", area, -1));
                    }
                }
                if (mc.getChildByPath("mob") != null) {
                    for (MapleData area : mc.getChildByPath("mob")) {
                        nodeInfo.addMobSpawn(MapleDataTool.getInt(area.getChildByPath("id")), MapleDataTool.getInt(area.getChildByPath("spendCP")));
                    }
                }
                if (mc.getChildByPath("guardianGenPos") != null) {
                    for (MapleData area : mc.getChildByPath("guardianGenPos")) {
                        nodeInfo.addGuardianSpawn(new Point(MapleDataTool.getInt(area.getChildByPath("x")), MapleDataTool.getInt(area.getChildByPath("y"))), MapleDataTool.getInt("team", area, -1));
                    }
                }
                if (mc.getChildByPath("skill") != null) {
                    for (MapleData area : mc.getChildByPath("skill")) {
                        nodeInfo.addSkillId(MapleDataTool.getInt(area));
                    }
                }
            }
            mapInfos.put(mapid, nodeInfo);
        }
        return nodeInfo;
    }
}
