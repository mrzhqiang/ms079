var status = -1;

function start(mode, type, selection) {
	qm.sendNext("回去耶雷弗回报情况吧。");
	qm.forceStartQuest();
	qm.forceCompleteQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.forceStartQuest();
	qm.forceCompleteQuest();
	qm.dispose();
}
