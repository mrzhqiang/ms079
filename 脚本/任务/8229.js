var status = -1;

function start(mode, type, selection) {
	qm.sendNext("Wow. Get this to Jack!");
	qm.forceStartQuest();
	qm.dispose();
}
function end(mode, type, selection) {
	qm.sendNext("Thanks!");
	if (qm.haveItem(4032018,1)) {
		qm.gainItem(4032018,-1);
		qm.forceCompleteQuest();
	}
	qm.dispose();
}
