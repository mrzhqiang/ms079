function enter(pi) {
    if (pi.haveItem(4032922)) {
	pi.warp(271030100,0);
    } else {
	pi.playerMessage("Needs a Cygnus Emblem to enter.");
    }
}