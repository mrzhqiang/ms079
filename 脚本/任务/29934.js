
var status = -1;

function start(mode, type, selection) {
	if (qm.getPlayer().getLevel() >= 10 && (qm.getPlayer().getJob() / 100) | 0 == 22) {
		qm.forceStartQuest(); //quest auto gives 
		qm.forceCompleteQuest();
	}
	qm.dispose();
}

function end(mode, type, selection) {
	if (qm.getPlayer().getLevel() >= 10 && (qm.getPlayer().getJob() / 100) | 0 == 22) {
		qm.forceStartQuest(); //quest auto gives 
		qm.forceCompleteQuest();
	}
	qm.dispose();
}