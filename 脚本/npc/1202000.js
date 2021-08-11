/*
 * Tutorial Lirin
 */

/* global cm */

var status = -1;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
if (cm.getPlayer().getMapId() != 140090000) {
if (status == 0) {
	cm.sendSimple("您有什么想知道的呢？有的话我会再度说明。  \n\r #b#L1#该如何使用小地图？#l \n\r #b#L2#该如何使用任务栏位？#l \n\r #b#L3#该如何使用道具？#l \n\r #b#L4#如何使用普通攻击？#l \n\r #b#L5#如何捡取东西？#l \n\r #b#L6#如何穿装备？#l \n\r #b#L7#如何打开技能视窗？#l \n\r #b#L8#如何把技能放到快捷键上？#l \n\r #b#L9#如何打破一个箱子？#l \n\r #b#L10#如何坐椅子？#l \n\r #b#L11#如何查看地图资讯？#l");
} else {
    cm.summonMsg(selection);
    cm.dispose();
}
} else {
    if (cm.getInfoQuest(21019).equals("")) {
	if (status == 0) {
	    cm.sendNext("您....终于醒了!");
	} else if (status == 1) {
	    cm.sendNextPrevS("...你是谁?", 2);
	} else if (status == 2) {
	    cm.sendNextPrev("我已经在这等你好久了. 等待那个与黑磨法师对抗的英雄苏醒...!");
	} else if (status == 3) {
	    cm.sendNextPrevS("等等, 你说什么..? 你是谁...?", 2);
	} else if (status == 4) {
	    cm.sendNextPrevS("等等... 我是谁...? 我既不起以前的事情了... 啊...我头好痛啊..", 2);
	} else if (status == 5) {
	    cm.updateInfoQuest(21019, "helper=clear");
	    //cm.showWZEffect("Effect/Direction1.img/aranTutorial/face");
	    //cm.showWZEffect("Effect/Direction1.img/aranTutorial/ClickLirin");
	    cm.playerSummonHint(true);
	    cm.dispose();
	}
    } else {
	if (status == 0) {
	    cm.sendNext("你还好吗？");
	} else if (status == 1) {
	    cm.sendNextPrevS("我... 什么都不记得了...这里是哪里？还有你是谁？", 2);
	} else if (status == 2) {
	    cm.sendNextPrev("放轻松. 因为黑磨法师的诅咒，让你想不起以前的了. 以前的事情已经不重要了. 我会帮助你想起所有事情的.");
	} else if (status == 3) {
	    cm.sendNextPrev("你曾经是这里的英雄. 几百年以前, 你与你的朋友们对抗黑魔法师，拯救了枫之谷的世界. 但那个时候，黑磨法师对你下了诅咒，将你冰冻起来，直到抹去你所有的记忆为止.");
	} else if (status == 4) {
	    cm.sendNextPrev("这里是瑞恩岛。黑魔法师将您囚禁在此地。诅咒的气候混乱，经年覆盖冰霜和雪。您在冰之窟的深处被发现的。");
	} else if (status == 5) {
	    cm.sendNextPrev("我的名字是#p1202000#。 是瑞恩岛的成员。瑞恩族根据古老的预言从很久以前就等待英雄回来。还有...终于找到您了。现在。就是这里....");
	} else if (status == 6) {
	    cm.sendNextPrev("好像一下说太多了。就算您不能马上了解也没有关系。您会慢慢知道所有事....#b我们先去村庄吧#k。在抵达村庄之前，如果还有什么想知道，我会逐一向您说明。");
	} else if (status == 7) {
	    cm.playerSummonHint(true);
	    cm.warp(140090100, 1);
	    cm.dispose();
	}
    }
}
}