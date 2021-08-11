function enter(pi) {
    if (pi.isQuestActive(22588)) {
	pi.forceCompleteQuest(22588);
	pi.forceCompleteQuest(22589);
	pi.playerMessage(5, "Go talk to Olaf in Lith Harbor to get to the Island of Sleeping Dragon!");
    }
}