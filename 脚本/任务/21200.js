var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    qm.sendNext("It's really urgent, and you'll regret it if you refuse to. #bIt has something to do with your pole arm,#k which means it has to do with your past. Who knows...? Maybe the pole arm is key to reawakening your abilities...?");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.askAcceptDecline("How's the training going? Wow, looking at you, I can tell your levels have shot through the roof. That's amazing... well, anyway, I see that you'r busy, but you'll have to return to the island for a bit.");
    } else if (status == 1) {
	qm.forceStartQuest(21200, "3"); //??
	qm.forceCompleteQuest();
	qm.forceStartQuest(21202); //skip just in case
	qm.forceStartQuest(21203, "0");
	qm.sendOk("Your #bGiant Pole Arm#k that's being kept in #bRien#k is acting strange all of a sudden. According to the book, the pole arm reacts like this when it's calling for its master. #bMaybe it's calling for you?#k? Please come back to the island and find out.");
	qm.dispose();
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 11) {
	    qm.sendNext("Hey, at least you tell me you tried!");
	    qm.dispose();
	    return;
	} else if (status == 13) {
	    qm.MovieClipIntroUI(true);
	    qm.warp(914090200, 0);
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNextS("Hmmmmmm mmmm mmmmm....", 2);
    } else if (status == 1) {
	qm.sendNextPrevS("#b(Giant Pole Arm is buzzing, but who's that boy standing there?)#k", 2);
    } else if (status == 2) {
	qm.sendNextPrevS("#b(I've never met him before. He doesn't look human.)#k", 2);
    } else if (status == 3) {
	qm.sendNextPrev("Hey Aran! Do you still not hear me? Seriously, can't you hear me? Ahhh, this is frustrating!");
    } else if (status == 4) {
	qm.sendNextPrevS("#b(Whoa, who was that? Sounds like an angry boy...)#k", 2);
    } else if (status == 5) {
	qm.sendNextPrev("Seriously, the one master I had turned out to be trapped in ice for hundreds of years, abandoning the weapon, and now the 'master' can't even hear me?");
    } else if (status == 6) {
	qm.sendNextPrevS("Who are you?", 2);
    } else if (status == 7) {
	qm.sendNextPrev("Aran? Do you hear me now? It's me, it's me! I'm your weapon #bMaha the pole arm!#k!");
    } else if (status == 8) {
	qm.sendNextPrevS("#b(...Maha? Giant pole Arm actually talks?)#k", 2);
    } else if (status == 9) {
	qm.sendNextPrev("Why do you have that look on your face like you can't believe it? I see that you have lost all your memories, but... did you also forget about me? How can you do that to me??");
    } else if (status == 10) {
	qm.sendNextPrevS("I'm sorry, but seriously... I don't remember a thing.", 2);
    } else if (status == 11) {
	qm.sendYesNo("Is that all you can say after all those years? I'm sorry? Do you understand how bored I was all by myself for hundreds of years? Bring it out if you can. Bring your memories out! Bring them all out! Dig them up if you need to!");
    } else if (status == 12) {
	qm.sendNextS("#b(The voice that claims to be Maha the Giant Pole Arm seem quite perturbed. This conversation is going nowhere. I better talk to Lirin first.)#k", 2);
	qm.forceCompleteQuest();
    } else if (status == 13) {
	qm.sendYesNo("Would you like to skip the video clip?  Even if you skip the scene, game play will not be affected.");
    } else if (status == 14) {
	qm.dispose();
    }
}