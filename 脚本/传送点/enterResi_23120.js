function enter(pi) {
    if (pi.getQuestStatus(23120) == 1) {
	if (pi.getPlayerCount(931000410) == 0) {
		var map = pi.getMap(931000410);
		map.resetFully();
		pi.warp(931000410, 0);
	} else {
	    pi.playerMessage("Being searched by someone else. Better come back later.");
	}
    }
}