function enter(pi) {
    if (pi.getQuestStatus(6134) == 1) {
	var em = pi.getEventManager("s4resurrection2");
	if (em == null) {
	    pi.playerMessage("You're not allowed to enter with unknown reason. Try again.");
	} else {
	    var prop = em.getProperty("started");
	    if (prop == null || prop.equals("false")) {
		em.startInstance(pi.getPlayer());
		return true;
	    } else {
		pi.playerMessage("Someone is already attempting on the quest.");
	    }
	}
    } else {
	pi.playerMessage("You can't enter sealed place.");
    }
    return false;
}