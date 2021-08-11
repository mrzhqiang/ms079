var status = -1;
function action(mode, type, selection) {
    if (cm.getMapId() == 926110600) {
	    cm.removeAll(4001130);
	    cm.removeAll(4001131);
	    cm.removeAll(4001132);
	    cm.removeAll(4001133);
	    cm.removeAll(4001134);
	    cm.removeAll(4001135);
	var em = cm.getEventManager("Juliet");
    if (em != null) {
	var itemid = 4001159;
	if (!cm.canHold(itemid, 1)) {
	    cm.sendOk("Please clear 1 ETC slot.");
	    cm.dispose();
	    return;
	}
	cm.gainItem(itemid, 1);
	if (em.getProperty("stage").equals("2")) {
    		//cm.gainNX(5000);
    		cm.gainExpR(140000);
	} else {
		//cm.gainNX(3500);
		cm.gainExpR(105000);
	}
    }
    cm.getPlayer().endPartyQuest(1205);
    cm.warp(926110700,0);
    cm.dispose();
    return;
    }
    if (mode > 0) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	    cm.removeAll(4001130);
	    cm.removeAll(4001131);
	    cm.removeAll(4001132);
	    cm.removeAll(4001133);
	    cm.removeAll(4001134);
	    cm.removeAll(4001135);
	cm.sendSimple("#b#L0#Get me out of here#l\r\n#L1#Get me Horus' Eye.#l\r\n#L2#Get me Rock of Wisdom#l#k");
    } else {
	if (selection == 0) {
    	    cm.warp(926110600,0);
	} else if (selection == 1) {
	    if (cm.canHold(1122010,1) && cm.haveItem(4001160,25) && cm.haveItem(4001159,25)) {
		cm.gainItem(1122010,1);
		cm.gainItem(4001160,-25);
		cm.gainItem(4001159,-25);
	    } else {
		cm.sendOk("You will need 25 Alcadno Marble and 25 Zenumist Marble to get Horus Eye, as well as have EQP space.");
	    }
	} else {
	    if (cm.canHold(2041212,1) && (cm.haveItem(4001160,10) || cm.haveItem(4001159,10))) {
		cm.gainItem(2041212,1);
		if (cm.haveItem(4001160,10)) {
			cm.gainItem(4001160,-10);
		} else {
			cm.gainItem(4001159,-10);
		}
	    } else {
		cm.sendOk("You will need 10 of either marble to get Rock of Wisdom, as well as have USE space.");
	    }
	}
    	cm.dispose();
    }
}