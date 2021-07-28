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
package handling.world;

import client.MapleCoolDownValueHolder;
import client.MapleDiseaseValueHolder;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerBuffStorage implements Serializable {

    private static final Map<Integer, List<PlayerBuffValueHolder>> buffs = new ConcurrentHashMap<Integer, List<PlayerBuffValueHolder>>();
    private static final Map<Integer, List<MapleCoolDownValueHolder>> coolDowns = new ConcurrentHashMap<Integer, List<MapleCoolDownValueHolder>>();
    private static final Map<Integer, List<MapleDiseaseValueHolder>> diseases = new ConcurrentHashMap<Integer, List<MapleDiseaseValueHolder>>();

    public static final void addBuffsToStorage(final int chrid, final List<PlayerBuffValueHolder> toStore) {
        buffs.put(chrid, toStore);
    }

    public static final void addCooldownsToStorage(final int chrid, final List<MapleCoolDownValueHolder> toStore) {
        coolDowns.put(chrid, toStore);
    }

    public static final void addDiseaseToStorage(final int chrid, final List<MapleDiseaseValueHolder> toStore) {
        diseases.put(chrid, toStore);
    }

    public static final List<PlayerBuffValueHolder> getBuffsFromStorage(final int chrid) {
        return buffs.remove(chrid);
    }

    public static final List<MapleCoolDownValueHolder> getCooldownsFromStorage(final int chrid) {
        return coolDowns.remove(chrid);
    }

    public static final List<MapleDiseaseValueHolder> getDiseaseFromStorage(final int chrid) {
        return diseases.remove(chrid);
    }
}
