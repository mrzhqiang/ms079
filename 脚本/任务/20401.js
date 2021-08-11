var status = -1;

function start(mode, type, selection) {
	qm.sendNext("我不知道但也许你能去打 #b僵尸#k 捡到僵尸东西将会有下一个步骤。");
	qm.forceStartQuest();
	qm.forceCompleteQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.forceStartQuest();
	qm.forceCompleteQuest();
	qm.dispose();
}