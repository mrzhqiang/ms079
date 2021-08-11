function start() {
    cm.sendYesNo("您想要离开了？？");
}

function action(mode, type, selection) {
    if (mode == 1) {
	if (cm.getMapId() == 674030200) { //boss map
		cm.warp(674030100,0);
	} else {
		var map = cm.getSavedLocation("CHRISTMAS");
		if (map > -1 && map != cm.getMapId()) {
			cm.warp(map, 0);
		} else {
    			cm.warp(100000000, 0);
		}
	}
    }
    cm.dispose();
}