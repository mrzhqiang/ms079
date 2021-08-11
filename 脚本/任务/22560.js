var status = -1;
//this quest is JOINING ORGANIZATION
function start(mode, type, selection) {
	qm.sendNext("Please kill 150 Curse Eye.");
	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.gainExp(2300);
	qm.forceCompleteQuest();
	qm.dispose();
}