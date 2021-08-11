function enter(pi) {
try {
	if (pi.getPlayer().getParty() != null && pi.getMap().getMonsterById(9300438) == null && pi.isLeader()) {
		//if (pi.getPlayer().getEventInstance() != null) {
		//	pi.warpParty_Instanced(((pi.getPlayer().getMapId() / 100) + 1) * 100 - (pi.getPlayer().getMapId() % 100));
		//} else {
			pi.warpParty(((pi.getPlayer().getMapId() / 100) + 1) * 100 - (pi.getPlayer().getMapId() % 100));
		//}
		pi.playPortalSE();
	} else {
		pi.playerMessage(5,"Please get the leader in this portal, and make sure Ice Knight is here.");
	}
} catch (e) {
	pi.playerMessage(5, "Error: " + e);
}
}