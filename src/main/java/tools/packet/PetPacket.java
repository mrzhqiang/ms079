package tools.packet;

import client.MapleCharacter;
import client.MapleStat;
import client.inventory.IItem;
import client.inventory.MaplePet;
import constants.ServerConstants;
import handling.MaplePacket;
import handling.SendPacketOpcode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.movement.LifeMovementFragment;
import tools.MaplePacketCreator;
import tools.data.output.MaplePacketLittleEndianWriter;

import java.util.List;

public class PetPacket {

    private static final Logger LOGGER = LoggerFactory.getLogger(PetPacket.class);

    private final static byte[] ITEM_MAGIC = new byte[]{(byte) 0x80, 5};

    public static final MaplePacket updatePet(final MaplePet pet, final IItem item, boolean active) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("updatePet--------------------");
        }
        mplew.writeShort(SendPacketOpcode.MODIFY_INVENTORY_ITEM.getValue());

        //mplew.writeShort(pet.getInventoryPosition());
        //mplew.write(pet.getInventoryPosition());//显示装备栏的坐标
        mplew.write(0);//改为write(0);,不显示它
        mplew.write(2);
        mplew.write(3);
        mplew.write(5);
        mplew.writeShort(pet.getInventoryPosition());
        mplew.write(0);
        mplew.write(5);
        mplew.writeShort(pet.getInventoryPosition());
        mplew.write(3);
        mplew.writeInt(pet.getPetItemId());
        mplew.write(1);
        mplew.writeLong(pet.getUniqueId());
        PacketHelper.addPetItemInfo(mplew, item, pet, active);
        return mplew.getPacket();
    }

    public static final MaplePacket removePet(final MapleCharacter chr, final int slot) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("removePet--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SPAWN_PET.getValue());
        mplew.writeInt(chr.getId());
        mplew.writeShort(slot);
        return mplew.getPacket();
    }

    public static final MaplePacket showPet(final MapleCharacter chr, final MaplePet pet, final boolean remove, final boolean hunger) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("showPet--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SPAWN_PET.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(chr.getPetIndex(pet));
        if (remove) {
            mplew.write(0);
            mplew.write(hunger ? 1 : 0);
        } else {
            mplew.write(1);
            mplew.write(0); //1?
            mplew.writeInt(pet.getPetItemId());
            mplew.writeMapleAsciiString(pet.getName());
            mplew.writeLong(pet.getUniqueId());
            //mplew.writePos(pet.getPos());
            mplew.writeShort(pet.getPos().x);
            mplew.writeShort(pet.getPos().y + 20);
            mplew.write(pet.getStance());
            mplew.writeLong(pet.getFh());
        }
        return mplew.getPacket();
    }

    public static void addPetInfo(MaplePacketLittleEndianWriter mplew, MapleCharacter chr, MaplePet pet, boolean showpet) {//079
        if (showpet) {
            mplew.write(1);
            mplew.write(chr.getPetIndex(pet));
        }
        mplew.writeInt(pet.getPetItemId());
        mplew.writeMapleAsciiString(pet.getName());
        mplew.writeLong(pet.getUniqueId());
        //mplew.writePos(pet.getPos());
        mplew.writeShort(pet.getPos().x);
        mplew.writeShort(pet.getPos().y);
        mplew.write(pet.getStance());
        mplew.writeLong(pet.getFh());
        //   mplew.writeShort(pet.getFh());
    }

    public static final MaplePacket removePet(final int cid, final int index) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("removePet--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SPAWN_PET.getValue());
        mplew.writeInt(cid);
        mplew.write(index);
        mplew.writeShort(0);
        return mplew.getPacket();
    }

    public static final MaplePacket movePet(final int cid, final int pid, final byte slot, final List<LifeMovementFragment> moves) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("movePet--------------------");
        }
        mplew.writeShort(SendPacketOpcode.MOVE_PET.getValue());
        mplew.writeInt(cid);
        mplew.write(slot);
        mplew.writeInt(pid);
        PacketHelper.serializeMovementList(mplew, moves);

        return mplew.getPacket();
    }

    public static final MaplePacket petChat(final int cid, final int un, final String text, final byte slot) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("petChat--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PET_CHAT.getValue());
        mplew.writeInt(cid);
        mplew.write(slot);
        mplew.writeShort(un);
        mplew.writeMapleAsciiString(text);
        mplew.write(0); //hasQuoteRing

        return mplew.getPacket();
    }
    public static final MaplePacket commandResponse(final int cid, final int slot, final int animation, final boolean success) {//079
 // public static final MaplePacket commandResponse(final int cid, final byte command, final byte slot, final boolean success, final boolean food) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("commandResponse--------------------");
        }
        mplew.writeShort(SendPacketOpcode.PET_COMMAND.getValue());
        mplew.writeInt(cid);
        mplew.write(slot);
        mplew.write((animation == 1) && (success) ? 1 : 0);
       // mplew.write(command == 1 ? 1 : 0);
        mplew.write(animation);
        if (animation == 1) {
            mplew.write(0);
        } else {
            mplew.writeShort(success ? 1 : 0);
        }
        return mplew.getPacket();
    }

    public static final MaplePacket showOwnPetLevelUp(final byte index) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("showOwnPetLevelUp--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SHOW_ITEM_GAIN_INCHAT.getValue());
        mplew.write(4);
        mplew.write(0);
        mplew.write(index); // Pet Index

        return mplew.getPacket();
    }

    public static final MaplePacket showPetLevelUp(final MapleCharacter chr, final byte index) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("showPetLevelUp--------------------");
        }
        mplew.writeShort(SendPacketOpcode.SHOW_FOREIGN_EFFECT.getValue());
        mplew.writeInt(chr.getId());
        mplew.write(4);
        mplew.write(0);
        mplew.write(index);

        return mplew.getPacket();
    }

    public static final MaplePacket emptyStatUpdate() {
        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("emptyStatUpdate--------------------");
        }
        return MaplePacketCreator.enableActions();
    }

    public static final MaplePacket petStatUpdate_Empty() {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("petStatUpdate_Empty--------------------");
        }
        mplew.writeShort(SendPacketOpcode.UPDATE_STATS.getValue());
        mplew.write(0);
        mplew.writeInt(MapleStat.PET.getValue());
        mplew.writeZeroBytes(25);
        return mplew.getPacket();
    }

    public static final MaplePacket petStatUpdate(final MapleCharacter chr) {
        final MaplePacketLittleEndianWriter mplew = new MaplePacketLittleEndianWriter();

        if (ServerConstants.properties.isPacketDebugLogger()) {
            LOGGER.debug("petStatUpdate--------------------");
        }
        mplew.writeShort(SendPacketOpcode.UPDATE_STATS.getValue());
        mplew.write(0);
        mplew.writeInt(MapleStat.PET.getValue());

        byte count = 0;
        for (final MaplePet pet : chr.getPets()) {
            if (pet.getSummoned()) {
                mplew.writeLong(pet.getUniqueId());
                count++;
            }
        }
        while (count < 3) {
            mplew.writeZeroBytes(8);
            count++;
        }
        mplew.write(0);
        //mplew.writeShort(0);
        return mplew.getPacket();
    }
}
