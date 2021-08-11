function enter(pi) {
	if (pi.getEvanIntroState("mo30=o;mo40=o;mo41=o;mo50=o;mo42=o;mo60=o")) {
		return false;
	}
	pi.updateEvanIntroState("mo30=o;mo40=o;mo41=o;mo50=o;mo42=o;mo60=o");
    pi.ShowWZEffect("Effect/OnUserEff.img/guideEffect/evanTutorial/evanBalloon60");
	return true;
}