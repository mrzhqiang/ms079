function enter(pi) {
    if (pi.getMap().getReactorByName("rnj3_out2").getState() > 0) {
	pi.warp(926100202,0);
    } else {
	pi.playerMessage(5, "The portal has not opened yet.");
    }
}