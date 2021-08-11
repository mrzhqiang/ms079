function enter(pi) {
    pi.saveLocation("SLEEP");
    pi.playPortalSE();
    pi.warp(749030000, "out00");
	return true;
}