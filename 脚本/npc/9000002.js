function start() {
	var map = cm.getSavedLocation("EVENT");
	if (map > -1 && map != cm.getMapId()) {
	if (cm.haveItem(4031017)) {
		cm.removeAll(4031017);
	}
		cm.warp(map, 0);
	} else {
	if (cm.haveItem(4031017)) {
		cm.removeAll(4031017);
	}
    		cm.warp(910000000, 0);
	}
    cm.dispose();
}

function action(mode, type, selection) {
}
