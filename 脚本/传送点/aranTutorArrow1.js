function enter(pi) { // tutor02
    if (pi.getInfoQuest(21002).equals("normal=o;arr0=o;mo1=o;mo2=o;mo3=o;mo4=o")) {
	pi.updateInfoQuest(21002, "normal=o;arr0=o;arr1=o;mo1=o;mo2=o;mo3=o;mo4=o");
	pi.AranTutInstructionalBubble("Effect/OnUserEff.img/guideEffect/aranTutorial/tutorialArrow1");
    }
}