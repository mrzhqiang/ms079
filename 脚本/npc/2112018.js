function action(mode, type, selection) {
	    cm.removeAll(4001130);
	    cm.removeAll(4001131);
	    cm.removeAll(4001132);
	    cm.removeAll(4001133);
	    cm.removeAll(4001134);
	    cm.removeAll(4001135);
	var em = cm.getEventManager(cm.getMapId() == 926100600 ? "Romeo" : "Juliet");
    if (em != null) {
	var itemid = cm.getMapId() == 926100600 ? 4001160 : 4001159;
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
    cm.warp(cm.getMapId() == 926100600 ? 926100700 : 926110700,0);
    cm.dispose();
}