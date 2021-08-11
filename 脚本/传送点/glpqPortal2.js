function enter(pi) {
    var em = pi.getEventManager("CWKPQ");
    if (em != null) {
	pi.warpS(610030300, 0);
	if (!em.getProperty("glpq3").equals("10")){
	    em.setProperty("glpq3", parseInt(em.getProperty("glpq3")) + 1);
	    pi.mapMessage(6, "An adventurer has passed through!");
	    if (em.getProperty("glpq3").equals("10")) {
		pi.mapMessage(6, "The Antellion grants you access to the next portal! Proceed!");
		pi.getMap().changeEnvironment("3pt", 2);
	    }
	}
    }
}