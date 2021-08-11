function enter(pi) {
	if(pi.isQuestActive(2314) || pi.isQuestFinished(2319)){
		pi.openNpc(1300014);
		return true;
	}
	return false;
}