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

import client.*;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;

import client.inventory.Equip;
import client.inventory.IItem;
import constants.GameConstants;
import client.inventory.ItemFlag;
import client.inventory.MapleInventory;
import client.inventory.MapleInventoryType;
import client.inventory.*;
import server.MapleCarnivalParty;
import server.Randomizer;
import server.MapleInventoryManipulator;
import server.MapleShopFactory;
import server.MapleSquad;
import server.maps.MapleMap;
import server.maps.Event_DojoAgent;
import server.maps.AramiaFireWorks;
import server.quest.MapleQuest;
import tools.MaplePacketCreator;
import tools.Pair;
import tools.packet.PlayerShopPacket;
import server.MapleItemInformationProvider;
import handling.channel.ChannelServer;
import handling.channel.MapleGuildRanking;
import database.DatabaseConnection;
import handling.channel.handler.HiredMerchantHandler;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.World;
import handling.world.guild.MapleGuild;
import server.MapleCarnivalChallenge;
import java.util.HashMap;
import handling.world.guild.MapleGuildAlliance;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import javax.script.Invocable;
import server.*;
import server.MapleSquad.MapleSquadType;
import server.maps.SpeedRunType;
import server.Timer.CloneTimer;
import server.life.*;
import server.maps.*;
import tools.StringUtil;

public class NPCConversationManager extends AbstractPlayerInteraction {

    private MapleClient c;
    private int npc, questid;
    private String getText;
    private byte type; // -1 = NPC, 0 = start quest, 1 = end quest
    private byte lastMsg = -1;
    public boolean pendingDisposal = false;
    private Invocable iv;
    private int wh = 0;

    /*
     * public NPCConversationManager(MapleClient c, int npc, int questid, byte
     * type, Invocable iv) { super(c); this.c = c; this.npc = npc; this.questid
     * = questid; this.type = type; this.iv = iv; }
     */
    public NPCConversationManager(MapleClient c, int npc, int questid, byte type, Invocable iv, int wh) {
        super(c);
        this.c = c;
        this.npc = npc;
        this.questid = questid;
        this.type = type;
        this.iv = iv;
        this.wh = wh;
    }

    public int getwh() {
        return this.wh;
    }

    public Invocable getIv() {
        return iv;
    }

    public String serverName() {
        return c.getChannelServer().getServerName();
    }

    public int getNpc() {
        return npc;
    }

    public int getQuest() {
        return questid;
    }

    public byte getType() {
        return type;
    }

    public void safeDispose() {
        pendingDisposal = true;
    }

    public void dispose() {
        NPCScriptManager.getInstance().dispose(c);
    }

    public void askMapSelection(final String sel) {
        if (lastMsg > -1) {
            return;
        }
        c.getSession().write(MaplePacketCreator.getMapSelection(npc, sel));
        lastMsg = 0xD;
    }

    public void sendNext(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { //sendNext will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", (byte) 0));
        lastMsg = 0;
    }

    public void sendNextS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 01", type));
        lastMsg = 0;
    }

    public void sendPrev(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", (byte) 0));
        lastMsg = 0;
    }

    public void sendPrevS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 00", type));
        lastMsg = 0;
    }

    public void sendNextPrev(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", (byte) 0));
        lastMsg = 0;
    }

    public void PlayerToNpc(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    public void sendNextPrevS(String text) {
        sendNextPrevS(text, (byte) 3);
    }

    public void sendNextPrevS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "01 01", type));
        lastMsg = 0;
    }

    public void sendOk(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", (byte) 0));
        lastMsg = 0;
    }

    public void sendOkS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0, text, "00 00", type));
        lastMsg = 0;
    }

    public void sendYesNo(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", (byte) 0));
        lastMsg = 1;
    }

    public void sendYesNoS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimpleS(text, type);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 1, text, "", type));
        lastMsg = 1;
    }

    public void sendAcceptDecline(String text) {
        askAcceptDecline(text);
    }

    public void sendAcceptDeclineNoESC(String text) {
        askAcceptDeclineNoESC(text);
    }

    public void askAcceptDecline(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0B, text, "", (byte) 0));
        lastMsg = 0xB;
    }

    public void askAcceptDeclineNoESC(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 0x0C, text, "", (byte) 0));
        lastMsg = 0xC;
    }

    public void askAvatar(String text, int card, int... args) {
        if (lastMsg > -1) {
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalkStyle(npc, text, card, args));
        lastMsg = 7;
    }

    public void sendSimple(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNext(text);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) 0));
        lastMsg = 4;
    }

    public void sendSimple(String text, int speaker) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNext(text);
            return;
        }
        getClient().getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) speaker));
        lastMsg = 4;
    }

    public void sendSimpleS(String text, byte type) {
        if (lastMsg > -1) {
            return;
        }
        if (!text.contains("#L")) { //sendSimple will dc otherwise!
            sendNextS(text, type);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalk(npc, (byte) 4, text, "", (byte) type));
        lastMsg = 4;
    }

    public void sendStyle(String text, int caid, int styles[]) {
        if (lastMsg > -1) {
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalkStyle(npc, text, caid, styles));
        lastMsg = 7;
    }

    public void sendGetNumber(String text, int def, int min, int max) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalkNum(npc, text, def, min, max));
        lastMsg = 3;
    }

    public void sendGetText(String text) {
        if (lastMsg > -1) {
            return;
        }
        if (text.contains("#L")) { // will dc otherwise!
            sendSimple(text);
            return;
        }
        c.getSession().write(MaplePacketCreator.getNPCTalkText(npc, text));
        lastMsg = 2;
    }

    public void setGetText(String text) {
        this.getText = text;
    }

    public String getText() {
        return getText;
    }

    public void setHair(int hair) {
        getPlayer().setHair(hair);
        getPlayer().updateSingleStat(MapleStat.HAIR, hair);
        getPlayer().equipChanged();
    }

    public void setFace(int face) {
        getPlayer().setFace(face);
        getPlayer().updateSingleStat(MapleStat.FACE, face);
        getPlayer().equipChanged();
    }

    public void setSkin(int color) {
        getPlayer().setSkinColor((byte) color);
        getPlayer().updateSingleStat(MapleStat.SKIN, color);
        getPlayer().equipChanged();
    }

    public int setRandomAvatar(int ticket, int[] args_all) {
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);

        int args = args_all[Randomizer.nextInt(args_all.length)];
        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            c.getPlayer().setFace(args);
            c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            c.getPlayer().setHair(args);
            c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    public int setAvatar(int ticket, int args) {
        if (!haveItem(ticket)) {
            return -1;
        }
        gainItem(ticket, (short) -1);

        if (args < 100) {
            c.getPlayer().setSkinColor((byte) args);
            c.getPlayer().updateSingleStat(MapleStat.SKIN, args);
        } else if (args < 30000) {
            c.getPlayer().setFace(args);
            c.getPlayer().updateSingleStat(MapleStat.FACE, args);
        } else {
            c.getPlayer().setHair(args);
            c.getPlayer().updateSingleStat(MapleStat.HAIR, args);
        }
        c.getPlayer().equipChanged();

        return 1;
    }

    public void sendStorage() {
        c.getPlayer().setConversation(4);
        c.getPlayer().getStorage().sendStorage(c, npc);
    }

    public void openShop(int id) {
        MapleShopFactory.getInstance().getShop(id).sendShop(c);
    }

    public int gainGachaponItem(int id, int quantity) {
        return gainGachaponItem(id, quantity, c.getPlayer().getMap().getStreetName() + " - " + c.getPlayer().getMap().getMapName());
    }

    public int gainGachaponItem(int id, int quantity, final String msg) {
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final IItem item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            final byte rareness = GameConstants.gachaponRareItem(item.getItemId());
            if (rareness > 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("[" + msg + "] " + c.getPlayer().getName(), " : 恭喜获得道具!", item, rareness, getPlayer().getClient().getChannel()).getBytes());
            }
            return item.getItemId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int gainGachaponItem(int id, int quantity, final String msg, int 概率) {
        try {
            if (!MapleItemInformationProvider.getInstance().itemExists(id)) {
                return -1;
            }
            final IItem item = MapleInventoryManipulator.addbyId_Gachapon(c, id, (short) quantity);

            if (item == null) {
                return -1;
            }
            if (概率 > 0) {
                World.Broadcast.broadcastMessage(MaplePacketCreator.getGachaponMega("[" + msg + "] " + c.getPlayer().getName(), " : Lucky winner of Gachapon!", item, (byte) 0, getPlayer().getClient().getChannel()).getBytes());
            }
            return item.getItemId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void changeJob(int job) {
        c.getPlayer().changeJob(job);
    }

    public void startQuest(int id) {
        MapleQuest.getInstance(id).start(getPlayer(), npc);
    }

    public void completeQuest(int id) {
        MapleQuest.getInstance(id).complete(getPlayer(), npc);
    }

    public void forfeitQuest(int id) {
        MapleQuest.getInstance(id).forfeit(getPlayer());
    }

    public void forceStartQuest() {
        MapleQuest.getInstance(questid).forceStart(getPlayer(), getNpc(), null);
    }

    public void forceStartQuest(int id) {
        MapleQuest.getInstance(id).forceStart(getPlayer(), getNpc(), null);
    }

    public void forceStartQuest(String customData) {
        MapleQuest.getInstance(questid).forceStart(getPlayer(), getNpc(), customData);
    }

    public void completeQuest() {
        forceCompleteQuest();
    }

    public void forceCompleteQuest() {
        MapleQuest.getInstance(questid).forceComplete(getPlayer(), getNpc());
    }

    public void forceCompleteQuest(final int id) {
        MapleQuest.getInstance(id).forceComplete(getPlayer(), getNpc());
    }

    public String getQuestCustomData() {
        return c.getPlayer().getQuestNAdd(MapleQuest.getInstance(questid)).getCustomData();
    }

    public void setQuestCustomData(String customData) {
        getPlayer().getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(customData);
    }

    public int getLevel() {
        return getPlayer().getLevel();
    }

    public int getMeso() {
        return getPlayer().getMeso();
    }

    public void gainAp(final int amount) {
        c.getPlayer().gainAp((short) amount);
    }

    public void expandInventory(byte type, int amt) {
        c.getPlayer().expandInventory(type, amt);
    }

    public void unequipEverything() {
        MapleInventory equipped = getPlayer().getInventory(MapleInventoryType.EQUIPPED);
        MapleInventory equip = getPlayer().getInventory(MapleInventoryType.EQUIP);
        List<Short> ids = new LinkedList<Short>();
        for (IItem item : equipped.list()) {
            ids.add(item.getPosition());
        }
        for (short id : ids) {
            MapleInventoryManipulator.unequip(getC(), id, equip.getNextFreeSlot());
        }
    }

    public final void clearSkills() {
        Map<ISkill, SkillEntry> skills = getPlayer().getSkills();
        for (Entry<ISkill, SkillEntry> skill : skills.entrySet()) {
            getPlayer().changeSkillLevel(skill.getKey(), (byte) 0, (byte) 0);
        }
    }

    public boolean hasSkill(int skillid) {
        ISkill theSkill = SkillFactory.getSkill(skillid);
        if (theSkill != null) {
            return c.getPlayer().getSkillLevel(theSkill) > 0;
        }
        return false;
    }

    public void showEffect(boolean broadcast, String effect) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.showEffect(effect));
        } else {
            c.getSession().write(MaplePacketCreator.showEffect(effect));
        }
    }

    public void playSound(boolean broadcast, String sound) {
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.playSound(sound));
        } else {
            c.getSession().write(MaplePacketCreator.playSound(sound));
        }
    }

    public void environmentChange(boolean broadcast, String env) {//环境变化
        if (broadcast) {
            c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.environmentChange(env, 2));
        } else {
            c.getSession().write(MaplePacketCreator.environmentChange(env, 2));
        }
    }

    public void updateBuddyCapacity(int capacity) {
        c.getPlayer().setBuddyCapacity((byte) capacity);
    }

    public int getBuddyCapacity() {
        return c.getPlayer().getBuddyCapacity();
    }

    public int partyMembersInMap() {
        int inMap = 0;
        for (MapleCharacter char2 : getPlayer().getMap().getCharactersThreadsafe()) {
            if (char2.getParty() == getPlayer().getParty()) {
                inMap++;
            }
        }
        return inMap;
    }

    public List<MapleCharacter> getPartyMembers() {
        if (getPlayer().getParty() == null) {
            return null;
        }
        List<MapleCharacter> chars = new LinkedList<MapleCharacter>(); // creates an empty array full of shit..
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            for (ChannelServer channel : ChannelServer.getAllInstances()) {
                MapleCharacter ch = channel.getPlayerStorage().getCharacterById(chr.getId());
                if (ch != null) { // double check <3
                    chars.add(ch);
                }
            }
        }
        return chars;
    }

    public void warpPartyWithExp(int mapId, int exp) {
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
            }
        }
    }

    public void warpPartyWithExpMeso(int mapId, int exp, int meso) {
        MapleMap target = getMap(mapId);
        for (MaplePartyCharacter chr : getPlayer().getParty().getMembers()) {
            MapleCharacter curChar = c.getChannelServer().getPlayerStorage().getCharacterByName(chr.getName());
            if ((curChar.getEventInstance() == null && getPlayer().getEventInstance() == null) || curChar.getEventInstance() == getPlayer().getEventInstance()) {
                curChar.changeMap(target, target.getPortal(0));
                curChar.gainExp(exp, true, false, true);
                curChar.gainMeso(meso, true);
            }
        }
    }

    public MapleSquad getSquad(String type) {
        return c.getChannelServer().getMapleSquad(type);
    }

    public int getSquadAvailability(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        }
        return squad.getStatus();
    }

    public boolean registerSquad(String type, int minutes, String startText) {
        if (c.getChannelServer().getMapleSquad(type) == null) {
            final MapleSquad squad = new MapleSquad(c.getChannel(), type, c.getPlayer(), minutes * 60 * 1000, startText);
            final boolean ret = c.getChannelServer().addMapleSquad(squad, type);
            if (ret) {
                final MapleMap map = c.getPlayer().getMap();

                map.broadcastMessage(MaplePacketCreator.getClock(minutes * 60));
                map.broadcastMessage(MaplePacketCreator.serverNotice(6, c.getPlayer().getName() + startText));
            } else {
                squad.clear();
            }
            return ret;
        }
        return false;
    }

    public boolean getSquadList(String type, byte type_) {
//        try {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return false;
        }
        if (type_ == 0 || type_ == 3) { // Normal viewing
            sendNext(squad.getSquadMemberString(type_));
        } else if (type_ == 1) { // Squad Leader banning, Check out banned participant
            sendSimple(squad.getSquadMemberString(type_));
        } else if (type_ == 2) {
            if (squad.getBannedMemberSize() > 0) {
                sendSimple(squad.getSquadMemberString(type_));
            } else {
                sendNext(squad.getSquadMemberString(type_));
            }
        }
        return true;
        /*
         * } catch (NullPointerException ex) {
         * FileoutputUtil.outputFileError(FileoutputUtil.ScriptEx_Log, ex);
         * return false; }
         */
    }

    public byte isSquadLeader(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else if (squad.getLeader() != null && squad.getLeader().getId() == c.getPlayer().getId()) {
            return 1;
        } else {
            return 0;
        }
    }

    public boolean reAdd(String eim, String squad) {
        EventInstanceManager eimz = getDisconnected(eim);
        MapleSquad squadz = getSquad(squad);
        if (eimz != null && squadz != null) {
            squadz.reAddMember(getPlayer());
            eimz.registerPlayer(getPlayer());
            return true;
        }
        return false;
    }

    public void banMember(String type, int pos) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.banMember(pos);
        }
    }

    public void acceptMember(String type, int pos) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            squad.acceptMember(pos);
        }
    }

    public String getReadableMillis(long startMillis, long endMillis) {
        return StringUtil.getReadableMillis(startMillis, endMillis);
    }

    public int addMember(String type, boolean join) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad != null) {
            return squad.addMember(c.getPlayer(), join);
        }
        return -1;
    }

    public byte isSquadMember(String type) {
        final MapleSquad squad = c.getChannelServer().getMapleSquad(type);
        if (squad == null) {
            return -1;
        } else if (squad.getMembers().contains(c.getPlayer())) {
            return 1;
        } else if (squad.isBanned(c.getPlayer())) {
            return 2;
        } else {
            return 0;
        }
    }

    public void resetReactors() {
        getPlayer().getMap().resetReactors();
    }

    public void genericGuildMessage(int code) {
        c.getSession().write(MaplePacketCreator.genericGuildMessage((byte) code));
    }

    public void disbandGuild() {
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0 || c.getPlayer().getGuildRank() != 1) {
            return;
        }
        World.Guild.disbandGuild(gid);
    }

    public void increaseGuildCapacity() {
        if (c.getPlayer().getMeso() < 2500000) {
            c.getSession().write(MaplePacketCreator.serverNotice(1, "You do not have enough mesos."));
            return;
        }
        final int gid = c.getPlayer().getGuildId();
        if (gid <= 0) {
            return;
        }
        World.Guild.increaseGuildCapacity(gid);
        c.getPlayer().gainMeso(-2500000, true, false, true);
    }

    public void displayGuildRanks() {
        c.getSession().write(MaplePacketCreator.showGuildRanks(npc, MapleGuildRanking.getInstance().getGuildRank()));
    }

    public boolean removePlayerFromInstance() {
        if (c.getPlayer().getEventInstance() != null) {
            c.getPlayer().getEventInstance().removePlayer(c.getPlayer());
            return true;
        }
        return false;
    }

    public boolean isPlayerInstance() {
        if (c.getPlayer().getEventInstance() != null) {
            return true;
        }
        return false;
    }

    public void changeStat(byte slot, int type, short amount) {
        Equip sel = (Equip) c.getPlayer().getInventory(MapleInventoryType.EQUIPPED).getItem(slot);
        switch (type) {
            case 0:
                sel.setStr(amount);
                break;
            case 1:
                sel.setDex(amount);
                break;
            case 2:
                sel.setInt(amount);
                break;
            case 3:
                sel.setLuk(amount);
                break;
            case 4:
                sel.setHp(amount);
                break;
            case 5:
                sel.setMp(amount);
                break;
            case 6:
                sel.setWatk(amount);
                break;
            case 7:
                sel.setMatk(amount);
                break;
            case 8:
                sel.setWdef(amount);
                break;
            case 9:
                sel.setMdef(amount);
                break;
            case 10:
                sel.setAcc(amount);
                break;
            case 11:
                sel.setAvoid(amount);
                break;
            case 12:
                sel.setHands(amount);
                break;
            case 13:
                sel.setSpeed(amount);
                break;
            case 14:
                sel.setJump(amount);
                break;
            case 15:
                sel.setUpgradeSlots((byte) amount);
                break;
            case 16:
                sel.setViciousHammer((byte) amount);
                break;
            case 17:
                sel.setLevel((byte) amount);
                break;
            case 18:
                sel.setEnhance((byte) amount);
                break;
            case 19:
                sel.setPotential1(amount);
                break;
            case 20:
                sel.setPotential2(amount);
                break;
            case 21:
                sel.setPotential3(amount);
                break;
            case 22:
                sel.setOwner(getText());
                break;
            default:
                break;
        }
        c.getPlayer().equipChanged();
    }

    public void killAllMonsters() {
        MapleMap map = c.getPlayer().getMap();
        double range = Double.POSITIVE_INFINITY;
        MapleMonster mob;
        for (MapleMapObject monstermo : map.getMapObjectsInRange(c.getPlayer().getPosition(), range, Arrays.asList(MapleMapObjectType.MONSTER))) {
            mob = (MapleMonster) monstermo;
            if (mob.getStats().isBoss()) {
                map.killMonster(mob, c.getPlayer(), false, false, (byte) 1);
            }
        }
        /*
         * int mapid = c.getPlayer().getMapId(); MapleMap map =
         * c.getChannelServer().getMapFactory().getMap(mapid);
         * map.killAllMonsters(true); // No drop.
         */
    }

    public void giveMerchantMesos() {
        long mesos = 0;
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM hiredmerchants WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
            } else {
                mesos = rs.getLong("mesos");
            }
            rs.close();
            ps.close();

            ps = (PreparedStatement) con.prepareStatement("UPDATE hiredmerchants SET mesos = 0 WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException ex) {
            System.err.println("Error gaining mesos in hired merchant" + ex);
        }
        c.getPlayer().gainMeso((int) mesos, true);
    }

    public void dc() {
        MapleCharacter victim = c.getChannelServer().getPlayerStorage().getCharacterByName(c.getPlayer().getName().toString());
        victim.getClient().getSession().close();
        victim.getClient().disconnect(true, false);

    }

    public long getMerchantMesos() {
        long mesos = 0;
        try {
            Connection con = (Connection) DatabaseConnection.getConnection();
            PreparedStatement ps = (PreparedStatement) con.prepareStatement("SELECT * FROM hiredmerchants WHERE merchantid = ?");
            ps.setInt(1, getPlayer().getId());
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                rs.close();
                ps.close();
            } else {
                mesos = rs.getLong("mesos");
            }
            rs.close();
            ps.close();
        } catch (SQLException ex) {
            System.err.println("Error gaining mesos in hired merchant" + ex);
        }
        return mesos;
    }

    public void openDuey() {
        c.getPlayer().setConversation(2);
        c.getSession().write(MaplePacketCreator.sendDuey((byte) 9, null));
    }

    public void openMerchantItemStore() {
        c.getPlayer().setConversation(3);
        c.getSession().write(PlayerShopPacket.merchItemStore((byte) 0x22));
    }

    public void openMerchantItemStore1() {
        final MerchItemPackage pack = loadItemFrom_Database(c.getPlayer().getId(), c.getPlayer().getAccountID());
        //c.getPlayer().setConversation(3);
        c.getSession().write(PlayerShopPacket.merchItemStore_ItemData(pack));
    }

    private static final MerchItemPackage loadItemFrom_Database(final int charid, final int accountid) {
        final Connection con = DatabaseConnection.getConnection();

        try {
            PreparedStatement ps = con.prepareStatement("SELECT * from hiredmerch where characterid = ? OR accountid = ?");
            ps.setInt(1, charid);
            ps.setInt(2, accountid);

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                ps.close();
                rs.close();
                return null;
            }
            final int packageid = rs.getInt("PackageId");

            final MerchItemPackage pack = new MerchItemPackage();
            pack.setPackageid(packageid);
            pack.setMesos(rs.getInt("Mesos"));
            pack.setSentTime(rs.getLong("time"));

            ps.close();
            rs.close();

            Map<Integer, Pair<IItem, MapleInventoryType>> items = ItemLoader.HIRED_MERCHANT.loadItems(false, charid);
            if (items != null) {
                List<IItem> iters = new ArrayList<IItem>();
                for (Pair<IItem, MapleInventoryType> z : items.values()) {
                    iters.add(z.left);
                }
                pack.setItems(iters);
            }

            return pack;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void sendRepairWindow() {
        c.getSession().write(MaplePacketCreator.sendRepairWindow(npc));
    }

    public final int getDojoPoints() {
        return c.getPlayer().getDojo();
    }

    public final int getDojoRecord() {
        return c.getPlayer().getDojoRecord();
    }

    public void setDojoRecord(final boolean reset) {
        c.getPlayer().setDojoRecord(reset);
    }

    public boolean start_DojoAgent(final boolean dojo, final boolean party) {
        if (dojo) {
            return Event_DojoAgent.warpStartDojo(c.getPlayer(), party);
        }
        return Event_DojoAgent.warpStartAgent(c.getPlayer(), party);
    }

    public boolean start_PyramidSubway(final int pyramid) {
        if (pyramid >= 0) {
            return Event_PyramidSubway.warpStartPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpStartSubway(c.getPlayer());
    }

    public boolean bonus_PyramidSubway(final int pyramid) {
        if (pyramid >= 0) {
            return Event_PyramidSubway.warpBonusPyramid(c.getPlayer(), pyramid);
        }
        return Event_PyramidSubway.warpBonusSubway(c.getPlayer());
    }

    public final short getKegs() {
        return AramiaFireWorks.getInstance().getKegsPercentage();
    }

    public void giveKegs(final int kegs) {
        AramiaFireWorks.getInstance().giveKegs(c.getPlayer(), kegs);
    }

    public final short getSunshines() {
        return AramiaFireWorks.getInstance().getSunsPercentage();
    }

    public void addSunshines(final int kegs) {
        AramiaFireWorks.getInstance().giveSuns(c.getPlayer(), kegs);
    }

    public final short getDecorations() {
        return AramiaFireWorks.getInstance().getDecsPercentage();
    }

    public void addDecorations(final int kegs) {
        try {
            AramiaFireWorks.getInstance().giveDecs(c.getPlayer(), kegs);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public final MapleInventory getInventory(int type) {
        return c.getPlayer().getInventory(MapleInventoryType.getByType((byte) type));
    }

    public final MapleCarnivalParty getCarnivalParty() {
        return c.getPlayer().getCarnivalParty();
    }

    public final MapleCarnivalChallenge getNextCarnivalRequest() {
        return c.getPlayer().getNextCarnivalRequest();
    }

    public final MapleCarnivalChallenge getCarnivalChallenge(MapleCharacter chr) {
        return new MapleCarnivalChallenge(chr);
    }

    public void setHP(short hp) {
        c.getPlayer().getStat().setHp(hp);
    }

    public void maxStats() {
        List<Pair<MapleStat, Integer>> statup = new ArrayList<Pair<MapleStat, Integer>>(2);
        c.getPlayer().getStat().setStr((short) 32767);
        c.getPlayer().getStat().setDex((short) 32767);
        c.getPlayer().getStat().setInt((short) 32767);
        c.getPlayer().getStat().setLuk((short) 32767);

        c.getPlayer().getStat().setMaxHp((short) 30000);
        c.getPlayer().getStat().setMaxMp((short) 30000);
        c.getPlayer().getStat().setHp((short) 30000);
        c.getPlayer().getStat().setMp((short) 30000);

        statup.add(new Pair<MapleStat, Integer>(MapleStat.STR, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.DEX, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.LUK, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.INT, Integer.valueOf(32767)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.HP, Integer.valueOf(30000)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXHP, Integer.valueOf(30000)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MP, Integer.valueOf(30000)));
        statup.add(new Pair<MapleStat, Integer>(MapleStat.MAXMP, Integer.valueOf(30000)));

        c.getSession().write(MaplePacketCreator.updatePlayerStats(statup, c.getPlayer().getJob()));
    }

    public Pair<String, Map<Integer, String>> getSpeedRun(String typ) {
        final SpeedRunType type = SpeedRunType.valueOf(typ);
        if (SpeedRunner.getInstance().getSpeedRunData(type) != null) {
            return SpeedRunner.getInstance().getSpeedRunData(type);
        }
        return new Pair<String, Map<Integer, String>>("", new HashMap<Integer, String>());
    }

    public boolean getSR(Pair<String, Map<Integer, String>> ma, int sel) {
        if (ma.getRight().get(sel) == null || ma.getRight().get(sel).length() <= 0) {
            dispose();
            return false;
        }
        sendOk(ma.getRight().get(sel));
        return true;
    }

    public Equip getEquip(int itemid) {
        return (Equip) MapleItemInformationProvider.getInstance().getEquipById(itemid);
    }

    public void setExpiration(Object statsSel, long expire) {
        if (statsSel instanceof Equip) {
            ((Equip) statsSel).setExpiration(System.currentTimeMillis() + (expire * 24 * 60 * 60 * 1000));
        }
    }

    public void setLock(Object statsSel) {
        if (statsSel instanceof Equip) {
            Equip eq = (Equip) statsSel;
            if (eq.getExpiration() == -1) {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
            } else {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADEABLE.getValue()));
            }
        }
    }

    public boolean addFromDrop(Object statsSel) {
        if (statsSel instanceof IItem) {
            final IItem it = (IItem) statsSel;
            return MapleInventoryManipulator.checkSpace(getClient(), it.getItemId(), it.getQuantity(), it.getOwner()) && MapleInventoryManipulator.addFromDrop(getClient(), it, false);
        }
        return false;
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type) {
        return replaceItem(slot, invType, statsSel, offset, type, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int offset, String type, boolean takeSlot) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        IItem item = getPlayer().getInventory(inv).getItem((byte) slot);
        if (item == null || statsSel instanceof IItem) {
            item = (IItem) statsSel;
        }
        if (offset > 0) {
            if (inv != MapleInventoryType.EQUIP) {
                return false;
            }
            Equip eq = (Equip) item;
            if (takeSlot) {
                if (eq.getUpgradeSlots() < 1) {
                    return false;
                } else {
                    eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() - 1));
                }
            }
            if (type.equalsIgnoreCase("Slots")) {
                eq.setUpgradeSlots((byte) (eq.getUpgradeSlots() + offset));
            } else if (type.equalsIgnoreCase("Level")) {
                eq.setLevel((byte) (eq.getLevel() + offset));
            } else if (type.equalsIgnoreCase("Hammer")) {
                eq.setViciousHammer((byte) (eq.getViciousHammer() + offset));
            } else if (type.equalsIgnoreCase("STR")) {
                eq.setStr((short) (eq.getStr() + offset));
            } else if (type.equalsIgnoreCase("DEX")) {
                eq.setDex((short) (eq.getDex() + offset));
            } else if (type.equalsIgnoreCase("INT")) {
                eq.setInt((short) (eq.getInt() + offset));
            } else if (type.equalsIgnoreCase("LUK")) {
                eq.setLuk((short) (eq.getLuk() + offset));
            } else if (type.equalsIgnoreCase("HP")) {
                eq.setHp((short) (eq.getHp() + offset));
            } else if (type.equalsIgnoreCase("MP")) {
                eq.setMp((short) (eq.getMp() + offset));
            } else if (type.equalsIgnoreCase("WATK")) {
                eq.setWatk((short) (eq.getWatk() + offset));
            } else if (type.equalsIgnoreCase("MATK")) {
                eq.setMatk((short) (eq.getMatk() + offset));
            } else if (type.equalsIgnoreCase("WDEF")) {
                eq.setWdef((short) (eq.getWdef() + offset));
            } else if (type.equalsIgnoreCase("MDEF")) {
                eq.setMdef((short) (eq.getMdef() + offset));
            } else if (type.equalsIgnoreCase("ACC")) {
                eq.setAcc((short) (eq.getAcc() + offset));
            } else if (type.equalsIgnoreCase("Avoid")) {
                eq.setAvoid((short) (eq.getAvoid() + offset));
            } else if (type.equalsIgnoreCase("Hands")) {
                eq.setHands((short) (eq.getHands() + offset));
            } else if (type.equalsIgnoreCase("Speed")) {
                eq.setSpeed((short) (eq.getSpeed() + offset));
            } else if (type.equalsIgnoreCase("Jump")) {
                eq.setJump((short) (eq.getJump() + offset));
            } else if (type.equalsIgnoreCase("ItemEXP")) {
                eq.setItemEXP(eq.getItemEXP() + offset);
            } else if (type.equalsIgnoreCase("Expiration")) {
                eq.setExpiration((long) (eq.getExpiration() + offset));
            } else if (type.equalsIgnoreCase("Flag")) {
                eq.setFlag((byte) (eq.getFlag() + offset));
            }
            if (eq.getExpiration() == -1) {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.LOCK.getValue()));
            } else {
                eq.setFlag((byte) (eq.getFlag() | ItemFlag.UNTRADEABLE.getValue()));
            }
            item = eq.copy();
        }
        MapleInventoryManipulator.removeFromSlot(getClient(), inv, (short) slot, item.getQuantity(), false);
        return MapleInventoryManipulator.addFromDrop(getClient(), item, false);
    }

    public boolean replaceItem(int slot, int invType, Object statsSel, int upgradeSlots) {
        return replaceItem(slot, invType, statsSel, upgradeSlots, "Slots");
    }

    public boolean isCash(final int itemId) {
        return MapleItemInformationProvider.getInstance().isCash(itemId);
    }

    public void buffGuild(final int buff, final int duration, final String msg) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        if (ii.getItemEffect(buff) != null && getPlayer().getGuildId() > 0) {
            final MapleStatEffect mse = ii.getItemEffect(buff);
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleCharacter chr : cserv.getPlayerStorage().getAllCharacters()) {
                    if (chr.getGuildId() == getPlayer().getGuildId()) {
                        mse.applyTo(chr, chr, true, null, duration);
                        chr.dropMessage(5, "Your guild has gotten a " + msg + " buff.");
                    }
                }
            }
        }
    }

    public boolean createAlliance(String alliancename) {
        MapleParty pt = c.getPlayer().getParty();
        MapleCharacter otherChar = c.getChannelServer().getPlayerStorage().getCharacterById(pt.getMemberByIndex(1).getId());
        if (otherChar == null || otherChar.getId() == c.getPlayer().getId()) {
            return false;
        }
        try {
            return World.Alliance.createAlliance(alliancename, c.getPlayer().getId(), otherChar.getId(), c.getPlayer().getGuildId(), otherChar.getGuildId());
        } catch (Exception re) {
            re.printStackTrace();
            return false;
        }
    }

    public boolean addCapacityToAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.changeAllianceCapacity(gs.getAllianceId())) {
                    gainMeso(-MapleGuildAlliance.CHANGE_CAPACITY_COST);
                    return true;
                }
            }
        } catch (Exception re) {
            re.printStackTrace();
        }
        return false;
    }

    public boolean disbandAlliance() {
        try {
            final MapleGuild gs = World.Guild.getGuild(c.getPlayer().getGuildId());
            if (gs != null && c.getPlayer().getGuildRank() == 1 && c.getPlayer().getAllianceRank() == 1) {
                if (World.Alliance.getAllianceLeader(gs.getAllianceId()) == c.getPlayer().getId() && World.Alliance.disbandAlliance(gs.getAllianceId())) {
                    return true;
                }
            }
        } catch (Exception re) {
            re.printStackTrace();
        }
        return false;
    }

    public byte getLastMsg() {
        return lastMsg;
    }

    public final void setLastMsg(final byte last) {
        this.lastMsg = last;
    }

    public final void maxAllSkills() {
        for (ISkill skil : SkillFactory.getAllSkills()) {
            if (GameConstants.isApplicableSkill(skil.getId())) { //no db/additionals/resistance skills
                teachSkill(skil.getId(), skil.getMaxLevel(), skil.getMaxLevel());
            }
        }
    }

    public final void resetStats(int str, int dex, int z, int luk) {
        c.getPlayer().resetStats(str, dex, z, luk);
    }

    public final boolean dropItem(int slot, int invType, int quantity) {
        MapleInventoryType inv = MapleInventoryType.getByType((byte) invType);
        if (inv == null) {
            return false;
        }
        return MapleInventoryManipulator.drop(c, inv, (short) slot, (short) quantity, true);
    }

    public final List<Integer> getAllPotentialInfo() {
        return new ArrayList<Integer>(MapleItemInformationProvider.getInstance().getAllPotentialInfo().keySet());
    }

    public final String getPotentialInfo(final int id) {
        final List<StructPotentialItem> potInfo = MapleItemInformationProvider.getInstance().getPotentialInfo(id);
        final StringBuilder builder = new StringBuilder("#b#ePOTENTIAL INFO FOR ID: ");
        builder.append(id);
        builder.append("#n#k\r\n\r\n");
        int minLevel = 1, maxLevel = 10;
        for (StructPotentialItem item : potInfo) {
            builder.append("#eLevels ");
            builder.append(minLevel);
            builder.append("~");
            builder.append(maxLevel);
            builder.append(": #n");
            builder.append(item.toString());
            minLevel += 10;
            maxLevel += 10;
            builder.append("\r\n");
        }
        return builder.toString();
    }

    public final void sendRPS() {
        c.getSession().write(MaplePacketCreator.getRPSMode((byte) 8, -1, -1, -1));
    }

    public final void setQuestRecord(Object ch, final int questid, final String data) {
        ((MapleCharacter) ch).getQuestNAdd(MapleQuest.getInstance(questid)).setCustomData(data);
    }

    public final void doWeddingEffect(final Object ch) {
        final MapleCharacter chr = (MapleCharacter) ch;
        // getMap().broadcastMessage(MaplePacketCreator.yellowChat(getPlayer().getName() + ", do you take " + chr.getName() + " as your wife and promise to stay beside her through all downtimes, crashes, and lags?"));
        CloneTimer.getInstance().schedule(new Runnable() {

            public void run() {
                if (chr == null || getPlayer() == null) {
                    warpMap(680000500, 0);
                } else {
                    // getMap().broadcastMessage(MaplePacketCreator.yellowChat(chr.getName() + ", do you take " + getPlayer().getName() + " as your husband and promise to stay beside him through all downtimes, crashes, and lags?"));
                }
            }
        }, 10000);
        CloneTimer.getInstance().schedule(new Runnable() {

            public void run() {
                if (chr == null || getPlayer() == null) {
                    if (getPlayer() != null) {
                        setQuestRecord(getPlayer(), 160001, "3");
                        setQuestRecord(getPlayer(), 160002, "0");
                    } else if (chr != null) {
                        setQuestRecord(chr, 160001, "3");
                        setQuestRecord(chr, 160002, "0");
                    }
                    warpMap(680000500, 0);
                } else {
                    setQuestRecord(getPlayer(), 160001, "2");
                    setQuestRecord(chr, 160001, "2");
                    sendNPCText(getPlayer().getName() + " and " + chr.getName() + ", I wish you two all the best on your AsteriaSEA journey together!", 9201002);
                    getMap().startExtendedMapEffect("You may now kiss the bride, " + getPlayer().getName() + "!", 5120006);
                    if (chr.getGuildId() > 0) {
                        World.Guild.guildPacket(chr.getGuildId(), MaplePacketCreator.sendMarriage(false, chr.getName()));
                    }
                    if (chr.getFamilyId() > 0) {
                        World.Family.familyPacket(chr.getFamilyId(), MaplePacketCreator.sendMarriage(true, chr.getName()), chr.getId());
                    }
                    if (getPlayer().getGuildId() > 0) {
                        World.Guild.guildPacket(getPlayer().getGuildId(), MaplePacketCreator.sendMarriage(false, getPlayer().getName()));
                    }
                    if (getPlayer().getFamilyId() > 0) {
                        World.Family.familyPacket(getPlayer().getFamilyId(), MaplePacketCreator.sendMarriage(true, chr.getName()), getPlayer().getId());
                    }
                }
            }
        }, 20000); //10 sec 10 sec
    }

    public void openDD(int type) {
        c.getSession().write(MaplePacketCreator.openBeans(getPlayer().getBeans(), type));
    }

    public void worldMessage(String text) {
        World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice(6, text).getBytes());
    }

    public int getBeans() {
        return getClient().getPlayer().getBeans();
    }

    public void gainBeans(int s) {
        getPlayer().gainBeans(s);
        c.getSession().write(MaplePacketCreator.updateBeans(c.getPlayer().getId(), s));
    }

    public int getHyPay(int type) {
        return getPlayer().getHyPay(type);
    }

    public void szhs(String ss) {
        c.getSession().write(MaplePacketCreator.游戏屏幕中间黄色字体(ss));
    }

    public void szhs(String ss, int id) {
        c.getSession().write(MaplePacketCreator.游戏屏幕中间黄色字体(ss, id));
    }

    public int gainHyPay(int hypay) {
        return getPlayer().gainHyPay(hypay);
    }

    public int addHyPay(int hypay) {
        return getPlayer().addHyPay(hypay);
    }

    public int delPayReward(int pay) {
        return getPlayer().delPayReward(pay);
    }

    public int getItemLevel(int id) {
        MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
        return ii.getReqLevel(id);
    }

    public void alatPQ() {
        //   c.getSession().write(MaplePacketCreator.updateAriantPQRanking(getText, npc, pendingDisposal))
    }

    public void xlkc(long days) {
        MapleQuestStatus marr = getPlayer().getQuestNoAdd(MapleQuest.getInstance(122700));
        if ((marr != null) && (marr.getCustomData() != null) && (Long.parseLong(marr.getCustomData()) >= System.currentTimeMillis())) {
            getPlayer().dropMessage(1, "项链扩充失败，您已经进行过项链扩充。");
        } else {
            String customData = String.valueOf(System.currentTimeMillis() + days * 24L * 60L * 60L * 1000L);
            getPlayer().getQuestNAdd(MapleQuest.getInstance(122700)).setCustomData(customData);
            getPlayer().dropMessage(1, "项链" + days + "扩充扩充成功！");
        }
    }

    /**
     * 查询怪物掉落
     *
     * @param mobId
     * @return
     */
    public String checkDrop(int mobId) {
        int rate = getClient().getChannelServer().getDropRate();
        MapleMonster mob = MapleLifeFactory.getMonster(mobId);
        if (MapleLifeFactory.getMonster(mobId) != null) {
            if (mob.getStats().isBoss()) {
                rate = getClient().getChannelServer().getBossDropRate();
            }
        }
        List ranks = MapleMonsterInformationProvider.getInstance().retrieveDrop(mobId);
        if ((ranks != null) && (ranks.size() > 0)) {
            int num = 0;
            int itemId = 0;
            int ch = 0;

            MapleItemInformationProvider ii = MapleItemInformationProvider.getInstance();
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < ranks.size(); i++) {
                MonsterDropEntry de = (MonsterDropEntry) ranks.get(i);
                if ((de.chance > 0) && ((de.questid <= 0) || ((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0)))) {
                    itemId = de.itemId;
                    if (!ii.itemExists(itemId)) {
                        continue;
                    }
                    if (num == 0) {
                        name.append("当前怪物 #o").append(mobId).append("# 的爆率为:\r\n");
                        name.append("--------------------------------------\r\n");
                    }
                    String namez = new StringBuilder().append("#z").append(itemId).append("#").toString();
                    if (itemId == 0) {
                        itemId = 4031041;
                        namez = new StringBuilder().append(de.Minimum * getClient().getChannelServer().getMesoRate()).append(" - ").append(de.Maximum * getClient().getChannelServer().getMesoRate()).append(" 的金币").toString();
                    }
                    ch = de.chance * rate;
                    //  if (getPlayer().isAdmin()) {
                    name.append(num + 1).append(") #v").append(itemId).append("#").append(namez).append(" - ").append(Integer.valueOf(ch >= 999999 ? 1000000 : ch).doubleValue() / 10000.0D).append("%的爆率. ").append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    // } else {
                    //     name.append(num + 1).append(") #v").append(itemId).append("#").append(namez).append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    // }
                    num++;
                }
            }
            if (name.length() > 0) {
                return name.toString();
            }
        }
        return "没有找到这个怪物的爆率数据。";
    }

    public String checkMapDrop() {
        List ranks = new ArrayList(MapleMonsterInformationProvider.getInstance().getGlobalDrop());
        int mapid = this.c.getPlayer().getMap().getId();
        int cashServerRate = getClient().getChannelServer().getCashRate();
        int globalServerRate = 1;
        if ((ranks != null) && (ranks.size() > 0)) {
            int num = 0;

            StringBuilder name = new StringBuilder();
            for (int i = 0; i < ranks.size(); i++) {
                MonsterGlobalDropEntry de = (MonsterGlobalDropEntry) ranks.get(i);
                if ((de.continent < 0) || ((de.continent < 10) && (mapid / 100000000 == de.continent)) || ((de.continent < 100) && (mapid / 10000000 == de.continent)) || ((de.continent < 1000) && (mapid / 1000000 == de.continent))) {
                    int itemId = de.itemId;
                    if (num == 0) {
                        name.append("当前地图 #r").append(mapid).append("#k - #m").append(mapid).append("# 的全局爆率为:");
                        name.append("\r\n--------------------------------------\r\n");
                    }
                    String names = new StringBuilder().append("#z").append(itemId).append("#").toString();
                    if ((itemId == 0) && (cashServerRate != 0)) {
                        itemId = 4031041;
                        names = new StringBuilder().append(de.Minimum * cashServerRate).append(" - ").append(de.Maximum * cashServerRate).append(" 的抵用卷").toString();
                    }
                    int chance = de.chance * globalServerRate;
                    if (getPlayer().isAdmin()) {
                        name.append(num + 1).append(") #v").append(itemId).append("#").append(names).append(" - ").append(Integer.valueOf(chance >= 999999 ? 1000000 : chance).doubleValue() / 10000.0D).append("%的爆率. ").append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    } else {
                        name.append(num + 1).append(") #v").append(itemId).append("#").append(names).append((de.questid > 0) && (MapleQuest.getInstance(de.questid).getName().length() > 0) ? new StringBuilder().append("需要接受任务: ").append(MapleQuest.getInstance(de.questid).getName()).toString() : "").append("\r\n");
                    }
                    num++;
                }
            }
            if (name.length() > 0) {
                return name.toString();
            }
        }
        return "当前地图没有设置全局爆率。";
    }

    public int getzb() {
        int money = 0;
        try {
            int cid = getPlayer().getAccountID();
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement limitCheck = con.prepareStatement("SELECT * FROM accounts WHERE id=" + cid + "");
            ResultSet rs = limitCheck.executeQuery();
            if (rs.next()) {
                money = rs.getInt("money");
            }
            limitCheck.close();
            rs.close();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return money;
    }

    public void setzb(int slot) {
        try {
            int cid = getPlayer().getAccountID();
            Connection con = DatabaseConnection.getConnection();
            try (PreparedStatement ps = con.prepareStatement("UPDATE accounts SET money =money+ " + slot + " WHERE id = " + cid + "")) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public int getmoneyb() {
        int moneyb = 0;
        try {
            int cid = getPlayer().getAccountID();
            Connection con = DatabaseConnection.getConnection();
            ResultSet rs;
            try (PreparedStatement limitCheck = con.prepareStatement("SELECT * FROM accounts WHERE id=" + cid + "")) {
                rs = limitCheck.executeQuery();
                if (rs.next()) {
                    moneyb = rs.getInt("moneyb");
                }
            }
            rs.close();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
        return moneyb;
    }

    public void setmoneyb(int slot) {
        try {
            int cid = getPlayer().getAccountID();
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE accounts SET moneyb =moneyb+ " + slot + " WHERE id = " + cid + "");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            ex.getStackTrace();
        }
    }

    public MapleMapFactory getMapFactory() {
        return getClient().getChannelServer().getMapFactory();
    }

    public void warpBack(int mid, final int retmap, final int time) {

        MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(mid);
        c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
        c.sendPacket(MaplePacketCreator.getClock(time));
        Timer.EventTimer.getInstance().schedule(new Runnable() {

            @Override
            public void run() {
                MapleMap warpMap = c.getChannelServer().getMapFactory().getMap(retmap);
                if (c.getPlayer() != null) {
                    c.sendPacket(MaplePacketCreator.stopClock());
                    c.getPlayer().changeMap(warpMap, warpMap.getPortal(0));
                    c.getPlayer().dropMessage(6, "到达目的地ヘ!");
                }
            }
        }, 1000 * time);
    }

    public void warpMapWithClock(final int mid, int seconds) {
        c.getPlayer().getMap().broadcastMessage(MaplePacketCreator.getClock(seconds));
        Timer.MapTimer.getInstance().schedule(new Runnable() {

            public void run() {
                if (c.getPlayer() != null) {
                    for (MapleCharacter chr : c.getPlayer().getMap().getCharactersThreadsafe()) {
                        chr.changeMap(mid);
                    }
                }
            }
        }, seconds * 1000);
    }

    public void showlvl() {
        c.sendPacket(MaplePacketCreator.showlevelRanks(npc, MapleGuildRanking.getInstance().getLevelRank()));
    }

    public void showmeso() {
        c.sendPacket(MaplePacketCreator.showmesoRanks(npc, MapleGuildRanking.getInstance().getMesoRank()));
    }

    public void ShowMarrageEffect() {
        c.getPlayer().getMap().broadcastMessage((MaplePacketCreator.sendMarrageEffect()));
    }

    public void 组队征集喇叭(int lx, String msg) {
        switch (lx) {
            case 1:
                World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(11, this.c.getChannel(), "组队征集令] : " + msg).getBytes());
                break;
            case 2:
                World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(12, this.c.getChannel(), "组队征集令]: " + msg).getBytes());
                break;
            case 3:
                World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(3, this.c.getChannel(), "组队征集令]: " + msg).getBytes());
        }
    }

    public void 喇叭(int lx, String msg) {
        switch (lx) {
            case 1:
                World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(11, this.c.getChannel(), "全服公告] : " + msg).getBytes());
                break;
            case 2:
                World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(12, this.c.getChannel(), "全服公告]: " + msg).getBytes());
                break;
            case 3:
                World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(3, this.c.getChannel(), "全服公告]: " + msg).getBytes());
        }
    }

    public void setgrname(int name) {
        getPlayer().setgrname(name);
    }

    public void setjzname(int name) {
        getPlayer().setjzname(name);
    }

    public int getgrname() {
        return getPlayer().getgrname();
    }

    public int getjzname() {
        return getPlayer().getjzname();
    }

    public int getdjjl() {//等级奖励
        return getPlayer().getdjjl();
    }

    public void setdjjl(int name) {//等级奖励
        getPlayer().setdjjl(name);
    }

    public int getqiandao() {//每日签到
        return getPlayer().getqiandao();
    }

    public void setqiandao(int set) {//每日签到
        getPlayer().setqiandao(set);
    }

    public String ms() {
        String result = "冒险岛";
        return result;
    }

    public void 即时存档() {
        c.getPlayer().saveToDB(true, true);
    }

    public void 刷新状态() {
        c.getPlayer().getClient().getSession().write(MaplePacketCreator.getCharInfo(c.getPlayer()));
        c.getPlayer().getMap().removePlayer(c.getPlayer());
        c.getPlayer().getMap().addPlayer(c.getPlayer());
        c.getSession().write(MaplePacketCreator.enableActions());
    }

    public void 刷新地图() {
        boolean custMap = true;
        int mapid = c.getPlayer().getMapId();
        MapleMap map = custMap ? c.getPlayer().getClient().getChannelServer().getMapFactory().getMap(mapid) : c.getPlayer().getMap();
        if (c.getPlayer().getClient().getChannelServer().getMapFactory().destroyMap(mapid)) {
            MapleMap newMap = c.getPlayer().getClient().getChannelServer().getMapFactory().getMap(mapid);
            MaplePortal newPor = newMap.getPortal(0);
            LinkedHashSet<MapleCharacter> mcs = new LinkedHashSet<>(map.getCharacters()); //菱形推断
            outerLoop:
            for (MapleCharacter m : mcs) {
                for (int x = 0; x < 5; x++) {
                    try {
                        m.changeMap(newMap, newPor);
                        continue outerLoop;
                    } catch (Throwable t) {
                    }
                }
            }
        }
    }

    public void getlfj(int xx, int a) {

        String msg;
        int nx = 0;
        if ((a == 0) && (xx < 100)) {
            this.c.getPlayer().dropMessage(1, "点卷不足！");
            return;
        }
        if ((a == 1) && (xx < 1000)) {
            this.c.getPlayer().dropMessage(1, "点卷不足！");
            return;
        }
        if ((a == 2) && (xx < 10000)) {
            this.c.getPlayer().dropMessage(1, "点卷不足！");
            return;
        }

        if (a == 0) {
            nx = 100;
        } else if (a == 1) {
            nx = 1000;
        } else if (a == 2) {
            nx = 10000;
        }

        int s = Randomizer.nextInt(5);
        int 灰色 = Randomizer.nextInt(5);
        int 彩色 = Randomizer.nextInt(5);
        int 倍率 = Randomizer.nextInt(6);
        int meso = 0;
        if (((灰色 == 0) && (彩色 == 0)) || ((灰色 == 1) && (彩色 == 1)) || ((灰色 == 2) && (彩色 == 2)) || ((灰色 == 3) && (彩色 == 3)) || ((灰色 == 4) && (彩色 == 4))) {
            showEffect(false, "miro/frame");
            showEffect(false, new StringBuilder().append("miro/RR1/").append(灰色).toString());
            showEffect(false, new StringBuilder().append("miro/RR2/").append(彩色).toString());
            showEffect(false, new StringBuilder().append("miro/RR3/").append(倍率).toString());
            switch (倍率) {
                case 0:
                    meso = 3;
                    break;
                case 1:
                    meso = 4;
                    break;
                case 2:
                    meso = 5;
                    break;
                case 3:
                    meso = 6;
                    break;
                case 4:
                    meso = 7;
                    break;
                case 5:
                    meso = 8;
                    break;
                default:
                    break;
            }

            this.c.getPlayer().modifyCSPoints(1, nx + nx / 100 * meso);
            this.c.getPlayer().dropMessage(-1, new StringBuilder().append("成功中奖：").append(meso).append("0%倍\r\n成功中奖：").append(meso).append("0%倍\r\n成功中奖：").append(meso).append("0%倍").toString());
            this.c.getPlayer().dropMessage(5, new StringBuilder().append(nx / 100 * meso * 10).append("").toString());
            msg = new StringBuilder().append(nx).append("倍游戏机！成功中奖：").append(meso).append("0%倍").toString();

            World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(11, this.c.getChannel(), new StringBuilder().append("[游戏机喇叭]").append(this.c.getPlayer().getName()).append(" : ").append(msg).toString()).getBytes());
        } else {
            showEffect(false, "miro/frame");
            showEffect(false, new StringBuilder().append("miro/RR1/").append(灰色).toString());
            showEffect(false, new StringBuilder().append("miro/RR2/").append(彩色).toString());
            showEffect(false, new StringBuilder().append("miro/RR3/").append(倍率).toString());
            this.c.getPlayer().dropMessage(-1, "很抱歉，您没中奖！");
            this.c.getPlayer().dropMessage(5, new StringBuilder().append(-nx).append("").toString());
            this.c.getPlayer().modifyCSPoints(1, -nx);
            msg = new StringBuilder().append(nx).append("倍游戏机！未中奖！").toString();

            World.Broadcast.broadcastSmega(MaplePacketCreator.serverNotice(12, this.c.getChannel(), new StringBuilder().append("[游戏机喇叭]").append(this.c.getPlayer().getName()).append(" : ").append(msg).toString()).getBytes());
        }
    }

    public void 获取等级排行榜() {
        MapleGuild.等级排行榜(getClient(), npc);
    }

    public void 获取人气排行榜() {
        MapleGuild.人气排行榜(getClient(), npc);
    }

    public void 获取金币排行榜() {
        MapleGuild.金币排行榜(getClient(), npc);
    }

    public void 获取家族排行榜() {
        MapleGuild.家族排行榜(getClient(), npc);
    }

    public void 获取杀怪排行榜() {
        MapleGuild.杀怪排行榜(getClient(), npc);
    }

    public int getsg() {
        int sg = 0;
        try {
            int cid = getPlayer().getAccountID();
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement limitCheck = con.prepareStatement("SELECT * FROM characters WHERE id=" + cid + "");
            ResultSet rs = limitCheck.executeQuery();
            if (rs.next()) {
                sg = rs.getInt("sg");
            }
            limitCheck.close();
            rs.close();
        } catch (SQLException ex) {
        }
        return sg;
    }

    public void setsg(int slot) {
        try {
            int cid = getPlayer().getAccountID();
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement("UPDATE characters SET sg =sg+ " + slot + " WHERE id = " + cid + "");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
        }
    }
}
