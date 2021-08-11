function enter(pi) {
    pi.playPortalSE();
    if (pi.getQuestStatus(21001) == 0) {
	pi.warp(914000220, 2);
    } else {
	pi.warp(914000400, 2);
    }
}