var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    qm.sendNext("不！ 狂狼勇士拒绝了！");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.askAcceptDecline("...差点被吓死...快！快点带我去找赫丽娜大人！");
    } else if (status == 1) {
	if (qm.getQuestStatus(21001) == 0) {
	    qm.gainItem(4001271, 1);
	    qm.forceStartQuest(21001, null);
	}
	qm.warp(914000300, 0);
	qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    qm.sendNext("孩子呢？ 倘若您救了那些孩子，就快点让他们上来吧！");
	    qm.dispose();
	    return;
	} else if (status == 8) { // watching the introduction
	    if (qm.haveItem(4001271)) {
		qm.gainItem(4001271, -1);
	    }
	    qm.MovieClipIntroUI(true);
	    qm.forceCompleteQuest();
	    qm.warp(914090010, 0);
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendYesNo("啊啊，您平安无事归来了！ 孩子呢？您把那些孩子带回来了吗？");
    } else if (status == 1) {
	qm.sendNext("真是太好了... 真是太好了.....");
    } else if (status == 2) {
	qm.sendNextPrevS("快点坐上飞行船吧！没时间了。", 3);
    } else if (status == 3) {
	qm.sendNextPrev("对，对了！现在不是谈这些事情的时机。黑魔法师的气息已经慢慢地靠近了！好像已经察觉方舟的位置了！不赶快出发的话，就会被逮个正着。");
    } else if (status == 4) {
	qm.sendNextPrevS("立刻出发！", 3);
    } else if (status == 5) {
	qm.sendNextPrev("狂狼勇士！你也坐上方舟吧！我虽然了解您想火拚到最后一刻的心情...可是已经太迟了！打仗这个任务就交给您的同伴，跟我们一起前往维多利亚岛吧！");
    } else if (status == 6) {
	qm.sendNextPrevS("绝对不行！", 3);
    } else if (status == 7) {
	qm.sendNextPrevS("赫丽娜，您先去维多利亚岛吧！我绝对不会死心的，我们后会有期。我要和同伴们一起去对付黑魔法师！", 3);
    } else if (status == 8) {
	qm.sendYesNo("请问您想跳过动画吗？");
    } else if (status == 9) { // Not watching
	if (qm.haveItem(4001271)) {
	    qm.gainItem(4001271, -1);
	}
	qm.forceCompleteQuest();
	qm.warp(140090000, 0);
	qm.dispose();
    }
}