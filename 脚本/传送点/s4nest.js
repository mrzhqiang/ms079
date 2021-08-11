function enter(pi) {
    if (pi.getQuestStatus(6241) == 1 || pi.getQuestStatus(6243) == 1) {
	if (pi.getJob() == 312) {
	    if (pi.haveItem(4001113)) {
		if (pi.getPlayerCount(924000100) > 0) {
		    pi.playerMessage("Other characters are on request. You can't enter.");
		    return false;
		}
		var em = pi.getEventManager("s4nest");
		if (em == null) {
		    pi.playerMessage("You're not allowed to enter with unknown reason. Try again." );
		} else {
		    em.startInstance(pi.getPlayer());
		    return true;
		}
	    } else {
		pi.playerMessage("You don't have Phoenix's Egg. You can't enter." );
	    }
	} else if (pi.getJob() == 322) {
	    if (pi.haveItem(4001114)) {
		if (pi.getPlayerCount(924000100) > 0) {
		    pi.playerMessage("Other characters are on request. You can't enter.");
		    return false;
		}
		var em = pi.getEventManager("s4nest");
		if (em == null) {
		    pi.playerMessage("You're not allowed to enter with unknown reason. Try again." );
		} else {
		    em.startInstance(pi.getPlayer());
		    return true;
		}
	    } else {
		pi.playerMessage("You don't have Freezer's Egg. You can't enter." );
	    }
	}
    } else {
	pi.playerMessage("You can't enter sealed place.");
    }
    return false;
}