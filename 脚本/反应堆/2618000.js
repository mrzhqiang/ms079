
function act() {
    if (rm.getReactor().getState() >= 7) {
	rm.mapMessage(6, "One of the beakers has been completed.");
	var em = rm.getEventManager(rm.getMapId() == 926100100 ? "Romeo" : "Juliet");
	if (em != null && rm.getReactor().getState() >= 7) {
		var react = rm.getMap().getReactorByName(rm.getMapId() == 926100100 ? "rnj2_door" : "jnr2_door");
		em.setProperty("stage3", parseInt(em.getProperty("stage3")) + 1);
		react.forceHitReactor(react.getState() + 1);
	}
    }
}