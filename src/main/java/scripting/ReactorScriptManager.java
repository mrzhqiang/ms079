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
package scripting;

import client.MapleClient;
import com.github.mrzhqiang.maplestory.domain.query.QDReactorDrop;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.maps.MapleReactor;
import server.maps.ReactorDropEntry;
import tools.FileoutputUtil;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReactorScriptManager extends AbstractScriptManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReactorScriptManager.class);

    private static final ReactorScriptManager instance = new ReactorScriptManager();
    private final Map<Integer, List<ReactorDropEntry>> drops = new HashMap<>();

    public static final ReactorScriptManager getInstance() {
        return instance;
    }

    public final void act(final MapleClient c, final MapleReactor reactor) {
        try {
            if (c.getPlayer().isGM()) {
                c.getPlayer().dropMessage("[系统提示]当前攻击的反应堆ID:" + reactor.getReactorId());
            }

            final Invocable iv = getInvocable("反应堆/" + reactor.getReactorId() + ".js", c);

            if (iv == null) {
                return;
            }
            final ScriptEngine scriptengine = (ScriptEngine) iv;
            ReactorActionManager rm = new ReactorActionManager(c, reactor);

            scriptengine.put("rm", rm);
            iv.invokeFunction("act");
        } catch (Exception e) {
            LOGGER.error("Error executing reactor script. ReactorID: " + reactor.getReactorId() + ", ReactorName: " + reactor.getName() + ":" + e);
            FileoutputUtil.log(FileoutputUtil.ScriptEx_Log, "Error executing reactor script. ReactorID: " + reactor.getReactorId() + ", ReactorName: " + reactor.getName() + ":" + e);
        }
    }

    public final List<ReactorDropEntry> getDrops(final int rid) {
        List<ReactorDropEntry> ret = drops.get(rid);
        if (ret != null) {
            return ret;
        }
        ret = new QDReactorDrop().reactorid.eq(rid).findStream()
                .map(it -> new ReactorDropEntry(it.itemid, it.chance, it.questid))
                .collect(Collectors.toList());

        drops.put(rid, ret);
        return ret;
    }

    public final void clearDrops() {
        drops.clear();
    }
}
