function start() {
	if (cm.getMapId() == 209000000) {
		cm.warp(209080000);
                cm.dispose();
	} else if (cm.getMapId() == 209080000) {
		cm.warp(209000000);
                cm.dispose();
        }
    }