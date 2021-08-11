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

var status = 0;
var regcost = 499;
var vipcost = 999;
var tempvar;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
	status++; if (mode == 0 && status == 1) {
	cm.dispose();
	return;
    } if (mode == 0 && status == 2) {
	cm.sendNext("我们也提供其他服务，决定好之前请仔细想想。");
	cm.dispose();
	return;
    }
    if (status == 0) {
	cm.sendNext("欢迎来到奇幻村旅馆。我们致力为您提供最好的服务。如果您累了，来这里休息一下如何？");
    }
    if (status == 1) {
	cm.sendSimple("我们提供两种房间，请选择你想要的\r\n#b#L0#一般桑拿室 (每次 " + regcost + " 金币)#l\r\n#L1#高级桑拿室 (每次" + vipcost + " 金币)#l");
    }
    if (status == 2) {
	tempvar = selection;
	if (tempvar == 0) {
	    cm.sendYesNo("你选择了一般桑拿室，你的HP和MP会回复得很快，你也可以在里面购买商品，你确定要进入吗？");
	}
	if (tempvar == 1) {
		cm.sendYesNo("你选择了高级桑拿室，你的HP和MP会比一般桑拿室回复得更快，也可以在里面找到特殊的物品，你确定要进入吗？");
	}
    }
    if (status == 3) {
	if (tempvar == 0) {
	    if (cm.getMeso() >= regcost) {
		cm.warp(105040401);
		cm.gainMeso(-regcost);
	    } else {
        cm.sendNext("很抱歉，看起来您似乎没有足够的金币。你至少要有 " + regcost + " 金币才能待在我们的一般桑拿室。");
	    }
	} if (tempvar == 1) {
	    if (cm.getMeso() >= vipcost) {
		cm.warp(105040402);
		cm.gainMeso(-vipcost);
	    } else {
        cm.sendNext("很抱歉，看起来您似乎没有足够的金币。你至少要有 " + vipcost + " 金币才能待在我们的高级桑拿室。");
	    }
	}
	cm.dispose();
    }
}
