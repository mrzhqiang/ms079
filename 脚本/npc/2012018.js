var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
	cm.sendYesNo("Would you like to head to Chryse?");
    } else if (status == 1) {
	if (cm.getMap(200100000) == null) {
	    cm.sendOk("Chryse is not available yet.");
	} else {
	    cm.warp(200100000, 0);
	}
	cm.dispose();
    }
}