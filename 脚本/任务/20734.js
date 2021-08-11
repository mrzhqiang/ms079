/*
	NPC Name: 		Cygnus
	Description: 		Quest - Encounter with the Young Queen
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	qm.dispose();
	return;
    }
    if (status == 0) {
	qm.sendNext("Hello, Chief Knight. Currently, Maple World is in great danger. We need a bigger army to protect this place from the Black Mage. And to build a stronger army, I decided to ally with the Explorer Chiefs. We created the Ultimate Explorer with our combined powers.");
    } else if (status == 1) {
	qm.sendYesNo("The Ultimate Explorer starts at Lv. 50 and is born with very special skills. Would you like to be reborn as an Ultimate Explorer?");
    } else if (status == 2) {
	if (!qm.getClient().canMakeCharacter(qm.getPlayer().getWorld())) {
	    qm.sendOk("You cannot make a character without a character slot.");
	} else {
	    qm.sendUltimateExplorer();
	}
	qm.dispose();
    }
}

function end(mode, type, selection) {
}