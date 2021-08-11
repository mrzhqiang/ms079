function start() {
	cm.sendYesNo("Would you like to leave this place?");
}

function action(mode, type, selection) {
    	if (mode == 1) {
		cm.warp(541020700,6);
	}
	cm.dispose();
}
