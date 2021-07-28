package handling.cashshop.handler;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Map;
import java.util.HashMap;

import constants.GameConstants;
import client.MapleClient;
import client.MapleCharacter;
import client.MapleCharacterUtil;
import client.inventory.*;
import constants.OtherSettings;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.login.LoginServer;
import handling.world.CharacterTransfer;
import handling.world.World;
import java.net.InetAddress;
import java.util.List;
import server.CashItemFactory;
import server.CashItemInfo;
import server.MTSStorage;
import server.MapleInventoryManipulator;
import server.MapleItemInformationProvider;
import tools.FileoutputUtil;
import tools.MaplePacketCreator;
import tools.packet.MTSCSPacket;
import tools.Pair;
import tools.data.input.SeekableLittleEndianAccessor;

public class CashShopOperation {

    public static void LeaveCS(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        if (chr == null) {
            return;
        }
        int channel = c.getChannel(); //角色要更换的频道
        ChannelServer toch = ChannelServer.getInstance(channel); //角色从商城出来更换的频道信息
        if (toch == null) {
            FileoutputUtil.log("log\\LeaveCashShop.txt", "玩家: " + chr.getName() + " 从商城离开发生错误.找不到频道[" + channel + "]的信息.");
            c.getSession().close(true);
            return;
        }
        //开始处理
        World.ChannelChange_Data(new CharacterTransfer(chr), chr.getId(), c.getChannel());
        CashShopServer.getPlayerStorage().deregisterPlayer(chr);
        c.updateLoginState(MapleClient.LOGIN_SERVER_TRANSITION, c.getSessionIPAddress());
        String s = c.getSessionIPAddress();
        LoginServer.addIPAuth(s.substring(s.indexOf('/') + 1, s.length()));
        c.getSession().write(MaplePacketCreator.getChannelChange(c, Integer.parseInt(toch.getIP().split(":")[1]))); //发送更换频道的封包信息
        chr.saveToDB(false, true);
        c.setPlayer(null);
        c.setReceiving(false);
    }

    public static void EnterCS(final int playerid, final MapleClient c) {
        CharacterTransfer transfer = CashShopServer.getPlayerStorage().getPendingCharacter(playerid);
        boolean mts = false;
        if (transfer == null) {
            transfer = CashShopServer.getPlayerStorageMTS().getPendingCharacter(playerid);
            mts = true;
            if (transfer == null) {
                c.getSession().close();
                return;
            }
        }
        MapleCharacter chr = MapleCharacter.ReconstructChr(transfer, c, false);

        c.setPlayer(chr);
        c.setAccID(chr.getAccountID());

        if (!c.CheckIPAddress()) { // Remote hack
            c.getSession().close();
            return;
        }

        final int state = c.getLoginState();
        boolean allowLogin = false;
        if (state == MapleClient.LOGIN_SERVER_TRANSITION || state == MapleClient.CHANGE_CHANNEL) {
            if (!World.isCharacterListConnected(c.loadCharacterNames(c.getWorld()))) {
                allowLogin = true;
            }
        }
        if (!allowLogin) {
            c.setPlayer(null);
            c.getSession().close();
            return;
        }
        c.updateLoginState(MapleClient.LOGIN_LOGGEDIN, c.getSessionIPAddress());
        if (mts) {
            CashShopServer.getPlayerStorageMTS().registerPlayer(chr);
            c.getSession().write(MTSCSPacket.startMTS(chr, c));
            MTSOperation.MTSUpdate(MTSStorage.getInstance().getCart(c.getPlayer().getId()), c);
        } else {
            CashShopServer.getPlayerStorage().registerPlayer(chr);
            c.getSession().write(MTSCSPacket.warpCS(c));
            CSUpdate(c);
        }
    }

    public static void CSUpdate(final MapleClient c) {

        c.sendPacket(MTSCSPacket.showCashInventory(c));
        c.getSession().write(MTSCSPacket.sendWishList(c.getPlayer(), false));
        c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
        //   c.getSession().write(MTSCSPacket.getCSInventory(c));
        c.getSession().write(MTSCSPacket.getCSGifts(c));
        //c.getSession().write(MTSCSPacket.getCSInventory(c));
        //  doCSPackets(c);
    }

    public static void TouchingCashShop(final MapleClient c) {
        c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
    }

    public static void CouponCode(final String code, final MapleClient c) {
        boolean validcode = false;
        int type = -1;
        int item = -1;

        try {
            validcode = MapleCharacterUtil.getNXCodeValid(code.toUpperCase(), validcode);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (validcode) {
            try {
                type = MapleCharacterUtil.getNXCodeType(code);
                item = MapleCharacterUtil.getNXCodeItem(code);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (type != 4) {
                try {
                    MapleCharacterUtil.setNXCodeUsed(c.getPlayer().getName(), code);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            /*
             * Explanation of type! Basically, this makes coupon codes do
             * different things!
             *
             * Type 1: A-Cash, Type 2: Maple Points Type 3: Item.. use SN Type
             * 4: A-Cash Coupon that can be used over and over Type 5: Mesos
             */
            Map<Integer, IItem> itemz = new HashMap<Integer, IItem>();
            int maplePoints = 0, mesos = 0;
            switch (type) {
                case 1:
                case 2:
                    c.getPlayer().modifyCSPoints(type, item, false);
                    maplePoints = item;
                    break;
                case 3:
                    CashItemInfo itez = CashItemFactory.getInstance().getItem(item);
                    if (itez == null) {
                        c.getSession().write(MTSCSPacket.sendCSFail(0));
                        doCSPackets(c);
                        return;
                    }
                    byte slot = MapleInventoryManipulator.addId(c, itez.getId(), (short) 1, "", (byte) 0);
                    if (slot <= -1) {
                        c.getSession().write(MTSCSPacket.sendCSFail(0));
                        doCSPackets(c);
                        return;
                    } else {
                        itemz.put(item, c.getPlayer().getInventory(GameConstants.getInventoryType(item)).getItem(slot));
                    }
                    break;
                case 4:
                    c.getPlayer().modifyCSPoints(1, item, false);
                    maplePoints = item;
                    break;
                case 5:
                    c.getPlayer().gainMeso(item, false);
                    mesos = item;
                    break;
            }
            c.getSession().write(MTSCSPacket.showCouponRedeemedItem(itemz, mesos, maplePoints, c));
        } else {
            c.getSession().write(MTSCSPacket.sendCSFail(validcode ? 0xA5 : 0xA7)); //A1, 9F
        }
        doCSPackets(c);
    }

    public static void BuyCashItem(final SeekableLittleEndianAccessor slea, final MapleClient c, final MapleCharacter chr) {
        OtherSettings item_id = new OtherSettings();
        String itembp_id[] = item_id.getItempb_id();
        String itemjy_id[] = item_id.getItemjy_id();

        final int action = slea.readByte();
        /*
         * if (action == 0) { slea.skip(2);
         * CouponCode(slea.readMapleAsciiString(), c); } else
         */
        switch (action) {
            case 3: {
                int useNX = slea.readByte() + 1;
                int snCS = slea.readInt();
                CashItemInfo item = CashItemFactory.getInstance().getItem(snCS);
                if (item == null) {
                    chr.dropMessage(1, "该物品暂未开放！");
                    doCSPackets(c);
                    return;
                }
                for (int i = 0; i < itembp_id.length; i++) {
                    if (item.getId() == Integer.parseInt(itembp_id[i])) {
                        c.getPlayer().dropMessage(1, "这个物品是禁止购买的.");
                        doCSPackets(c);
                        return;
                    }
                }
                if (item.getPrice() < 100) {
                    c.getPlayer().dropMessage(1, "价格(" + item.getPrice() + ")低于100点卷的物品是禁止购买的.");
                    doCSPackets(c);
                    return;
                }
                if (item != null && chr.getCSPoints(useNX) >= item.getPrice()) {
                    /*
                        * if (!item.genderEquals(c.getPlayer().getGender())) {
                        * c.getSession().write(MTSCSPacket.sendCSFail(0xA6));
                        * doCSPackets(c); return; } else if
                        * (c.getPlayer().getCashInventory().getItemsSize() >= 100) {
                        * c.getSession().write(MTSCSPacket.sendCSFail(0xB1));
                        * doCSPackets(c); return; }
                     */
 /*
                        * for (int i : GameConstants.cashBlock) { if (item.getId() ==
                        * i) { c.getPlayer().dropMessage(1,
                        * GameConstants.getCashBlockedMsg(item.getId()));
                        * doCSPackets(c); return; } }
                     */
                    chr.modifyCSPoints(useNX, -item.getPrice(), false);
                    IItem itemz = chr.getCashInventory().toItem(item);
                    /*
                        * if (itemz.getUniqueId() == 0 || itemz.getItemId() !=
                        * item.getId() || itemz.getQuantity() != item.getCount()) {
                        * String note1 = "时间：" + FileoutputUtil.CurrentReadable_Time()
                        * + " " + "|| 购买物品是否现金物品：" + itemz.getUniqueId() + " " + "||
                        * 购买物品ID：" + itemz.getItemId() + "!=" + item.getId() + "||
                        * 购买物品数量：" + itemz.getQuantity() + "!=" + item.getCount() + "||
                        * 购买物品SN：" + item.getSN() + " " + "|| 购买物品是否判定出售：" +
                        * item.onSale() + "\r\n";
                        * FileoutputUtil.packetLog("日志\\log\\购买商城物品信息错误\\" + chr.getName()
                        * + ".log", note1); }
                     */
                    if (itemz != null && itemz.getUniqueId() > 0 && itemz.getItemId() == item.getId() && itemz.getQuantity() == item.getCount()) {
                        /*
                            * String note = "时间：" +
                            * FileoutputUtil.CurrentReadable_Time() + " " + "||
                            * 购买物品ID：" + item.getId() + " " + "|| 购买物品价格：" +
                            * item.getPrice() + " " + "|| 购买物品数量：" + item.getCount() +
                            * " " + "|| 购买物品SN：" + item.getSN() + " " + "||
                            * 购买物品是否判定出售：" + item.onSale() + "\r\n";
                            * FileoutputUtil.packetLog("日志\\log\\购买商城物品信息正常\\" +
                            * chr.getName() + ".log", note);
                         */
                        if (useNX == 1) {
                            byte flag = itemz.getFlag();
                            boolean 交易 = true;
                            for (int i = 0; i < itemjy_id.length; i++) {
                                if (itemz.getItemId() == Integer.parseInt(itemjy_id[i])) {
                                    交易 = false;
                                }
                            }
                            if (交易 == true) {
                                if (itemz.getType() == MapleInventoryType.EQUIP.getType()) {
                                    flag |= ItemFlag.KARMA_EQ.getValue();
                                } else {
                                    flag |= ItemFlag.KARMA_USE.getValue();
                                }
                                itemz.setFlag(flag);
                            }
                        }
                        chr.getCashInventory().addToInventory(itemz);
                        //c.getSession().write(MTSCSPacket.confirmToCSInventory(itemz, c.getAccID(), item.getSN()));
                        c.getSession().write(MTSCSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
                    } else {
                        c.getSession().write(MTSCSPacket.sendCSFail(0));
                    }
                } else {
                    c.getSession().write(MTSCSPacket.sendCSFail(0));
                }       //  c.getPlayer().saveToDB(true, true);
                c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer())); //显示点卷
                c.getSession().write(MaplePacketCreator.enableActions()); //能行动
                break;
            }
            case 4:
            case 32: {
                //gift, package
                int snCS = slea.readInt();
                int type = slea.readByte() + 1;
                String recipient = slea.readMapleAsciiString();
                String message = slea.readMapleAsciiString();
                final CashItemInfo item = CashItemFactory.getInstance().getItem(snCS);
                IItem itemz = chr.getCashInventory().toItem(item);
                if (c.getPlayer().isAdmin()) {
                    System.out.println("包裹购买 ID: " + snCS);
                }
                if (item.getPrice() < 100) {
                    c.getPlayer().dropMessage(1, "价格低于100点卷的物品是禁止购买的.");
                    doCSPackets(c);
                    return;
                }
                if (itemz != null && itemz.getUniqueId() > 0 && itemz.getItemId() == item.getId() && itemz.getQuantity() == item.getCount()) {
                    if (item == null || c.getPlayer().getCSPoints(type) < item.getPrice() || message.length() > 73 || message.length() < 1) { //dont want packet editors gifting random stuff =P
                        c.getSession().write(MTSCSPacket.sendCSFail(0));
                        doCSPackets(c);
                        return;
                    }
                    Pair<Integer, Pair<Integer, Integer>> info = MapleCharacterUtil.getInfoByName(recipient, c.getPlayer().getWorld());
                    if (info == null || info.getLeft().intValue() <= 0 || info.getLeft().intValue() == c.getPlayer().getId() || info.getRight().getLeft().intValue() == c.getAccID()) {
                        c.getSession().write(MTSCSPacket.sendCSFail(0xA2)); //9E v75
                        doCSPackets(c);
                        return;
                    } else if (!item.genderEquals(info.getRight().getRight().intValue())) {
                        c.getSession().write(MTSCSPacket.sendCSFail(0xA3));
                        doCSPackets(c);
                        return;
                    } else {
                        c.getPlayer().getCashInventory().gift(info.getLeft().intValue(), c.getPlayer().getName(), message, item.getSN(), MapleInventoryIdentifier.getInstance());
                        c.getPlayer().modifyCSPoints(type, -item.getPrice(), false);
                        c.getSession().write(MTSCSPacket.sendGift(item.getId(), item.getCount(), recipient));
                    }
                } else {
                    c.getPlayer().dropMessage(1, "这个物品是禁止购买的.");
                    doCSPackets(c);
                }
                break;
            }
            case 5:
                // Wishlist
                chr.clearWishlist();
                if (slea.available() < 40) {
                    c.getSession().write(MTSCSPacket.sendCSFail(0));
                    doCSPackets(c);
                    return;
                }
                int[] wishlist = new int[10];
                for (int i = 0; i < 10; i++) {
                    wishlist[i] = slea.readInt();
                }
                chr.setWishlist(wishlist);
                c.getSession().write(MTSCSPacket.sendWishList(chr, true));
                break;
            case 6: { // 扩充背包栏
                // 扩充背包栏
                int 余额 = slea.readByte() + 1;
                boolean 优惠价 = slea.readByte() > 0;
                if (优惠价) {
                    int snCS = slea.readInt();
                    byte types = 1;
                    switch (snCS) {
                        case 50200018:
                            types = 1;
                            break;
                        case 50200019:
                            types = 2;
                            break;
                        case 50200020:
                            types = 3;
                            break;
                        case 50200021:
                            types = 4;
                            break;
                        case 50200043:
                            types = 5;
                            break;
                    }
                    MapleInventoryType type = MapleInventoryType.getByType((byte) types);
                    /*if (chr.isGM()) {
                        System.out.println("增加道具栏  snCS " + snCS + " 扩充: " + types);
                    }*/
                    if (chr.getCSPoints(余额) >= 1100 && chr.getInventory(type).getSlotLimit() < 96) {
                        chr.modifyCSPoints(余额, -1100, false);
                        chr.getInventory(type).addSlot((byte) 8);
                        chr.dropMessage(1, "扩充" + snCS + "成功，当前栏位: " + chr.getInventory(type).getSlotLimit() + " 个。");
                        RefreshCashShop(c);//调用刷新点卷函数
                        chr.getStorage().saveToDB();
                    } else {
                        chr.dropMessage(1, "扩充" + snCS + "失败，点卷余额不足或者栏位已超过上限。");
                    }
                } else {
                    final MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());

                    if (chr.getCSPoints(余额) >= 600 && chr.getInventory(type).getSlotLimit() < 96) {
                        chr.modifyCSPoints(余额, -600, false);
                        chr.getInventory(type).addSlot((byte) 4);
                        chr.dropMessage(1, "背包已增加到 " + chr.getInventory(type).getSlotLimit() + " 个。");
                        RefreshCashShop(c);//调用刷新点卷函数
                        chr.getStorage().saveToDB();
                    } else {
                        chr.dropMessage(1, "扩充失败，点卷余额不足或者栏位已达到上限。");
                        c.getSession().write(MTSCSPacket.sendCSFail(0xA4));
                    }
                }
                //扩充背包各栏位结束
                break;
            }
            case 7:
                // 扩充仓库修复完成
                int 余额 = slea.readByte() + 1;
                int 优惠价 = slea.readByte() > 0 ? 2 : 1;
                if (chr.getCSPoints(余额) >= (优惠价 == 2 ? 1100 : 600) && chr.getStorage().getSlots() < (97 - (4 * 优惠价))) {
                    chr.modifyCSPoints(余额, (优惠价 == 2 ? -1100 : -600), false);
                    chr.getStorage().increaseSlots((byte) (4 * 优惠价));
                    chr.getStorage().saveToDB();
                    chr.dropMessage(1, "仓库扩充成功，当前栏位: " + chr.getStorage().getSlots() + " 个。");
                    RefreshCashShop(c);//调用刷新点卷函数
                } else {
                    chr.dropMessage(1, "仓库扩充失败，点卷余额不足或者栏位已超过上限 96 个位置。");
                }
                //仓库扩充完毕
                break;
            case 8: {
                //...9 = pendant slot expansion
                int useNX = slea.readByte() + 1;
                CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
                int slots = c.getCharacterSlots();
                if (slots > 15) {
                    chr.dropMessage(1, "角色列表已满无法增加！");
                }
                if (item == null || c.getPlayer().getCSPoints(useNX) < item.getPrice() || slots > 15) {
                    c.getSession().write(MTSCSPacket.sendCSFail(0));
                    doCSPackets(c);
                    return;
                }
                c.getPlayer().modifyCSPoints(useNX, -item.getPrice(), false);
                if (c.gainCharacterSlot()) {
                    c.getSession().write(MTSCSPacket.increasedStorageSlots(slots + 1));
                    chr.dropMessage(1, "角色列表已增加到：" + c.getCharacterSlots() + "个");
                } else {
                    c.getSession().write(MTSCSPacket.sendCSFail(0));
                }
                break;
            }
            case 0x0D: {
                //get item from csinventory 商城=>包裹
                //uniqueid, 00 01 01 00, type->position(short)
                int uniqueid = slea.readInt(); //csid.. not like we need it anyways
                slea.readInt();//0
                slea.readByte();//物品类型
                byte type = slea.readByte();
                byte unknown = slea.readByte();
                IItem item = c.getPlayer().getCashInventory().findByCashId(uniqueid);
                if (item != null && item.getQuantity() > 0 && MapleInventoryManipulator.checkSpace(c, item.getItemId(), item.getQuantity(), item.getOwner())) {
                    IItem item_ = item.copy();
                    byte slot = (byte) MapleInventoryManipulator.addbyItem(c, item_, true);
                    if (slot >= 0) {
                        if (item_.getPet() != null) {
                            item_.getPet().setInventoryPosition(type);
                            c.getPlayer().addPet(item_.getPet());
                        }
                        c.getPlayer().getCashInventory().removeFromInventory(item);
                        c.getSession().write(MTSCSPacket.confirmFromCSInventory(item_, type));
                    } else {
                        c.getSession().write(MaplePacketCreator.serverNotice(1, "您的包裹已满."));
                    }
                } else {
                    c.getSession().write(MaplePacketCreator.serverNotice(1, "放入背包错误A."));
                }
                break;
            }
            case 0x0E: {
                //put item in cash inventory 包裹=>商城
                int uniqueid = (int) slea.readLong();
                MapleInventoryType type = MapleInventoryType.getByType(slea.readByte());
                IItem item = c.getPlayer().getInventory(type).findByUniqueId(uniqueid);
                if (item != null && item.getQuantity() > 0 && item.getUniqueId() > 0 && c.getPlayer().getCashInventory().getItemsSize() < 100) {
                    IItem item_ = item.copy();
                    c.getPlayer().getInventory(type).removeItem(item.getPosition(), item.getQuantity(), false);
                    int sn = CashItemFactory.getInstance().getItemSN(item_.getItemId());
                    if (item_.getPet() != null) {
                        c.getPlayer().removePet(item_.getPet(), false);
                    }
                    item_.setPosition((byte) 0);
                    item_.setGMLog("购物商场购买: " + FileoutputUtil.CurrentReadable_Time());
                    c.getPlayer().getCashInventory().addToInventory(item_);
                    c.sendPacket(MTSCSPacket.confirmToCSInventory(item, c.getAccID(), sn));
                } else {
                    c.sendPacket(MTSCSPacket.sendCSFail(0xB1));
                }
                RefreshCashShop(c);
                break;
            }
            //36是够买挚友戒指相关 0x24
            case 36:
            //29是够买结婚戒指相关 0x1D
            case 29: {
                int sn = slea.readInt();
                if (sn == 209000310) {
                    sn = 20900026;
                }
                final CashItemInfo item = CashItemFactory.getInstance().getItem(sn);
                final String partnerName = slea.readMapleAsciiString();
                final String msg = slea.readMapleAsciiString();

                for (int i = 0; i < itembp_id.length; i++) {
                    if (item.getId() == Integer.parseInt(itembp_id[i])) {
                        c.getPlayer().dropMessage(1, "这个物品是禁止购买的.");
                        doCSPackets(c);
                        return;
                    }
                }
                if (item == null || !GameConstants.isEffectRing(item.getId()) || c.getPlayer().getCSPoints(1) < item.getPrice() || msg.length() > 73 || msg.length() < 1) {
                    chr.dropMessage(1, "购买戒指错误：\r\n你没有足够的点卷或者该物品不存在。。");
                    doCSPackets(c);
                    return;
                } else if (!item.genderEquals(c.getPlayer().getGender())) {
                    chr.dropMessage(1, "购买戒指错误：B\r\n请联系GM！。");
                    doCSPackets(c);
                    return;
                } else if (c.getPlayer().getCashInventory().getItemsSize() >= 100) {
                    chr.dropMessage(1, "购买戒指错误：C\r\n请联系GM！。");
                    doCSPackets(c);
                    return;

                } else if (item.getPrice() == 2990) {
//                c.getPlayer().dropMessage(1, "此物品暂时不开放.");
//                doCSPackets(c);
//                return;
                }

                Pair<Integer, Pair<Integer, Integer>> info = MapleCharacterUtil.getInfoByName(partnerName, c.getPlayer().getWorld());
                if (info == null || info.getLeft() <= 0 || info.getLeft() == c.getPlayer().getId()) {
                    chr.dropMessage(1, "购买戒指错误：D\r\n请联系GM！。");
                    doCSPackets(c);
                    return;
                } else if (info.getRight().getLeft() == c.getAccID()) {
                    chr.dropMessage(1, "购买戒指错误：E\r\n请联系GM！。");
                    doCSPackets(c);
                    return;
                } else {
                    if (info.getRight().getRight() == c.getPlayer().getGender() && action == 29) {
                        chr.dropMessage(1, "购买戒指错误：F\r\n请联系GM！。");
                        doCSPackets(c);
                        return;
                    }

                    int err = MapleRing.createRing(item.getId(), c.getPlayer(), partnerName, msg, info.getLeft(), item.getSN());

                    if (err != 1) {
                        chr.dropMessage(1, "购买戒指错误：G\r\n请联系GM！。");
                        doCSPackets(c);
                        return;
                    }
                    c.getPlayer().modifyCSPoints(1, -item.getPrice(), false);
                    c.getSession().write(MTSCSPacket.商城送礼(item.getId(), item.getCount(), partnerName));
                    chr.sendNote(partnerName, partnerName + " 您已收到" + chr.getName() + "送给您的礼物，请进入现金商城查看！");
                    //int chz = WorldFindService.getInstance().findChannel(partnerName);
                    int chz = World.Find.findChannel(partnerName);
                    if (chz > 0) {
                        MapleCharacter receiver = ChannelServer.getInstance(chz).getPlayerStorage().getCharacterByName(partnerName);
                        if (receiver != null) {
                            receiver.showNote();
                        }
                    }
                }
                doCSPackets(c); //刷新下免得卡住
                break;
            }
            case 0x1F: {
                //购买礼包
                /*
                    * int 关闭 = 1; if (关闭 == 1) { chr.dropMessage(1, "暂不支持。");
                    * c.getPlayer().saveToDB(true, true);
                    * c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
                    * //显示点卷 c.getSession().write(MaplePacketCreator.enableActions());
                    * //能行动 return; }
                 */
                int type = slea.readByte() + 1;
                int snID = slea.readInt();
                final CashItemInfo item = CashItemFactory.getInstance().getItem(snID);
                for (int i = 0; i < itembp_id.length; i++) {
                    if (snID == Integer.parseInt(itembp_id[i])) {
                        c.getPlayer().dropMessage(1, "这个物品是禁止购买的.");
                        doCSPackets(c);
                        return;
                    }
                }
                if (c.getPlayer().isAdmin()) {
                    System.out.println("礼包购买 ID: " + snID);
                }
                switch (snID) {
                    case 10001818:
                        c.getPlayer().dropMessage(1, "这个物品是禁止购买的.");
                        doCSPackets(c);
                        break;
                }
                List<CashItemInfo> ccc = null;
                if (item != null) {
                    ccc = CashItemFactory.getInstance().getPackageItems(item.getId());

                    /*if (item == null || ccc == null || c.getPlayer().getCSPoints(type) < item.getPrice()) {
                    chr.dropMessage(1, "购买礼包错误：\r\n你没有足够的点卷或者该物品不存在。");
                    //c.getSession().write(MTSCSPacket.sendCSFail(0));
                    doCSPackets(c);
                    return;
                } else*/ if (!item.genderEquals(c.getPlayer().getGender())) {
                        chr.dropMessage(1, "购买礼包错误：B\r\n请联系GM！。");
                        //c.getSession().write(MTSCSPacket.sendCSFail(0xA6));
                        doCSPackets(c);
                        return;
                    } else if (c.getPlayer().getCashInventory().getItemsSize() >= (100 - ccc.size())) {
                        chr.dropMessage(1, "购买礼包错误：C\r\n请联系GM！。");
                        //c.getSession().write(MTSCSPacket.sendCSFail(0xB1));
                        doCSPackets(c);
                        return;
                    }

                    Map<Integer, IItem> ccz = new HashMap<Integer, IItem>();
                    for (CashItemInfo i : ccc) {
                        for (int iz : GameConstants.cashBlock) {
                            if (i.getId() == iz) {
                                continue;
                            }
                        }
                        IItem itemz = chr.getCashInventory().toItem(i, chr, MapleInventoryManipulator.getUniqueId(i.getId(), null), "");
                        if (itemz == null || itemz.getUniqueId() <= 0 || itemz.getItemId() != i.getId()) {
                            continue;
                        }
                        ccz.put(i.getSN(), itemz);
                        c.getPlayer().getCashInventory().addToInventory(itemz);
                        c.getSession().write(MTSCSPacket.showBoughtCSItem(itemz, item.getSN(), c.getAccID()));
                    }
                    chr.modifyCSPoints(type, -item.getPrice(), false);
                }
                //  c.sendPacket(MTSCSPacket.showBoughtCSPackage(ccz, c.getAccID()));
//            Map<Integer, IItem> ccz = new HashMap<Integer, IItem>();
//            for (CashItemInfo i : ccc) {
//                IItem itemz = c.getPlayer().getCashInventory().toItem(i);
//                if (itemz == null || itemz.getUniqueId() <= 0 || itemz.getItemId() != i.getId()) {
//                    continue;
//                }
//                ccz.put(i.getSN(), itemz);
//                c.getPlayer().getCashInventory().addToInventory(itemz);
//            }
//            chr.modifyCSPoints(type, -item.getPrice(), false);
//            c.getSession().write(MTSCSPacket.showBoughtCSPackage(ccz, c.getAccID(), item.getSN()));
//            c.getSession().write(MTSCSPacket.getCSInventory(c));
//            c.getSession().write(MTSCSPacket.getCSGifts(c));
                break;
            }
            case 0x2A: {
                int snCS = slea.readInt();
                //CashItemInfo item = CashItemFactory.getItem(snCS);
                if ((snCS == 50200031) && (c.getPlayer().getCSPoints(1) >= 500)) {
                    c.getPlayer().modifyCSPoints(1, -500);
                    c.getPlayer().modifyCSPoints(2, 500);
                    c.getSession().write(MaplePacketCreator.serverNotice(1, "兑换500抵用卷成功"));
                } else if ((snCS == 50200032) && (c.getPlayer().getCSPoints(1) >= 1000)) {
                    c.getPlayer().modifyCSPoints(1, -1000);
                    c.getPlayer().modifyCSPoints(2, 1000);
                    c.getSession().write(MaplePacketCreator.serverNotice(1, "兑换抵1000用卷成功"));
                } else if ((snCS == 50200033) && (c.getPlayer().getCSPoints(1) >= 5000)) {
                    c.getPlayer().modifyCSPoints(1, -5000);
                    c.getPlayer().modifyCSPoints(2, 5000);
                    c.getSession().write(MaplePacketCreator.serverNotice(1, "兑换5000抵用卷成功"));
                } else {
                    c.getSession().write(MaplePacketCreator.serverNotice(1, "没有找到这个道具的信息！\r\n或者你点卷不足无法兑换！"));
                }
                c.getSession().write(MTSCSPacket.enableCSorMTS());
                c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
                c.getSession().write(MaplePacketCreator.enableActions());
                break;
            }
            case 33: {
                int 关闭 = 1;
                if (关闭 == 1) {
                    chr.dropMessage(1, "暂不支持。");
                    c.getPlayer().saveToDB(true, true);
                    c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer())); //显示点卷
                    c.getSession().write(MaplePacketCreator.enableActions()); //能行动
                    return;
                }
                final CashItemInfo item = CashItemFactory.getInstance().getItem(slea.readInt());
                if (item == null || !MapleItemInformationProvider.getInstance().isQuestItem(item.getId())) {
                    c.getSession().write(MTSCSPacket.sendCSFail(0));
                    doCSPackets(c);
                    return;
                } else if (c.getPlayer().getMeso() < item.getPrice()) {
                    c.getSession().write(MTSCSPacket.sendCSFail(0xB8));
                    doCSPackets(c);
                    return;
                } else if (c.getPlayer().getInventory(GameConstants.getInventoryType(item.getId())).getNextFreeSlot() < 0) {
                    c.getSession().write(MTSCSPacket.sendCSFail(0xB1));
                    doCSPackets(c);
                    return;
                }
                for (int iz : GameConstants.cashBlock) {
                    if (item.getId() == iz) {
                        c.getPlayer().dropMessage(1, GameConstants.getCashBlockedMsg(item.getId()));
                        doCSPackets(c);
                        return;
                    }
                }
                byte pos = MapleInventoryManipulator.addId(c, item.getId(), (short) item.getCount(), null, (byte) 0);
                if (pos < 0) {
                    c.getSession().write(MTSCSPacket.sendCSFail(0xB1));
                    doCSPackets(c);
                    return;
                }
                chr.gainMeso(-item.getPrice(), false);
                c.getSession().write(MTSCSPacket.showBoughtCSQuestItem(item.getPrice(), (short) item.getCount(), pos, item.getId()));
                break;
            }
            default:
                c.getSession().write(MTSCSPacket.sendCSFail(0));
                break;
        }
        doCSPackets(c);
    }

    private static final MapleInventoryType getInventoryType(final int id) {
        switch (id) {
            case 50200075:
                return MapleInventoryType.EQUIP;
            case 50200074:
                return MapleInventoryType.USE;
            case 50200073:
                return MapleInventoryType.ETC;
            default:
                return MapleInventoryType.UNDEFINED;
        }
    }

    private static final void RefreshCashShop(MapleClient c) {
        c.sendPacket(MTSCSPacket.showCashInventory(c));
        c.sendPacket(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
        c.sendPacket(MTSCSPacket.enableCSUse());
        c.getPlayer().getCashInventory().checkExpire(c);
    }

    private static final void doCSPackets(MapleClient c) {
        c.getSession().write(MTSCSPacket.getCSInventory(c));
        c.getSession().write(MTSCSPacket.enableCSorMTS());
        c.getSession().write(MTSCSPacket.sendWishList(c.getPlayer(), false));
        c.getSession().write(MTSCSPacket.showNXMapleTokens(c.getPlayer()));
        // c.getSession().write(MTSCSPacket.enableCSUse());
        c.getSession().write(MaplePacketCreator.enableActions());
        c.getPlayer().getCashInventory().checkExpire(c);
    }
}
