function enter(pi) {
    if (pi.isQuestActive(21201)) { //aran first job
	pi.forceCompleteQuest(21201);
	pi.playerMessage(5, "You recovered your memories!");
    }
    pi.warp(108000700,0);
 //what does this even do
}