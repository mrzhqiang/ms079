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
status = -1;
close = false;
oldSelection = -1;

function start() {
    var text = "这里是检票口";
    if (cm.haveItem(4031713) || cm.haveItem(4031036) || cm.haveItem(4031037) || cm.haveItem(4031038))
        text += " 你要使用这票??#b";
    else
        close = true;
    if (cm.haveItem(4031713))
        text += "\r\n#L3##t4031713#";
    for (var i = 0; i < 3; i++)
        if (cm.haveItem(4031036 + i))
            text += "\r\n#L" + i + "##t" + (4031036 + i) +"#";
    if (close) {
        cm.sendOk(text);
        cm.dispose();
    } else
        cm.sendSimple(text);
}

function action(mode, type, selection) {
    status++;
    if (mode != 1) {
        if(mode == 0)
            cm.sendNext("你必须有一些经济负担，对吧？");
        cm.dispose();
        return;
    }
    if (status == 0) {
        if (selection == 3) {
            var em = cm.getEventManager("Subway");
            if (em.getProperty("entry") == "true")
                cm.sendYesNo("它看起来像有足够的空间用于这搭。请将您的车票准备好，所以我可以让你的车程将是漫长的，但你会得到你的目的地就好了。你怎么看？你想要得到这个拼车？");
            else {
                cm.sendNext("我们停止接收票前1分钟了，所以请务必要在这里的时间。");
                cm.dispose();
            }
        }else{
            cm.sendNext("好运~~"); //Not GMS-like
        }
        oldSelection = selection;
    } else if (status == 1) {
        if (oldSelection == 3) {
            cm.gainItem(4031713, -1);
            cm.warp(600010004);
        } else {
            cm.gainItem(4031036 + oldSelection, -1);
            cm.warp(103000900 + (oldSelection * 3));
        }
        cm.dispose();
    }
}