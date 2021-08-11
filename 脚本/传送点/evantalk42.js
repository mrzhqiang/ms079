function enter(pi) {
	if (pi.getEvanIntroState("mo30=o;mo40=o;mo41=o;mo42=o")) {
		return false;
	}
	pi.updateEvanIntroState("mo30=o;mo40=o;mo41=o;mo42=o");
    pi.ShowWZEffect("Effect/OnUserEff.img/guideEffect/evanTutorial/evanBalloon42");
	return true;
}