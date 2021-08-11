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
package server.maps;

import java.awt.Point;

import client.MapleCharacter;
import client.MapleClient;
import constants.GameConstants;
import client.anticheat.CheatingOffense;
import handling.channel.ChannelServer;
import server.MapleStatEffect;
import tools.MaplePacketCreator;

public class MapleSummon extends AbstractAnimatedMapleMapObject {

    private final int ownerid, skillLevel, ownerLevel, skill;
    private int fh;
    private MapleMap map; //required for instanceMaps
    private short hp;
    private boolean changedMap = false;
    private SummonMovementType movementType;
    // Since player can have more than 1 summon [Pirate] 
    // Let's put it here instead of cheat tracker
    private int lastSummonTickCount;
    private byte Summon_tickResetCount;
    private long Server_ClientSummonTickDiff;

    public MapleSummon(final MapleCharacter owner, final MapleStatEffect skill, final Point pos, final SummonMovementType movementType) {
        super();
        this.ownerid = owner.getId();
        this.ownerLevel = owner.getLevel();
        this.skill = skill.getSourceId();
        this.map = owner.getMap();
        this.skillLevel = skill.getLevel();
        this.movementType = movementType;
        setPosition(pos);
        try {
            this.fh = owner.getMap().getFootholds().findBelow(pos).getId();
        } catch (NullPointerException e) {
            this.fh = 0; //lol, it can be fixed by movement
        }

        if (!isPuppet()) { // Safe up 12 bytes of data, since puppet doesn't attack.
            lastSummonTickCount = 0;
            Summon_tickResetCount = 0;
            Server_ClientSummonTickDiff = 0;
        }
    }

    @Override
    public final void sendSpawnData(final MapleClient client) {
    }

    @Override
    public final void sendDestroyData(final MapleClient client) {
        client.getSession().write(MaplePacketCreator.removeSummon(this, false));
    }

    public final void updateMap(final MapleMap map) {
        this.map = map;
    }

    public final MapleCharacter getOwner() {
        return map.getCharacterById(ownerid);
    }

    public final int getFh() {
        return fh;
    }

    public final void setFh(final int fh) {
        this.fh = fh;
    }

    public final int getOwnerId() {
        return ownerid;
    }

    public final int getOwnerLevel() {
        return ownerLevel;
    }

    public final int getSkill() {
        return skill;
    }

    public final short getHP() {
        return hp;
    }

    public final void addHP(final short delta) {
        this.hp += delta;
    }

    public final SummonMovementType getMovementType() {
        return movementType;
    }

    public final boolean isPuppet() {
        switch (skill) {
            case 3111002:
            case 3211002:
            case 13111004:
            case 4341006:
            case 33111003:
                return true;
        }
        return false;
    }

    public final boolean isGaviota() {
        return skill == 5211002;
    }

    public final boolean isBeholder() {
        return skill == 1321007;
    }

    public final boolean isMultiSummon() {
        return skill == 5211002 || skill == 5211001 || skill == 5220002 || skill == 32111006;
    }

    public final boolean isSummon() {
        switch (skill) {
            case 12111004:
            case 1321007: //beholder
            case 2311006:
            case 2321003:
            case 2121005:
            case 2221005:
            case 5211001: // Pirate octopus summon
            case 5211002:
            case 5220002: // wrath of the octopi
            case 13111004:
            case 11001004:
            case 12001004:
            case 13001004:
            case 14001005:
            case 15001004:
                return true;
        }
        return false;
    }

    public final int getSkillLevel() {
        return skillLevel;
    }

    public final int getSummonType() {
        if (isPuppet()) {
            return 0;
        }
        switch (skill) {
            case 1321007:
                return 2;
            case 35111001: //satellite.
            case 35111009:
            case 35111010:
                return 3;
            case 35121009: //bots n. tots
                return 4;
            //case 4111007: //TEMP
            //	return 6; //TEMP
        }
        return 1;
    }

    @Override
    public final MapleMapObjectType getType() {
        return MapleMapObjectType.SUMMON;
    }

    public final void CheckSummonAttackFrequency(final MapleCharacter chr, final int tickcount) {
        final int tickdifference = (tickcount - lastSummonTickCount);
        if (tickdifference < GameConstants.getSummonAttackDelay(skill)) {
            chr.getCheatTracker().registerOffense(CheatingOffense.召唤兽快速攻击);
        }
        final long STime_TC = System.currentTimeMillis() - tickcount;
        final long S_C_Difference = Server_ClientSummonTickDiff - STime_TC;
        if (S_C_Difference > 200) {
            chr.getCheatTracker().registerOffense(CheatingOffense.召唤兽快速攻击);
        }
        Summon_tickResetCount++;
        if (Summon_tickResetCount > 4) {
            Summon_tickResetCount = 0;
            Server_ClientSummonTickDiff = STime_TC;
        }
        lastSummonTickCount = tickcount;
    }

    public final boolean isChangedMap() {
        return changedMap;
    }

    public final void setChangedMap(boolean cm) {
        this.changedMap = cm;
    }
}
