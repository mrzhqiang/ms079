/*
 * Cygnus 2nd Job advancement - Proof of test
 * Wind Breaker
 */

var status = -1;

function start(mode, type, selection) {
}

function end(mode, type, selection) {
    if (mode == 0) {
	if (status == 0) {
	    qm.sendNext("我猜你还没准备好。");
	    qm.dispose();
	    return;
	} else if (status >= 2) {
	    status--;
	} else {
	    qm.dispose();
	    return;
	}
    } else {
	status++;
    }
    if (status == 0) {
	if (qm.getQuestStatus(20203) == 0) {
	    qm.forceStartQuest();
	    qm.dispose();
	} else {
	    if (qm.haveItem(4032098, 30)) {
		qm.sendYesNo("所以，你准备好二转了？");
	    } else {
		qm.dispose(); // Hack
	    }
	}
    } else if (status == 1) {
	if (!qm.canHold(1142067)) {
	    qm.sendOk("请确认装备栏是否足够。");
	    qm.dispose();
	} else {
	    qm.forceCompleteQuest();
	    if (qm.getJob() != 1310) {
		qm.changeJob(1310); // Wind Breaker
		qm.gainItem(4032098, -30);
		qm.gainItem(1142067, 1);
	    }
	    qm.sendNext("训练已经结束。你现在皇家骑士团的骑士官员。");
	}
    } else if (status == 2) {
	qm.sendPrev("好运！");
	qm.dispose();
    }
}