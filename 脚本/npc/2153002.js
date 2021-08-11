function action(mode, type, selection) {
	if (cm.haveItem(1003134,1,true,true) || cm.isQuestFinished(23947)) {
		cm.warp(310050000,0);
	} else {
		cm.sendNext("Sorry, you may not enter.");
	}
	cm.safeDispose();
}