var status = -1;

function start(mode, type, selection) {
	qm.sendNext("Find someone to translate this.");
	if (qm.canHold(4032032,1)) {
		qm.gainItem(4032032,1);
		qm.forceCompleteQuest();
	}
	qm.dispose();
}
function end(mode, type, selection) {
	qm.dispose();
}
