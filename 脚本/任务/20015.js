/*
	NPC Name: 		Cygnus
	Description: 		Quest - Encounter with the Young Queen
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendNext("You may be hesitant now, but I can see an incredible amount of courage behind your eyes. Close your eyes and feel the courage and passion inside of you.");
	    qm.safeDispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("Did you know? Maple World may look peaceful, but certain areas are filled with forces of darkness. The Black Mage and those who want to revive the Black Mage are threatening Maple World.");
    } else if (status == 1) {
	qm.sendNextPrev("We can't just sit here and do nothing while our enemies get stronger. Our own fear will only come back to haunt us.");
    } else if (status == 2) {
	qm.askAcceptDecline("But I won't worry too much. Someone as determined as you will be able to protect Maple World from danger, right? If you are brave enough to volunteer to become one of the Knights, I know I can count on you. \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#i1142065# #t1142065# - 1");
    } else if (status == 3) {
	if (qm.getQuestStatus(20015) == 0) {
	    qm.gainItem(1142065, 1);
	    qm.forceCompleteQuest();
	}
	qm.sendNext("Heehee, I knew you'd say that. But you know you still have a ways to go before you can fight for Maple World, right?");
    } else if (status == 4) {
	qm.sendPrev("#p1101002#, my Tactician, who is standing right next to me, will help you become an honorable Knight. I'll be looking forward to your progress. I'm counting on you!");
	qm.safeDispose();
    }
}

function end(mode, type, selection) {
}