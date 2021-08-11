function enter(pi) {
    if (pi.getQuestStatus(20301) == 1 ||
	pi.getQuestStatus(20302) == 1 ||
	pi.getQuestStatus(20303) == 1 ||
	pi.getQuestStatus(20304) == 1 ||
	pi.getQuestStatus(20305) == 1) {
	if (pi.getPlayerCount(108010640) == 0) {
	    if (pi.haveItem(4032179, 1)) {
		pi.removeNpc(108010640, 1104104);
		var map = pi.getMap(108010640);
		map.killAllMonsters(false);
		map.spawnNpc(1104104, new java.awt.Point(542, 88));
		pi.warp(108010640, 0);
	    } else {
		pi.playerMessage("You do not have the Erev Search Warrent to do so, please get it from Nineheart.");
	    }
	} else {
	    pi.playerMessage("The forest is already being searched by someone else. Better come back later.");
	}
    } else {
	pi.warp(130020000, 10);
    }
}