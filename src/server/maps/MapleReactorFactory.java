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

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import provider.MapleData;
import provider.MapleDataProvider;
import provider.MapleDataProviderFactory;
import provider.MapleDataTool;
import tools.Pair;
import tools.StringUtil;

public class MapleReactorFactory {

    private static final MapleDataProvider data = MapleDataProviderFactory.getDataProvider(new File(System.getProperty("net.sf.odinms.wzpath") + "/Reactor.wz"));
    private static Map<Integer, MapleReactorStats> reactorStats = new HashMap<Integer, MapleReactorStats>();

    public static final MapleReactorStats getReactor(int rid) {
        MapleReactorStats stats = reactorStats.get(Integer.valueOf(rid));
        if (stats == null) {
            int infoId = rid;
            MapleData reactorData = data.getData(StringUtil.getLeftPaddedStr(Integer.toString(infoId) + ".img", '0', 11));
            MapleData link = reactorData.getChildByPath("info/link");
            if (link != null) {
                infoId = MapleDataTool.getIntConvert("info/link", reactorData);
                stats = reactorStats.get(Integer.valueOf(infoId));
            }
            if (stats == null) {
                stats = new MapleReactorStats();
                reactorData = data.getData(StringUtil.getLeftPaddedStr(Integer.toString(infoId) + ".img", '0', 11));
                if (reactorData == null) {
                    return stats;
                }
                /*int i = 0;
                 switch(rid) {
                 case 2002011:
                 case 2002012:
                 case 2002013:
                 case 2618003:
                 case 2618004:
                 case 2618005:
                 case 2618006:
                 case 2618007:
                 case 2002018:
                 case 9101000:
                 case 2512001:
                 case 6109016:
                 case 6109017:
                 case 6109018:
                 case 6109019:
                 case 6109020:
                 i = 1;
                 break;
                 case 2006000:
                 i = 20;
                 break;
                 }
                 for (int x = 0; x < i; x++) {
                 stats.addState((byte) x, 0, null, (byte) (x+1), 0); //test
                 }*/
                boolean canTouch = MapleDataTool.getInt("info/activateByTouch", reactorData, 0) > 0;
                boolean areaSet = false;
                boolean foundState = false;
                for (byte i = 0; true; i++) {
                    MapleData reactorD = reactorData.getChildByPath(String.valueOf(i));
                    if (reactorD == null) {
                        break;
                    }
                    MapleData reactorInfoData_ = reactorD.getChildByPath("event");
                    if (reactorInfoData_ != null && reactorInfoData_.getChildByPath("0") != null) {
                        MapleData reactorInfoData = reactorInfoData_.getChildByPath("0");
                        Pair<Integer, Integer> reactItem = null;
                        int type = MapleDataTool.getIntConvert("type", reactorInfoData);
                        if (type == 100) { //reactor waits for item
                            reactItem = new Pair<Integer, Integer>(MapleDataTool.getIntConvert("0", reactorInfoData), MapleDataTool.getIntConvert("1", reactorInfoData, 1));
                            if (!areaSet) { //only set area of effect for item-triggered reactors once
                                stats.setTL(MapleDataTool.getPoint("lt", reactorInfoData));
                                stats.setBR(MapleDataTool.getPoint("rb", reactorInfoData));
                                areaSet = true;
                            }
                        }
                        foundState = true;
                        stats.addState(i, type, reactItem, (byte) MapleDataTool.getIntConvert("state", reactorInfoData), MapleDataTool.getIntConvert("timeOut", reactorInfoData_, -1), (byte) ((MapleDataTool.getIntConvert("2", reactorInfoData, 0) > 0) || (reactorInfoData.getChildByPath("clickArea") != null) || (type == 9) ? 1 : canTouch ? 2 : 0));
                    } else {
                        stats.addState(i, 999, null, (byte) (foundState ? -1 : (i + 1)), 0, (byte) 0);
                    }
                }
                reactorStats.put(Integer.valueOf(infoId), stats);
                if (rid != infoId) {
                    reactorStats.put(Integer.valueOf(rid), stats);
                }
            } else { // stats exist at infoId but not rid; add to map
                reactorStats.put(Integer.valueOf(rid), stats);
            }
        }
        return stats;
    }
}
