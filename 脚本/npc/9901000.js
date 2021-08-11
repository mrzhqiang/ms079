importPackage(java.util);
importPackage(java.lang);
var status = -1;
var sr = null;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode != 1) {
		cm.dispose();
		return;
	}
	status++;
	if (status == 0) {
		cm.sendSimple("Hello, what would you like to do?#b\r\n#L0#Fight Papulatus in a Speed Run.(Speed Run will start immediately)#l\r\n#L1#View the Papulatus Speed Runs.#l#k");
	} else if (status == 1) {
		if (selection == 0) {
			if (cm.getClient().getChannel() != 1 && cm.getClient().getChannel() != 2) {
				cm.sendOk("This boss may only be attempted on channel 1 and 2");
			} else if (!cm.haveItem(4031172)) {
				cm.sendOk("You need the Ludibrium Medal.");
			} else if (cm.getPlayer().isGM()) {
				cm.sendOk("GMs may not do Speed Runs.");
			} else if (cm.getPlayerCount(220080001) <= 0) {
				var map = cm.getMap(220080001);
				map.resetFully();
				map.startSpeedRun(cm.getPlayer().getName());
				cm.playerMessage(5, "The Speed Run has started!");
				cm.warp(220080001, "st00");
			} else {
				cm.sendOk("Someone is already in the map.");
			}
			cm.dispose();
		} else {
			sr = cm.getSpeedRun("Papulatus");
			if (sr.getLeft().equals("")) {
				cm.sendOk("No speedruns have been done yet.");
				cm.dispose();
			} else {
				cm.sendSimple(sr.getLeft());
			}
		}
	} else if (status == 2 && sr != null && selection > 0 && cm.getSR(sr, selection)) {
		status = -1;	
	}
}