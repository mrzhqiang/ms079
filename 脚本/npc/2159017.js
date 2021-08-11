function action(mode, type, selection) {
	if (cm.getMap().getAllMonstersThreadsafe().size() != 0) {
		cm.sendNext("Please! Destroy the Ice Knight!");
	} else {
		cm.warpParty(932000400,0);
	}
    cm.dispose();
}