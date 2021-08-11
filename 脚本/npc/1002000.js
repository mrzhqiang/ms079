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

/* Regular Cab */

var status = 0;
var maps = [104000000, 102000000, 101000000, 100000000, 103000000, 120000000];
var cost = [120, 120, 80, 100, 100, 120];
var townText = [
    ["是个很不错的地方"],
    ["是个很不错的地方"],
    ["是个很不错的地方"],
    ["是个很不错的地方"],
    ["是个很不错的地方"],
    ["是个很不错的地方"],
    ["是个很不错的地方"]
];
var selectedMap = -1;
var town = false;

function start() {
    cm.sendNext("你想头部到一些其他的城镇？随着一点点的资金介入，我可以做到这一点。这是一个稍微贵一些，但我有一个特殊的90％的折扣，适合新手。");
}

function action(mode, type, selection) {
    status++;
    if (mode != 1) {
        if ((mode == 0 && !town) || mode == -1) {
            if (type == 1 && mode != -1)
                cm.sendNext("有很多看在这个小镇，让我知道如果你想要去别的地方。");
            cm.dispose();
            return;
        } else
            status -= 2;
    }
    if (status == 1)
        cm.sendSimple("如果这是你第一次你可能会感到困惑这个地方，这是可以理解的。请问这个地方的任何问题??.\r\n#L0##b有多少个城市在这个岛上?#l\r\n#L1#我想去别的城市..#k#l");
    else if (status == 2) {
        if (selection == 0) {
            town = true;
            var text = "岛上有7大城市你想了解哪一个城市??#b";
            for (var i = 0; i < maps.length; i++)
                text += "\r\n#L" + i + "##m" + maps[i] + "##l";
            cm.sendSimple(text);
        } else if (selection == 1) {
            var selStr = cm.getJob() == 0 ? "你是新手所以你有90%的折扣\r\n请问你想去哪??#b" : "哦，你不是一个新手，是吧？我怕我可能会向您收取全价。你想去哪？#b";
            for (var i = 0; i < maps.length; i++)
                selStr += "\r\n#L" + i + "##m" + maps[i] + "# (" + (cost[i] * (cm.getJob() == 0 ? 1 : 10)) + "金币)#l";
            cm.sendSimple(selStr);
        }
    } else if (town) {
        if (selectedMap == -1)
            selectedMap = selection;
        if (status == 3)
            cm.sendNext(townText[selectedMap][status - 3]);
        else
            townText[selectedMap][status - 3] == undefined ? cm.dispose() : cm.sendNextPrev(townText[selectedMap][status - 3]);
    } else if (status == 3) {
        selectedMap = selection;
        cm.sendYesNo("我猜你不想待这里。你真的想要移动到 #b#m" + maps[selection] + "##k? 我将相你收取 #b" + (cost[selection] * (cm.getJob() == 0 ? 1 : 10)) + " 金币#k. 你怎么看??");
    } else if (status == 4) {
        if (cm.getMeso() < (cost[selectedMap] * (cm.getJob() == 0 ? 1 : 10)))
            cm.sendNext("你没有足够的金币我不能帮助你!!");
        else {
            cm.gainMeso(-(cost[selectedMap] * (cm.getJob() == 0 ? 1 : 10)));
            cm.warp(maps[selectedMap]);
        }
        cm.dispose();
    }
}
