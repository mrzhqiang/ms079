var status = -1;

function start(mode, type, selection) {
	qm.sendNext("也许你应该再回到#b死龙巢穴#k 看看是否有奇怪的迹象...");
	qm.forceStartQuest();
	qm.forceCompleteQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.forceStartQuest();
	qm.forceCompleteQuest();
	qm.dispose();
}