var status = -1;
function action(mode, type, selection) {
    if (mode == 1) 
        status++;
    else 
	status--;
    if (status == 0) {
    	cm.sendNextS("Looks like we lost him. Of course, I could''ve easily handled him, no problemo, but I wasn''t sure I protect you kiddos at the same time. *chuckle* What''re you two doing here anyway? Didn''t your parents warn you to steer clear of the mines?", 8);
    } else if (status == 1) {
	cm.sendNextPrevS("It''s my fault! #h0# was just trying to help! #h0# rescued me!", 4, 2159007);
    } else if (status == 2) {
	cm.sendNextPrevS("Rescued you, eh? Hm, you are dressed kind of funny, little girl. Ooooh. Were you a prisoner of the Black Wings?", 8);
    } else if (status == 3) {
	cm.sendNextPrevS("#b(Vita quickly explains the situation.)#k", 4, 2159007);
    } else if (status == 4) {
	cm.sendNextPrevS("But who are you? Where did you come from? And why did you rescue us?", 2);
    } else if (status == 5) {
	cm.sendNextPrevS("I am a proud member of the Resistance, a group secretly fighting and undermining the Black Wings. I cannot tell you who I am, but I go by the codename of J.", 8);
    } else if (status == 6) {
	cm.sendNextPrevS("Now, please return to town and stay away from the mines. As for you, Vita, come with me. If you''re left unprotected, I fear the Black Wings will come look for you. No one can keep you safe like I can! Now, keep my words a secret. The fate of the Resistance depends on your discretion.", 8);
    } else if (status == 7) {
	cm.sendNextPrevS("Wait, before you go, tell me one thing. How can I join the Resistance?", 2);
    } else if (status == 8) {
	cm.sendNextPrevS("Ah, little youngling, so you wish to fight the Black Wings, do you? Your heart is noble, but there is little you can do to aid our efforts until you reach Lv. 10. Do so, and I will have someone from the Resistance contact you. That''s a promise, kiddo. Now, I must be off, but perhaps we will meet again someday!", 8);
    } else if (status == 9) {
	cm.forceCompleteQuest(23007);
	cm.gainItem(2000000,3);
	cm.gainItem(2000003,3);
	cm.gainExp(90);
	cm.MovieClipIntroUI(false);
	cm.warp(310000000,8);
    	cm.dispose();
    }
}