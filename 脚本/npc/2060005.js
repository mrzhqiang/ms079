/*
	This file is part of the cherry Maple Story Server
    Copyright (C) 2008 Patrick Huy <patrick.huy@frz.cc> 
                       Matthias Butz <matze@cherry.de>
                       Jan Christian Meyer <vimes@cherry.de>

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

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1 || mode == 0) {
		cm.dispose();
	} else {
		if (mode == 1)
			status++;
		if (status == 0) {
			cm.sendSimple ("我能算你便宜些，要点什么？\r\n#b#L0#小浣猪 [5,000,000 金币]#l\r\n#L1#银色野猪 [20,000,000 金币]#l\r\n#L2#赤羚龙 [50,000,000 金币]#l\r\n#L3#5个研究报告书 [1,000,000 金币]#l\r\n#L4#5个费洛蒙 [1,000,000 金币]#l");
		} else if (status == 1) {
			if(!cm.canHold(1902000)){
				cm.sendOk("你的背包没有足够的空间，请确保有足够的空间再购买！"); 
				cm.dispose();
				return;
			}
			if (selection == 0) {
				if (cm.getPlayer().getMeso() < 5000000) {
					cm.sendOk("你没有足够的金币。无法购买！");
					cm.dispose();
					return;
				}
				else if (cm.getPlayer().getLevel() < 70) {
					cm.sendOk("你至少需要#b70#k级才可购买#b小浣猪#k.加油吧！欢迎再次光临！");
					cm.dispose();
					return;
				}
				cm.gainItem(1902000, 1);
				cm.gainMeso(-5000000); 
			}
			else if (selection == 1) {
				if (cm.getPlayer().getMeso() < 20000000) {
					cm.sendOk("你没有足够的金币。无法购买！");
					cm.dispose();
					return;
				}
				else if (cm.getPlayer().getLevel() < 120) {
					cm.sendOk("你至少需要#b120#k级才可购买#b银色野猪#k.加油吧！欢迎再次光临！");
					cm.dispose();
					return;
				}
				cm.gainItem(1902001, 1);
				cm.gainMeso(-20000000); 
			} 
			else if (selection == 2) {
				if (cm.getPlayer().getMeso() < 50000000) {
					cm.sendOk("你没有足够的金币。无法购买！");
					cm.dispose();
					return;
				}
				else if (cm.getPlayer().getLevel() < 200) {
					cm.sendOk("你需要到达#b200#k级才可购买#b赤羚龙#k.加油吧！欢迎再次光临！");
					cm.dispose();
					return;
				}
				cm.gainItem(1902002, 1);
				cm.gainMeso(-50000000); 
			}
			else if (selection == 3) {
				if (cm.getPlayer().getMeso() < 1000000) {
					cm.sendOk("你没有足够的金币。无法购买！");
					cm.dispose();
					return;
				}
				cm.gainItem(4031508, 5);
				cm.gainMeso(-1000000); 
			}
			else if (selection == 4) {
				if (cm.getPlayer().getMeso() < 1000000) {
					cm.sendOk("你没有足够的金币。无法购买！");
					cm.dispose();
					return;
				}
				cm.gainItem(4031507, 5);
				cm.gainMeso(-1000000); 		
			 
	         }
			cm.sendOk("购买成功。有坐骑走起路来就快的多了！\r\n如果你没有获得#b皮鞍子#k和#b骑兽技能#k的话也可以在我这里获得。获得方法接我的任务就可以了。接了任务后再购买任务所需要的物品就可以了！");
			cm.dispose();
			return;
		}
	}
}
