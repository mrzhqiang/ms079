var status = -1;

function start(mode, type, selection) {
	if (!qm.canHold(4310018, 25) || !qm.canHold(1112611,1)) {
	    qm.sendOk("Please make some EQP/ETC space.");
	} else {
	    qm.gainItem(4310018, 25);
	    qm.gainItem(1112611, 1);
	    qm.forceCompleteQuest(50709);
	    qm.sendOk("Come to Leafre.");
	    qm.forceCompleteQuest();
	}
	qm.dispose();
}
function end(mode, type, selection) {
	if (!qm.canHold(4310018, 25) || !qm.canHold(1112611,1)) {
	    qm.sendOk("Please make some EQP/ETC space.");
	} else {
	    qm.gainItem(4310018, 25);
	    qm.gainItem(1112611, 1);
	    qm.forceCompleteQuest(50709);
	    qm.sendOk("Come to Leafre.");
	    qm.forceCompleteQuest();
	}
	qm.dispose();
}
