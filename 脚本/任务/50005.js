/*
	NPC Name: 		Asia
	Description: 		Quest - A rush of Core Blaze
*/

var status = -1;

function start(mode, type, selection) {
    qm.dispose();
}

function end(mode, type, selection) {
    if (qm.getQuestStatus(50005) == 0) {
	qm.forceStartQuest();
	qm.dispose();
    } else {
	if (mode == 1) {
	    status++;
	} else {
	    status--;
	}
	if (status == 0) {
	    qm.sendNext("(I re-entered Shibuya of 2102. What I see down there is... #p9120033#!) \n......You are......!");
	} else if (status == 1) {
	    qm.sendNextPrev("...Ah, so you're sent here to defeat #o9400296#. Seriously, I'm so sorry...\n(As he uttered those words, #p9120033# couldn't bear looking at me in the eye)");
	} else if (status == 2) {
	    qm.sendNextPrev("The enemy headquarters is located at the center of Roppongi, at #bRoppongi Mall#k. Of course, you won't be able to enter the face-on. At the main lobby, an army of robots called #o9400287# stand by, serving as security. Your first task is to enter the building wile faking those robots out.\n(As he mentioned these, #p9120033# handed me a single copy of globe.)");
	} else if (status == 3) {
	    qm.sendNextPrev("The truth is, there have been underground pathways that leads from Shibuya to Roppongi Mall. Using this will allow you to enter the building without being noticed by #o9400287#. This is the map that'll lead you to there. It's just a straight line, so you should have no trouble entering there, but I'll give you the map nontheless.");
	} else if (status == 4) {
	    qm.sendOk("Please head over to Shibuya in year 2102, and use the undergroud path to enter the mall. Since the mall is the headquarters, you may encounter a number of monsters you wil have never seen. Please do not underestimate them. Good luck!");
	    qm.forceCompleteQuest();
	    qm.safeDispose();
	}
    }
}