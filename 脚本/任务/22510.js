var status = -1;
//this quest is DELIVER LETTER
function start(mode, type, selection) {
	if (qm.canHold(4032455,1)) {
		qm.sendNext("Go deliver this to Chief Stan of Henesys.");
		qm.gainItem(4032455,1);
		qm.forceStartQuest();
	} else {
		qm.sendNext("Please have some space.");
	}
	qm.dispose();
}

function end(mode, type, selection) {
	if (qm.haveItem(4032455,1)) {
		qm.sendNext("Thank you.");
		qm.getPlayer().gainSP(1, 0);
		qm.gainExp(450);
		qm.gainItem(4032455, -1);
		qm.forceCompleteQuest();
	} else {
		qm.sendNext("Please get me the letter.");
	}
	qm.dispose();
}