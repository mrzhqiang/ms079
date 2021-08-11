var status = -1;

function start(mode, type, selection) {
	qm.sendNext("Wow. Hold on while I translate this..");
	if (qm.haveItem(4032032,1)) {
		qm.gainItem(4032032,-1);
		qm.forceStartQuest();
	}
	qm.dispose();
}
function end(mode, type, selection) {
	qm.sendNext("Here you are!");
	if (qm.canHold(4032018,1)) {
		qm.gainItem(4032018,1);
		qm.forceCompleteQuest();
	}
	qm.dispose();
}
