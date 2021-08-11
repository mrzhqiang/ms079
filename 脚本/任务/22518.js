var status = -1;
//this quest is POWER B FORE
function start(mode, type, selection) {	
	qm.sendNext("Please eliminate 100 Spores.");
	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	if (!qm.canHold(2000004, 20) || !qm.canHold(2000002, 20) || !qm.canHold(4032457,1)) {
		qm.sendNext("Please make use/etc space.");
		qm.dispose();
		return;
	}
	qm.gainItem(4032457,1);
	qm.gainItem(2000004, 20);
	qm.gainItem(2000002, 20);
	qm.getPlayer().gainSP(1, 0);
	qm.gainExp(520);
	qm.forceCompleteQuest();
	qm.dispose();
}