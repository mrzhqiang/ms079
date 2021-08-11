var status = -1;

function start(mode, type, selection) {
	qm.sendNext("...Wow.");
	qm.gainExp(35935);
	qm.forceCompleteQuest();
	qm.dispose();
}
function end(mode, type, selection) {
	qm.dispose();
}
