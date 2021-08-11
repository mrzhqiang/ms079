var status = -1;
//TEMPORARY QUEST NOW SKIPPING
//this quest is AFTER SHEDDING 2
function start(mode, type, selection) {
	qm.gainItem(4032503,1);
	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.forceCompleteQuest();
	qm.dispose();
}