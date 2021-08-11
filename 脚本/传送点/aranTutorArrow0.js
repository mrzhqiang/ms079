function enter(pi) {
    if (pi.getInfoQuest(21002).equals("mo1=o;mo2=o;mo3=o")) {
	pi.updateInfoQuest(21002, "arr0=o;mo1=o;mo2=o;mo3=o");
	pi.AranTutInstructionalBubble("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow3");
    }
}