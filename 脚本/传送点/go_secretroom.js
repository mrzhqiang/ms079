/*
	This file is part of the ZeroFusion MapleStory Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@odinms.de>
                       Jan Christian Meyer <vimes@odinms.de>
    ZeroFusion organized by "RMZero213" <RMZero213@hotmail.com>

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

var baseid = 106021000;
var dungeonid = 106021001;
var dungeons = 10;

function enter(pi) {
	if (!pi.haveItem(4032405)) {
		pi.playerMessage(5, "好像已經被上鎖了...");
		return false;
	}
	if (pi.getPlayer().getMapId() == baseid) {
	    for(var i = 0; i < dungeons; i++) {
		if (pi.getMap(dungeonid + i).getCharactersSize() == 0) {
		    		pi.warp(dungeonid + i, 0);
		    return true;
		}
	    }
	    pi.playerMessage(5, "裡面已經有人在挑戰....請稍後再嘗試！！");
		return false;
	} else {
			pi.warp(baseid, "in00");
	}
	return true;
}
