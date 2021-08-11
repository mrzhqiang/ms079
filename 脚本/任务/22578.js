var status = -1;
//this quest is SECRET ORGANIZATION QUESTION
function start(mode, type, selection) {
	qm.sendNext("Let's talk to Mir.");
	qm.forceStartQuest();
	qm.getPlayer().gainSP(2, 4);
	qm.forceCompleteQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.getPlayer().gainSP(2, 4);
	qm.forceCompleteQuest();
	qm.dispose();
}