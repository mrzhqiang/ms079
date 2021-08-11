function enter(pi) {
	if(pi.isQuestActive(22010) || pi.getPlayer().getJob() != 2001) {
		pi.warp(100030310);
	} else {
		pi.playerMessage("Cannot enter the Lush Forest without a reason.");
	}
	return true;
}