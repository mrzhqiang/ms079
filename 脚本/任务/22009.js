/*
	Description: 	Quest -  Verifying the Farm Situation
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendOk("What? Think hard about this! If the farm fails, what are we going to survive on! Huh? Talk to me again and press ACCEPT this time!");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.askAcceptDecline("If the number of foxes has increased near the farm just like it has near our house, that'll interface with Dad's farm work. We should investigate this. Don't you agree?");
    } else if (status == 1) {
	qm.forceStartQuest();
	qm.sendOk("Go to the #bCentral Farm#k and ask #bDad#k about the situation. If the number of Cunning Foxes has increased there as well, we're going to have to conduct a major Cunning Fox hunt.");
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
	qm.sendOk("What is it, Evan? I'm sure you're not here to deliver another Lunchbox with Love, and I'm too busy to play with you... What? Have the number of foxes increased here?");
    } else if (status == 1) {
	qm.sendNext("Well, I'm not sure. I've been too busy to notice. The #bPig#ks have been acting crazy, jumping all over the place. Even the foxes seem to be running away from the Pigs...");
	qm.gainExp(260);
	qm.forceCompleteQuest();
    } else if (status == 2) {
	qm.sendPrev("Ah, maybe that is why the Cunning Fox population near the house has increased. They ran there to escape from the Pigs. Hmm...");
	qm.dispose();
    }
}