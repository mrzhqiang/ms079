var status = -1;

function start(mode, type, selection) {
 	if (!qm.canHold(4031894, 1)) {
	    qm.sendNext("Please make some space..");
	} else {
	    qm.gainItem(4031894, 1);
	    qm.forceCompleteQuest();
	}
	qm.dispose();
}
function end(mode, type, selection) {
 	if (!qm.canHold(4031894, 1)) {
	    qm.sendNext("Please make some space..");
	} else {
	    qm.gainItem(4031894, 1);
	    qm.forceCompleteQuest();
	}
	qm.dispose();
}
