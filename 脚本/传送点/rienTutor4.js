function enter(pi) {
    if (pi.getQuestStatus(21013) == 2) {
	pi.playPortalSE();
	pi.warp(140090500, 1);
    } else {
	pi.playerMessage(5, "再進入下一張地圖之前，請先完成任務。");
    }
}