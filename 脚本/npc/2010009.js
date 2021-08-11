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

/**
 * Guild Alliance NPC
 */

var status;
var choice;
var guildName;
var partymembers;

function start() {
	//cm.sendOk("The Guild Alliance is currently under development.");
	//cm.dispose();
	partymembers = cm.getPartyMembers();
	status = -1;
	action(1,0,0);
}

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
		cm.sendSimple("哈啰 我是雷娜里欧！ 很高兴为您服务～#k\r\n#b#L0#我想要知道公会联盟是什么？#l\r\n#L1#我要怎么建立公会联盟呢？#l\r\n#L2#我想要建立公会联盟#l\r\n#L3#我想要新增更多的公会到联盟#l\r\n#L4#我想要解散公会联盟#l");
	} else if (status == 1) {
		choice = selection;
	    if (selection == 0) {
		    cm.sendOk("公会联盟就是让两方的公会成员可以聊天做一些有趣的事情。");
			cm.dispose();
		} else if (selection == 1) {
			cm.sendOk("为了成立公会联盟，两个公会的会长需要组队，然后这个组队里的队长就会选为公会联盟的会长。");
			cm.dispose();
		} else if(selection == 2) {
			if (cm.getPlayer().getParty() == null || partymembers == null || partymembers.size() != 2 || !cm.isLeader()) {
				cm.sendOk("你不能创建一个公会联盟，直到你找到另一个公会。"); //Not real text
				cm.dispose();
			} else if (partymembers.get(0).getGuildId() <= 0 || partymembers.get(0).getGuildRank() > 1) {
				cm.sendOk("你不能创建一个公会联盟，直到你有自己的公会。");
				cm.dispose();
			} else if (partymembers.get(1).getGuildId() <= 0 || partymembers.get(1).getGuildRank() > 1) {
				cm.sendOk("你的成员似乎没有自己的工会。");
				cm.dispose();
			} else {
				var gs = cm.getGuild(cm.getPlayer().getGuildId());
				var gs2 = cm.getGuild(partymembers.get(1).getGuildId());
				if (gs.getAllianceId() > 0) {
					cm.sendOk("你不能再创建因为你已经和其他结为同盟了。");
					cm.dispose();
				} else if (gs2.getAllianceId() > 0) {
					cm.sendOk("你的成员已经和其他公会结为同盟了。");
					cm.dispose();
				} else if (cm.partyMembersInMap() < 2) {
					cm.sendOk("请确保其他成员在同张地图上。");
					cm.dispose();
				} else
                cm.sendYesNo("哦，你有兴趣创建一个公会联盟？");
			}
		} else if (selection == 3) {
			if (cm.getPlayer().getGuildRank() == 1 && cm.getPlayer().getAllianceRank() == 1) {
				cm.sendYesNo("为了增加矿大 需要支付 10,000,000 金币. 你确定要继续吗？"); //ExpandGuild Text
			} else {
			    cm.sendOk("只有公会联盟长可以扩大联盟。");
				cm.dispose();
			}
		} else if(selection == 4) {
			if (cm.getPlayer().getGuildRank() == 1 && cm.getPlayer().getAllianceRank() == 1) {
				cm.sendYesNo("你真的想要解散公会联盟？？");
			} else {
				cm.sendOk("只有公会联盟长才可以解散。");
				cm.dispose();
			}
		}
	} else if(status == 2) {
	    if (choice == 2) {
		    cm.sendGetText("现在请输入你想要的公会联盟名称 (最大字元限制. 12 个字)");
		} else if (choice == 3) {
			if (cm.getPlayer().getGuildId() <= 0) {
				cm.sendOk("你不能增加不存公会联盟。");
				cm.dispose();
			} else {
				if (cm.addCapacityToAlliance()) {
					cm.sendOk("你成功增加了公会联盟容量。");
				} else {
					cm.sendOk("很抱歉，由于你的公会联盟容量已经满了，所以不能再扩充。");
				}
				cm.dispose();
			}
		} else if (choice == 4) {
			if (cm.getPlayer().getGuildId() <= 0) {
				cm.sendOk("你不能解散不存在的公会联盟。");
				cm.dispose();
			} else {
				if (cm.disbandAlliance()) {
					cm.sendOk("成功解散公会联盟。");
				} else {
					cm.sendOk("解散公会联盟时候发生错误。");
				}
				cm.dispose();
			}
		}
	} else if (status == 3) {
		guildName = cm.getText();
	    cm.sendYesNo("这个 #b"+ guildName + "#k 是你想要的公会联盟名字吗？？");
	} else if (status == 4) {
			if (!cm.createAlliance(guildName)) {
				cm.sendNext("这个名字不能使用，请尝试其他的。"); //Not real text
				status = 1;
				choice = 2;
			} else
				cm.sendOk("成功的创建了公会联盟！！");
				cm.dispose();
	}
}
