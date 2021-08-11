var status = -1;
//this quest is DRAGON TYPES 2
function start(mode, type, selection) {
	qm.sendNext("Go talk to Grendel the Really Old of Ellinia.");
	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.forceCompleteQuest();
	qm.dispose();
}