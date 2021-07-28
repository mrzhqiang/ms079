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

import java.awt.Point;

import client.MapleClient;
import client.anticheat.CheatingOffense;
import handling.channel.ChannelServer;
import scripting.PortalScriptManager;
import server.MaplePortal;
import tools.MaplePacketCreator;

public class MapleGenericPortal implements MaplePortal {

    private String name, target, scriptName;
    private Point position;
    private int targetmap, type, id;
    private boolean portalState = true;

    public MapleGenericPortal(final int type) {
        this.type = type;
    }

    @Override
    public final int getId() {
        return id;
    }

    public final void setId(int id) {
        this.id = id;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public final Point getPosition() {
        return position;
    }

    @Override
    public final String getTarget() {
        return target;
    }

    @Override
    public final int getTargetMapId() {
        return targetmap;
    }

    @Override
    public final int getType() {
        return type;
    }

    @Override
    public final String getScriptName() {
        return scriptName;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final void setPosition(final Point position) {
        this.position = position;
    }

    public final void setTarget(final String target) {
        this.target = target;
    }

    public final void setTargetMapId(final int targetmapid) {
        this.targetmap = targetmapid;
    }

    @Override
    public final void setScriptName(final String scriptName) {
        this.scriptName = scriptName;
    }

    @Override
    public final void enterPortal(final MapleClient c) {
        if (getPosition().distanceSq(c.getPlayer().getPosition()) > 22500) {
            c.getPlayer().getCheatTracker().registerOffense(CheatingOffense.使用过远传送点);
        }
        final MapleMap currentmap = c.getPlayer().getMap();
        if (portalState || c.getPlayer().isGM()) {
            if (getScriptName() != null) {
                c.getPlayer().checkFollow();
                try {
                    PortalScriptManager.getInstance().executePortalScript(this, c);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            } else if (getTargetMapId() != 999999999) {
                final MapleMap to = ChannelServer.getInstance(c.getChannel()).getMapFactory().getMap(getTargetMapId());
                if (!c.getPlayer().isGM()) {
                    if (to == null) {
                        c.getPlayer().dropMessage(5, "本地图目前尚未开放.");
                        c.getSession().write(MaplePacketCreator.enableActions());
                        return;
                    }
                    if (to.getLevelLimit() > 0 && to.getLevelLimit() > c.getPlayer().getLevel()) {
                        c.getPlayer().dropMessage(5, "You are too low of a level to enter this place.");
                        c.getSession().write(MaplePacketCreator.enableActions());
                        return;
                    }
                    //if (to.getForceMove() > 0 && to.getForceMove() < c.getPlayer().getLevel()) {
                    //    c.getPlayer().dropMessage(5, "You are too high of a level to enter this place.");
                    //    c.getSession().write(MaplePacketCreator.enableActions());
                    //    return;
                    //}
                } else if (to == null) {
                    c.getPlayer().dropMessage(5, "本地图目前尚未开放.");
                    c.getSession().write(MaplePacketCreator.enableActions());
                    return;
                }
                c.getPlayer().changeMapPortal(to, to.getPortal(getTarget()) == null ? to.getPortal(0) : to.getPortal(getTarget())); //late resolving makes this harder but prevents us from loading the whole world at once
            }
        }
        if (c != null && c.getPlayer() != null && c.getPlayer().getMap() == currentmap) { // Character is still on the same map.
            c.getSession().write(MaplePacketCreator.enableActions());
        }
    }

    @Override
    public boolean getPortalState() {
        return portalState;
    }

    @Override
    public void setPortalState(boolean ps) {
        this.portalState = ps;
    }
}
