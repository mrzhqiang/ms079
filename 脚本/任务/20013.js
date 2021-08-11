/*
	NPC Name: 		Kia
	Description: 		Quest - Cygnus tutorial helper
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendNext("如果需要就回来找我。");
	    qm.safeDispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("#b(*当当*)#k");
    } else if (status == 1) {
	qm.sendNextPrev("嘿！你吓到我了！. 我不知道我有一个访客. 你是贵族 #p1102006# 在谈论着. 欢迎! 我是 #p1102007#, 我的兴趣是制作 #b椅子#k. 我正在考虑让一个作为欢迎你的礼物。");
    } else if (status == 2) {
	qm.sendNextPrev("别急，我不能给你一个椅子，因为我没有足够的材料。你能找到我需要的材料？在这个区域附近，你会发现很多箱子里面的物品。你能不能给我带回 一个 #t4032267# 和一个  #t4032268# 在那些箱子里面。");
    } else if (status == 3) {
	qm.sendNextPrev("你知道如何打破那个箱子? 使用你的 #b普通攻击#k 来敲破箱子。");
    } else if (status == 4) {
	qm.askAcceptDecline("请给我 1个 #b#t4032267##k 和 1个 #b#t4032268##k 在那些箱子里面. 然后我就会做一个最棒的椅子给你， 我会在这里等着你！");
    } else if (status == 5) {
	qm.forceStartQuest();
	qm.summonMsg(9);
	qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	qm.sendNext("你已经完成了？\r\n太棒了！");
    } else if (status == 1) {
	qm.sendNextPrev("来这是给你的 #t3010060#. 你怎么看?? 漂亮吧呵呵^^ 你可以 #b把椅子放到快捷键上面并使用它让HP恢复更快。#k. 椅子在 #b装饰栏里面#k 打开你的道具栏, 所以请确认是不是收到椅子了 #b#p1102008##k. 好了，请按照箭头指示走你会看到另一个人。 \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#i3010060# 1 #t3010060# \r\n#fUI/UIWindow.img/QuestIcon/8/0# 95 经验值");
    } else if (status == 2) {
	qm.gainItem(4032267, -1);
	qm.gainItem(4032268, -1);
	qm.gainItem(3010060, 1);
	qm.forceCompleteQuest();
	qm.forceCompleteQuest(20000);
	qm.forceCompleteQuest(20001);
	qm.forceCompleteQuest(20002);
	qm.forceCompleteQuest(20015);
	qm.gainExp(95);
	qm.summonMsg(10);
	qm.dispose();
    }
}