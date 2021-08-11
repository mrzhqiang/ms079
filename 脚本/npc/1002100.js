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
// Jane the Alchemist
var status = -1;
var amount = -1;
var items = [[2000002,310],[2022003,1060],[2022000,1600],[2001000,3120]];
var item;

function start() {
    if (cm.getQuestStatus(2013))
        cm.sendNext("这是你...谢谢你，我能得到很多完成。现在我已经做了一堆物品。如果你需要什么，让我知道.");
    else {
        if (cm.getQuestStatus(2010))
            cm.sendNext("你似乎没有强大到足以能够购买我的药水......");
        else
            cm.sendOk("需要完成任务才可以跟我买药水喔!");
        cm.dispose();
    }
}

function action(mode, type, selection) {
    status++;
    if (mode != 1){
        if(mode == 0 && type == 1)
            cm.sendNext("我仍然有不少你以前把我的材料。这些项目都存在这样把你的时间选择...");
        cm.dispose();
        return;
    }
	/*
    if (status == 0){
        var selStr = "你想购买那些药水??#b";
        for (var i = 0; i < items.length; i++)
            selStr += "\r\n#L" + i + "##i" + items[i][0] + "# (价格 : " + items[i][1] + " 金币)#l";
        cm.sendSimple(selStr);
    } else if (status == 1) {
        item = items[selection];
        var recHpMp = ["300 HP.","1000 HP.","800 MP","1000 HP and MP."];
        cm.sendGetNumber("你想买 #b#t" + item[0] + "##k? #t" + item[0] + "# 允许您恢复 " + recHpMp[selection] + " 你想买多少个??", 1, 1, 100);
    } else if (status == 2) {
        cm.sendYesNo("你购买这些 #r" + selection + "#k #b#t" + item[0] + "#(s)#k? #t" + item[0] + "# 费用为 " + item[1] + " 金币 为一体，所以总出来是 #r" + (item[1] * selection) + "#k 金币.");
        amount = selection;
    } else if (status == 3) {
        if (cm.getMeso() < item[1] * amount)
            cm.sendNext("确认你的金币是否足够,和检查你的消耗拦是否足够,如果有你至少 #r" + (item[1] * selectedItem) + "#k 金币.");
        else {
            if (cm.canHold(item[0])) {
                cm.gainMeso(-item[1] * amount);
                cm.gainItem(item[0], amount);
                cm.sendNext("谢谢你的到来。东西在这里可以随时进行，所以如果你需要的东西，欢迎再来.");
            } else
                cm.sendNext("确认你的金币是否足够,和检查你的消耗拦是否足够..");
        }
        cm.dispose();
    } 
	*/
	cm.dispose();
}
