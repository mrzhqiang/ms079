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
package tools.packet;

import client.MapleCharacter;
import client.inventory.IItem;
import client.MapleClient;
import constants.ServerConstants;
import handling.MaplePacket;
import handling.SendPacketOpcode;
import handling.channel.handler.PlayerInteractionHandler;
import java.util.List;
import server.MerchItemPackage;
import server.shops.AbstractPlayerStore.BoughtItem;
import server.shops.HiredMerchant;
import server.shops.IMaplePlayerShop;
import server.shops.MapleMiniGame;
import server.shops.MaplePlayerShop;
import server.shops.MaplePlayerShopItem;
import tools.Pair;
import tools.data.output.MaplePacketLittleEndianWriter;

public class PlayerShopPacket {

    public static final MaplePacket addCharBox(final MapleCharacter c, final int type) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("addCharBox--------------------");
        }
        mplew.writeShort(SendPacketOpcode.UPDATE_CHAR_BOX.getValue());
        mplew.writeInt(c.getId());
        PacketHelper.addAnnounceBox(mplew, c);

        return mplew.getPacket();
    }

    public static final MaplePacket removeCharBox(final MapleCharacter c) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("removeCharBox--------------------");
        }
        mplew.writeShort(SendPacketOpcode.UPDATE_CHAR_BOX.getValue());
        mplew.writeInt(c.getId());
        mplew.write(0);

        return mplew.getPacket();
    }

    public static final MaplePacket sendTitleBox() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("sendTitleBox--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SEND_TITLE_BOX.getValue());
        mplew.write(7);

        return mplew.getPacket();
    }

    public static final MaplePacket sendPlayerShopBox(final MapleCharacter c) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("sendPlayerShopBox--------------------");
        }
        mplew.writeShort(SendPacketOpcode.UPDATE_CHAR_BOX.getValue());
        mplew.writeInt(c.getId());
        PacketHelper.addAnnounceBox(mplew, c);

        return mplew.getPacket();
    }

    public static final MaplePacket getHiredMerch(final MapleCharacter chr, final HiredMerchant merch, final boolean firstTime) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("getHiredMerch--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());

        mplew.write(5);
        mplew.write(5);
        mplew.write(4);
        mplew.writeShort(merch.getVisitorSlot(chr));
        mplew.writeInt(merch.getItemId());
        mplew.writeMapleAsciiString("雇佣商人");

        for (final Pair<Byte, MapleCharacter> storechr : merch.getVisitors()) {
            mplew.write(storechr.left);
            PacketHelper.addCharLook(mplew, storechr.right, false);
            mplew.writeMapleAsciiString(storechr.right.getName());
//            mplew.writeShort(storechr.right.getJob());
        }
        mplew.write(-1);
        mplew.writeShort(0);
        mplew.writeMapleAsciiString(merch.getOwnerName());
        if (merch.isOwner(chr)) {
            mplew.writeInt(merch.getTimeLeft());
            mplew.write(firstTime ? 1 : 0);
            mplew.write(merch.getBoughtItems().size());
            for (BoughtItem SoldItem : merch.getBoughtItems()) {
                mplew.writeInt(SoldItem.id);
                mplew.writeShort(SoldItem.quantity); // number of purchased
                mplew.writeInt(SoldItem.totalPrice); // total price
                mplew.writeMapleAsciiString(SoldItem.buyer); // name of the buyer
            }
            mplew.writeInt(merch.getMeso());
        }
        mplew.writeMapleAsciiString(merch.getDescription());
        mplew.write(10);
        mplew.writeInt(merch.getMeso()); // meso
        mplew.write(merch.getItems().size());

        for (final MaplePlayerShopItem item : merch.getItems()) {
            mplew.writeShort(item.bundles);
            mplew.writeShort(item.item.getQuantity());
            mplew.writeInt(item.price);
            PacketHelper.addItemInfo(mplew, item.item, true, true);
        }
        return mplew.getPacket();
    }

    public static final MaplePacket getPlayerStore(final MapleCharacter chr, final boolean firstTime) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("getPlayerStore--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        IMaplePlayerShop ips = chr.getPlayerShop();

        switch (ips.getShopType()) {
            case 2:
                mplew.write(5);
                mplew.write(4);
                mplew.write(4);
                break;
            case 3:
                mplew.write(5);
                mplew.write(2);
                mplew.write(2);
                break;
            case 4:
                mplew.write(5);
                mplew.write(1);
                mplew.write(2);
                break;
        }
        mplew.writeShort(ips.getVisitorSlot(chr));
        PacketHelper.addCharLook(mplew, ((MaplePlayerShop) ips).getMCOwner(), false);
        mplew.writeMapleAsciiString(ips.getOwnerName());
//        mplew.writeShort(((MaplePlayerShop) ips).getMCOwner().getJob());
        for (final Pair<Byte, MapleCharacter> storechr : ips.getVisitors()) {
            mplew.write(storechr.left);
            PacketHelper.addCharLook(mplew, storechr.right, false);
            mplew.writeMapleAsciiString(storechr.right.getName());
//            mplew.writeShort(storechr.right.getJob());
        }
        mplew.write(0xFF);
        mplew.writeMapleAsciiString(ips.getDescription());
        mplew.write(10);
        mplew.write(ips.getItems().size());

        for (final MaplePlayerShopItem item : ips.getItems()) {
            mplew.writeShort(item.bundles);
            mplew.writeShort(item.item.getQuantity());
            mplew.writeInt(item.price);
            PacketHelper.addItemInfo(mplew, item.item, true, true);
        }
        return mplew.getPacket();
    }

    public static final MaplePacket shopChat(final String message, final int slot) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("shopChat--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(6);
        mplew.write(8);
        mplew.write(slot);
        mplew.writeMapleAsciiString(message);

        return mplew.getPacket();
    }

    public static final MaplePacket shopErrorMessage(final int error, final int type) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("shopErrorMessage--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x0A);
        mplew.write(type);
        mplew.write(error);

        return mplew.getPacket();
    }

    public static final MaplePacket spawnHiredMerchant(final HiredMerchant hm) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("spawnHiredMerchant--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SPAWN_HIRED_MERCHANT.getValue());
        mplew.writeInt(hm.getOwnerId());
        mplew.writeInt(hm.getItemId());
        mplew.writePos(hm.getPosition());
        mplew.writeShort(0);
        mplew.writeMapleAsciiString(hm.getOwnerName());
        PacketHelper.addInteraction(mplew, hm);

        return mplew.getPacket();
    }

    public static final MaplePacket destroyHiredMerchant(final int id) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("destroyHiredMerchant--------------------");
        }
        mplew.writeShort(SendPacketOpcode.DESTROY_HIRED_MERCHANT.getValue());
        mplew.writeInt(id);

        return mplew.getPacket();
    }

    public static final MaplePacket shopItemUpdate(final IMaplePlayerShop shop) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("shopItemUpdate--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(23);
        if (shop.getShopType() == 1) {
            mplew.writeInt(0);
        }
        mplew.write(shop.getItems().size());

        for (final MaplePlayerShopItem item : shop.getItems()) {
            mplew.writeShort(item.bundles);
            mplew.writeShort(item.item.getQuantity());
            mplew.writeInt(item.price);
            PacketHelper.addItemInfo(mplew, item.item, true, true);
        }
        return mplew.getPacket();
    }

    public static final MaplePacket shopVisitorAdd(final MapleCharacter chr, final int slot) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("shopVisitorAdd--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(4);
        mplew.write(slot);
        PacketHelper.addCharLook(mplew, chr, false);
        mplew.writeMapleAsciiString(chr.getName());
//        mplew.writeShort(chr.getJob());

        return mplew.getPacket();
    }

    public static final MaplePacket shopVisitorLeave(final byte slot) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("shopVisitorLeave--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x0A);
        if (slot > 0) {   //位置
            mplew.write(slot);
            mplew.write(slot);
            mplew.write(slot);
        }
        return mplew.getPacket();
    }

    public static final MaplePacket Merchant_Buy_Error(final byte message) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("Merchant_Buy_Error--------------------");
        }
        // 2 = You have not enough meso
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x17);
        mplew.write(message);

        return mplew.getPacket();
    }

    public static final MaplePacket updateHiredMerchant(final HiredMerchant shop) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("updateHiredMerchant--------------------");
        }
        mplew.writeShort(SendPacketOpcode.UPDATE_HIRED_MERCHANT.getValue());
        mplew.writeInt(shop.getOwnerId());
        PacketHelper.addInteraction(mplew, shop);

        return mplew.getPacket();
    }

    public static final MaplePacket merchItem_Message(final byte op) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("merchItem_Message--------------------");
        }
        mplew.writeShort(SendPacketOpcode.MERCH_ITEM_MSG.getValue());
        mplew.write(op);

        return mplew.getPacket();
    }

    public static final MaplePacket merchItemStore(final byte op) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("merchItemStore--------------------");
        }
        // [28 01] [22 01] - Invalid Asiasoft Passport
        // [28 01] [22 00] - Open Asiasoft pin typing
        mplew.writeShort(SendPacketOpcode.MERCH_ITEM_STORE.getValue());
        mplew.write(op);

        switch (op) {
            case 0x24:
                mplew.writeZeroBytes(8);
                break;
            default:
                mplew.write(0);
                break;
        }

        return mplew.getPacket();
    }

    public static final MaplePacket merchItemStore_ItemData(final MerchItemPackage pack) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("merchItemStore_ItemData--------------------");
        }
        mplew.writeShort(SendPacketOpcode.MERCH_ITEM_STORE.getValue());
        mplew.write(0x23);
        mplew.writeInt(9030000); // Fredrick
        mplew.writeInt(32272); // pack.getPackageid()
        mplew.writeZeroBytes(5);
        mplew.writeInt(pack.getMesos());
        mplew.write(0);
        mplew.write(pack.getItems().size());

        for (final IItem item : pack.getItems()) {
            PacketHelper.addItemInfo(mplew, item, true, true);
        }
        mplew.writeZeroBytes(3);

        return mplew.getPacket();
    }

    public static MaplePacket getMiniGame(MapleClient c, MapleMiniGame minigame) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGame--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(5);
        mplew.write(minigame.getGameType());
        mplew.write(minigame.getMaxSize());
        mplew.writeShort(minigame.getVisitorSlot(c.getPlayer()));
        PacketHelper.addCharLook(mplew, minigame.getMCOwner(), false);
        mplew.writeMapleAsciiString(minigame.getOwnerName());
//        mplew.writeShort(minigame.getMCOwner().getJob());
        for (Pair<Byte, MapleCharacter> visitorz : minigame.getVisitors()) {
            mplew.write(visitorz.getLeft());
            PacketHelper.addCharLook(mplew, visitorz.getRight(), false);
            mplew.writeMapleAsciiString(visitorz.getRight().getName());
//            mplew.writeShort(visitorz.getRight().getJob());
        }
        mplew.write(-1);
        mplew.write(0);
        addGameInfo(mplew, minigame.getMCOwner(), minigame);
        for (Pair<Byte, MapleCharacter> visitorz : minigame.getVisitors()) {
            mplew.write(visitorz.getLeft());
            addGameInfo(mplew, visitorz.getRight(), minigame);
        }
        mplew.write(-1);
        mplew.writeMapleAsciiString(minigame.getDescription());
        mplew.writeShort(minigame.getPieceType());
        return mplew.getPacket();
    }

    public static MaplePacket getMiniGameReady(boolean ready) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameReady--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(ready ? 0x39 : 0x3A);
        return mplew.getPacket();
    }

    public static MaplePacket getMiniGameExitAfter(boolean ready) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameExitAfter--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(ready ? 0x37 : 0x38);
        return mplew.getPacket();
    }

    public static MaplePacket getMiniGameStart(int loser) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameStart--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x3C);
        mplew.write(loser == 1 ? 0 : 1);
        return mplew.getPacket();
    }

    public static MaplePacket getMiniGameSkip(int slot) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameSkip--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x3E);
        //owner = 1 visitor = 0?
        mplew.write(slot);
        return mplew.getPacket();
    }
        public static MaplePacket getMiniGameSkip1(int slot) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameSkip1--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x3E);
        //owner = 1 visitor = 0?
        mplew.write(slot == 1 ? 0 : 1);
        return mplew.getPacket();
    }

    public static MaplePacket getMiniGameRequestTie() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameRequestTie--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x30);
        return mplew.getPacket();
    }
      
    public static MaplePacket getMiniGameRequestREDO() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameRequestREDO--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x35);
        return mplew.getPacket();
    }


    public static MaplePacket getMiniGameDenyTie() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameDenyTie--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x31);
        return mplew.getPacket();
    }
    
       public static MaplePacket getMiniGameDenyREDO() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameDenyREDO--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x30);
        return mplew.getPacket();
    }

    public static MaplePacket getMiniGameFull() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameFull--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.writeShort(5);
        mplew.write(2);
        return mplew.getPacket();
    }

    public static MaplePacket getMiniGameMoveOmok(int move1, int move2, int move3) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameMoveOmok--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x3F);
        mplew.writeInt(move1);
        mplew.writeInt(move2);
        mplew.write(move3);
        return mplew.getPacket();
    }

    public static MaplePacket getMiniGameNewVisitor(MapleCharacter c, int slot, MapleMiniGame game) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameNewVisitor--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(4);
        mplew.write(slot);
        PacketHelper.addCharLook(mplew, c, false);
        mplew.writeMapleAsciiString(c.getName());
//        mplew.writeShort(c.getJob());
        addGameInfo(mplew, c, game);
        return mplew.getPacket();
    }

    public static void addGameInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, MapleMiniGame game) {
        mplew.writeInt(game.getGameType()); // start of visitor; unknown
        mplew.writeInt(game.getWins(chr));
        mplew.writeInt(game.getTies(chr));
        mplew.writeInt(game.getLosses(chr));
        mplew.writeInt(game.getScore(chr)); // points
      //  mplew.writeInt(0); // 台版自己加的
    }

    public static MaplePacket getMiniGameClose(byte number) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameClose--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0xA);
        mplew.write(1);
        mplew.write(number);
        return mplew.getPacket();
    }

    public static MaplePacket getMatchCardStart(MapleMiniGame game, int loser) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMatchCardStart--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x3C);
        mplew.write(loser == 1 ? 0 : 1);
        int times = game.getPieceType() == 1 ? 20 : (game.getPieceType() == 2 ? 30 : 12);
        mplew.write(times);
        for (int i = 1; i <= times; i++) {
            mplew.writeInt(game.getCardId(i));
        }
        return mplew.getPacket();
    }

    public static MaplePacket getMatchCardSelect(int turn, int slot, int firstslot, int type) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMatchCardSelect--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x43);
        mplew.write(turn);
        mplew.write(slot);
        if (turn == 0) {
            mplew.write(firstslot);
            mplew.write(type);
        }
        return mplew.getPacket();
    }

    public static MaplePacket getMiniGameResult(MapleMiniGame game, int type, int x) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("getMiniGameResult--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x3D);
        mplew.write(type); //lose = 0, tie = 1, win = 2
        game.setPoints(x, type);
        if (type != 0) {
            game.setPoints(x == 1 ? 0 : 1, type == 2 ? 0 : 1);
        }
        if (type != 1) {
            if (type == 0) {
                mplew.write(x == 1 ? 0 : 1); //who did it?
            } else {
                mplew.write(x);
            }
        }
        addGameInfo(mplew, game.getMCOwner(), game);
        for (Pair<Byte, MapleCharacter> visitorz : game.getVisitors()) {
            addGameInfo(mplew, visitorz.right, game);
        }

        return mplew.getPacket();
    }

    public static final MaplePacket MerchantVisitorView(List<String> visitor) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("MerchantVisitorView--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x2C);
        mplew.writeShort(visitor.size());
        for (String visit : visitor) {
            mplew.writeMapleAsciiString(visit);
            mplew.writeInt(1); /////for the lul
        }
        return mplew.getPacket();
    }

    public static final MaplePacket MerchantBlackListView(final List<String> blackList) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.调试输出封包) {
            System.out.println("MerchantBlackListView--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(0x2D);
        mplew.writeShort(blackList.size());
        for (int i = 0; i < blackList.size(); i++) {
            if (blackList.get(i) != null) {
                mplew.writeMapleAsciiString(blackList.get(i));
            }
        }
        return mplew.getPacket();
    }

    //BELOW ARE UNUSED PLEASE RECONSIDER.
    public static final MaplePacket sendHiredMerchantMessage(final byte type) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("sendHiredMerchantMessage--------------------");
        }
        // 07 = send title box
        // 09 = Please pick up your items from Fredrick and then try again.
        // 0A = Your another character is using the item now. Please close the shop with that character or empty your store bank.
        // 0B = You cannot open it now.
        // 0F = Please retrieve your items from Fredrick.
        mplew.writeShort(SendPacketOpcode.MERCH_ITEM_MSG.getValue());
        mplew.write(type);

        return mplew.getPacket();
    }

    public static final MaplePacket shopMessage(final int type) { // show when closed the shop
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.调试输出封包) {
            System.out.println("shopMessage--------------------");
        }
        // 0x28 = All of your belongings are moved successfully.
        mplew.writeShort(SendPacketOpcode.PLAYER_INTERACTION.getValue());
        mplew.write(type);
        mplew.write(0);

        return mplew.getPacket();
    }
}
