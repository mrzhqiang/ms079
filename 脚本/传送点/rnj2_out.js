function enter(pi) {
    var em = pi.getEventManager("Romeo");
    if (em != null && em.getProperty("stage3").equals("3")) {
	pi.warp(926100200,0);
    } else {
	pi.playerMessage(5, "The portal has not opened yet.");
    }
}