/*
	NPC Name: 		Kimu
	Description: 		Quest - Cygnus tutorial helper
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 3) {
	    qm.sendNext("Whoa, whoa! Are you really declining my offer? Well, you'll be able to #blevel-up quicker#k with our help, so let me know if you change your mind. Even if you've declined a Quest, you can receive the Quest again if you just come and talk to me.");
	    qm.safeDispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("Welcome to Ereve! And you are? Oh, you're #b#h0##k! Good to meet you. I've been waiting. You've come to become a #p1101000# Knight, right? My name is #p1102004#, and I'm currently guiding Noblesses like you at the request of Empress Cygnus.");
    } else if (status == 1) {
	qm.sendNextPrev("If you want to officially become a part of #p1101000# Knights, you must first meet the Empress. She's at the center of this island, accompanied by #p1101001#. My brothers and I would like to share with you a few things that are considered #bBasic Knowledge#k in Maple World before you go. Would that be okay?");
    } else if (status == 2) {
	qm.sendNextPrev("Oh, let me warn you that this is a Quest. You may have noticed that NPCs around Maple World occasionally ask you for various favors. A favor of that sort is called a #bQuest#k. You will receive reward items or EXP upon completing Quests, so I strongly suggest you diligently fulfill the favors of Maple NPCs.");
    } else if (status == 3) {
	qm.askAcceptDecline("Would you like to meet #b#p1102005##k, who can tell you about hunting? You can find #p1102005# by following the arrow to the left.");
    } else if (status == 4) {
	qm.forceStartQuest();
	qm.summonMsg(2);
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
	qm.sendNext("你是贵族?? 我哥哥 #p1102004# 派来的? 很高兴认识你! 我是 #p1102005#. 我将送你 #p1102004# . 请记得，你可以按你的道具栏 #bI 键#k. 红药水能帮助你恢复HP蓝色药水能帮助你恢复MP，这是一个好主意学习如何使用它们能让你事先充分了解当你处于危险之中使用。. \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#i2000020# 5 #t2000020# \r\n#i2000021# 5 #t2000021# 5 \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 15 经验值");
    } else if (status == 1) {
	qm.gainItem(2000020, 5);
	qm.gainItem(2000021, 5);
	qm.forceCompleteQuest();
	qm.gainExp(15);
	qm.summonMsg(3);
	qm.dispose();
    }
}