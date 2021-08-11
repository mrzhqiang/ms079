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
var zones = 0;
var cost = 1000;

function start() {
    cm.sendNext("嗨~~ 我是地铁站 服务员..");
    if (cm.getQuestStatus(2055) || cm.getQuestStatus(2055))
        zones++;
    if (cm.getQuestStatus(2056) || cm.getQuestStatus(2056))
        zones++;
    if (cm.getQuestStatus(2057) || cm.getQuestStatus(2057))
        zones++;
}

function action(mode, type, selection) {
    status++;
    if (mode != 1){
        cm.dispose();
        return;
    }
    if (status == 0) {
        if (zones == 0)
            cm.dispose();
        else {
            var selStr = "你想要买哪种票??#b";
            for (var i = 0; i < zones; i++)
                selStr += "\r\n#L" + i + "#工地 B" + (i+1) + " (" + cost + " 金币)#l";
            cm.sendSimple(selStr);
        }
    } else if (status == 1) {
        if (cm.getMeso() < cost)
            cm.sendOk("看来你没有足够的金币...");
        else {
            cm.gainMeso(-cost);
            cm.gainItem(4031036 + selection,1);
        }
        cm.dispose();
    }
}
