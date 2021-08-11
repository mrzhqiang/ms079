function enter(pi) {
    if (pi.getInfoQuest(21019).equals("helper=clear")) {
	pi.updateInfoQuest(21019, "miss=o;helper=clear");
	pi.playerSummonHint(false);
    }
}