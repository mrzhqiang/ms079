package com.github.mrzhqiang.maplestory.service;

import client.MapleCharacter;
import client.MapleCoolDownValueHolder;
import client.MapleDiseaseValueHolder;
import client.inventory.MapleInventoryType;
import client.inventory.MaplePet;
import client.inventory.PetDataFactory;
import com.google.common.base.Stopwatch;
import handling.cashshop.CashShopServer;
import handling.channel.ChannelServer;
import handling.channel.PlayerStorage;
import handling.world.CharacterTransfer;
import handling.world.CheaterData;
import handling.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.github.mrzhqiang.maplestory.timer.Timer;
import server.maps.MapleMap;
import server.maps.MapleMapItem;
import tools.CollectionUtil;
import tools.MaplePacketCreator;
import tools.packet.PetPacket;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public final class WorldService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorldService.class);

    private final ChannelService channelService;
    private final GuildService guildService;
    private final AllianceService allianceService;
    private final FamilyService familyService;
    private final MessengerService messengerService;
    private final PartyService partyService;

    @Inject
    public WorldService(ChannelService channelService, GuildService guildService,
                        AllianceService allianceService, FamilyService familyService,
                        MessengerService messengerService, PartyService partyService) {
        this.channelService = channelService;
        this.guildService = guildService;
        this.allianceService = allianceService;
        this.familyService = familyService;
        this.messengerService = messengerService;
        this.partyService = partyService;
    }

    public void init() {
        Stopwatch worldWatch = Stopwatch.createStarted();
        LOGGER.info(">>> 初始化 [世界服务器]");
        channelService.init();
        guildService.init();
        allianceService.init();
        familyService.init();
        messengerService.init();
        partyService.init();
        LOGGER.info("<<< [世界服务器] 初始化完毕，耗时：{}", worldWatch.stop());
    }

    public String getStatus() throws RemoteException {
        StringBuilder ret = new StringBuilder();
        int totalUsers = 0;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            ret.append("Channel ");
            ret.append(cs.getChannel());
            ret.append(": ");
            int channelUsers = cs.getConnectedClients();
            totalUsers += channelUsers;
            ret.append(channelUsers);
            ret.append(" users\n");
        }
        ret.append("Total users online: ");
        ret.append(totalUsers);
        ret.append("\n");
        return ret.toString();
    }

    public Map<Integer, Integer> getConnected() {
        Map<Integer, Integer> ret = new HashMap<Integer, Integer>();
        int total = 0;
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            int curConnected = cs.getConnectedClients();
            ret.put(cs.getChannel(), curConnected);
            total += curConnected;
        }
        ret.put(0, total);
        return ret;
    }

    public List<CheaterData> getCheaters() {
        List<CheaterData> allCheaters = new ArrayList<CheaterData>();
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            allCheaters.addAll(cs.getCheaters());
        }
        Collections.sort(allCheaters);
        return CollectionUtil.copyFirst(allCheaters, 10);
    }

    public boolean isConnected(String charName) {
        return World.Find.findChannel(charName) > 0;
    }

    public void toggleMegaphoneMuteState() {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            cs.toggleMegaphoneMuteState();
        }
    }

    public void ChannelChange_Data(CharacterTransfer Data, int characterid, int toChannel) {
        getStorage(toChannel).registerPendingPlayer(Data, characterid);
    }

    public boolean isCharacterListConnected(List<String> charName) {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            for (final String c : charName) {
                if (cs.getPlayerStorage().getCharacterByName(c) != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasMerchant(int accountID) {
        for (ChannelServer cs : ChannelServer.getAllInstances()) {
            if (cs.containsMerchant(accountID)) {
                return true;
            }
        }
        return false;
    }

    public PlayerStorage getStorage(int channel) {
        if (channel == -20) {
            return CashShopServer.getPlayerStorageMTS();
        } else if (channel == -10) {
            return CashShopServer.getPlayerStorage();
        }
        return ChannelServer.getInstance(channel).getPlayerStorage();
    }

    public String getAllowLoginTip(List<String> charNames) {
        StringBuilder ret = new StringBuilder("账号下其他角色在游戏: ");
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            for (String name : charNames) {
                if (cserv.isConnected(name)) {
                    ret.append(name);
                    ret.append(" ");
                }
            }
        }
        return ret.toString();
    }

    public int getPendingCharacterSize() {
        int ret = CashShopServer.getPlayerStorage().pendingCharacterSize();
        for (ChannelServer cserv : ChannelServer.getAllInstances()) {
            ret += cserv.getPlayerStorage().pendingCharacterSize();
        }
        return ret;
    }

    public void registerRespawn() {
        Timer.WORLD.register(new Respawn(this), 3000); //divisible by 9000 if possible.
        //3000 good or bad? ive no idea >_>
        //buffs can also be done, but eh

    }

    public static class Respawn implements Runnable { //is putting it here a good idea?

        private final WorldService worldService;

        private int numTimes = 0;

        public Respawn(WorldService worldService) {
            this.worldService = worldService;
        }

        @Override
        public void run() {
            numTimes++;
            for (ChannelServer cserv : ChannelServer.getAllInstances()) {
                for (MapleMap map : cserv.getMapFactory().getAllMaps()) { //iterating through each map o_x
                    worldService.handleMap(map, numTimes, map.getCharactersSize());
                }
                for (MapleMap map : cserv.getMapFactory().getAllInstanceMaps()) {
                    worldService.handleMap(map, numTimes, map.getCharactersSize());
                }
            }
        }
    }

    public void handleMap(final MapleMap map, final int numTimes, final int size) {
        if (map.getItemsSize() > 0) {
            for (MapleMapItem item : map.getAllItemsThreadsafe()) {
                if (item.shouldExpire()) {
                    item.expire(map);
                } else if (item.shouldFFA()) {
                    item.setDropType((byte) 2);
                }
            }
        }
        if (map.characterSize() > 0) {
            if (map.canSpawn()) {
                map.respawn(false);
            }
            boolean hurt = map.canHurt();
            for (MapleCharacter chr : map.getCharactersThreadsafe()) {
                handleCooldowns(chr, numTimes, hurt);
            }
        }
        if (numTimes % 10 == 0 && (map.getId() == 220080001 && map.playerCount() == 0)) {
            ChannelServer.getInstance(map.getChannel()).getMapFactory().getMap(220080000).resetReactors();
        }
    }

    public void scheduleRateDelay(final String type, long delay) {
        Timer.WORLD.schedule(new Runnable() {

            @Override
            public void run() {
                final String rate = type;

                if (rate.equals("经验")) {
                    for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                        cservs.setExpRate(1);
                    }
                } else if (rate.equals("爆率")) {
                    for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                        cservs.setDropRate(1);
                    }
                } else if (rate.equals("金币")) {
                    for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                        cservs.setMesoRate(1);
                    }
                } else if (rate.equalsIgnoreCase("boss爆率")) {
                    for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                        cservs.setBossDropRate(1);
                    }
                } else if (rate.equals("宠物经验")) {
//                    for (ChannelServer cservs : ChannelServer.getAllInstances()) {
//                        cservs.setPetExpRate(1);
//                    }
                }
                for (ChannelServer cservs : ChannelServer.getAllInstances()) {
                    cservs.broadcastPacket(MaplePacketCreator.serverNotice(6, " 系统双倍活动已经结束。系统已成功自动切换为正常游戏模式！"));
                }
            }
        }, delay * 1000);
    }

    public void handleCooldowns(final MapleCharacter chr, final int numTimes, final boolean hurt) { //is putting it here a good idea? expensive?
        final long now = System.currentTimeMillis();
        for (MapleCoolDownValueHolder m : chr.getCooldowns()) {
            if (m.startTime + m.length < now) {
                final int skil = m.skillId;
                chr.removeCooldown(skil);
                chr.getClient().getSession().write(MaplePacketCreator.skillCooldown(skil, 0));
            }
        }
        for (MapleDiseaseValueHolder m : chr.getAllDiseases()) {
            if (m.startTime + m.length < now) {
                chr.dispelDebuff(m.disease);
            }
        }
        if (numTimes % 20 == 0) { //we're parsing through the characters anyway (:
            for (MaplePet pet : chr.getPets()) {
                if (pet.getSummoned()) {
                    if (pet.getPetItemId() == 5000054 && pet.getSecondsLeft() > 0) {
                        pet.setSecondsLeft(pet.getSecondsLeft() - 1);
                        if (pet.getSecondsLeft() <= 0) {
                            chr.unequipPet(pet, true, true);
                            return;
                        }
                    }
                    int newFullness = pet.getFullness() - PetDataFactory.getHunger(pet.getPetItemId());
                    if (newFullness <= 5) {
                        pet.setFullness(15);
                        chr.unequipPet(pet, true, true);
                    } else {
                        pet.setFullness(newFullness);
                        chr.getClient().getSession().write(PetPacket.updatePet(pet, chr.getInventory(MapleInventoryType.CASH).getItem(pet.getInventoryPosition()), true));
                    }
                }
            }
        }
        if (hurt && chr.isAlive()) {
            if (chr.getInventory(MapleInventoryType.EQUIPPED).findById(chr.getMap().getHPDecProtect()) == null) {
                if (chr.getMapId() == 749040100 && chr.getInventory(MapleInventoryType.CASH).findById(5451000) == null) { //minidungeon
                    chr.addHP(-chr.getMap().getHPDec());
                } else if (chr.getMapId() != 749040100) {
                    chr.addHP(-chr.getMap().getHPDec());
                }
            }
        }
    }
}
