function enter(pi) {
    if (pi.getQuestStatus(6240) == 1 || pi.getQuestStatus(6241) == 1) {
	if (!pi.haveItem(4001113)) {
	    if (pi.getPlayerCount(921100200) == 0) {
		pi.playPortalSE();
		pi.warp(921100200, 0);
		return true;
	    } else {
		pi.playerMessage("Other characters are on request. You can't enter.");
	    }
	} else {
	    pi.playerMessage("You already have Phoenix's egg. You can't enter.");
	}
    } else if (pi.getQuestStatus(6240) == 2 && pi.getQuestStatus(6241) == 0) {
	if (!pi.haveItem(4001113)) {
	    pi.playPortalSE();
	    pi.warp(921100200, 0);
	    return true;
	} else {
	    pi.playerMessage("You already have Phoenix's egg. You can't enter." );
	}
    } else {
	pi.playerMessage("You can't enter sealed place.");
    }
    return false;
}