var status = -1;

function action(mode, type, selection) {
	if (cm.isQuestActive(2566) && !cm.haveItem(4032985,1)) {
		cm.gainItem(4032985,1);
		cm.sendNext("You recovered the Ignition Device.");
	}
	cm.dispose();
}