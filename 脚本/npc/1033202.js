var status = -1;

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (cm.isQuestActive(24007) || cm.isQuestFinished(24007)) {
		cm.sendNext("Please, save us.");
		cm.dispose();
		return;
	}
	if (status == 0) {
		cm.sendPlayerToNpc("Elders! You survived! What happened?");
	} else if (status == 1) {
		cm.sendNextNoESC("A fierce, freezing curse has fallen upon the town, and you too, your Highness. From you, most of all, in fact. Is this the power of the Black Mage?");
	} else if (status == 2) {
		cm.sendNextNoESC("The children are already trapped in ice. The adults will follow them; it takes time to freeze stronger elves, which is why you're alright, but we're not.", 1033204);
	} else if (status == 3) {
		cm.sendPlayerToNpc("This is my fault. I let the Black Mage curse us anyway...");
	} else if (status == 4) {
		cm.sendNextNoESC("So it is his doing... I knew it.", 1033203);
	} else if (status == 5) {
		cm.sendNextNoESC("The Black Mage has cursed our sovereign, and the curse has spread...");
	} else if (status == 6) {
		cm.sendPlayerToNpc("Please, I didn't mean for this to happen. I should've been more careful...");
	} else if (status == 7) {
		cm.sendNextNoESC("Even beyond the seal, the Black Mage wields such power... It is a miracle you were able to seal him.");
	} else if (status == 8) {
		cm.sendNextNoESC("It is not your fault. No one could have stopped this. You are a hero.", 1033204);
	} else if (status == 9) {
		cm.sendPlayerToNpc("I shouldn't have fought him at all! If only I knew this would happen... I've failed my people...");
	} else if (status == 10) {
		cm.sendNextNoESC("Don't say such things! Even if you let him be, he would have come for us sooner or later.", 1033204);
	} else if (status == 11) {
		cm.sendNextNoESC("It is our fault, we have failed you, your Highness.");
	} else if (status == 12) {
		cm.sendPlayerToNpc("No! This isn't your fault! I don't regret fighting... I just regret failing to protect you.");
	} else if (status == 13) {
		cm.sendNextNoESC("This is not your burden alone. The decision to fight was the decision of the Elves, and we all share the results, whatever they may be.", 1033204);
	} else if (status == 14) {
		cm.sendPlayerToNpc("...Everyone...");
	} else if (status == 15) {
		cm.sendNextNoESC("Regardless, we will survive. We will overcome this together. The hope for the Elves lives on as long as your Highness is safe.");
	} else if (status == 16) {
		cm.sendNextNoESC("We can't stop the curse, but we can outlive it. We should seal Elluel before the curse can spread beyond the village. #bWe Elves should all slumber here, undisturbed.#k Time is on our side and we have nothing to worry about.", 1033204);
	} else if (status == 17) {
		cm.sendNextNoESC("Eventually we will all awaken together. Not even the curse will last forever; we will emerge victors.");
	} else if (status == 18) {
		cm.sendPlayerToNpc("Okay. I'll seal the village with my remaining strength...");
		cm.forceStartQuest(24007, "1");
		cm.dispose();
	}
}