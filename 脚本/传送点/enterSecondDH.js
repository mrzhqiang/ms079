function enter(pi) {
    if (pi.getQuestStatus(20201) == 1 || 
	pi.getQuestStatus(20202) == 1 ||
	pi.getQuestStatus(20203) == 1 ||
	pi.getQuestStatus(20204) == 1 ||
	pi.getQuestStatus(20205) == 1) {
	pi.warp(108000600, 0);
    }
}
