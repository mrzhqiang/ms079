var status = -1;
//this quest is SECRET ORGANIZATION SUSPICION
function start(mode, type, selection) {
	qm.sendNext("Let's talk to Mir.");
	qm.forceStartQuest();
	qm.getPlayer().gainSP(1, 5);
	qm.forceCompleteQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.getPlayer().gainSP(1, 5);
	qm.forceCompleteQuest();
	qm.dispose();
}