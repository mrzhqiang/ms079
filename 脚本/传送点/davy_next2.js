function enter(pi) {
    if (pi.getMap().getAllMonstersThreadsafe().size() == 0) {
	pi.warp(925100300,0); //next
    } else {
	pi.playerMessage(5, "The portal is not opened yet.");
    }
}