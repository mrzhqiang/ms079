function enter(pi) {
    var em = pi.getEventManager("Romeo");
    if (em != null && em.getProperty("stage5").equals("2")) {
	pi.warp(926100300,0);
    } else {
	pi.playerMessage(5, "The portal has not opened yet.");
    }
}