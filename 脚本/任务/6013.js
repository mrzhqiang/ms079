
var status = -1;

function start(mode, type, selection) {
}

function end(mode, type, selection) {
	if (qm.getPlayer().getLevel() >= 200 && qm.getPlayer().getJob() / 100 == 5) {
		qm.gainItem(1902002,1);
		qm.forceStartQuest();
		qm.forceCompleteQuest();
	}
	qm.dispose();
}