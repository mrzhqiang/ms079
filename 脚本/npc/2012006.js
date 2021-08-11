/*
	This file is part of the OdinMS Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc>
		       Matthias Butz <matze@odinms.de>
		       Jan Christian Meyer <vimes@odinms.de>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation version 3 as published by
    the Free Software Foundation. You may not use, modify or distribute
    this program under any other version of the GNU Affero General Public
    License.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
var status = -1;
var sel;

function start() {
    cm.sendNext("#w亲爱的 #h #, 我是 #p2012006#.\r\n请问你是否有记得购买船票?");
}

function action(mode, type, selection) {
    if (mode < 1) {
        cm.dispose();
        return;
    }
    status++;
    if (status == 0)
        cm.sendSimple("#w请问有神么可以服务的??\r\n\r\n#L0#去魔法森林#l\r\n#L1#去玩具城#l\r\n#L2#去神木村#l\r\n#L3#去桃花仙境#l\r\n#L4#去纳希沙漠#l\r\n#L5#去耶雷弗#l");
    else if (status == 1) {
        sel = selection;
        cm.sendNext("好 #h #, 我将带你到 #m" + (200000110 + (sel * 10)) + "#");
    } else if(status == 2){
        cm.warp(200000110 + (sel * 10));
        cm.dispose();
    }
}