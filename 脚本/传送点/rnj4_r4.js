function enter(pi) {
    var em = pi.getEventManager("Romeo");
    if (em != null && em.getProperty("stage6_3").equals("0")) {
	pi.warp(926100304,0);
	em.setProperty("stage6_3", "1");
    } else {
	pi.playerMessage(5, "Someone has already gone in this portal.");
    }
}