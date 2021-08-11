var status = -1;

function start(mode, type, selection) {
	if (!qm.canHold(4310018, 19) || !qm.canHold(1112608,1)) {
	    qm.sendOk("Please make some EQP/ETC space.");
	} else {
	    qm.gainItem(4310018, 19);
	    qm.gainItem(1112608, 1);
	    qm.forceStartQuest(50701);
	    qm.forceCompleteQuest();
	}
	qm.dispose();
}
function end(mode, type, selection) {
	if (!qm.canHold(4310018, 19) || !qm.canHold(1112608,1)) {
	    qm.sendOk("Please make some EQP/ETC space.");
	} else {
	    qm.gainItem(4310018, 19);
	    qm.gainItem(1112608, 1);
	    qm.forceStartQuest(50701);
	    qm.forceCompleteQuest();
	}
	qm.dispose();
}
