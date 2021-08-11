var status = -1;

function start(mode, type, selection) {
	if (!qm.canHold(4310018, 15) || !qm.canHold(1112606,1)) {
	    qm.sendOk("Please make some EQP/ETC space.");
	} else {
	    qm.gainItem(4310018, 15);
	    qm.gainItem(1112606, 1);
	    qm.forceCompleteQuest(50694);
	    qm.sendOk("Come to Nihal Desert.");
	    qm.forceCompleteQuest();
	}
	qm.dispose();
}
function end(mode, type, selection) {
	if (!qm.canHold(4310018, 15) || !qm.canHold(1112606,1)) {
	    qm.sendOk("Please make some EQP/ETC space.");
	} else {
	    qm.gainItem(4310018, 15);
	    qm.gainItem(1112606, 1);
	    qm.forceCompleteQuest(50694);
	    qm.sendOk("Come to Nihal Desert.");
	    qm.forceCompleteQuest();
	}
	qm.dispose();
}
