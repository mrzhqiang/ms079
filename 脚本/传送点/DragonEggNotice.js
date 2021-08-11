function enter(pi) {
	if (pi.getEvanIntroState("egg=o;mo30=o;mo40=o;mo41=o;mo50=o;mo42=o;mo60=o")) {
		return false;
	}
	pi.updateEvanIntroState("egg=o;mo30=o;mo40=o;mo41=o;mo50=o;mo42=o;mo60=o");
    pi.evanTutorial("UI/tutorial/evan/8/0", -1);
	return true;
}