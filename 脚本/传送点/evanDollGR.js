function enter(pi) {
    if (pi.isQuestActive(22556)) {
	pi.playerMessage(5, "Investigation complete!");
	pi.forceCompleteQuest(22556);
    }
    pi.warp(910600010,0);
}