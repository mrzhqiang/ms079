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
package client.inventory;

public interface IItem extends Comparable<IItem> {

    byte getType();

    short getPosition();

    byte getFlag();

    boolean getLocked();

    short getQuantity();

    String getOwner();

    String getGMLog();

    int getItemId();

    MaplePet getPet();

    int getUniqueId();

    IItem copy();

    long getExpiration();

    void setFlag(byte flag);

    void setLocked(byte flag);
    
    void setUniqueId(int id);

    void setPosition(short position);

    void setExpiration(long expire);

    void setOwner(String owner);

    void setGMLog(String GameMaster_log);

    void setQuantity(short quantity);

    void setGiftFrom(String gf);

    void setEquipLevel(byte j);

    byte getEquipLevel();

    String getGiftFrom();

    MapleRing getRing();
}
