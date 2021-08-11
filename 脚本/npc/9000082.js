function start() {
	if (cm.getPlayer().getMapId() == 950101100) {
		if (cm.haveItem(4001433, 30)) {
			cm.gainItem(4001433, -30);
		} else {
			cm.removeAll(4001433);
		}
		cm.warp(950100000,0);
		cm.dispose();
		return;
	}
    cm.sendYesNo("If you leave now, you'll have to start over. Are you sure you want to leave?");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(950101100);
    }
    cm.dispose();
}