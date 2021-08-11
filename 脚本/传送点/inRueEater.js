function enter(pi) {
    if (pi.isQuestActive(23970)) {
	pi.forceCompleteQuest(23970);
	pi.playerMessage("Quest complete.");
    }
}