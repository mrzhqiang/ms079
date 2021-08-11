function enter(pi) {
    if (pi.getQuestStatus(6242) == 1 || pi.getQuestStatus(6243) == 1) {
	if (!pi.haveItem(4001114)) {
	    if (pi.getPlayerCount(921100200) == 0) {
		pi.playPortalSE();
		pi.warp(921100210, 0);
		return true;
	    } else {
		pi.playerMessage("Other characters are on request. You can't enter.");
	    }
	} else {
	    pi.playerMessage("You don't have Freezer's Egg. You can't enter.");
	}
    } else if (pi.getQuestStatus(6242) == 2 && pi.getQuestStatus(6243) == 0) {
	if (!pi.haveItem(4001114)) {
	    pi.playPortalSE();
	    pi.warp(921100210, 0);
	    return true;
	} else {
	    pi.playerMessage("You don't have Freezer's Egg. You can't enter." );
	}
    } else {
	pi.playerMessage("You can't enter sealed place.");
    }
    return false;
}