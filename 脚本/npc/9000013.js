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
/* Edited by: kevintjuh93
    NPC Name:         Jean
    Map(s):         Victoria Road : Lith Harbour (104000000)
    Description:         Event Assistant
*/
var status = 0;

function start() {
    cm.sendNext("嗨 我是 #b东尼#k. 我在等待我的大哥 #b江#k. 他应该现在在这里...");
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 2 && mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        else
            status--;
        if (status == 1) {
            cm.sendNextPrev("嗯......我该怎么办？本次活动将开始，很快......很多人去参与这项活动，所以我们最好快点来......");
        } else if (status == 2) {
            cm.sendSimple("嗨... 你为什么不跟我走？我想我的兄弟会与其他人...\r\n#L0##e1.#n#b 什么样的活动内容??#k#l\r\n#L1##e2.#n#b 介绍活动内容让我认识..#k#l\r\n#L2##e3.#n#b 好了，我们走吧！#k#l");
        } else if (status == 3) {
            if (selection == 0) {
                cm.sendNext("所有本月初，冒险岛环球庆祝其三周年！全球机制将举行惊喜GM活动在整个活动期间，所以留在你的脚趾，并确保参与活动的至少一个为伟大的奖品！");
                cm.dispose();
            } else if (selection == 1) {
                cm.sendSimple("有许多活动官则。这将帮助在你开始活动之前。好了...选择你想了解的活动.. #b\r\n#L0# 爬绳子#l\r\n#L1# 终极忍耐#l\r\n#L2# 滚雪球#l\r\n#L3# 打果子#l\r\n#L6# 打瓶盖#l\r\n#L4# 是非题大考验#l\r\n#L5# 寻宝#l#k");
            } else if (selection == 2) {
				if (!cm.canHold()) {
					cm.sendNext("请确认是否身上有空位。");
				} else if (cm.getChannelServer().getEvent() > -1) {
					if (cm.haveItem(4031017)) {
						cm.removeAll(4031017);
					}
					cm.saveReturnLocation("EVENT");
					cm.getPlayer().setChalkboard(null);
					cm.warp(cm.getChannelServer().getEvent(), cm.getChannelServer().getEvent() == 109080000 || cm.getChannelServer().getEvent() == 109080010 ? 0 : "join00");
				} else {
					cm.sendNext("活动尚未开放，请确认是否你有在24小时内参加过一个活动。请稍后在试！");
				}
				cm.dispose();
			}
        } else if (status == 4) {
            if (selection == 0) {
                cm.sendNext("#b[爬绳子]#k 自己#e#rGoogle#k!");
                cm.dispose();
            } else if (selection == 1) {
                cm.sendNext("#b[终极忍耐] 自己#e#rGoogle#k!");
                cm.dispose();
            } else if (selection == 2) {
                cm.sendNext("#b[滚雪球]#k 自己#e#rGoogle#k!");
                cm.dispose();
            } else if (selection == 3) {
                cm.sendNext("#b[打果子]#k 自己#e#rGoogle#k!");
                cm.dispose();
			} else if (selection == 6) {
                cm.sendNext("#b[打瓶盖]#k 自己#e#rGoogle#k!");
                cm.dispose();
            } else if (selection == 4) {
                cm.sendNext("#b[是非题大考验]#k 自己#e#rGoogle#k!");
                cm.dispose();
            } else if (selection == 5) {
                cm.sendNext("#b[寻宝]#k 自己#e#rGoogle#k!");
                cm.dispose();
            }
        }   
    }
}  