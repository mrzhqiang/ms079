var status = -1;
//this quest is SECRET ORGANIZATION 3
function start(mode, type, selection) {
	qm.sendNext("Talk to Hiver in Ludibrium Village.");
	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.gainExp(1600);
	qm.forceCompleteQuest();
	qm.dispose();
}