function start() {
	cm.sendYesNo("Do you want to get out now?");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(100000000, 0);
    }
    cm.dispose();
}