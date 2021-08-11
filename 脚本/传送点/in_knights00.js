function enter(pi) {
    if (pi.isQuestActive(31124)) {
	pi.forceCompleteQuest(31124);
	pi.playerMessage("Quest complete");
    }
    pi.warp(271030010,0);
}