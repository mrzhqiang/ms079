function enter(pi) {
    if (pi.haveItem(4031346)) {
	if (pi.getMapId() == 240010100) {
	    pi.playPortalSE();
	    pi.warp(101010000, "minar00");
	} else {
	    pi.playPortalSE();
	    pi.warp(240010100, "elli00");
	}
	pi.gainItem(4031346, -1);
	pi.playerMessage("The Magical Seed is spent and you are transferred to somewhere.");
	return true;
    } else {
	pi.playerMessage("Magic Seed is needed to go through the portal.");
	return false;
    }
}
