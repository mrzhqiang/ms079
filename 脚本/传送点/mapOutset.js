function enter(pi) {
    if (pi.getMap().getForcedReturnId() == 999999999) {
	pi.warp(910000000,0); //dont care lol
    } else {
	pi.warp(pi.getMap().getForcedReturnId());
    }
}