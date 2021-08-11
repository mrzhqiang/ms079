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
		cm.sendNextNoESC("You r-r-really have wings...");
    } else if (status == 1) {
		cm.sendNextNoESC("Who are you? Are you part of the Black Wings? A spy? Actually, wait, no. That doesn't make sense...", 2159312);
    } else if (status == 2) {
		cm.sendNextNoESC("Keep your guard up. We are still clueless...", 2159313);
	} else if (status == 3) {
		cm.sendNextNoESC("Who are you? What is your relationship with the Black Wings?", 2159315);
	} else if (status == 4) {
		cm.sendPlayerToNpc("I have no idea who these Black Wings are. What do you want to know about me? I'm not even sure where to start.");
	} else if (status == 5) {
		cm.sendNextNoESC("Let's start with your name, organization, background... and those wings on your back.", 2159342);
	} else if (status == 6) {
		cm.sendPlayerToNpc("My name is #h0#. I am not currently part of any organization, though I was once one of the Black Mage's Commanders. I rebelled against him, and we fought, but he defeated me. When I awoke, I saw the Black Wings. Oh, and I was born with these wings. I am a demon.");
	} else if (status == 7) {
		cm.sendNextNoESC("You were a commander under the Black Mage? How? He's been sealed for hundreds of years!", 2159315);
	} else if (status == 8) {
		cm.sendDirectionStatus(1, 2000);
		cm.sendDirectionInfo("Effect/Direction6.img/effect/tuto/balloonMsg1/3");
		cm.sendNextNoESC("Hmm.. he might be delusional.");
	} else if (status == 9) {
		cm.sendPlayerToNpc("(Hundreds of years ago? But, this place is so strange. How long have I been asleep? Could the Heroes have sealed the Black Mage?)");
	} else if (status == 10) {
		cm.sendNextNoESC("This makes no sense. Are you lying?");
	} else if (status == 11) {
		cm.sendNextNoESC("No. He may be crazy, but he's no liar.", 2159345);
	} else if (status == 12) {
		cm.sendNextNoESC("So... He's either crazy or telling the truth. Black Jack is never wrong.", 2159316);
	} else if (status == 13) {
		cm.sendNextNoESC("He's somehow from the past, before the Black Mage was sealed. Why did you rebel, if you were a Commander?", 2159315);
	} else if (status == 14) {
		cm.sendPlayerToNpc("That's personal. Now, you answer my questions. Who are you people? Who are the Black Wings?");
	} else if (status == 15) {
		cm.sendNextNoESC("We're the resistance, a group formed in secret to protect our home, Edelstein, from the Black Wings. Those nasty folks stealing your energy were the Black Wings. They have been draining energy from the city since a while back, and they work for the Black Mage.", 2159342);
	} else if (status == 16) {
		cm.sendPlayerToNpc("They follow the Black Mage? Isn't he sealed?");
	} else if (status == 17) {
		cm.sendNextNoESC("He is, but they're trying to release him again.", 2159342);
	} else if (status == 18) {
		cm.sendPlayerToNpc("The Black Mage is returning? That's excellent news... That means I can still have my revenge.");
	} else if (status == 19) {
		cm.sendNextNoESC("You're kinda crazy, but we're on the same side. Why don't you join us?", 2159342);
	} else if (status == 20) {
		cm.sendNextNoESC("What are you saying?! You really believe him? Even if he was telling the truth, he was a Commander!", 2159315);
	} else if (status == 21) {
		cm.sendNextNoESC("He seems to hate the Black Mage as much as we do, if not more. Even if he #bused to be#k a Commander, he is not one any more. We can always use more members, as long as our goals are the same. We can work together.", 2159342);
	} else if (status == 22) {
		cm.sendPlayerToNpc("Wait, what's going on?");
	} else if (status == 23) {
		cm.sendNextNoESC("There's no need to catch up. The decision has been made. If you want to fight the Black Mage, you have to go through the Black Wings. Let's work together to bring them down.");
	} else if (status == 24) {
		cm.sendNextNoESC("I don't expect you to fully trust us yet, but we can work on that, piece by piece, as we take the Black Wings apart.", 2159342);
	} else if (status == 25) {
		cm.sendPlayerToNpc("True. I will join you, for now... Allow me to thank you for saving me, as well.");
	} else if (status == 26) {
		cm.sendNextNoESC("Hearing that is a relief. You're welcome.", 2159342);
	} else if (status == 27) {
		cm.sendPlayerToNpc("I am loyal to those loyal to me.");
	} else if (status == 28) {
		cm.sendNextNoESC("Works for me. All right, please make yourself at home.", 2159315);
	} else if (status == 29) {
		cm.EnableUI(0);
		cm.DisableUI(false);
		cm.sendDirectionStatus(4, 0);
		cm.forceStartQuest(23209, "1");
		cm.forceCompleteQuest(23279);
		cm.forceCompleteQuest(7621);
		cm.forceCompleteQuest(29958);
		cm.gainItem(1142341, 1);
		cm.getPlayer().changeJob(3100);
		cm.warp(310010000,0);
		cm.dispose();
	}
}