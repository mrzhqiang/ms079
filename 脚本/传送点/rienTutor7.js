function enter(pi) {
    if (pi.getQuestStatus(21014) == 2 || pi.getPlayer().getJob() != 2000) {
	pi.playPortalSE();
	pi.warp(140010100, 2);
    } else {
	pi.playerMessage(5, "The town of Rien is to the right. Take the portal on the right and go into town to meet Lilin.");
    }
}