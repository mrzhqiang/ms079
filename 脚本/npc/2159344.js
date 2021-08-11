var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
		cm.sendPlayerToNpc("Where am I? I don't recognize this place... Everything hurts, ugh.");
    } else if (status == 1) {
		cm.sendDirectionStatus(3, 1);
		cm.sendDirectionStatus(1, 2000);
		cm.sendPlayerToNpc("(...This looks like a Treatment Room. Where am I? What happened to me?.. I have to remember.)");
    } else if (status == 2) {
		cm.sendPlayerToNpc("(...The Black Mage broke his promise and destroyed the southern part of Ossyria, where my family was. He destroyed my home... Where is my locket?! Did I lose it? No...)");
	} else if (status == 3) {
		cm.sendPlayerToNpc("(I went to the Temple of Time to take revenge on the Black Mage... On the way, I let Mastema go, to get away from the Commanders. Arkarium tried to stop me, but I was determined... I wonder how the Heroes did.)");
	} else if (status == 4) {
		cm.sendPlayerToNpc("(...The Black Mage was too powerful to me, but I thought I could do at least a little damage. All I managed was tearing his robe.. How pathetic... But how did I survive?...The Heroes?)");
	} else if (status == 5) {
		cm.sendPlayerToNpc("(Ugh, a headache. I don't even know where I am now. Does this mean Maple World wasn't destroyed?)");
	} else if (status == 6) {
		cm.sendPlayerToNpc("(I should check on myself.. I'm going to need my Demon Fury no matter what, but how much is left?!)");
	} else if (status == 7) {
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg0/13");
		cm.sendPlayerToNpc("(No! My Demon Aegis is weak.. Almost all of my power and abilities are gone... I can hardly fight like this.)");
	} else if (status == 8) {
		cm.sendPlayerToNpc("I need to go. Sitting here won't accomplish anything.");
	} else if (status == 9) {
		cm.sendDirectionStatus(3, 1);
		cm.sendDirectionStatus(1, 2000);
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/3");
		cm.sendPlayerToNpc("(I hear something...)");
	} else if (status == 10) {
		cm.sendNextNoESC("The Energy Conducting Device was connected to an egg. While I was examining it, that person broke out of the egg and defeated all of the Black Wings. It was crazy!", 2159342);
	} else if (status == 11) {
		cm.sendNextNoESC("If it was anyone else telling me this, I would laugh. But this... who is this person?", 2159315);
	} else if (status == 12) {
		cm.sendPlayerToNpc("They're talking about me...");
	} else if (status == 13) {
		cm.sendNextNoESC("I've never seen skills like his. So powerful... If he hadn't used up all his power, I wouldn't have brought him here. He's dangerous. Is he a test subject? We must stop that blasted mad man Gelimer.\r\nIn any case, he should be up by now. I'll check on him.", 2159342);
	} else if (status == 14) {
		cm.sendDirectionStatus(3, 2);
		cm.sendDirectionStatus(1, 2000);
		cm.sendNextNoESC("Ah, you're awake. How do you feel? Tired?", 2159342);
	} else if (status == 15) {
		cm.sendPlayerToNpc("Did you save me?");
	} else if (status == 16) {
		cm.sendNextNoESC("Yes, you were badly wounded. I can't just leave you there with the Black Wings. I think we're on the same side. We have plenty to talk about, so how about you come with me?", 2159342);
	} else if (status == 17) {
		cm.sendPlayerToNpc("(Interrogation?... No...) Very well.");
	} else if (status == 18) {
		cm.warp(931050010,0);
		cm.dispose();
	}
}