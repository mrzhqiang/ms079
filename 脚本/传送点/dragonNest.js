function enter(pi) {
    if (pi.haveItem(4001094)) {
	if (pi.getQuestStatus(3706) > 0) {
	    if (pi.getPlayerCount(240040611) == 0) {
		pi.removeNpc(240040611, 2081008);
		pi.resetMap(240040611);
		pi.playPortalSE();
		pi.warp(240040611, "sp");
	    } else {
		pi.playerMessage(5, "有人已經在裡面，試圖完成任務。請稍後再試。");
	    }
	} else {
	    pi.playerMessage(5, "你不必追求開始。請稍後再試。");
	}
    } else {
	pi.playerMessage(5, "為了進入的前提下，你需要有九靈的蛋。");
    }
}