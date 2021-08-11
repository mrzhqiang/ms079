/*
 *  NPC   : Gold Compass
 *  Free Market event NPC
 */

var status = -1;

function start() {
    if (cm.getMapId() == 910000000) {
	cm.sendNext("Seriously, how is this world going to function without moi, the great Cassandra? I have yet to spend a day free of worries about this great world. What am I talking about, you ask?");
    } else {
	cm.dispose();
    }
}

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	cm.sendOk("Well, if you are not interested in treasures, then??");
	cm.dispose();
	return;
    }

    if (status == 0) {
	cm.sendNext("Apparently, the treasure storage of the richest man in the world of Maple, Gold Richie, has been destroyed by this herd of pigs eating up all his treasures, and he wants this taken care of right now. #b#h0#!#k Well, I don't find you too trustworthy, but I can't find anyone else willing to help, so... will you do it? Who knows, you might end up taking one of his treasures home... but make sure your 'USE' inventory has at least one slot available!");
    } else if (status == 1) {
	cm.sendNext("The only thing I have right now is #t2430007#, but you can find the following items through a small fee: #b#t3994102#, #t3994103#, #t3994104#, and \n#t3994105##k. Once you gather them all up, and double-click the #b#t2430007##k that I gave you, you'll be able to build the #b#t2430008##k that'll lead you to Gold Richie's Treasure Storage.");
    } else if (status == 2) {
	cm.sendSimple("What do you want now? \r\n#b#L0# #t2430007# for 3 million mesos#l \r\n#b#L1##t3994102# x20 for 100,000 mesos#l \r\n#b#L2##t3994103# x20 for 100,000 mesos#l \r\n#b#L3##t3994104# x20 for 100,000 mesos#l \r\n#b#L4##t3994105# x20 for 100,000 mesos#l");
    } else if (status == 3) {
	if (selection == 0) {
	    if (cm.canHold(2430007) && cm.getMeso() >= 3000000) {
		cm.gainItem(2430007, 1)
		cm.gainMeso(-3000000);
	    } else {
		cm.sendOk("You arn't gonna fool me are you?");
	    }
	} else if (selection == 1) {
	    if (cm.canHold(3994102) && cm.getMeso() >= 100000) {
		cm.gainItem(3994102, 20)
		cm.gainMeso(-100000);
	    } else {
		cm.sendOk("You arn't gonna fool me are you?");
	    }
	} else if (selection == 2) {
	    if (cm.canHold(3994103) && cm.getMeso() >= 100000) {
		cm.gainItem(3994103, 20)
		cm.gainMeso(-100000);
	    } else {
		cm.sendOk("You arn't gonna fool me are you?");
	    }
	} else if (selection == 3) {
	    if (cm.canHold(3994104) && cm.getMeso() >= 100000) {
		cm.gainItem(3994104, 20)
		cm.gainMeso(-100000);
	    } else {
		cm.sendOk("You arn't gonna fool me are you?");
	    }
	} else if (selection == 4) {
	    if (cm.canHold(3994105) && cm.getMeso() >= 100000) {
		cm.gainItem(3994105, 20)
		cm.gainMeso(-100000);
	    } else {
		cm.sendOk("You arn't gonna fool me are you?");
	    }
	}
	cm.dispose();
    }
}
