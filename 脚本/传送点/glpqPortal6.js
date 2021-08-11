function enter(pi) {
    var em = pi.getEventManager("CWKPQ");
    if (em != null) {
	if (!em.getProperty("glpq6").equals("3")){
	    pi.playerMessage("The portal is not opened yet.");
	} else {
	    pi.warp(610030700, 0);
	}
    }
}