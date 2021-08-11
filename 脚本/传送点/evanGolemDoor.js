function enter(pi) {
    if (pi.isQuestActive(22557)) {
	pi.forceCompleteQuest(22557);
	pi.playerMessage(5, "Camilla rescued!");
	pi.getPlayer().gainSP(1);
    }
    pi.warp(106010102,0);
    pi.playPortalSE();
}