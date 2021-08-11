function enter(pi) {
	if (pi.getEvanIntroState("dt00=o;mo00=o")) {
		return false;
	}
	pi.updateEvanIntroState("dt00=o;mo00=o");
	pi.showMapEffect("evan/dragonTalk00");
	return true;
}