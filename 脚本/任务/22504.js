/*
	Description: 	Quest - Tasty Milk 1
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    qm.sendNext("No use trying to find an answer to this on my own. I'd better look for #bsomeone older and wiser than master!#k");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("Ugh. This isn't going to work. I need something else. No plants. No meat. What, you have no idea? But you're the master, and you're older than me, too. You must know what'd be good for me!");
    } else if (status == 1) {
	qm.sendNextPrevS("#But I don't. It's not like age has anything to do with this...", 2);
    } else if (status == 2) {
	qm.askAcceptDecline("Since you're older, you must be more experienced in the world, too. Makes sense that you'd know more than me. Oh, fine. I'll ask someone who's even older than you, master!");
    } else if (status == 3) {
	qm.forceStartQuest();
	qm.sendOkS("#b(You already asked Dad once, but you don't have any better ideas. Time to ask him again!)#k", 2);
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
	qm.sendOk("What? You're still trying to feed that lizard? Eh, so it won't eat the Handful of Hay or the Pork? Picky little fellow. Oh? The lizard is still a baby?");
    } else if (status == 1) {
	qm.gainExp(260);
	qm.forceCompleteQuest();
	qm.dispose();
    }
}