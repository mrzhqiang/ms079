/*
	NPC Name: 		Grendel the really old
	Description: 		Quest - In search of the lost memory
*/
var status = -1;

function start(mode, type, selection) {
    if (mode == -1) {
	qm.dispose();
    } else {
	if (mode == 1)
	    status++;
	else
	    status--;
	if (status == 0) {
	    qm.sendNext("Oh my gosh, you've grown so much since we first met! You've lost your memories? I'll take care of that.");
	    qm.forceCompleteQuest();
	    qm.forceCompleteQuest(3507);
	    qm.dispose();
	}
    //	qm.forceStartQuest();
    }
}

function end(mode, type, selection) {
    if (mode == -1) {
	qm.dispose();
    } else {
	if (mode == 1)
	    status++;
	else
	    status--;
	if (status == 0) {
	    qm.sendNextPrev("Test");
	    qm.dispose();
	}
    }
}