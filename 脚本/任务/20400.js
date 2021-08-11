var status = -1;

function start(mode, type, selection) {
	qm.sendNext("去冰原雪域找#b杰德#k 他会告诉你详细情况。");
	qm.forceStartQuest();
	qm.forceCompleteQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.forceStartQuest();
	qm.forceCompleteQuest();
	qm.dispose();
}