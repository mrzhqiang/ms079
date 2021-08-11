
var status = -1;

function start(mode, type, selection) {
	if (qm.canHold(1142244,1) && qm.getPlayer().getLevel() >= 10 && ((qm.getPlayer().getJob() / 1000) | 0) == 3) {
		qm.forceStartQuest();
		qm.forceCompleteQuest();
		qm.gainItem(1142244,1);
	}
	qm.dispose();
}

function end(mode, type, selection) {
	if (qm.canHold(1142244,1) && qm.getPlayer().getLevel() >= 10 && ((qm.getPlayer().getJob() / 1000) | 0) == 3) {
		qm.forceStartQuest();
		qm.forceCompleteQuest();
		qm.gainItem(1142244,1);
	}
	qm.dispose();
}