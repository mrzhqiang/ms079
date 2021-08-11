function enter(pi) {
	if (pi.haveItem(1002971,1)) {
		pi.warp(980040010,0);
 		pi.playPortalSE();
	} else {
		pi.playerMessage(5, "You need the Pink Bean Hat before entering.");
	}
}