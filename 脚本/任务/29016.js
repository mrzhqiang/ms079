
var status = -1;

function start(mode, type, selection) {
}

function end(mode, type, selection) {
	if (qm.canHold(1142137,1) && !qm.haveItem(1142137,1)) {
		qm.gainItem(1142137,1);
		qm.forceStartQuest();
		qm.forceCompleteQuest();
	}
	qm.dispose();
}