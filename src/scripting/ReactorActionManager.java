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

import java.awt.Point;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import client.inventory.Equip;
import client.inventory.IItem;
import client.inventory.Item;
import constants.GameConstants;
import client.MapleCharacter;
import client.MapleClient;
import client.inventory.MapleInventoryType;
import handling.channel.ChannelServer;
import server.MapleCarnivalFactory;
import server.MapleCarnivalFactory.MCSkill;
import server.MapleItemInformationProvider;
import server.Randomizer;
import server.life.MapleLifeFactory;
import server.maps.ReactorDropEntry;
import server.maps.MapleReactor;
import tools.MaplePacketCreator;
import server.life.MapleMonster;
import server.maps.MapleMap;

public class ReactorActionManager extends AbstractPlayerInteraction {

    private MapleReactor reactor;

    public ReactorActionManager(MapleClient c, MapleReactor reactor) {
        super(c);
        this.reactor = reactor;
    }

    // only used for meso = false, really. No minItems because meso is used to fill the gap
    public void dropItems() {
        dropItems(false, 0, 0, 0, 0);
    }

    public void dropItems(boolean meso, int mesoChance, int minMeso, int maxMeso) {
        dropItems(meso, mesoChance, minMeso, maxMeso, 0);
    }

    public void dropItems(boolean meso, int mesoChance, int minMeso, int maxMeso, int minItems) {
        final List<ReactorDropEntry> chances = ReactorScriptManager.getInstance().getDrops(reactor.getReactorId());
        final List<ReactorDropEntry> items = new LinkedList<ReactorDropEntry>();

        if (meso) {
            if (Math.random() < (1 / (double) mesoChance)) {
                items.add(new ReactorDropEntry(0, mesoChance, -1));
            }
        }

        int numItems = 0;
        // narrow list down by chances
        final Iterator<ReactorDropEntry> iter = chances.iterator();
        // for (DropEntry d : chances) {
        while (iter.hasNext()) {
            ReactorDropEntry d = (ReactorDropEntry) iter.next();
            double count = (1.0 / d.chance);
            if (Math.random() < (1.0 / (double) d.chance) && (d.questid <= 0 || getPlayer().getQuestStatus(d.questid) == 1)) {
                numItems++;
                items.add(d);
            }
        }

        // if a minimum number of drops is required, add meso
        while (items.size() < minItems) {
            items.add(new ReactorDropEntry(0, mesoChance, -1));
            numItems++;
        }
        final Point dropPos = reactor.getPosition();

        dropPos.x -= (12 * numItems);

        int range, mesoDrop;
        final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        for (final ReactorDropEntry d : items) {
            if (d.itemId == 0) {
                range = maxMeso - minMeso;
                mesoDrop = Randomizer.nextInt(range) + minMeso * ChannelServer.getInstance(getClient().getChannel()).getMesoRate();
                reactor.getMap().spawnMesoDrop(mesoDrop, dropPos, reactor, getPlayer(), false, (byte) 0);
            } else {
                IItem drop;
                if (GameConstants.getInventoryType(d.itemId) != MapleInventoryType.EQUIP) {
                    drop = new Item(d.itemId, (byte) 0, (short) 1, (byte) 0);
                } else {
                    drop = ii.randomizeStats((Equip) ii.getEquipById(d.itemId));
                }
                reactor.getMap().spawnItemDrop(reactor, getPlayer(), drop, dropPos, false, false);
            }
            dropPos.x += 25;
        }
    }

    @Override
    public void spawnNpc(int npcId) {
        spawnNpc(npcId, getPosition());
    }

    // returns slightly above the reactor's position for monster spawns
    public Point getPosition() {
        Point pos = reactor.getPosition();
        pos.y -= 10;
        return pos;
    }

    public MapleReactor getReactor() {
        return reactor;
    }

    public void spawnZakum() {
        reactor.getMap().spawnZakum(getPosition().x, getPosition().y);
    }

    public void spawnFakeMonster(int id) {
        spawnFakeMonster(id, 1, getPosition());
    }

    // summon one monster, remote location
    public void spawnFakeMonster(int id, int x, int y) {
        spawnFakeMonster(id, 1, new Point(x, y));
    }

    // multiple monsters, reactor location
    public void spawnFakeMonster(int id, int qty) {
        spawnFakeMonster(id, qty, getPosition());
    }

    // multiple monsters, remote location
    public void spawnFakeMonster(int id, int qty, int x, int y) {
        spawnFakeMonster(id, qty, new Point(x, y));
    }

    // handler for all spawnFakeMonster
    private void spawnFakeMonster(int id, int qty, Point pos) {
        for (int i = 0; i < qty; i++) {
            reactor.getMap().spawnFakeMonsterOnGroundBelow(MapleLifeFactory.getMonster(id), pos);
        }
    }

    public void killAll() {
        reactor.getMap().killAllMonsters(true);
    }

    public void killMonster(int monsId) {
        reactor.getMap().killMonster(monsId);
    }

    // summon one monster on reactor location
    @Override
    public void spawnMonster(int id) {
        spawnMonster(id, 1, getPosition());
    }

    // summon monsters on reactor location
    @Override
    public void spawnMonster(int id, int qty) {
        spawnMonster(id, qty, getPosition());
    }

    public void dispelAllMonsters(final int num) { //dispels all mobs, cpq
        final MCSkill skil = MapleCarnivalFactory.getInstance().getGuardian(num);
        if (skil != null) {
            for (MapleMonster mons : getMap().getAllMonstersThreadsafe()) {
                mons.dispelSkill(skil.getSkill());
            }
        }
    }
}
