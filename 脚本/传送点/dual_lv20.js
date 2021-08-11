function enter(pi) {
    if (pi.getPlayer().getLevel() >= 20) {
	pi.warp(103050310,0);
	pi.playPortalSE();
    } else {
	pi.playerMessage(5, "You must be level 20.");
    }
}