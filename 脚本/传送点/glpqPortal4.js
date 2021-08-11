function enter(pi) {
    var em = pi.getEventManager("CWKPQ");
    if (em != null) {
	if (!em.getProperty("glpq4").equals("5")){
	    pi.playerMessage("The portal is not opened yet.");
	} else {
	    pi.warp(610030500, 0);
	}
    }
}