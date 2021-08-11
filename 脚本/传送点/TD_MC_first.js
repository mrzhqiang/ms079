function enter(pi) {
    var level = pi.getPlayerStat("LVL");
    if (level >= 30 && level <= 38) {
	pi.playPortalSE();
	pi.warp(106020000, 1);
    } else {
	pi.playerMessage(5, "未知的力量无法进入....");
    }
}