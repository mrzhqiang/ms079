var status = -1;
//TEMPORARY QUEST NOW SKIPPING
//this quest is AFTER SHEDDING 1
function start(mode, type, selection) {
	qm.gainItem(1142156,1);
	qm.removeAll(4032502);
	qm.forceCompleteQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.forceCompleteQuest();
	qm.dispose();
}