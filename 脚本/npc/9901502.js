function action(mode, type, selection) {
    if (cm.haveItem(1002971,1) && cm.canHold(1052202,1) && !cm.haveItem(1052202,1)) {
	cm.gainItem(1052202,1);
    } else {
    	cm.sendOk ("If you bring me the Pink Bean Hat and an empty slot, you will gain the Pink Bean Overall if you don't already have it.");
    }
    cm.safeDispose();
}