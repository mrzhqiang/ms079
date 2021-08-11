function start(mode, type, selection) {
    qm.dispose();
}

var status = -1;

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 4) {
	    qm.sendNext("哦，那样啊。英雄果然很忙啊....哭哭。要是改变主意了，随时可以来找我。");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	if (qm.getQuestStatus(21011) == 0) {
	    qm.forceStartQuest();
	    qm.dispose();
	    return;
	}
	qm.sendNext("刚才我好像听到说“英雄回来了...”，是我听错了吗？什么？没听错吗？真的这位...这位是英雄吗？！");
    } else if (status == 1) {
	qm.sendNextPrev("   #i4001171#");
    } else if (status == 2) {
	qm.sendNextPrev("真是高兴啊...竟然能这样见到英雄，真是荣幸啊！求您握个手吧，顺便再抱一下我就更好了，但首先还是先签个名吧...");
    } else if (status == 3) {
	qm.sendNextPrev("可是...英雄怎么没有带武器呢。据我所知英雄有自己武器...啊！应该是和黑魔法师决斗时弄掉了。");
    } else if (status == 4) {
	qm.sendYesNo("凑合着用可能会太寒酸，不过#b请你先收下这把剑吧！#k 这是我送给英雄的礼物。英雄空着手总是有点奇怪... \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#i1302000# 1 #t1302000# \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 35 经验值");
    } else if (status == 5) {
	if (qm.getQuestStatus(21011) == 1) {
	    qm.gainItem(1302000, 1);
	    qm.gainExp(35);
	}
	qm.forceCompleteQuest();
	qm.sendNextPrevS("#b(连技能一点都不像英雄...连剑都好陌生。我之前真的有用过剑吗？剑该怎么配戴呢？)#k", 3);
    } else if (status == 6) {
	qm.summonMsg(16); // How to equip shiet
	qm.dispose();
    }
}