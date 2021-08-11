var status = -1;

function action(mode, type, selection) {
    var em = cm.getEventManager("Romeo");
    if (em == null) {
	cm.dispose();
	return;
    }
    if (!cm.canHold(4001130,1)) {
	cm.sendOk("I will need 1 ETC space.");
	cm.dispose();
	return;
    }
    if (cm.getPlayer().getMapId() == 926100000) { //just first stage
	if (java.lang.Math.random() < 0.1) {
	    if (em.getProperty("stage1").equals("0")) {
		em.setProperty("stage1", "1");
		cm.getMap().setReactorState();
	    }
	} else if (java.lang.Math.random() < 0.05) {
	    if (em.getProperty("stage").equals("0")) {
		cm.gainItem(4001130,1);
	    }
	}
    }
    cm.dispose();
}