function enter(pi) {
	var returnMap = pi.getSavedLocation("PACH");
	if (returnMap < 0) {
		returnMap = 100000000;
	}
	var target = pi.getPlayer().getClient().getChannelServer().getMapFactory().getMap(returnMap);
	var portal = target.getPortal("pachinkoDoor");
	if (portal == null) {
		portal = target.getPortal(0);
	}
	pi.clearSavedLocation("PACH");
	pi.getPlayer().changeMap(target, portal);
	return true;
}