
var status = -1;

function start(mode, type, selection) {
	if (qm.haveItem(1142257,1) && qm.getPlayer().getLevel() >= 10) {
		qm.forceStartQuest();
		qm.forceCompleteQuest();
	}
	qm.dispose();
}

function end(mode, type, selection) {
	if (qm.haveItem(1142257,1) && qm.getPlayer().getLevel() >= 10) {
		qm.forceStartQuest();
		qm.forceCompleteQuest();
	}
	qm.dispose();
}