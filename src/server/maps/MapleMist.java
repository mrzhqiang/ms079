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
import java.awt.Rectangle;

import client.ISkill;
import client.MapleCharacter;
import client.MapleClient;
import client.SkillFactory;
import handling.MaplePacket;
import java.lang.ref.WeakReference;
import server.MapleStatEffect;
import server.life.MapleMonster;
import server.life.MobSkill;
import tools.MaplePacketCreator;

public class MapleMist extends AbstractMapleMapObject {

    private Rectangle mistPosition;
    private MapleStatEffect source;
    private MobSkill skill;
    private boolean isMobMist;
    private int skillDelay, skilllevel, isPoisonMist, ownerId;

    public MapleMist(Rectangle mistPosition, MapleMonster mob, MobSkill skill) {
        this.mistPosition = mistPosition;
        this.ownerId = mob.getId();
        this.skill = skill;
        this.skilllevel = skill.getSkillLevel();

        isMobMist = true;
        isPoisonMist = 0;
        skillDelay = 0;
    }

    public MapleMist(Rectangle mistPosition, MapleCharacter owner, MapleStatEffect source) {
        this.mistPosition = mistPosition;
        this.ownerId = owner.getId();
        this.source = source;
        this.skillDelay = 8;
        this.isMobMist = false;
        this.skilllevel = owner.getSkillLevel(SkillFactory.getSkill(source.getSourceId()));

        switch (source.getSourceId()) {
            case 4221006: // Smoke Screen
                isPoisonMist = 0;
                break;
            case 14111006:
            case 2111003: // FP mist
            case 12111005: // Flame wizard, [Flame Gear]
                isPoisonMist = 1;
                break;
            case 22161003: //Recovery Aura
                isPoisonMist = 2;
                break;
        }
    }

    //fake
    public MapleMist(Rectangle mistPosition, MapleCharacter owner) {
        this.mistPosition = mistPosition;
        this.ownerId = owner.getId();
        this.source = new MapleStatEffect();
        this.source.setSourceId(2111003);
        this.skilllevel = 30;
        isMobMist = false;
        isPoisonMist = 0;
        skillDelay = 8;
    }

    @Override
    public MapleMapObjectType getType() {
        return MapleMapObjectType.MIST;
    }

    @Override
    public Point getPosition() {
        return mistPosition.getLocation();
    }

    public ISkill getSourceSkill() {
        return SkillFactory.getSkill(source.getSourceId());
    }

    public boolean isMobMist() {
        return isMobMist;
    }

    public int isPoisonMist() {
        return isPoisonMist;
    }

    public int getSkillDelay() {
        return skillDelay;
    }

    public int getSkillLevel() {
        return skilllevel;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public MobSkill getMobSkill() {
        return this.skill;
    }

    public Rectangle getBox() {
        return mistPosition;
    }

    public MapleStatEffect getSource() {
        return source;
    }

    @Override
    public void setPosition(Point position) {
    }

    public MaplePacket fakeSpawnData(int level) {
        return MaplePacketCreator.spawnMist(this);
    }

    @Override
    public void sendSpawnData(final MapleClient c) {
        c.getSession().write(MaplePacketCreator.spawnMist(this));
    }

    @Override
    public void sendDestroyData(final MapleClient c) {
        c.getSession().write(MaplePacketCreator.removeMist(getObjectId(), false));
    }

    public boolean makeChanceResult() {
        return source.makeChanceResult();
    }
}
