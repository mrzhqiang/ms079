function enter(pi) {
    if (pi.isQuestActive(3935)) {
	pi.forceCompleteQuest(3935);
	pi.playerMessage("Quest complete.");
    }
}