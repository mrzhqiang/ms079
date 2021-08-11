function enter(pi) {
	if (pi.getEvanIntroState("mo30=o;mo40=o")) {
		return false;
	}
	pi.updateEvanIntroState("mo30=o;mo40=o");
        pi.ShowWZEffect("Effect/OnUserEff.img/guideEffect/evanTutorial/evanBalloon40");
	return true;
}