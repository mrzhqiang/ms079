var status = -1;
//TEMPORARY QUEST NOW SKIPPING
//this quest is HEROS ECHO LEVEL 200
function start(mode, type, selection) {
	qm.teachSkill(20021005, 1, 1);
	qm.forceStartQuest();
	qm.forceCompleteQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.teachSkill(20021005, 1, 1);
	qm.forceStartQuest();
	qm.forceCompleteQuest();
	qm.dispose();
}