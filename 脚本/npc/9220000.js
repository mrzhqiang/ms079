function start() {
    cm.sendYesNo("Would you like to go back?");
}

function action(mode,type,selection) {
    if (mode == 1) {
	var map = cm.getSavedLocation("CHRISTMAS");
	if (map > -1 && map != cm.getMapId()) {
		cm.warp(map, 0);
	} else {
    		cm.warp(100000000, 0);
	}
    }
    cm.dispose();
}