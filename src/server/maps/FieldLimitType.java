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

public enum FieldLimitType {

    Jump(0x1),
    MovementSkills(0x2),
    SummoningBag(0x04),
    MysticDoor(0x08),
    ChannelSwitch(0x10),
    RegularExpLoss(0x20),
    VipRock(0x40),
    Minigames(0x80),
    NoClue1(0x100), // APQ and a couple quest maps have this
    Mount(0x200),
    PotionUse(0x400), //or 0x40000
    Event(0x2000),
    Pet(0x8000), //needs confirmation
    Event2(0x10000),
    DropDown(0x20000), //   NoClue7(0x40000) // Seems to .. disable Rush if 0x2 is set
    ;
    private final int i;

    private FieldLimitType(int i) {
        this.i = i;
    }

    public final int getValue() {
        return i;
    }

    public final boolean check(int fieldlimit) {
        return (fieldlimit & i) == i;
    }
}
