var status = -1;

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
		cm.sendPlayerToNpc("Afrien? Freud! Are you okay?");
	} else if (status == 1) {
		cm.sendNextNoESC("Mercedes... you survived.");
	} else if (status == 2) {
		cm.sendPlayerToNpc("Of course. I managed to seal him away. I can't let myself die for that. What about you? And the others? Where are they?");
	} else if (status == 3) {
		cm.sendNextNoESC("We may have defeated the Black Mage, but he sent everyone flying in different directions with that last spell. We're lucky we ended up in the same place.");
	} else if (status == 4) {
		cm.sendPlayerToNpc("I didn't realiez how far away we ended up. At least we're safe. I feel so weak... and cold... has it always been snowy here? It's hot, and yet snow is falling. Strange...");
	} else if (status == 5) {
		cm.sendNextNoESC("You can't feel it? Mercedes, the great curse... has been place upon you, Freud, and the others. An icy, cold curse, clinging to you. It looks like the Black Mage isn't letting us off so easily..");
	} else if (status == 6) {
		cm.sendPlayerToNpc("A c-curse... you should be able to survive it, but what about Freud? He looks weak...");
	} else if (status == 7) {
		cm.sendNextNoESC("I'll take care of him. For now, I'm more worried about you. You're the #bruler of the Elves.#k If the curse is on you, it'll be placed on #rall of the Elves!#k Hurry back to #bElluel#k If the #bBlack Mage's curse is on all of the Elves#k, then you must return to  your people.");
	} else if (status == 8) {
		cm.sendPlayerToNpc("...! All right! Afrien, we'll meet again!");
	} else if (status == 9) {
		cm.sendPlayerToNpc("(The other heroes will make it through somehow. For now, I'll return to town using my skill.)");
	} else if (status == 10) {
		cm.warp(910150001,0);
		cm.dispose();
	}
}