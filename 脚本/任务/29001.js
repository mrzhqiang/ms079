
var status = -1;

function start(mode, type, selection) {
}

function end(mode, type, selection) {
	if (qm.canHold(1142002,1) && !qm.haveItem(1142002,1) && qm.getPlayer().getNumQuest() >= 800) {
		qm.gainItem(1142002,1);
		qm.forceStartQuest();
		qm.forceCompleteQuest();
	}
	qm.dispose();
}