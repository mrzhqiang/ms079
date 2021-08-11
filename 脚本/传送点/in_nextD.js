function enter(pi) {
    if (pi.isQuestActive(31144)) {
	pi.forceCompleteQuest(31144);
	pi.playerMessage("Quest complete");
    }
}