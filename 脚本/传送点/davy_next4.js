function enter(pi) {
    if (pi.getMap().getReactorByName("sMob1").getState() >= 1 && pi.getMap().getReactorByName("sMob2").getState() >= 1 && pi.getMap().getReactorByName("sMob3").getState() >= 1 && pi.getMap().getReactorByName("sMob4").getState() >= 1) {
	if (pi.isLeader()) {
	    pi.warpParty(925100500); //next
	} else {
	    pi.playerMessage(5, "The leader must be here");
	}
    } else {
	pi.playerMessage(5, "The portal is not opened yet.");
    }
}