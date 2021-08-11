var status = -1;

function start(mode, type, selection) {
	qm.sendNext("The monkey is giving you an apple..");
	qm.forceCompleteQuest();
	qm.dispose();
}
function end(mode, type, selection) {
	qm.dispose();
}
