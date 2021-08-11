function enter(pi) {
    if (pi.getMap().getAllMonstersThreadsafe().size() == 0) {
	pi.warp(926100100,0);
    } else {
	pi.playerMessage(5, "The portal has not opened yet.");
    }
}