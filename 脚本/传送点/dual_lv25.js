function enter(pi) {
    if (pi.getPlayer().getLevel() >= 25) {
	pi.warp(103050340,0);
	pi.playPortalSE();
    } else {
	pi.playerMessage(5, "You must be level 25.");
    }
}