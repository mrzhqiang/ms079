var status = -1;
//this quest is SECRET ORGANIZATION 2
function start(mode, type, selection) {
	if (qm.canHold(4032471,1)) {
		qm.gainItem(4032471,1);	
		qm.forceStartQuest();
		qm.forceCompleteQuest();
		qm.gainExp(7100);
		qm.getPlayer().gainSP(1, 4);
	
	} else {
		qm.sendNext("Make ETC space.");
	}
	qm.dispose();
}

function end(mode, type, selection) {
	if (qm.canHold(4032471,1)) {
		qm.gainItem(4032471,1);	
		qm.forceStartQuest();
		qm.forceCompleteQuest();
		qm.gainExp(7100);
		qm.getPlayer().gainSP(1, 4);
	} else {
		qm.sendNext("Make ETC space.");
	}
	qm.dispose();
}