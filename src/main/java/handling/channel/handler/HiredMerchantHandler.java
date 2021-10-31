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

import client.inventory.IItem;
import client.inventory.MapleInventoryType;
import client.MapleClient;
import client.MapleCharacter;
import com.github.mrzhqiang.maplestory.domain.DHiredMerch;
import com.github.mrzhqiang.maplestory.domain.query.QDHiredMerch;
import constants.GameConstants;
import client.inventory.ItemLoader;
import handling.world.World;

import java.util.Map;

import server.MapleInventoryManipulator;
import server.MerchItemPackage;
import server.MapleItemInformationProvider;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.packet.PlayerShopPacket;
import tools.data.input.SeekableLittleEndianAccessor;

public class HiredMerchantHandler {

    public static final void UseHiredMerchant(final SeekableLittleEndianAccessor slea, final MapleClient c) {
//	slea.readInt(); // TimeStamp

        if (c.getPlayer().getMap().allowPersonalShop()) {
            final byte state = checkExistance(c.getPlayer().getAccountID(), c.getPlayer().getId());

            switch (state) {
                case 1:
                    c.getPlayer().dropMessage(1, "请先去找弗兰德里领取你之前摆摊的东西");
                    // "(第二組密碼隨便打)");
                    break;
                case 0:
                    boolean merch = World.hasMerchant(c.getPlayer().getAccountID());
                    if (!merch) {
//		    c.getPlayer().dropMessage(1, "雇佣商人不開放嚕");
                        c.getSession().write(PlayerShopPacket.sendTitleBox());
                    } else {
                        c.getPlayer().dropMessage(1, "请换个地方开或者是你已经有开店了");
                    }
                    break;
                default:
                    c.getPlayer().dropMessage(1, "发生未知错误.");
                    break;
            }
        } else {
            c.getSession().close();
        }
    }

    private static byte checkExistance(int accid, int charid) {
        DHiredMerch one = new QDHiredMerch().account.id.eq(accid).character.id.eq(charid).findOne();
        if (one != null) {
            return 1;
        }
        return 0;
    }

    public static void MerchantItemStore(final SeekableLittleEndianAccessor slea, final MapleClient c) {
        if (c.getPlayer() == null) {
            return;
        }
        final byte operation = slea.readByte();
        if (operation == 20 || operation == 26) {
            if (c.getPlayer().getLastHM() + 24 * 60 * 60 * 1000 > System.currentTimeMillis()) {
                c.getPlayer().dropMessage(1, "24小时内无法进行操作，\r\n请24小时之后再进行操作。\r\n");
                c.getSession().write(MaplePacketCreator.enableActions());
                c.getPlayer().setConversation(0);
                return;
            }
        }
        switch (operation) {
            case 20: {
                slea.readMapleAsciiString();

                final int conv = c.getPlayer().getConversation();
                boolean merch = World.hasMerchant(c.getPlayer().getAccountID());
                if (merch) {
                    c.getPlayer().dropMessage(1, "请关闭商店后再试一次.");
                    c.getPlayer().setConversation(0);
                } else if (conv == 3) { // Hired Merch 雇来的东西
                    final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getId(), c.getPlayer().getAccountID());

                    if (pack == null) {
                        c.getPlayer().dropMessage(1, "你没有物品可以领取!");
                        c.getPlayer().setConversation(0);
                    } else if (pack.getItems().size() <= 0) { //error fix for complainers.对于抱怨错误修复。
                        if (!check(c.getPlayer(), pack)) {
                            c.getSession().write(PlayerShopPacket.merchItem_Message((byte) 0x21));
                            return;
                        }
                        if (deletePackage(c.getPlayer().getId(), c.getPlayer().getAccountID(), pack.getPackageid())) {
                            FileoutputUtil.logToFile_chr(c.getPlayer(), "日志/logs/Log_雇佣金币领取记录.txt", " 领回金币 " + pack.getMesos());
                            c.getPlayer().gainMeso(pack.getMesos(), false);
                            c.getPlayer().setConversation(0);
                            c.getPlayer().dropMessage("领取金币" + pack.getMesos());
                            //     c.getSession().write(PlayerShopPacket.merchItem_Message((byte) 0x1d));
                            //      c.getPlayer().setLastHM(System.currentTimeMillis());
                        } else {
                            c.getPlayer().dropMessage(1, "发生未知错误。");
                        }
                        c.getPlayer().setConversation(0);
                        c.getSession().write(MaplePacketCreator.enableActions());
                    } else {
                        c.getSession().write(PlayerShopPacket.merchItemStore_ItemData(pack));
                    }
                }
                break;
            }
            case 25: { // 要求拿出物品
                if (c.getPlayer().getConversation() != 3) {
                    return;
                }
                c.getSession().write(PlayerShopPacket.merchItemStore((byte) 0x24));
                break;
            }
            case 26: { // 取出物品
                if (c.getPlayer().getConversation() != 3) {
                    c.getPlayer().dropMessage(1, "发生未知错误1.");
                    c.getSession().write(MaplePacketCreator.enableActions());
                    return;
                }
                final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getId(), c.getPlayer().getAccountID());

                if (pack == null) {
                    c.getPlayer().dropMessage(1, "发生未知错误。\r\n你没有物品可以领取！");
                    return;
                }
                if (!check(c.getPlayer(), pack)) {
                    c.getSession().write(PlayerShopPacket.merchItem_Message((byte) 0x21));
                    return;
                }
                if (deletePackage(c.getPlayer().getId(), c.getPlayer().getAccountID(), pack.getPackageid())) {
                    c.getPlayer().gainMeso(pack.getMesos(), false);
                    for (IItem item : pack.getItems()) {
                        MapleInventoryManipulator.addFromDrop(c, item, false);
                    }
                    c.getSession().write(PlayerShopPacket.merchItem_Message((byte) 0x1d));
                    String item_id = "";
                    String item_name = "";
                    for (IItem item : pack.getItems()) {
                        item_id += item.getItemId() + "(" + item.getQuantity() + "), ";
                        item_name += MapleItemInformationProvider.getInstance().getName(item.getItemId()) + "(" + item.getQuantity() + "), ";
                    }
                    FileoutputUtil.logToFile_chr(c.getPlayer(), "日志/logs/Log_雇佣领取记录.txt", " 领回金币 " + pack.getMesos() + " 领回道具数量 " + pack.getItems().size() + " 道具 " + item_id);
                    FileoutputUtil.logToFile_chr(c.getPlayer(), "日志/logs/Log_雇佣领取记录2.txt", " 领回金币 " + pack.getMesos() + " 领回道具数量 " + pack.getItems().size() + " 道具 " + item_name);
                    //    c.getPlayer().setLastHM(System.currentTimeMillis());
                } else {
                    c.getPlayer().dropMessage(1, "发生未知错误.");
                }
                break;
            }
            case 27: { // Exit
                c.getPlayer().setConversation(0);
                break;
            }
        }
    }

    private static void getShopItem(MapleClient c) {
        if (c.getPlayer().getConversation() != 3) {
            return;
        }
        final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getId(), c.getPlayer().getAccountID());

        if (pack == null) {
            c.getPlayer().dropMessage(1, "发生未知错误。");
            return;
        }
        if (!check(c.getPlayer(), pack)) {
            c.getPlayer().dropMessage(1, "你背包格子不够。");
            //    c.getSession().write(PlayerShopPacket.merchItem_Message((byte) 0x21));
            return;
        }
        if (deletePackage(c.getPlayer().getId(), c.getPlayer().getAccountID(), pack.getPackageid())) {
            c.getPlayer().gainMeso(pack.getMesos(), false);
            for (IItem item : pack.getItems()) {
                MapleInventoryManipulator.addFromDrop(c, item, false);
            }
            c.getPlayer().dropMessage(5, "领取成功。");
            //  c.getSession().write(PlayerShopPacket.merchItem_Message((byte) 0x1d));
        } else {
            c.getPlayer().dropMessage(1, "发生未知错误。");
        }
    }

    private static final boolean check(final MapleCharacter chr, final MerchItemPackage pack) {
        if (chr.getMeso() + pack.getMesos() < 0) {
            return false;
        }
        byte eq = 0, use = 0, setup = 0, etc = 0, cash = 0;
        for (IItem item : pack.getItems()) {
            final MapleInventoryType invtype = GameConstants.getInventoryType(item.getItemId());
            if (invtype == MapleInventoryType.EQUIP) {
                eq++;
            } else if (invtype == MapleInventoryType.USE) {
                use++;
            } else if (invtype == MapleInventoryType.SETUP) {
                setup++;
            } else if (invtype == MapleInventoryType.ETC) {
                etc++;
            } else if (invtype == MapleInventoryType.CASH) {
                cash++;
            }
            /*
             * if
             * (MapleItemInformationProvider.getInstance().isPickupRestricted(item.getItemId())
             * && chr.haveItem(item.getItemId(), 1)) { return false; }
             */
        }
        /*
         * if (chr.getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() < eq
         * || chr.getInventory(MapleInventoryType.USE).getNumFreeSlot() < use ||
         * chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() < setup
         * || chr.getInventory(MapleInventoryType.ETC).getNumFreeSlot() < etc ||
         * chr.getInventory(MapleInventoryType.CASH).getNumFreeSlot() < cash) {
         * return false; }
         */
        if (chr.getInventory(MapleInventoryType.EQUIP).getNumFreeSlot() <= eq
                || chr.getInventory(MapleInventoryType.USE).getNumFreeSlot() <= use
                || chr.getInventory(MapleInventoryType.SETUP).getNumFreeSlot() <= setup
                || chr.getInventory(MapleInventoryType.ETC).getNumFreeSlot() <= etc
                || chr.getInventory(MapleInventoryType.CASH).getNumFreeSlot() <= cash) {
            return false;
        }
        return true;
    }

    private static boolean deletePackage(int charid, int accid, int packageid) {
        new QDHiredMerch().character.id.eq(charid).account.id.eq(accid).id.eq(packageid).delete();
        ItemLoader.deleteItems(5, packageid, accid, charid);
        return true;
    }

    private static MerchItemPackage loadItemFrom_Database(int charid, int accountid) {
        DHiredMerch one = new QDHiredMerch().character.id.eq(charid).account.id.eq(accountid).findOne();
        if (one == null) {
            return null;
        }

        MerchItemPackage pack = new MerchItemPackage();
        pack.setPackageid(one.id);
        pack.setMesos(one.mesos);
        pack.setSentTime(one.time);

        Map<Integer, Pair<IItem, MapleInventoryType>> items = ItemLoader.loadItems_hm(5, one.id, accountid);
        if (!items.isEmpty()) {
            List<IItem> iters = new ArrayList<>();
            for (Pair<IItem, MapleInventoryType> z : items.values()) {
                iters.add(z.left);
            }
            pack.setItems(iters);
        }
        return pack;
    }
}
