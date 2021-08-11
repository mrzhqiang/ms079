function enter(pi) {
    var em = pi.getEventManager("Pirate");
    if (em != null && em.getProperty("stage2").equals("3")) {
	pi.warp(925100200,0); //next
    } else {
	pi.playerMessage(5, "The portal is not opened yet.");
    }
}