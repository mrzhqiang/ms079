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
		cm.sendNextNoESC("Commander! Where were you? I thought maybe Arkarium had actually made a move against you...");
    } else if (status == 1) {
		cm.sendNextNoESC("Things are strange these days. Arkarium has it for you, since you were the one who managed to catch the Goddess of the Temple of Time. He only managed to blind her, but he still thinks he deserves all the credit...Fool.");
    } else if (status == 2) {
		cm.sendDirectionStatus(3, 2);
		cm.sendNextNoESC("...Are you okay? You seem different... Yeah, you are. You used to scold me when I asked you such things, but now... Hey, you don't look so good. Did something happen? Are you hurt?");
	} else if (status == 3) {
		cm.sendPlayerToNpc("...Tell me, Mastema, Who do you serve? Is it me, or the Black Mage?");
	} else if (status == 4) {
		cm.sendNextNoESC("W-wha?");
	} else if (status == 5) {
		cm.sendPlayerToNpc("Answer me!");
	} else if (status == 6) {
		cm.sendNextNoESC("Well, I'm loyal to the Black Mage, of course. But we pledged our lives for each other. I go where you go.");
	} else if (status == 7) {
		cm.sendPlayerToNpc("I have a favor to ask of you then... Give this letter to the #rHeroes#k.");
	} else if (status == 8) {
		cm.sendNextNoESC("Do what?! Why? What are you thinking? Are you trying to make things worse? You're finished as a Commander once anyone finds out you're trying to communicate with the Heroes!");
	} else if (status == 9) {
		cm.sendPlayerToNpc("I am already finished as a Commander.");
	} else if (status == 10) {
		cm.sendNextNoESC("What? Are you betraying the Black Mage? Why are you doing this?");
	} else if (status == 11) {
		cm.sendPlayerToNpc("There's no time to explain. Please do as I say. If not...");
	} else if (status == 12) {
		cm.sendNextNoESC("No, I'll do it. I'm just worried. What about your family? Wont' they be in dang-");
	} else if (status == 13) {
		cm.sendPlayerToNpc("Not another word about my family!");
	} else if (status == 14) {
		cm.sendNextNoESC("What? Did something happen to them already?");
	} else if (status == 15) {
		cm.sendPlayerToNpc("....");
	} else if (status == 16) {
		cm.sendNextNoESC("I see... You've always been the quiet type, but sometimes silence speaks for itself. Very well. I'll give this letter to the Heroes.");
	} else if (status == 17) {
		cm.sendPlayerToNpc("Thank you. Sorry for asking such a task of you.");
	} else if (status == 18) {
		cm.sendNextNoESC("Don't be sorry. I owe you my life, after all. Alright, I'm going. Good luck.");
	} else if (status == 19) {
		cm.removeNpc(2159307);
		cm.sendDirectionStatus(1, 720);
		cm.sendPlayerToNpc("...Your loyalty means so much to me. Thank you.");
	} else if (status == 20) {
		cm.sendDirectionStatus(3, 2);
		cm.sendDirectionStatus(4, 0);
		cm.warp(927000080,0);
		cm.dispose();
	}
}