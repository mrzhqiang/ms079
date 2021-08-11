function enter(pi) {
    var em = pi.getEventManager("Romeo");
    if (em != null && em.getProperty("stage1").equals("1")) {
	pi.warp(926100001,0);
    } else {
	pi.playerMessage(5, "The portal has not opened yet.");
    }
}