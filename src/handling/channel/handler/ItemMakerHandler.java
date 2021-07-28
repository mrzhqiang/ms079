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
import java.util.ArrayList;
import java.util.Map;

import client.inventory.IItem;
import client.inventory.Equip;
import client.SkillFactory;
import client.MapleClient;
import client.inventory.MapleInventoryType;
import constants.GameConstants;
import server.ItemMakerFactory;
import server.ItemMakerFactory.GemCreateEntry;
import server.ItemMakerFactory.ItemMakerCreateEntry;
import server.Randomizer;
import server.MapleItemInformationProvider;
import server.MapleInventoryManipulator;
import tools.Pair;
import tools.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class ItemMakerHandler {

    public static final void ItemMaker(final SeekableLittleEndianAccessor slea, final MapleClient c) {
        //System.out.println(slea.toString()); //change?
        final int makerType = slea.readInt();

        switch (makerType) {
            case 1: { // Gem
                final int toCreate = slea.readInt();

                if (GameConstants.isGem(toCreate)) {
                    final GemCreateEntry gem = ItemMakerFactory.getInstance().getGemInfo(toCreate);
                    if (gem == null) {
                        return;
                    }
                    if (!hasSkill(c, gem.getReqSkillLevel())) {
                        return; // H4x
                    }
                    if (c.getPlayer().getMeso() < gem.getCost()) {
                        return; // H4x
                    }
                    final int randGemGiven = getRandomGem(gem.getRandomReward());

                    if (c.getPlayer().getInventory(GameConstants.getInventoryType(randGemGiven)).isFull()) {
                        return; // We'll do handling for this later
                    }
                    final int taken = checkRequiredNRemove(c, gem.getReqRecipes());
                    if (taken == 0) {
                        return; // We'll do handling for this later
                    }
                    c.getPlayer().gainMeso(-gem.getCost(), false);
                    MapleInventoryManipulator.addById(c, randGemGiven, (byte) (taken == randGemGiven ? 9 : 1), (byte) 0); // Gem is always 1

                    c.getSession().write(MaplePacketCreator.ItemMaker_Success());
                    c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.ItemMaker_Success_3rdParty(c.getPlayer().getId()), false);
                } else if (GameConstants.isOtherGem(toCreate)) {
                    //non-gems that are gems
                    //stim and numEnchanter always 0
                    final GemCreateEntry gem = ItemMakerFactory.getInstance().getGemInfo(toCreate);
                    if (gem == null) {
                        return;
                    }
                    if (!hasSkill(c, gem.getReqSkillLevel())) {
                        return; // H4x
                    }
                    if (c.getPlayer().getMeso() < gem.getCost()) {
                        return; // H4x
                    }

                    if (c.getPlayer().getInventory(GameConstants.getInventoryType(toCreate)).isFull()) {
                        return; // We'll do handling for this later
                    }
                    if (checkRequiredNRemove(c, gem.getReqRecipes()) == 0) {
                        return; // We'll do handling for this later
                    }
                    c.getPlayer().gainMeso(-gem.getCost(), false);
                    if (GameConstants.getInventoryType(toCreate) == MapleInventoryType.EQUIP) {
                        MapleInventoryManipulator.addbyItem(c, MapleItemInformationProvider.getInstance().getEquipById(toCreate));
                    } else {
                        MapleInventoryManipulator.addById(c, toCreate, (byte) 1, (byte) 0); // Gem is always 1
                    }

                    c.getSession().write(MaplePacketCreator.ItemMaker_Success());
                    c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.ItemMaker_Success_3rdParty(c.getPlayer().getId()), false);
                } else {
                    final boolean stimulator = slea.readByte() > 0;
                    final int numEnchanter = slea.readInt();

                    final ItemMakerCreateEntry create = ItemMakerFactory.getInstance().getCreateInfo(toCreate);
                    if (create == null) {
                        return;
                    }
                    if (numEnchanter > create.getTUC()) {
                        return; // h4x
                    }
                    if (!hasSkill(c, create.getReqSkillLevel())) {
                        return; // H4x
                    }
                    if (c.getPlayer().getMeso() < create.getCost()) {
                        return; // H4x
                    }
                    if (c.getPlayer().getInventory(GameConstants.getInventoryType(toCreate)).isFull()) {
                        return; // We'll do handling for this later
                    }
                    if (checkRequiredNRemove(c, create.getReqItems()) == 0) {
                        return; // We'll do handling for this later
                    }
                    c.getPlayer().gainMeso(-create.getCost(), false);

                    final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
                    final Equip toGive = (Equip) ii.getEquipById(toCreate);

                    if (stimulator || numEnchanter > 0) {
                        if (c.getPlayer().haveItem(create.getStimulator(), 1, false, true)) {
                            ii.randomizeStats(toGive);
                            MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, create.getStimulator(), 1, false, false);
                        }
                        for (int i = 0; i < numEnchanter; i++) {
                            final int enchant = slea.readInt();
                            if (c.getPlayer().haveItem(enchant, 1, false, true)) {
                                final Map<String, Byte> stats = ii.getItemMakeStats(enchant);
                                if (stats != null) {
                                    addEnchantStats(stats, toGive);
                                    MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, enchant, 1, false, false);
                                }
                            }
                        }
                    }
                    MapleInventoryManipulator.addbyItem(c, toGive);
                    c.getSession().write(MaplePacketCreator.ItemMaker_Success());
                    c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.ItemMaker_Success_3rdParty(c.getPlayer().getId()), false);
                }
                break;
            }
            case 3: { // Making Crystals
                final int etc = slea.readInt();
                if (c.getPlayer().haveItem(etc, 100, false, true)) {
                    MapleInventoryManipulator.addById(c, getCreateCrystal(etc), (short) 1, (byte) 0);
                    MapleInventoryManipulator.removeById(c, MapleInventoryType.ETC, etc, 100, false, false);

                    c.getSession().write(MaplePacketCreator.ItemMaker_Success());
                    c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.ItemMaker_Success_3rdParty(c.getPlayer().getId()), false);
                }
                break;
            }
            case 4: { // Disassembling EQ.
                final int itemId = slea.readInt();
                c.getPlayer().updateTick(slea.readInt());
                final byte slot = (byte) slea.readInt();

                final IItem toUse = c.getPlayer().getInventory(MapleInventoryType.EQUIP).getItem(slot);
                if (toUse == null || toUse.getItemId() != itemId || toUse.getQuantity() < 1) {
                    return;
                }
                final MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();

                if (!ii.isDropRestricted(itemId) && !ii.isAccountShared(itemId)) {
                    final int[] toGive = getCrystal(itemId, ii.getReqLevel(itemId));
                    MapleInventoryManipulator.addById(c, toGive[0], (byte) toGive[1], (byte) 0);
                    MapleInventoryManipulator.removeFromSlot(c, MapleInventoryType.EQUIP, slot, (byte) 1, false);
                }
                c.getSession().write(MaplePacketCreator.ItemMaker_Success());
                c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.ItemMaker_Success_3rdParty(c.getPlayer().getId()), false);
                break;
            }
        }
    }

    private static final int getCreateCrystal(final int etc) {
        int itemid;
        final short level = MapleItemInformationProvider.getInstance().getItemMakeLevel(etc);

        if (level >= 31 && level <= 50) {
            itemid = 4260000;
        } else if (level >= 51 && level <= 60) {
            itemid = 4260001;
        } else if (level >= 61 && level <= 70) {
            itemid = 4260002;
        } else if (level >= 71 && level <= 80) {
            itemid = 4260003;
        } else if (level >= 81 && level <= 90) {
            itemid = 4260004;
        } else if (level >= 91 && level <= 100) {
            itemid = 4260005;
        } else if (level >= 101 && level <= 110) {
            itemid = 4260006;
        } else if (level >= 111 && level <= 120) {
            itemid = 4260007;
        } else if (level >= 121) {
            itemid = 4260008;
        } else {
            throw new RuntimeException("Invalid Item Maker id");
        }
        return itemid;
    }

    private static final int[] getCrystal(final int itemid, final int level) {
        int[] all = new int[2];
        all[0] = -1;
        if (level >= 31 && level <= 50) {
            all[0] = 4260000;
        } else if (level >= 51 && level <= 60) {
            all[0] = 4260001;
        } else if (level >= 61 && level <= 70) {
            all[0] = 4260002;
        } else if (level >= 71 && level <= 80) {
            all[0] = 4260003;
        } else if (level >= 81 && level <= 90) {
            all[0] = 4260004;
        } else if (level >= 91 && level <= 100) {
            all[0] = 4260005;
        } else if (level >= 101 && level <= 110) {
            all[0] = 4260006;
        } else if (level >= 111 && level <= 120) {
            all[0] = 4260007;
        } else if (level >= 121 && level <= 200) {
            all[0] = 4260008;
        } else {
            throw new RuntimeException("Invalid Item Maker type" + level);
        }
        if (GameConstants.isWeapon(itemid) || GameConstants.isOverall(itemid)) {
            all[1] = Randomizer.rand(5, 11);
        } else {
            all[1] = Randomizer.rand(3, 7);
        }
        return all;
    }

    private static final void addEnchantStats(final Map<String, Byte> stats, final Equip item) {
        short s = stats.get("incPAD");
        if (s != 0) {
            item.setWatk((short) (item.getWatk() + s));
        }
        s = stats.get("incMAD");
        if (s != 0) {
            item.setMatk((short) (item.getMatk() + s));
        }
        s = stats.get("incACC");
        if (s != 0) {
            item.setAcc((short) (item.getAcc() + s));
        }
        s = stats.get("incEVA");
        if (s != 0) {
            item.setAvoid((short) (item.getAvoid() + s));
        }
        s = stats.get("incSpeed");
        if (s != 0) {
            item.setSpeed((short) (item.getSpeed() + s));
        }
        s = stats.get("incJump");
        if (s != 0) {
            item.setJump((short) (item.getJump() + s));
        }
        s = stats.get("incMaxHP");
        if (s != 0) {
            item.setHp((short) (item.getHp() + s));
        }
        s = stats.get("incMaxMP");
        if (s != 0) {
            item.setMp((short) (item.getMp() + s));
        }
        s = stats.get("incSTR");
        if (s != 0) {
            item.setStr((short) (item.getStr() + s));
        }
        s = stats.get("incDEX");
        if (s != 0) {
            item.setDex((short) (item.getDex() + s));
        }
        s = stats.get("incINT");
        if (s != 0) {
            item.setInt((short) (item.getInt() + s));
        }
        s = stats.get("incLUK");
        if (s != 0) {
            item.setLuk((short) (item.getLuk() + s));
        }
        s = stats.get("randOption");
        if (s > 0) {
            final boolean success = Randomizer.nextBoolean();
            final int ma = item.getMatk(), wa = item.getWatk();
            if (wa > 0) {
                item.setWatk((short) (success ? (wa + s) : (wa - s)));
            }
            if (ma > 0) {
                item.setMatk((short) (success ? (ma + s) : (ma - s)));
            }
        }
        s = stats.get("randStat");
        if (s > 0) {
            final boolean success = Randomizer.nextBoolean();
            final int str = item.getStr(), dex = item.getDex(), luk = item.getLuk(), int_ = item.getInt();
            if (str > 0) {
                item.setStr((short) (success ? (str + s) : (str - s)));
            }
            if (dex > 0) {
                item.setDex((short) (success ? (dex + s) : (dex - s)));
            }
            if (int_ > 0) {
                item.setInt((short) (success ? (int_ + s) : (int_ - s)));
            }
            if (luk > 0) {
                item.setLuk((short) (success ? (luk + s) : (luk - s)));
            }
        }
    }

    private static final int getRandomGem(final List<Pair<Integer, Integer>> rewards) {
        int itemid;
        final List<Integer> items = new ArrayList<Integer>();

        for (final Pair p : rewards) {
            itemid = (Integer) p.getLeft();
            for (int i = 0; i < (Integer) p.getRight(); i++) {
                items.add(itemid);
            }
        }
        return items.get(Randomizer.nextInt(items.size()));
    }

    private static final int checkRequiredNRemove(final MapleClient c, final List<Pair<Integer, Integer>> recipe) {
        int itemid = 0;
        for (final Pair<Integer, Integer> p : recipe) {
            if (!c.getPlayer().haveItem(p.getLeft(), p.getRight(), false, true)) {
                return 0;
            }
        }
        for (final Pair<Integer, Integer> p : recipe) {
            itemid = p.getLeft();
            MapleInventoryManipulator.removeById(c, GameConstants.getInventoryType(itemid), itemid, p.getRight(), false, false);
        }
        return itemid;
    }

    private static final boolean hasSkill(final MapleClient c, final int reqlvl) {
        if (GameConstants.isKOC(c.getPlayer().getJob())) { // KoC Maker skill.
            return c.getPlayer().getSkillLevel(SkillFactory.getSkill(10001007)) >= reqlvl;
        } else if (GameConstants.isAran(c.getPlayer().getJob())) { // KoC Maker skill.
            return c.getPlayer().getSkillLevel(SkillFactory.getSkill(20001007)) >= reqlvl;
        } else if (GameConstants.isEvan(c.getPlayer().getJob())) { // KoC Maker skill.
            return c.getPlayer().getSkillLevel(SkillFactory.getSkill(20011007)) >= reqlvl;
        } else if (GameConstants.isResist(c.getPlayer().getJob())) { // KoC Maker skill.
            return c.getPlayer().getSkillLevel(SkillFactory.getSkill(30001007)) >= reqlvl;
        } else {
            return c.getPlayer().getSkillLevel(SkillFactory.getSkill(1007)) >= reqlvl;
        }
    }
}
