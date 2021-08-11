/*
	Description: 	Quest - A Bite of Pork
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendNext("How can you starve me like this. I'm just a baby. This is wrong!");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("No, no, no. This isn't what I need. I need something more nutritious, master!");
    } else if (status == 1) {
	qm.sendNextPrevS("#bHm... So you're not a herbivore. You might be a carnivore. You're a Dragon, after all. How does some Pork sound?#k", 2);
    } else if (status == 2) {
	qm.askAcceptDecline("What's a...Pork? Never heard of it, but if it's yummy, I accept! Just feed me something tasty. Anything but plants!");
    } else if (status == 3) {
	qm.forceStartQuest();
	qm.sendOkS("#b(Try giving Mir some Pork. You have to hunt a few Pigs at the farm. Ten should be plenty...)#k", 2);
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
	qm.sendOk("Oh, is this what you brought me to eat? So this is the Pork you were taking about? Let me try.");
    } else if (status == 1) {
	qm.gainExp(1850);
	qm.gainItem(4032453, -10);
	qm.sendNext("(Chomp, chomp, gulp...)");
	qm.forceCompleteQuest();
    } else if (status == 2) {
	qm.sendPrev("Uggh... This doesn't taste too bad but I don't think I can digest it. This isn't for me...");
	qm.dispose();
    }
}