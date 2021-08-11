var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
		cm.sendSimple("You currently have #r" + cm.getPlayer().getBattlePoints() + "BP.#b\r\n#L3##i4310015##t4310015# x 1 (500BP)#l\r\n#L4##i4310015##t4310015# x 4 (1500BP)#l\r\n#L5##i4310015##t4310015# x 7 (2500BP)#l");
    } else if (status == 1) {
	if (selection == 3) {
	    if (cm.getPlayer().getBattlePoints() >= 500 && cm.canHold(4310015,1)) {
			cm.getPlayer().setBattlePoints(cm.getPlayer().getBattlePoints() - 500);
			cm.gainItem(4310015,1);
		} else {
			cm.sendOk("Check if you have the correct BP.");
		}
	} else if (selection == 4) {
	    if (cm.getPlayer().getBattlePoints() >= 1500 && cm.canHold(4310015,4)) {
			cm.getPlayer().setBattlePoints(cm.getPlayer().getBattlePoints() - 1500);
			cm.gainItem(4310015,4);
		} else {
			cm.sendOk("Check if you have the correct BP.");
		}
	} else if (selection == 5) {
	    if (cm.getPlayer().getBattlePoints() >= 2500 && cm.canHold(4310015,7)) {
			cm.getPlayer().setBattlePoints(cm.getPlayer().getBattlePoints() - 2500);
			cm.gainItem(4310015,7);
		} else {
			cm.sendOk("Check if you have the correct BP.");
		}
	}
	cm.dispose();
    }
}