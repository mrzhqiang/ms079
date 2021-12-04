package com.github.mrzhqiang.maplestory.service;

import client.MapleCharacter;
import com.github.mrzhqiang.maplestory.config.ServerProperties;
import com.github.mrzhqiang.maplestory.domain.VCharacterAggregate;
import com.github.mrzhqiang.maplestory.domain.query.QVCharacterAggregate;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import handling.channel.ChannelServer;
import handling.world.MapleParty;
import handling.world.MaplePartyCharacter;
import handling.world.PartyOperation;
import handling.world.World;
import tools.MaplePacketCreator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.atomic.AtomicInteger;

@Singleton
public final class PartyService {

    private final AtomicInteger runningPartyId = new AtomicInteger();

    private final Cache<Integer, MapleParty> cached;

    @Inject
    public PartyService(ServerProperties properties) {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder()
                .maximumSize(100000);
        if (properties.isDebug()) {
            builder.recordStats();
        }
        this.cached = builder.build();
    }

    public void init() {
        int party = new QVCharacterAggregate()
                .select(QVCharacterAggregate.alias().party)
                .findOneOrEmpty()
                .map(VCharacterAggregate::getParty)
                .map(integer -> integer + 2)
                .orElse(1);
        runningPartyId.set(party);
    }

    public void partyChat(int partyid, String chattext, String namefrom) {
        MapleParty party = getParty(partyid);
        if (party == null) {
            throw new IllegalArgumentException("no party with the specified partyid exists");
        }

        for (MaplePartyCharacter partychar : party.getMembers()) {
            int ch = World.Find.findChannel(partychar.getName());
            if (ch > 0) {
                MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(partychar.getName());
                if (chr != null && !chr.getName().equalsIgnoreCase(namefrom)) { //Extra check just in case
                    chr.getClient().getSession().write(MaplePacketCreator.multiChat(namefrom, chattext, 1));
                }
            }
        }
    }

    public void updateParty(int partyid, PartyOperation operation, MaplePartyCharacter target) {
        MapleParty party = getParty(partyid);
        if (party == null) {
            return; //Don't update, just return. And definitely don't throw a damn exception.
            //throw new IllegalArgumentException("no party with the specified partyid exists");
        }
        switch (operation) {
            case JOIN:
                party.addMember(target);
                break;
            case EXPEL:
            case LEAVE:
                party.removeMember(target);
                break;
            case DISBAND:
                disbandParty(partyid);
                break;
            case SILENT_UPDATE:
            case LOG_ONOFF:
                party.updateMember(target);
                break;
            case CHANGE_LEADER:
            case CHANGE_LEADER_DC:
                party.setLeader(target);
                break;
            default:
                throw new RuntimeException("Unhandeled updateParty operation " + operation.name());
        }
        if ((operation == PartyOperation.LEAVE) || (operation == PartyOperation.EXPEL)) {
            int ch = World.Find.findChannel(target.getName());
            if (ch > 0) {
                MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(target.getName());
                if (chr != null) {
                    chr.getClient().getSession().write(MaplePacketCreator.updateParty(chr.getClient().getChannel(), party, operation, target));
                    chr.setParty(null);
                }
            }
            if ((target.getId() == party.getLeader().getId()) && (party.getMembers().size() > 0)) {
                MaplePartyCharacter lchr = null;
                for (MaplePartyCharacter pchr : party.getMembers()) {
                    if ((pchr != null) && ((lchr == null) || (lchr.getLevel() < pchr.getLevel()))) {
                        lchr = pchr;
                    }
                }
                if (lchr != null) {
                    updateParty(party.getId(), PartyOperation.CHANGE_LEADER_DC, lchr);
                }
            }
        }
        if (party.getMembers().size() <= 0) {
            disbandParty(party.getId());
        }
        for (MaplePartyCharacter partychar : party.getMembers()) {
            int ch = World.Find.findChannel(partychar.getName());
            if (ch > 0) {
                MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(partychar.getName());
                if (chr != null) {
                    if (operation == PartyOperation.DISBAND) {
                        chr.setParty(null);
                    } else {
                        chr.setParty(party);
                    }
                    chr.getClient().getSession().write(MaplePacketCreator.updateParty(chr.getClient().getChannel(), party, operation, target));
                }
            }
        }
        switch (operation) {
            case LEAVE:
            case EXPEL:
                int ch = World.Find.findChannel(target.getName());
                if (ch > 0) {
                    MapleCharacter chr = ChannelServer.getInstance(ch).getPlayerStorage().getCharacterByName(target.getName());
                    if (chr != null) {
                        chr.getClient().getSession().write(MaplePacketCreator.updateParty(chr.getClient().getChannel(), party, operation, target));
                        chr.setParty(null);
                    }
                }
        }
    }

    public MapleParty createParty(MaplePartyCharacter chrfor) {
        int partyid = runningPartyId.getAndIncrement();
        MapleParty party = new MapleParty(partyid, chrfor);
        cached.put(party.getId(), party);
        return party;
    }

    public MapleParty getParty(Integer partyid) {
        return cached.getIfPresent(partyid);
    }

    public void disbandParty(int partyid) {
        cached.invalidate(partyid);
    }

}
