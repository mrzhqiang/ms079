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
package handling.channel.handler;

import java.util.List;

import client.inventory.IItem;
import client.MapleClient;
import client.MapleCharacter;
import client.MapleDisease;
import client.inventory.ExpTable;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import constants.GameConstants;
import client.inventory.PetCommand;
import client.inventory.PetDataFactory;
import handling.world.MaplePartyCharacter;
import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import server.Randomizer;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import server.life.MapleMonster;
import server.movement.LifeMovementFragment;
import server.maps.FieldLimitType;
import server.maps.MapleMapItem;
import tools.MaplePacketCreator;
import tools.packet.PetPacket;
import tools.data.input.SeekableLittleEndianAccessor;

public class PetHandler {

    public static final void PickExceptionList(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null || chr.getMap() == null) {
            return;
        }
        int itemid = slea.readInt();
        if (!chr.haveItem(itemid)) {
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
    }

    public static final void SpawnPet(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        chr.updateTick(slea.readInt());
        byte slot = slea.readByte();
        slea.readByte();
        chr.spawnPet(slot, slea.readByte() > 0);

    }

    public static final void Pet_AutoPotion(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        slea.skip(13);
        final byte slot = slea.readByte();
        if (chr == null || !chr.isAlive() || chr.getMapId() == 749040100 || chr.getMap() == null || chr.hasDisease(MapleDisease.POTION)) {
            return;
        }
        final IItem toUse = chr.getInventory(MapleInventoryType.USE).getItem(slot);

        if (toUse == null || toUse.getQuantity() < 1) {
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        final long time = System.currentTimeMillis();
        if (chr.getNextConsume() > time) {
            chr.dropMessage(5, "你可能不使用这个项目.");
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        if (!FieldLimitType.PotionUse.check(chr.getMap().getFieldLimit()) || chr.getMapId() == 610030600) { //cwk quick hack
            if (MapleItemInformationProvider.getInstance().getItemEffect(toUse.getItemId()).applyTo(chr)) {
                MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.USE, slot, (short) 1, false);
                if (chr.getMap().getConsumeItemCoolTime() > 0) {
                    chr.setNextConsume(time + (chr.getMap().getConsumeItemCoolTime() * 1000));
                }
            }
        } else {
            c.getSession().write(MaplePacketCreator.enableActions());
        }
    }

    public static final void PetChat(final int petid, final short command, final String text, MapleCharacter chr) {
        if (chr == null || chr.getMap() == null || chr.getPetIndex(petid) < 0) {
            return;
        }
        chr.getMap().broadcastMessage(chr, PetPacket.petChat(chr.getId(), command, text, chr.getPetIndex(petid)), true);
    }

    public static final void PetCommand(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        final byte petIndex = chr.getPetIndex(slea.readInt());
        if (petIndex == -1) {
            return;
        }
        MaplePet pet = chr.getPet(petIndex);
        if (pet == null) {
            return;
        }
        slea.skip(5);
        final byte command = slea.readByte();
        final PetCommand petCommand = PetDataFactory.getPetCommand(pet.getPetItemId(), (int) command);

        boolean success = false;
        if (Randomizer.nextInt(99) <= petCommand.getProbability()) {
            success = true;
            if (pet.getCloseness() < 30000) {
                int newCloseness = pet.getCloseness() + petCommand.getIncrease();
                if (newCloseness > 30000) {
                    newCloseness = 30000;
                }
                pet.setCloseness(newCloseness);
                if (newCloseness >= GameConstants.getClosenessNeededForLevel(pet.getLevel() + 1)) {
                    pet.setLevel(pet.getLevel() + 1);
                    c.getSession().write(PetPacket.showOwnPetLevelUp(petIndex));
                    chr.getMap().broadcastMessage(PetPacket.showPetLevelUp(chr, petIndex));
                }
                c.getSession().write(PetPacket.updatePet(pet, chr.getInventory(MapleInventoryType.CASH).getItem((byte) pet.getInventoryPosition()), true));
            }
        }
        c.getPlayer().getMap().broadcastMessage(c.getPlayer(), PetPacket.commandResponse(c.getPlayer().getId(), petIndex, command, success), true);
        //chr.getMap().broadcastMessage(chr, PetPacket.commandResponse(chr.getId(), command, petIndex, success, false), true);
    }

    public static final void PetFood(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (c.getPlayer().getNoPets() == 0) {
            c.getSession().write(MaplePacketCreator.enableActions());
            return;
        }
        int slot = 0;
        List<MaplePet> pets = c.getPlayer().getPets();
        for (MaplePet pet : pets) {
            if (pet.getFullness() < 100) {
                slot = c.getPlayer().getPetSlot(pet);
            }
        }
        MaplePet pet = c.getPlayer().getPet(slot);
        slea.readInt();
        slea.readShort();
        int itemId = slea.readInt();

        boolean gainCloseness = false;

        if (Randomizer.nextInt(101) > 50) {
            gainCloseness = true;
        }
        if (pet.getFullness() < 100) {
            int newFullness = pet.getFullness() + 30;
            if (newFullness > 100) {
                newFullness = 100;
            }
            pet.setFullness(newFullness);
            if ((gainCloseness) && (pet.getCloseness() < 30000)) {
                int newCloseness = pet.getCloseness() + 1;
                if (newCloseness > 30000) {
                    newCloseness = 30000;
                }
                pet.setCloseness(newCloseness);
                if (newCloseness >= ExpTable.getClosenessNeededForLevel(pet.getLevel() + 1)) {
                    pet.setLevel(pet.getLevel() + 1);
                }

            }

            c.getSession().write(PetPacket.updatePet(pet, c.getPlayer().getInventory(MapleInventoryType.CASH).getItem((byte) pet.getInventoryPosition()), true));
            c.getPlayer().getMap().broadcastMessage(c.getPlayer(), PetPacket.commandResponse(c.getPlayer().getId(), slot, 1, true), true);
        } else {
            if (gainCloseness) {
                int newCloseness = pet.getCloseness() - 1;
                if (newCloseness < 0) {
                    newCloseness = 0;
                }
                pet.setCloseness(newCloseness);
                if (newCloseness < ExpTable.getClosenessNeededForLevel(pet.getLevel())) {
                    pet.setLevel(pet.getLevel() - 1);
                }
            }

            c.getPlayer().getMap().broadcastMessage(c.getPlayer(), PetPacket.commandResponse(c.getPlayer().getId(), slot, 1, false), true);
        }
        MapleInventoryManipulator.removeById(c, MapleInventoryType.USE, itemId, 1, true, false);
    }

    public static final void MovePet(final SeekableLittleEndianAccessor slea, final MapleCharacter chr) {
        final int petId = slea.readInt();
        slea.skip(8);
        final List<LifeMovementFragment> res = MovementParse.parseMovement(slea, 3);

        if (res != null && chr != null && res.size() != 0) { // map crash hack
            final byte slot = chr.getPetIndex(petId);
            if (slot == -1) {
                return;
            }
            chr.getPet(slot).updatePosition(res);
            chr.getMap().broadcastMessage(chr, PetPacket.movePet(chr.getId(), petId, slot, res), false);
            if (chr.getPlayerShop() != null || chr.getConversation() > 0 || chr.getTrade() != null) { //hack
                return;
            }
            if (chr.getStat().hasVac && (chr.getStat().hasMeso || chr.getStat().hasItem)) {
                List<MapleMapItem> objects = chr.getMap().getAllItems();
                for (MapleMapItem mapitem : objects) {
                    final Lock lock = mapitem.getLock();
                    lock.lock();
                    try {
                        if (mapitem.isPickedUp()) {
                            continue;
                        }
                        if (mapitem.getOwner() != chr.getId() && mapitem.isPlayerDrop()) {
                            continue;
                        }
                        if (mapitem.getOwner() != chr.getId() && ((!mapitem.isPlayerDrop() && mapitem.getDropType() == 0) || (mapitem.isPlayerDrop() && chr.getMap().getEverlast()))) {
                            continue;
                        }
                        if (!mapitem.isPlayerDrop() && mapitem.getDropType() == 1 && mapitem.getOwner() != chr.getId() && (chr.getParty() == null || chr.getParty().getMemberById(mapitem.getOwner()) == null)) {
                            continue;
                        }
                        if (mapitem.getMeso() > 0 && chr.getStat().hasMeso) {
                            if (chr.getParty() != null && mapitem.getOwner() != chr.getId()) {
                                final List<MapleCharacter> toGive = new LinkedList<MapleCharacter>();

                                for (MaplePartyCharacter mem : chr.getParty().getMembers()) {
                                    MapleCharacter m = chr.getMap().getCharacterById(mem.getId());
                                    if (m != null) {
                                        toGive.add(m);
                                    }
                                }
                                for (final MapleCharacter m : toGive) {
                                    m.gainMeso(mapitem.getMeso() / toGive.size() + (m.getStat().hasPartyBonus ? (int) (mapitem.getMeso() / 20.0) : 0), true, true);
                                }
                            } else {
                                chr.gainMeso(mapitem.getMeso(), true, true);
                            }
                            InventoryHandler.removeItem_Pet(chr, mapitem, slot);
                        } else if (chr.getStat().hasItem && MapleItemInformationProvider.getInstance().isPickupBlocked(mapitem.getItem().getItemId())) {
                            if (InventoryHandler.useItem(chr.getClient(), mapitem.getItemId())) {
                                InventoryHandler.removeItem_Pet(chr, mapitem, slot);
                            } else if (MapleInventoryManipulator.checkSpace(chr.getClient(), mapitem.getItem().getItemId(), mapitem.getItem().getQuantity(), mapitem.getItem().getOwner())) {
                                if (mapitem.getItem().getQuantity() >= 50 && GameConstants.isUpgradeScroll(mapitem.getItem().getItemId())) {

                                    chr.getClient().setMonitored(true); //hack check

                                }

                                if (MapleInventoryManipulator.addFromDrop(chr.getClient(), mapitem.getItem(), true, mapitem.getDropper() instanceof MapleMonster)) {

                                    InventoryHandler.removeItem_Pet(chr, mapitem, slot);
                                }
                            }
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
    }
}
