function enter(pi) {
    var em = pi.getEventManager("Juliet");
    if (em != null && em.getProperty("stage4").equals("2")) {
	pi.warp(926110203,0);
    } else {
	pi.playerMessage(5, "The portal has not opened yet.");
    }
}