function enter(pi) {
	if (pi.getEvanIntroState("mo00=o")) {
		return false;
	}
	pi.updateEvanIntroState("mo00=o");
        pi.ShowWZEffect("Effect/OnUserEff.img/guideEffect/evanTutorial/evanBalloon00");
	return true;
}