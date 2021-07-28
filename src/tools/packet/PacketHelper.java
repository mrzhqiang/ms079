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

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import client.inventory.IEquip;
import client.inventory.Item;
import client.ISkill;
import constants.GameConstants;
import client.inventory.MapleRing;
import client.inventory.MaplePet;
import client.MapleCharacter;
import client.MapleCoolDownValueHolder;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.MapleQuestStatus;
import client.inventory.IItem;
import client.SkillEntry;
import client.inventory.*;
import constants.ServerConstants;
import server.MapleItemInformationProvider;
import tools.Pair;
import server.movement.LifeMovementFragment;
import server.shops.AbstractPlayerStore;
import server.shops.IMaplePlayerShop;
import tools.DateUtil;
import tools.KoreanDateUtil;
import tools.data.output.LittleEndianWriter;
import tools.data.output.MaplePacketLittleEndianWriter;

public class PacketHelper {

    private final static long FT_UT_OFFSET = 116444592000000000L; // EDT
    public final static long MAX_TIME = 150842304000000000L; //00 80 05 BB 46 E6 17 02
    public static final byte unk1[] = new byte[]{(byte) 0x00, (byte) 0x40, (byte) 0xE0, (byte) 0xFD};
    public static final byte unk2[] = new byte[]{(byte) 0x3B, (byte) 0x37, (byte) 0x4F, (byte) 0x01};

    public static final long getKoreanTimestamp(final long realTimestamp) {
        if (realTimestamp == -1) {
            return MAX_TIME;
        }
        long time = (realTimestamp / 1000 / 60); // convert to minutes
        return ((time * 600000000) + FT_UT_OFFSET);
    }

    public static final long getTime(final long realTimestamp) {
        if (realTimestamp == -1) {
            return MAX_TIME;
        }
        long time = (realTimestamp / 1000); // convert to seconds
        return ((time * 10000000) + FT_UT_OFFSET);
    }

    public static long getFileTimestamp(long timeStampinMillis, boolean roundToMinutes) {
        if (SimpleTimeZone.getDefault().inDaylightTime(new Date())) {
            timeStampinMillis -= 3600000L;
        }
        long time;
        if (roundToMinutes) {
            time = (timeStampinMillis / 1000 / 60) * 600000000;
        } else {
            time = timeStampinMillis * 10000;
        }
        return time + FT_UT_OFFSET;
    }

    public static void addQuestInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        final List<MapleQuestStatus> started = chr.getStartedQuests();
        mplew.writeShort(started.size());

        for (final MapleQuestStatus q : started) {
            mplew.writeShort(q.getQuest().getId());
            mplew.writeMapleAsciiString(q.getCustomData() != null ? q.getCustomData() : "");
        }
        final List<MapleQuestStatus> completed = chr.getCompletedQuests();
        int time;
        mplew.writeShort(completed.size());

        for (final MapleQuestStatus q : completed) {
            mplew.writeShort(q.getQuest().getId());
            time = KoreanDateUtil.getQuestTimestamp(q.getCompletionTime());
            //  mplew.writeInt(time); // maybe start time? no effect.
            //    mplew.writeInt(time); // completion time
            mplew.writeLong(time);
        }
    }

    public static final void addSkillInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        final Map<ISkill, SkillEntry> skills = chr.getSkills();
        mplew.writeShort(skills.size());
        for (final Entry<ISkill, SkillEntry> skill : skills.entrySet()) {
            mplew.writeInt(skill.getKey().getId());
            mplew.writeInt(skill.getValue().skillevel);
//            addExpirationTime(mplew, skill.getValue().expiration);
            if (skill.getKey().isFourthJob()) {
                mplew.writeInt(skill.getValue().masterlevel);
            }
        }
    }

    public static final void addCoolDownInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        final List<MapleCoolDownValueHolder> cd = chr.getCooldowns();
        mplew.writeShort(cd.size());
        for (final MapleCoolDownValueHolder cooling : cd) {
            mplew.writeInt(cooling.skillId);
            mplew.writeShort((int) (cooling.length + cooling.startTime - System.currentTimeMillis()) / 1000);
        }
    }

    public static final void addRocksInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        final int[] mapz = chr.getRegRocks();
        for (int i = 0; i < 5; i++) { // VIP teleport map
            mplew.writeInt(mapz[i]);
        }

        final int[] map = chr.getRocks();
        for (int i = 0; i < 10; i++) { // VIP teleport map
            mplew.writeInt(map[i]);
        }
    }

    public static final void addMonsterBookInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        mplew.writeInt(chr.getMonsterBookCover());
        mplew.write(0);
        chr.getMonsterBook().addCardPacket(mplew);
    }

    public static final void addRingInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {

        mplew.writeShort(0);
        Pair<List<MapleRing>, List<MapleRing>> aRing = chr.getRings(true);
        List<MapleRing> cRing = aRing.getLeft();
        mplew.writeShort(cRing.size());
        for (MapleRing ring : cRing) {
            mplew.writeInt(ring.getPartnerChrId());
            mplew.writeAsciiString(ring.getPartnerName(), 13);
            mplew.writeLong(ring.getRingId());
            mplew.writeLong(ring.getPartnerRingId());
        }
        List<MapleRing> fRing = aRing.getRight();
        mplew.writeShort(fRing.size());
        for (MapleRing ring : fRing) {
            mplew.writeInt(ring.getPartnerChrId());
            mplew.writeAsciiString(ring.getPartnerName(), 13);
            mplew.writeLong(ring.getRingId());
            mplew.writeLong(ring.getPartnerRingId());
            mplew.writeInt(ring.getItemId());
        }
        mplew.writeShort(0);
//        mplew.writeShort((short) (chr.getMarriageRing(false) != null ? 1 : 0));
//        int marriageId = 30000;
//        if (chr.getMarriageRing(false) != null) {
//            mplew.writeInt(marriageId);
//            mplew.writeInt(chr.getId());
//            mplew.writeInt(chr.getMarriageRing(false).getPartnerChrId());
//            mplew.writeShort(3);
//            mplew.writeInt(chr.getMarriageRing(false).getRingId());
//            mplew.writeInt(chr.getMarriageRing(false).getPartnerRingId());
//            mplew.writeAsciiString(chr.getName(), 13);
//            mplew.writeAsciiString(chr.getMarriageRing(false).getPartnerName(), 13);
//        }

    }

    public static void addInventoryInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr) {
        // mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.writeMapleAsciiString(chr.getName());
        mplew.writeInt(chr.getMeso()); // mesos
        mplew.writeInt(chr.getId());
        mplew.writeInt(chr.getBeans());
        mplew.writeInt(0);
        mplew.write(chr.getInventory(MapleInventoryType.EQUIP).getSlotLimit()); // equip slots
        if (ServerConstants.调试输出封包) {
            System.out.println("-------背包装备格子数据输出：" + chr.getInventory(MapleInventoryType.EQUIP).getSlotLimit());
        }
        mplew.write(chr.getInventory(MapleInventoryType.USE).getSlotLimit()); // use slots
        if (ServerConstants.调试输出封包) {
            System.out.println("-------背包消耗格子数据输出：" + chr.getInventory(MapleInventoryType.USE).getSlotLimit());
        }
        mplew.write(chr.getInventory(MapleInventoryType.SETUP).getSlotLimit()); // set-up slots
        if (ServerConstants.调试输出封包) {
            System.out.println("-------背包特殊格子数据输出：" + chr.getInventory(MapleInventoryType.SETUP).getSlotLimit());
        }
        mplew.write(chr.getInventory(MapleInventoryType.ETC).getSlotLimit()); // etc slots
        if (ServerConstants.调试输出封包) {
            System.out.println("-------背包其他格子数据输出：" + chr.getInventory(MapleInventoryType.ETC).getSlotLimit());
        }
        mplew.write(chr.getInventory(MapleInventoryType.CASH).getSlotLimit()); // cash slots
        if (ServerConstants.调试输出封包) {
            System.out.println("-------背包现金格子数据输出：" + chr.getInventory(MapleInventoryType.CASH).getSlotLimit());
        }

        //   mplew.write(unk1);
        //  mplew.write(unk2); 
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        MapleInventory iv = chr.getInventory(MapleInventoryType.EQUIPPED);
        Collection<IItem> equippedC = iv.list();
        List<Item> equipped = new ArrayList<Item>(equippedC.size());

        for (IItem item : equippedC) {
            equipped.add((Item) item);
        }
        Collections.sort(equipped);
        for (Item item : equipped) {
            // if (item.getPosition() > -100) {
            if (item.getPosition() < 0 && item.getPosition() > -100) {
                addItemInfo(mplew, item, false, false);
            }
        }
        mplew.write(0); // start of equipped nx 结束加载1
        for (Item item : equipped) {
            //  if (item.getPosition() <= -100) {
            if (item.getPosition() <= -100 && item.getPosition() > -1000) {
                addItemInfo(mplew, item, false, false);
            }
        }

        mplew.write(0); // start of equip inventory 结束加载2
        iv = chr.getInventory(MapleInventoryType.EQUIP);
        for (IItem item : iv.list()) {
            addItemInfo(mplew, item, false, false);
        }
        /*
         * mplew.writeShort(0); //start of other equips
         *
         * for (Item item : equipped) { if (item.getPosition() <= -1000) {
         * addItemInfo(mplew, item, false, false); } }
         */

        mplew.write(0); // start of use inventory 结束加载3
        iv = chr.getInventory(MapleInventoryType.USE);
        for (IItem item : iv.list()) {
            addItemInfo(mplew, item, false, false);
        }
        mplew.write(0); // start of set-up inventory 结束加载4
        iv = chr.getInventory(MapleInventoryType.SETUP);
        for (IItem item : iv.list()) {
            addItemInfo(mplew, item, false, false);
        }
        mplew.write(0); // start of etc inventory 结束加载5
        iv = chr.getInventory(MapleInventoryType.ETC);
        for (IItem item : iv.list()) {
            addItemInfo(mplew, item, false, false);
        }
        mplew.write(0); // start of cash inventory 结束加载6
        iv = chr.getInventory(MapleInventoryType.CASH);
        for (IItem item : iv.list()) {
            addItemInfo(mplew, item, false, false);
        }
        mplew.write(0); //结束加载7
    }

    public static final void addCharStats(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        mplew.writeInt(chr.getId()); // character id
        mplew.writeAsciiString(chr.getName(), 13);
        mplew.write(chr.getGender()); // gender (0 = male, 1 = female)
        mplew.write(chr.getSkinColor()); // skin color
        mplew.writeInt(chr.getFace()); // face
        mplew.writeInt(chr.getHair()); // hair
        mplew.writeZeroBytes(24);
        mplew.write(chr.getLevel()); // level
        mplew.writeShort(chr.getJob()); // job
        chr.getStat().connectData(mplew);
        mplew.writeShort(chr.getRemainingAp()); // remaining ap
        /*
         * if (GameConstants.isEvan(chr.getJob()) ||
         * GameConstants.isResist(chr.getJob())) { final int size =
         * chr.getRemainingSpSize(); mplew.write(size); for (int i = 0; i <
         * chr.getRemainingSps().length; i++) { if (chr.getRemainingSp(i) > 0) {
         * mplew.write(i + 1); mplew.write(chr.getRemainingSp(i)); } } } else {
         */
        mplew.writeShort(chr.getRemainingSp()); // remaining sp
        //  }
        mplew.writeInt(chr.getExp()); // exp
        mplew.writeShort(chr.getFame()); // fame
        mplew.writeInt(0); // Gachapon exp
        mplew.writeLong(PacketHelper.getTime(System.currentTimeMillis()));
        mplew.writeInt(chr.getMapId()); // current map id
        mplew.write(chr.getInitialSpawnpoint()); // spawnpoint
//        mplew.writeShort(chr.getSubcategory()); //1 here = db
        // mplew.writeZeroBytes(30);
    }

    public static void addCharLook(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, boolean mega) {
        addCharLook(mplew, chr, mega, true);
    }

    public static final void addCharLook(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr, final boolean mega, boolean channelserver) {
        mplew.write(chr.getGender());
        mplew.write(chr.getSkinColor());
        mplew.writeInt(chr.getFace());
        mplew.write(mega ? 0 : 1);
        mplew.writeInt(chr.getHair());

        final Map<Byte, Integer> myEquip = new LinkedHashMap<Byte, Integer>();
        final Map<Byte, Integer> maskedEquip = new LinkedHashMap<Byte, Integer>();
        MapleInventory equip = chr.getInventory(MapleInventoryType.EQUIPPED);

        for (final IItem item : equip.list()) {
            if (item.getPosition() < -128) { //not visible
                continue;
            }
            byte pos = (byte) (item.getPosition() * -1);

            if (pos < 100 && myEquip.get(pos) == null) {
                myEquip.put(pos, item.getItemId());
            } else if ((pos > 100 || pos == -128) && pos != 111) {
                pos = (byte) (pos == -128 ? 28 : pos - 100);
                if (myEquip.get(pos) != null) {
                    maskedEquip.put(pos, myEquip.get(pos));
                }
                myEquip.put(pos, item.getItemId());
            } else if (myEquip.get(pos) != null) {
                maskedEquip.put(pos, item.getItemId());
            }
        }
        for (final Entry<Byte, Integer> entry : myEquip.entrySet()) {
            mplew.write(entry.getKey());
            mplew.writeInt(entry.getValue());
        }
        mplew.write(0xFF); // end of visible itens
        // masked itens
        for (final Entry<Byte, Integer> entry : maskedEquip.entrySet()) {
            mplew.write(entry.getKey());
            mplew.writeInt(entry.getValue());
        }
        mplew.write(0xFF); // ending markers

        final IItem cWeapon = equip.getItem((byte) -111);
        mplew.writeInt(cWeapon != null ? cWeapon.getItemId() : 0);

        /*
         * final IItem invA =
         * chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -114);
         * final IItem invB =
         * chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -122);
         * final IItem invC =
         * chr.getInventory(MapleInventoryType.EQUIPPED).getItem((byte) -124);
         * //final int peteqid = inv != null ? inv.getItemId() : 0;
         * mplew.writeInt(invA != null ? invA.getItemId() : 0);
         * mplew.writeInt(invB != null ? invB.getItemId() : 0);
         * mplew.writeInt(invC != null ? invC.getItemId() : 0);
         */
        for (int i = 0; i < 3; i++) {
            if (channelserver) {
                mplew.writeInt(chr.getPet(i) != null ? chr.getPet(i).getPetItemId() : 0);
            } else {
                mplew.writeInt(0);
            }
        }
    }

    public static void addExpirationTime(final MaplePacketLittleEndianWriter mplew, long time) {
        mplew.writeLong(getTime(time));
    }
//
//    public static final void addExpirationTime(final MaplePacketLittleEndianWriter mplew, final long time) {
//        mplew.write(0);
//        mplew.writeShort(1408); // 80 05
//        if (time != -1) {
//            mplew.writeInt(KoreanDateUtil.getItemTimestamp(time));
//            mplew.write(1);
//        } else {
//            mplew.writeInt(400967355);
//            mplew.write(2);
//        }
//    }

    public static void addDDItemInfo(MaplePacketLittleEndianWriter mplew, IItem item, boolean zeroPosition, boolean leaveOut, boolean cs) {
        short pos = item.getPosition();
        if (zeroPosition) {
            if (!leaveOut) {
                mplew.write(0);
            }
        } else if (pos <= -1) {
            pos = (byte) (pos * -1);
            if (pos > 100) {

                mplew.write(pos - 100);
            } else {
                mplew.write(pos);
            }
        } else {
            mplew.write(item.getPosition());
        }
        mplew.write(item.getPet() != null ? 3 : item.getType());
        mplew.writeInt(item.getItemId());
        boolean hasUniqueId = item.getUniqueId() > 0;
        //marriage rings arent cash items so dont have uniqueids, but we assign them anyway for the sake of rings
        mplew.write(hasUniqueId ? 1 : 0);
        if (hasUniqueId) {
            mplew.writeLong(item.getUniqueId());
        }
        addExpirationTime(mplew, item.getExpiration());
        if (item.getType() == 1) {
            final IEquip equip = (IEquip) item;
            mplew.write(equip.getUpgradeSlots());
            mplew.write(equip.getLevel());
            mplew.writeShort(equip.getStr()); // str
            mplew.writeShort(equip.getDex()); // dex
            mplew.writeShort(equip.getInt()); // int
            mplew.writeShort(equip.getLuk()); // luk
            mplew.writeShort(equip.getHp()); // hp
            mplew.writeShort(equip.getMp()); // mp
            mplew.writeShort(equip.getWatk()); // watk
            mplew.writeShort(equip.getMatk()); // matk
            mplew.writeShort(equip.getWdef()); // wdef
            mplew.writeShort(equip.getMdef()); // mdef
            mplew.writeShort(equip.getAcc()); // accuracy
            mplew.writeShort(equip.getAvoid()); // avoid
            mplew.writeShort(equip.getHands()); // hands
            mplew.writeShort(equip.getSpeed()); // speed
            mplew.writeShort(equip.getJump()); // jump
            mplew.writeMapleAsciiString(equip.getOwner()); //拥有者名字 没名字发 short 0
            mplew.writeShort(equip.getFlag());
            mplew.write(0);
            mplew.write(0);
            //道具交易次数?
            mplew.writeShort(0);
            //道具经验?
            mplew.writeShort(0);
            // 0 无绑定; 1 绑定
            mplew.write(0);
            mplew.write(0);
            // if (!masking && !cs) {
            mplew.writeLong(0);
            mplew.writeShort(0);
            mplew.writeShort(0);
            mplew.writeShort(0);
            mplew.writeLong(DateUtil.getFileTimestamp(System.currentTimeMillis()));
            // } else {
            //  mplew.write(HexTool.getByteArrayFromHexString("00 00 00 00 00 00"));
            // mplew.writeLong(DateUtil.getFileTimestamp(System.currentTimeMillis()));
            //}
            mplew.writeInt(-1);
        } else {
            mplew.writeShort(item.getQuantity());
            mplew.writeMapleAsciiString(item.getOwner());
            mplew.writeShort(0); // this seems to end the item entry
            // but only if its not a THROWING STAR :))9 O.O!

            if (GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId())) {
                mplew.writeInt(2);
                mplew.writeShort(0x54);
                mplew.write(0);
                mplew.write(0x34);
            }
        }
    }

    public static void addItemInfo(final MaplePacketLittleEndianWriter mplew, final IItem item, final boolean zeroPosition, final boolean leaveOut) {
        addItemInfo(mplew, item, zeroPosition, leaveOut, false);
    }

    public static void addItemInfo(final MaplePacketLittleEndianWriter mplew, final IItem item, final boolean zeroPosition, final boolean leaveOut, final boolean trade) {
        //boolean isPet = item.getPet() != null && item.getPet().getUniqueId() > -1;
        //boolean isRing = false;
        //Equip equip_ = null;
        short pos = item.getPosition();
        //if (item.getType() == 1) {
        //    equip_ = (Equip) item;
        //    isRing = equip_.getRing() != null && equip_.getRing().getRingId() > -1;
        //}

        if (zeroPosition) {
            if (!leaveOut) {
                mplew.write(0);
            }
        } else if (pos <= -1) {
            pos = (byte) (pos * -1);
            if (pos > 100) {

                mplew.write(pos - 100);
            } else {
                mplew.write(pos);
            }
        } else {
            mplew.write(item.getPosition());
        }
        mplew.write(item.getPet() != null ? 3 : item.getType());
        mplew.writeInt(item.getItemId());
        boolean hasUniqueId = item.getUniqueId() > 0;
        //marriage rings arent cash items so dont have uniqueids, but we assign them anyway for the sake of rings
        mplew.write(hasUniqueId ? 1 : 0);
        if (hasUniqueId) {
           // if (isPet) {
           //     mplew.writeLong(item.getPet().getUniqueId());
          //  } else {
                mplew.writeLong(item.getUniqueId());
          //  }
        }

        if (item.getPet() != null) { // Pet
            addPetItemInfo(mplew, item, item.getPet(), true);
        } else {
            addExpirationTime(mplew, item.getExpiration());
            if (item.getType() == 1) {
                final IEquip equip = (IEquip) item;
                mplew.write(equip.getUpgradeSlots());
                mplew.write(equip.getLevel());

                mplew.writeShort(equip.getStr());
                mplew.writeShort(equip.getDex());
                mplew.writeShort(equip.getInt());
                mplew.writeShort(equip.getLuk());

                mplew.writeShort(equip.getHp());
                mplew.writeShort(equip.getMp());

                mplew.writeShort(equip.getWatk());
                mplew.writeShort(equip.getMatk());
                mplew.writeShort(equip.getWdef());
                mplew.writeShort(equip.getMdef());

                mplew.writeShort(equip.getAcc());
                mplew.writeShort(equip.getAvoid());
                mplew.writeShort(equip.getHands());
                mplew.writeShort(equip.getSpeed());
                mplew.writeShort(equip.getJump());

                mplew.writeMapleAsciiString(equip.getOwner());//拥有者名字
                mplew.writeShort(equip.getFlag());//是否可交易
                if (!hasUniqueId) {
                    mplew.write(0);//是否为锁定物品 equip.getLocked()
                    mplew.write(Math.max(equip.getBaseLevel(), equip.getEquipLevel())); // Item level
                    mplew.writeInt(equip.getExpPercentage()); // Item Exp... 98% = 25%
                    mplew.writeInt(equip.getViciousHammer());
                    mplew.writeLong(0); //some tracking ID
                } else {
                    mplew.writeShort(0);
                    mplew.writeShort(0);
                    mplew.writeShort(0);
                    mplew.writeShort(0);
                    mplew.writeShort(0);
                }
                if (GameConstants.is豆豆装备(equip.getItemId())) {
                    mplew.writeInt(0);
                    mplew.writeLong(DateUtil.getFileTimestamp(System.currentTimeMillis()));
                } else {
                    addExpirationTime(mplew, item.getExpiration());
                }

                mplew.writeInt(-1);
                // mplew.write(unk1);
                //  mplew.write(unk2);
            } else {
                mplew.writeShort(item.getQuantity());
                mplew.writeMapleAsciiString(item.getOwner());
                mplew.writeShort(item.getFlag());

                if (GameConstants.isThrowingStar(item.getItemId()) || GameConstants.isBullet(item.getItemId())) {
                    mplew.writeInt(2);
                    mplew.writeShort(0x54);
                    mplew.write(0);
                    mplew.write(0x34);
                }
            }
        }
    }

    public static final void serializeMovementList(final LittleEndianWriter lew, final List<LifeMovementFragment> moves) {
        lew.write(moves.size());
        for (LifeMovementFragment move : moves) {
            move.serialize(lew);
        }
    }

    public static final void addAnnounceBox(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        if (chr.getPlayerShop() != null && chr.getPlayerShop().isOwner(chr) && chr.getPlayerShop().getShopType() != 1 && chr.getPlayerShop().isAvailable()) {
            addInteraction(mplew, chr.getPlayerShop());
        } else {
            mplew.write(0);
        }
    }

    public static final void addInteraction(final MaplePacketLittleEndianWriter mplew, IMaplePlayerShop shop) {
        mplew.write(shop.getGameType());
        mplew.writeInt(((AbstractPlayerStore) shop).getObjectId());
        mplew.writeMapleAsciiString(shop.getDescription());
        if (shop.getShopType() != 1) {
            mplew.write(shop.getPassword().length() > 0 ? 1 : 0); //password = false
        }
        mplew.write(shop.getItemId() % 10);
        mplew.write(shop.getSize()); //current size
        mplew.write(shop.getMaxSize()); //full slots... 4 = 4-1=3 = has slots, 1-1=0 = no slots
        if (shop.getShopType() != 1) {
            mplew.write(shop.isOpen() ? 0 : 1);
        }
    }

    public static final void addCharacterInfo(final MaplePacketLittleEndianWriter mplew, final MapleCharacter chr) {
        mplew.writeLong(-1);
        mplew.write(0);
        addCharStats(mplew, chr);
        mplew.write(chr.getBuddylist().getCapacity());
        // Bless
        //  if (chr.getBlessOfFairyOrigin() != null) {
        //     mplew.write(1);
        //     mplew.writeMapleAsciiString(chr.getBlessOfFairyOrigin());
        //  } else {
        mplew.write(1);
        // }
        // End
        addInventoryInfo(mplew, chr);
        addSkillInfo(mplew, chr);
        addCoolDownInfo(mplew, chr);
        addQuestInfo(mplew, chr);
        addRingInfo(mplew, chr);
        addRocksInfo(mplew, chr);
        addMonsterBookInfo(mplew, chr);
        chr.QuestInfoPacket(mplew); // for every questinfo: int16_t questid, string questdata
        mplew.writeInt(0); // PQ rank
        mplew.writeShort(0);
    }

    public static final void addPetItemInfo(final MaplePacketLittleEndianWriter mplew, final IItem item, final MaplePet pet, boolean active) {
        if (item == null) {
            mplew.writeLong(getKoreanTimestamp((long) (System.currentTimeMillis() * 1.5)));
        } else {
            addExpirationTime(mplew, item.getExpiration() <= System.currentTimeMillis() ? -1 : item.getExpiration());
        }
        //PacketHelper.addExpirationTime(mplew, -1); //always
        mplew.writeAsciiString(pet.getName(), 13);
        mplew.write(pet.getLevel());
        mplew.writeShort(pet.getCloseness());
        mplew.write(pet.getFullness());
        if (item == null) {
            mplew.writeLong(PacketHelper.getKoreanTimestamp((long) (System.currentTimeMillis())));
        } else {
            PacketHelper.addExpirationTime(mplew, item.getExpiration() <= System.currentTimeMillis() ? -1 : item.getExpiration());
        }
        mplew.writeShort(0);
        mplew.writeShort(pet.getFlags());
        mplew.writeInt(pet.getPetItemId() == 5000054 && pet.getSecondsLeft() > 0 ? pet.getSecondsLeft() : 0); //in seconds, 3600 = 1 hr.
        //mplew.writeShort(0);
        mplew.write(0);
        mplew.write(active ? pet.getSummoned() ? pet.getSummonedValue() : 0 : 0);//显示装备栏上宠物的位置
        /*
         * for (int i = 0; i < 4; i++) { mplew.write(0); //0x40 before, changed
         * to 0? }
         */
 /*
         * if (pet.getPetItemId() == 5000054) { mplew.writeInt(0);
         * mplew.writeInt(pet.getSecondsLeft() > 0 ? pet.getSecondsLeft() : 0);
         * //in seconds, 3600 = 1 hr. mplew.writeShort(0); } else {
         * mplew.writeShort(0); mplew.writeLong(item != null &&
         * item.getExpiration() <= System.currentTimeMillis() ? 0 : 1); }
         */
//        mplew.writeZeroBytes(5); // 1C 5C 98 C6 0
    }

    /**
     * 添加戒指信息
     *
     * @param mplew
     * @param item
     * @param zeroPosition
     * @param leaveOut
     * @param cs ti
     */
    private static void addRingItemInfo(MaplePacketLittleEndianWriter mplew, IItem item, boolean zeroPosition, boolean leaveOut, boolean cs) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        boolean ring = false;
        IEquip equip = null;
        if (item.getType() == 1) {
            equip = (IEquip) item;
            if (equip.getRing() != null) {
                ring = true;
            }
        }
        short pos = item.getPosition();
        boolean masking = false;
        boolean equipped = false;

        if (zeroPosition) {
            if (!leaveOut) {
                mplew.write(0);
            }
        } else if (pos <= (byte) -1) {
            pos *= -1;
            if ((pos > 100 || pos == -128) || ring) {
                masking = true;
                //mplew.write(0);
                mplew.write(pos - 100);
            } else {
                mplew.write(pos);
            }
            equipped = true;
        } else {
            mplew.write(item.getPosition());
        }
        /*
         * if (ring) { mplew.write(1); mplew.writeInt(equip.getUniqueId());
         * mplew.writeInt(0); }
         */
 /*
         * if (!equipped) { mplew.write(0); }
         */
        mplew.write(item.getType());
        mplew.writeInt(item.getItemId());
        mplew.write(1);
        mplew.writeInt(equip.getUniqueId());
        mplew.writeInt(0);
        mplew.writeLong(DateUtil.getFileTimestamp(item.getExpiration()));
        mplew.write(equip.getUpgradeSlots());
        mplew.write(equip.getLevel());
        mplew.writeShort(equip.getStr()); // str
        mplew.writeShort(equip.getDex()); // dex
        mplew.writeShort(equip.getInt()); // int
        mplew.writeShort(equip.getLuk()); // luk
        mplew.writeShort(equip.getHp()); // hp
        mplew.writeShort(equip.getMp()); // mp
        mplew.writeShort(equip.getWatk()); // watk
        mplew.writeShort(equip.getMatk()); // matk
        mplew.writeShort(equip.getWdef()); // wdef
        mplew.writeShort(equip.getMdef()); // mdef
        mplew.writeShort(equip.getAcc()); // accuracy
        mplew.writeShort(equip.getAvoid()); // avoid
        mplew.writeShort(equip.getHands()); // hands
        mplew.writeShort(equip.getSpeed()); // speed
        mplew.writeShort(equip.getJump()); // jump
        mplew.writeMapleAsciiString(equip.getOwner());
        //道具交易次数?
        mplew.writeShort(0);
        //道具经验?
        mplew.writeShort(0);
        mplew.write(1);
        mplew.write(0);
        mplew.writeShort(0);
        mplew.writeShort(0);
        mplew.writeShort(0);
        mplew.writeLong(DateUtil.getFileTimestamp(System.currentTimeMillis()));
        mplew.writeInt(-1);
    }
}
