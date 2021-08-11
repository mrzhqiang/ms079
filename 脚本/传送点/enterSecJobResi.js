function enter(pi) {
    if (pi.getQuestStatus(23120) == 1) {
	if (pi.getPlayerCount(931000420) == 0) {
		var map = pi.getMap(931000420);
		map.resetFully();
		pi.warp(931000420, 0);
	} else {
	    pi.playerMessage("Being searched by someone else. Better come back later.");
	}
    } else if (pi.getQuestStatus(23023) == 1 || pi.getQuestStatus(23024) == 1 || pi.getQuestStatus(23025) == 1) {
	if (pi.getPlayerCount(931000100) == 0) {
		pi.removeNpc(931000100, 2159100);
		var map = pi.getMap(931000100);
		map.killAllMonsters(false);
		map.spawnNpc(2159100, new java.awt.Point(-157, -23));
		pi.warp(931000100, 0);
	} else {
	    pi.playerMessage("Being searched by someone else. Better come back later.");
	}
    } else {
	pi.warp(310000010, 0);
    }
}