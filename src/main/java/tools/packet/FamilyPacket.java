package tools.packet;

import client.MapleCharacter;
import constants.ServerConstants;
import handling.MaplePacket;
import handling.SendPacketOpcode;
import handling.world.World;
import handling.world.family.MapleFamily;
import handling.world.family.MapleFamilyBuff;
import handling.world.family.MapleFamilyBuff.MapleFamilyBuffEntry;
import handling.world.family.MapleFamilyCharacter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.Pair;
import tools.data.output.MaplePacketLittleEndianWriter;

import java.util.List;

public class FamilyPacket {

    private static final Logger LOGGER = LoggerFactory.getLogger(FamilyPacket.class);

    public static MaplePacket getFamilyData() {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("getFamilyData--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FAMILY.getValue());
       /*  String[] title = {"直接移动到学院成员身边", "直接召唤学院成员", "我的爆率 1.5倍(15分钟)", "我的经验值 1.5倍(15分钟)", "学院成员的团结(30分钟)", "我的爆率 2倍(15分钟)", "我的经验值 2倍(15分钟)", "我的爆率 2倍(30分钟)", "我的经验值 2倍(30分钟)", "我的组队爆率 2倍(30分钟)", "我的组队经验值 2倍(30分钟)"};
        String[] description = {"[对象] 我\n[效果] 直接可以移动到指定的学院成员身边。", "[对象] 学院成员 1名\n[效果] 直接可以召唤指定的学院成员到现在的地图。", "[对象] 我\n[持续效果] 15分钟\n[效果] 打怪爆率增加到 #c1.5倍# \n※ 与爆率活动重叠时失效。", "[对象] 我\n[持续效果] 15分钟\n[效果] 打怪经验值增加到 #c1.5倍# \n※ 与经验值活动重叠时失效。", "[启动条件] 校谱最低层学院成员6名以上在线时\n[持续效果] 30分钟\n[效果] 爆率和经验值增加到 #c2倍# ※ 与爆率、经验值活动重叠时失效。", "[对象] 我\n[持续效果] 15分钟\n[效果] 打怪爆率增加到 #c2倍# \n※ 与爆率活动重叠时失效。", "[对象] 我\n[持续效果] 15分钟\n[效果] 打怪经验值增加到 #c2倍# \n※ 与经验值活动重叠时失效。", "[对象] 我\n[持续效果] 30分钟\n[效果] 打怪爆率增加到 #c2倍# \n※ 与爆率活动重叠时失效。", "[对象] 我\n[持续效果] 30分钟\n[效果] 打怪经验值增加到 #c2倍# \n※ 与经验值活动重叠时失效。", "[对象] 我所属组队\n[持续效果] 30分钟\n[效果] 打怪爆率增加到 #c2倍# \n※ 与爆率活动重叠时失效。", "[对象] 我所属组队\n[持续效果] 30分钟\n[效果] 打怪经验值增加到 #c2倍# \n※ 与经验值活动重叠时失效。"};
        int[] repCost = {3, 5, 7, 8, 10, 12, 15, 20, 25, 40, 50};
        mplew.writeInt(11);
        for (int i = 0; i < 11; i++) {
            mplew.write(i > 4 ? i % 2 + 1 : i);
            mplew.writeInt(repCost[i] * 100);
            mplew.writeInt(1);
            mplew.writeMapleAsciiString(title[i]);
            mplew.writeMapleAsciiString(description[i]);
        }*/
        List<MapleFamilyBuffEntry> entries = MapleFamilyBuff.getBuffEntry();
        mplew.writeInt(entries.size()); // Number of events

        for (MapleFamilyBuffEntry entry : entries) {
            mplew.write(entry.type);
            mplew.writeInt(entry.rep * 100);
            mplew.writeInt(entry.count);
            mplew.writeMapleAsciiString(entry.name);
            mplew.writeMapleAsciiString(entry.desc);
        }
        return mplew.getPacket();
    }

    public static MaplePacket changeRep(int r) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("changeRep--------------------");
        }
        mplew.writeShort(SendPacketOpcode.REP_INCREASE.getValue());
        mplew.writeInt(r);
        mplew.writeInt(0);
        return mplew.getPacket();
    }

    public static MaplePacket getFamilyInfo(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("getFamilyInfo--------------------");
        }
        mplew.writeShort(SendPacketOpcode.OPEN_FAMILY.getValue());
        mplew.writeInt(chr.getCurrentRep()); //rep
        mplew.writeInt(chr.getTotalRep()); // total rep
        mplew.writeInt(chr.getTotalRep()); //rep recorded today
        mplew.writeShort(chr.getNoJuniors());
        mplew.writeShort(2);
        mplew.writeShort(chr.getNoJuniors());
        MapleFamily family = World.Family.getFamily(chr.getFamilyId());
        if (family != null) {
            mplew.writeInt(family.getLeaderId()); //??? 9D 60 03 00
            mplew.writeMapleAsciiString(family.getLeaderName());
            mplew.writeMapleAsciiString(family.getNotice()); //message?
        } else {
            mplew.writeLong(0);
        }
        List<Pair<Integer, Integer>> b = chr.usedBuffs();
        mplew.writeInt(b.size());
        for (Pair<Integer, Integer> ii : b) {
            mplew.writeInt(ii.getLeft()); //buffid
            mplew.writeInt(ii.getRight()); //times used
        }
        return mplew.getPacket();
    }

    public static void addFamilyCharInfo(MapleFamilyCharacter ldr, MaplePacketLittleEndianWriter mplew) {
        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("addFamilyCharInfo--------------------");
        }
        mplew.writeInt(ldr.getId());
        mplew.writeInt(ldr.getSeniorId());
        mplew.writeShort(ldr.getJobId());
        mplew.write(ldr.getLevel());
        mplew.write(ldr.isOnline() ? 1 : 0);
        mplew.writeInt(ldr.getCurrentRep());
        mplew.writeInt(ldr.getTotalRep());
        mplew.writeInt(ldr.getTotalRep()); //recorded rep to senior
        mplew.writeInt(ldr.getTotalRep()); //then recorded rep to sensen
        mplew.writeLong(Math.max(ldr.getChannel(), 0)); //channel->time online
        mplew.writeMapleAsciiString(ldr.getName());
    }

    public static MaplePacket getFamilyPedigree(MapleCharacter chr) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("getFamilyPedigree--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SEND_PEDIGREE.getValue());
        mplew.writeInt(chr.getId());
        MapleFamily family = World.Family.getFamily(chr.getFamilyId());
        int descendants = 2, gens = 0, generations = 0;
        if (family == null) {
            mplew.writeInt(2);
            addFamilyCharInfo(new MapleFamilyCharacter(chr.character, 0, true), mplew); //leader
        } else {
            mplew.writeInt(family.getMFC(chr.getId()).getPedigree().size() + 1); //+ 1 for leader, but we don't want leader seeing all msgs
            addFamilyCharInfo(family.getMFC(family.getLeaderId()), mplew);

            if (chr.getSeniorId() > 0) {
                MapleFamilyCharacter senior = family.getMFC(chr.getSeniorId());
                if (senior.getSeniorId() > 0) {
                    addFamilyCharInfo(family.getMFC(senior.getSeniorId()), mplew);
                }
                addFamilyCharInfo(senior, mplew);
            }
        }
        addFamilyCharInfo(chr.getMFC() == null ? new MapleFamilyCharacter(chr.character, 0,true) : chr.getMFC(), mplew);
        if (family != null) {
            if (chr.getSeniorId() > 0) {
                MapleFamilyCharacter senior = family.getMFC(chr.getSeniorId());
                if (senior != null) {
                    if (senior.getJunior1() > 0 && senior.getJunior1() != chr.getId()) {
                        addFamilyCharInfo(family.getMFC(senior.getJunior1()), mplew);
                    } else if (senior.getJunior2() > 0 && senior.getJunior2() != chr.getId()) {
                        addFamilyCharInfo(family.getMFC(senior.getJunior2()), mplew);
                    }
                }
            }
            if (chr.getJunior1() > 0) {
                addFamilyCharInfo(family.getMFC(chr.getJunior1()), mplew);
            }
            if (chr.getJunior2() > 0) {
                addFamilyCharInfo(family.getMFC(chr.getJunior2()), mplew);
            }
            if (chr.getJunior1() > 0) {
                MapleFamilyCharacter junior = family.getMFC(chr.getJunior1());
                if (junior.getJunior1() > 0) {
                    descendants++;
                    addFamilyCharInfo(family.getMFC(junior.getJunior1()), mplew);
                }
                if (junior.getJunior2() > 0) {
                    descendants++;
                    addFamilyCharInfo(family.getMFC(junior.getJunior2()), mplew);
                }
            }
            if (chr.getJunior2() > 0) {
                MapleFamilyCharacter junior = family.getMFC(chr.getJunior2());
                if (junior.getJunior1() > 0) {
                    descendants++;
                    addFamilyCharInfo(family.getMFC(junior.getJunior1()), mplew);
                }
                if (junior.getJunior2() > 0) {
                    descendants++;
                    addFamilyCharInfo(family.getMFC(junior.getJunior2()), mplew);
                }
            }
            gens = family.getGens();
            generations = family.getMemberSize();
        }
        mplew.writeLong(descendants);
        mplew.writeInt(gens);
        mplew.writeInt(-1);
        mplew.writeInt(generations);
        if (family != null) {
            if (chr.getJunior1() > 0) {
                MapleFamilyCharacter junior = family.getMFC(chr.getJunior1());
                if (junior.getJunior1() > 0) {
                    mplew.writeInt(junior.getJunior1());
                    mplew.writeInt(family.getMFC(junior.getJunior1()).getDescendants());
                }
                if (junior.getJunior2() > 0) {
                    mplew.writeInt(junior.getJunior2());
                    mplew.writeInt(family.getMFC(junior.getJunior2()).getDescendants());
                }
            }
            if (chr.getJunior2() > 0) {
                MapleFamilyCharacter junior = family.getMFC(chr.getJunior2());
                if (junior.getJunior1() > 0) {
                    mplew.writeInt(junior.getJunior1());
                    mplew.writeInt(family.getMFC(junior.getJunior1()).getDescendants());
                }
                if (junior.getJunior2() > 0) {
                    mplew.writeInt(junior.getJunior2());
                    mplew.writeInt(family.getMFC(junior.getJunior2()).getDescendants());
                }
            }
        }

        List<Pair<Integer, Integer>> b = chr.usedBuffs();
        mplew.writeInt(b.size());
        for (Pair<Integer, Integer> ii : b) {
            mplew.writeInt(ii.getLeft()); //buffid
            mplew.writeInt(ii.getRight()); //times used
        }
        mplew.writeShort(2);
        return mplew.getPacket();
    }

    public static MaplePacket sendFamilyInvite(int cid, int otherLevel, int otherJob, String inviter) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("sendFamilyInvite--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FAMILY_INVITE.getValue());
        mplew.writeInt(cid); //the inviter
//	mplew.writeInt(otherLevel);
//	mplew.writeInt(otherJob);
        mplew.writeMapleAsciiString(inviter);

        return mplew.getPacket();
    }

    public static MaplePacket getSeniorMessage(String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("getSeniorMessage--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SENIOR_MESSAGE.getValue());
        mplew.writeMapleAsciiString(name);
        return mplew.getPacket();
    }

    public static MaplePacket sendFamilyJoinResponse(boolean accepted, String added) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("sendFamilyJoinResponse--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FAMILY_JUNIOR.getValue());
        mplew.write(accepted ? 1 : 0);
        mplew.writeMapleAsciiString(added);
        return mplew.getPacket();
    }

    public static MaplePacket familyBuff(int type, int buffnr, int amount, int time) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("familyBuff--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FAMILY_BUFF.getValue());
        mplew.write(type);
        if (type >= 2 && type <= 4) {
            mplew.writeInt(buffnr);
            //first int = exp, second int = drop
            mplew.writeInt(type == 3 ? 0 : amount);
            mplew.writeInt(type == 2 ? 0 : amount);
            mplew.write(0);
            mplew.writeInt(time);
        }
        return mplew.getPacket();
    }

    public static MaplePacket cancelFamilyBuff() {
        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("cancelFamilyBuff--------------------");
        }
        return familyBuff(0, 0, 0, 0);
    }

    public static MaplePacket familyLoggedIn(boolean online, String name) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("familyLoggedIn--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FAMILY_LOGGEDIN.getValue());
        mplew.write(online ? 1 : 0);
        mplew.writeMapleAsciiString(name);
        return mplew.getPacket();
    }

    public static MaplePacket familySummonRequest(String name, String mapname) {
        MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();
        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("familySummonRequest--------------------");
        }
        mplew.writeShort(SendPacketOpcode.FAMILY_USE_REQUEST.getValue());
        mplew.writeMapleAsciiString(name);
        mplew.writeMapleAsciiString(mapname);
        return mplew.getPacket();
    }
}
