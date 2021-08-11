var status = -1;

function start(mode, type, selection) {
	qm.sendNext("Find 10 Stormbreaker Badges.");
	qm.forceStartQuest();
	qm.dispose();
}
function end(mode, type, selection) {
	if (qm.haveItem(4032006,10)) {
		qm.sendNext("Good job!");
		qm.gainExp(85000);
		qm.forceCompleteQuest();
		qm.gainItem(4032006,-10);
	} else {
		qm.sendNext("Please find 10 Stormbreaker Badges.");
	}
	qm.dispose();
}
