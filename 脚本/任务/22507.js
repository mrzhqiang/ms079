var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 14) {
	    qm.sendNext("Uh, you're kidding me, right? Tell me your finger slipped! Go ahead and accept the quest.");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("I knew it! I knew we were connected, master! When you get stronger, I get stronger, too. And when I get stronger, you can use my strength! That's our pact. I knew I picked a good master!");
    } else if (status == 1) {
	qm.sendNextPrevS("#bI see. How did we end up in this pact anyway?#k", 2);
    } else if (status == 2) {
	qm.sendNextPrev("I don't know. I was just an egg. I can't really remember... though I faintly recall you, master, walking toward me in a foggy forest. I remember your surprise upon seeing me. And I was calling out to you in return.");
    } else if (status == 3) {
	qm.sendNextPrevS("#b(Wait! That sounds just like that one dream you had... Did the two of you meet in a dream? Is it possible that the giant Dragon you saw in that dream was...Mir?#k", 2);
    } else if (status == 4) {
	qm.sendNextPrev("Master, you and I are one in spirit. I knew it the moment I saw you. That's why I wanted to make the pact with you. No one else. You had to pay the price I set, of course.");
    } else if (status == 5) {
	qm.sendNextPrevS("#bI paid a price?#k", 2);
    } else if (status == 6) {
	qm.sendNextPrev("Don't you remember? When you recognized me and touched me? That was the one condition I set. The moment you touched my egg, you and I become one in spirit.");
    } else if (status == 7) {
	qm.sendNextPrevS("#One in...spirit?", 2);
    } else if (status == 8) {
	qm.sendNextPrev("Yes! The Spirit Pact! You and I have separate bodies, but we share one spirit. That's why you get stronger when I get stronger, and vice versa! Awesome, right? At least, I think so.");
    } else if (status == 9) {
	qm.sendNextPrevS("#bI have no idea what you're talking about, but it sounds like a pretty big deal.#k", 2);
    } else if (status == 10) {
	qm.sendNextPrev("Of course it's a big deal, silly master! You never have to worry about monster again. You have me to protect you now! Go ahead and test me. In fact, let's go right now!");
    } else if (status == 11) {
	qm.sendNextPrevS("#bBut it's peaceful here. There are no dangerous monster around.#k", 2);
    } else if (status == 12) {
	qm.sendNextPrev("WHAT?! That's no fun! Don't you like adventuring, master? Fighting monster on behalf of your people, defeating evil, rescuing the innocent, and all that? You're not into that kind of thing?");
    } else if (status == 13) {
	qm.sendNextPrevS("#bIt's not part of my five year plan. I'm just kidding, but seriously, I'm a farmer's son...#k", 2);
    } else if (status == 14) {
	qm.askAcceptDecline("Bah, well let me tell you this. It's impossible for a Dragon Master to live a peaceful life. I'll have plenty of chances to prove my skills. Trust me, our life will be one big adventure. Promise me that you'll stick with me, okay?");
    } else if (status == 15) {
	qm.forceStartQuest();
	qm.sendNextS("Hehehe, alright then, master. Let's get to it!", 1);
    } else if (status == 16) {
	qm.sendNextPrevS("#b(You're a bit confused, but you are now traveling with Mir the Dragon. Perhaps you'll go on an adventure together, like he said.)#k", 3);
    } else if (status == 17) {
	qm.sendPrevS("#b(You still have an errand to run. Your dad needs to talk to you, so go and see him now.)#k", 2);
	qm.dispose();
    }
}

function end(mode, type, selection) {
}