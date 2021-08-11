var status = -1;
function end(mode, type, selection) {
	    qm.forceCompleteQuest();
	if (qm.getJob() == 400) {
	    qm.changeJob(430);
	    qm.resetStats(4, 25, 4, 4);
	    qm.expandInventory(1, 4);
	    qm.expandInventory(2, 4);
	    qm.expandInventory(3, 4);
	    qm.expandInventory(4, 4);
	    qm.gainItem(1342000, 1);
	    qm.sendNext("You are now a Dual Blader.");
	}
	qm.dispose();
}

function start(mode, type, selection) {
    qm.dispose();
}
