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

import server.MapleStatInfo;

public interface IEquip extends IItem {

    public static enum ScrollResult {

        SUCCESS, FAIL, CURSE
    }
    public static final int ARMOR_RATIO = 350000;
    public static final int WEAPON_RATIO = 700000;

    byte getUpgradeSlots();

    byte getLevel();

    public byte getViciousHammer();

    public int getItemEXP();

    public int getExpPercentage();

    public byte getEquipLevel();

    public int getEquipLevels();
    
    public int getEquipExp();

    public int getEquipExpForLevel();

    public int getBaseLevel();

    public short getStr();

    public short getDex();

    public short getInt();

    public short getLuk();

    public short getHp();

    public short getMp();

    public short getWatk();

    public short getMatk();

    public short getWdef();

    public short getMdef();

    public short getAcc();

    public short getAvoid();

    public short getHands();

    public short getSpeed();

    public short getJump();

    public int getDurability();

    public byte getEnhance();

    public byte getState();

    public short getPotential1();

    public short getPotential2();

    public short getPotential3();

    public short getHpR();

    public short getMpR();
}
