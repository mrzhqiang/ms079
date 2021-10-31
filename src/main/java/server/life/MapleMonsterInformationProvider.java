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

import com.github.mrzhqiang.maplestory.domain.query.QDDropData;
import com.github.mrzhqiang.maplestory.domain.query.QDDropDataGlobal;
import constants.GameConstants;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import client.inventory.MapleInventoryType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MapleMonsterInformationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapleMonsterInformationProvider.class);

    private static final MapleMonsterInformationProvider instance = new MapleMonsterInformationProvider();
    private final Map<Integer, List<MonsterDropEntry>> drops = new HashMap<Integer, List<MonsterDropEntry>>();
    private final List<MonsterGlobalDropEntry> globaldrops = new ArrayList<MonsterGlobalDropEntry>();

    protected MapleMonsterInformationProvider() {
        retrieveGlobal();
    }

    public static final MapleMonsterInformationProvider getInstance() {
        return instance;
    }

    public final List<MonsterGlobalDropEntry> getGlobalDrop() {
        return globaldrops;
    }

    private void retrieveGlobal() {
        new QDDropDataGlobal().chance.gt(0).findEach(it ->
                globaldrops.add(new MonsterGlobalDropEntry(it)));
    }

    public List<MonsterDropEntry> retrieveDrop(int monsterId) {
        if (drops.containsKey(monsterId)) {
            return drops.get(monsterId);
        }

        List<MonsterDropEntry> ret = new LinkedList<>();

        new QDDropData().dropperid.eq(monsterId).findEach(it -> {
            if (GameConstants.getInventoryType(it.itemid) == MapleInventoryType.EQUIP) {
                it.chance = it.chance / 3; //in GMS/SEA it was raised
            }
            ret.add(new MonsterDropEntry(it));
        });
        drops.put(monsterId, ret);
        return ret;
    }

    public final void clearDrops() {
        drops.clear();
        globaldrops.clear();
        retrieveGlobal();
    }
}
