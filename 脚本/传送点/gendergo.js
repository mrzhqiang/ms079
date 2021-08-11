function enter(pi) {
    if (pi.getPlayerStat("GENDER") == 0) {
	pi.warpS(670010200, "male01");
    } else {
	pi.warpS(670010200, "female01");
    }
}