function act() {
	var em = rm.getEventManager("Pirate");
	if (em != null) {
		rm.mapMessage(6, "One of the boxes have been activated.");
		em.setProperty("stage5", parseInt(em.getProperty("stage5")) + 1);
	}
}