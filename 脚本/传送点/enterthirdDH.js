function enter(pi) {
    if (pi.getQuestStatus(20601) == 1 || pi.getQuestStatus(20602) == 1 || pi.getQuestStatus(20603) == 1 || pi.getQuestStatus(20604) == 1 || pi.getQuestStatus(20605) == 1) {
	if (pi.getPlayerCount(913010200) == 0) {
	    var map = pi.getMap(913010200);
	    map.killAllMonsters(false);
	    map.respawn(true);
	    pi.warp(913010200, 0);
	} else {
	    pi.playerMessage("有人在地圖裡面了，請稍後再嘗試。");
	}
    } else {
	pi.playerMessage("我只給有修練的人進入。");
    }
}