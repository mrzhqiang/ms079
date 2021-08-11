var status = -1;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			cm.sendSimple("等等！下面这些信息是10级玩家在游戏过程中自然而然就能了解的事项。不必事先特别了解，在游戏过程中慢慢摸索也可以的。不过，为了照顾那些习惯事先做好万全准备的玩家，在这里还是做些介绍。\r\n\r\n你有什么问题要问我的？\r\n\#L0#请仔细介绍一下你自己吧。#l \r\n#b#L1#如何转职，去哪转？#l \r\n#b#L2#可否使用辅助程序？#l \r\n#b#L3#背包空间太小？#l \r\n#b#L4#游戏点券从何而来？#l \r\n#b#L5#服务器送点券吗？#l");
		} else if (status == 1) {
			if (selection == 0) {
				cm.sendNext("我是守护希纳斯女王的神兽的部下。神兽接到了女皇的命令。女皇让我为参加骑士团的人提供引导，所以我在这里为你进行引导。在你成为骑士或者达到11级之前，我都会跟着你。如果有什么问题，可以随时问我。");
				cm.dispose();
			} else if (selection == 1) {
				cm.sendNext("为了各位玩家方便期间，目前游戏所有职业转职都不需要做任务，直接在各村落找#bnpc 蘑菇博士#k就可进行正常转职！");
				cm.dispose();
			} else if (selection == 2) {
				cm.sendNext("对于目前游戏来说，经验已经很高了。辅助程序仅可使用挂机。但是不可使用无敌吸怪等，详细请见系统公告！");
				cm.dispose();
			} else if (selection == 3) {
				cm.sendNext("背包空间和盛大一样。需要到商城扩充才能得到更多的空间！");
				cm.dispose();
			} else if (selection == 4) {
				cm.sendNext("游戏点券可购买，详细情况请联系客服人员或者联系游戏管理员！或群管理！");
				cm.dispose();
			} else if (selection == 5) {
				cm.sendNext("本服务器不送点券和任何装备。所有的东西都要靠自己的努力的来。不过服务器设置有奖励系统！详细信息请参见系统公告！");
				cm.dispose();
			}
		}
	}
}