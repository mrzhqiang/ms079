var status = -1;

function start(mode, type, selection) {
	qm.sendNext("I wish you... sweet dreams...");
	qm.forceCompleteQuest();
	qm.dispose();
}
function end(mode, type, selection) {
	qm.sendNext("I wish you... sweet dreams.");
	qm.forceCompleteQuest();
	qm.dispose();
}
