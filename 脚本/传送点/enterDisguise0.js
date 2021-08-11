function enter(pi) {
    if (pi.getJob() >= 1000) {
	if (pi.haveItem(4032179)) { // Search warrent
	    pi.playerMessage("The erev search begins.");
	}
	pi.playPortalSE();
	pi.warp(130010000, 3);
    } else {
	pi.playerMessage("Only the knights of Cygnus may enter.");
    }
}