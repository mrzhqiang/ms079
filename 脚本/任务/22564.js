var status = -1;
//this quest is DRAGON KNOWLEDGE
function start(mode, type, selection) {
	qm.sendNext("Go talk to Chief Tatamo of Leafre.");
	qm.forceStartQuest();
	qm.getPlayer().gainSP(1, 2);
	qm.forceCompleteQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.getPlayer().gainSP(1, 2);
	qm.forceCompleteQuest();
	qm.dispose();
}