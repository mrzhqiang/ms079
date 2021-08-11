var status = -1;
function action(mode, type, selection) {
    if (mode == 1) 
        status++;
    else 
	status--;
    if (status == 0) {
    	cm.sendNext("The experiment is going well, quite well. The endless supply of Rue is certainly speeding things along. Joining the Black Wings was a wise decision, a wise decision indeed. Muahaha!");
    } else if (status == 1) {
	cm.sendNextPrevS("I say, you have great foresight about these things.", 4, 2159008);
    } else if (status == 2) {
	cm.sendNextPrev("The android the Black Wings wanted will be completed soon. Oh yes, very soon. Then, the next stage will begin! I will conduct an experiment wilder than their wildest dreams!");
    } else if (status == 3) {
	cm.sendNextPrevS("Pardon? The next stage?", 4, 2159008);
    } else if (status == 4) {
	cm.sendNextPrev("Teeheehee, do you still not comprehend what I''m trying to create? Look around! Here''s a clue: it''s eons more interesting than a simple android. Eons more interesting.");
    } else if (status == 5) {
	cm.sendNextPrevS("What?? All these test subjects... I say, sir, just what are you planning to do?", 4, 2159008);
    } else if (status == 6) {
	cm.sendNextPrev("Now, now, you may not understand the grandness of my experiments. I don''t expect you to. No, I don''t expect you to. Just focus on your job and make sure none of the test subjects run away.");
    } else if (status == 7) {
	cm.sendNextPrev("Hey... Did you hear that?");
    } else if (status == 8) {
	cm.sendNextPrevS("Huh? Well... Now that you mention it, I do hear something. Yes, I do hear something...", 4, 2159008);
    } else if (status == 9) {
	cm.updateInfoQuest(23007, "vel00=2;vel01=1");
	cm.trembleEffect(0,500);
	cm.MovieClipIntroUI(true);
	cm.showWZEffect("Effect/Direction4.img/Resistance/TalkInLab");
    	cm.dispose();
    }
}