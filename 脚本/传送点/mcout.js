function enter(pi) {
	var returnMap = pi.getSavedLocation("MONSTER_CARNIVAL");
	if (returnMap < 0) {
		returnMap = 103000000;
	}
	pi.clearSavedLocation("MONSTER_CARNIVAL");
	pi.warp(returnMap,0);
	return true;
}