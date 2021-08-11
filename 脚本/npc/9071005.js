
var status = 0;

function start() {
    if (cm.getMapId() == 951000000) {
	cm.dispose();
	return;
    }
    cm.sendYesNo("Would you like to go back to the Monster Park?");
}

function action(mode, type, selection) {
    if (mode == 1) {
        cm.warp(951000000);
    }
    cm.dispose();
}