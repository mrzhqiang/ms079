function enter(pi) {
    if (pi.getQuestStatus(20020) == 0) {
	pi.summonMsg(1);
	pi.forceCompleteQuest(20020);
    }
}