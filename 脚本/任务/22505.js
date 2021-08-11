/*
	Description: 	Quest - Tasty Milk 2
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendOk("Hmm... I think most babies are the same. Think about it and let me know if you change your mind.");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.askAcceptDecline("He's so big I didn't realize he was a baby. He probably can't digest meat yet. My guess is that all #bbabies need milk#k first");
    } else if (status == 1) {
	qm.forceStartQuest();
	qm.sendNext("You can get milk from the #bMilk Cow#k at the #bHuge Path#k. Why don't you go ask her to give you some?");
    } else if (status == 2) {
	qm.sendPrev("Oh, and once you're done feeding the lizard, can you come back to me? I have something to talk to you about.");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	qm.sendOk("Mooo!");
    } else if (status == 1) {
	qm.gainExp(1150);
	qm.forceCompleteQuest();
	qm.dispose();
    }
}