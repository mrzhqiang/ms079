function enter(pi) {
	if (pi.isQuestActive(22596)) {
		pi.warp(922030001,0);
		pi.forceCompleteQuest(22596);
		pi.gainAp(5);
		pi.playerMessage(5, "Ibech has ran away! Gained 5 AP!");
	} else if (pi.isQuestFinished(22596)) {
		pi.warp(922030001,0);
	} else {
		pi.warp(922030000,0);
	}
}