
var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("You don't believe me? Grrrrr, you're getting me mad!");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("I'm finally here! *inhales* Ah, this must be air I'm breathing. And that, that must be the sun! And that, a tree! And that, a plant! And that, a flower! Woohahahaha! This is incredible! This is much better than I imagined the world to be while I was trapped inside the egg. And you... Are you my master? Hm, I pictured you differently.");
	if (status == 1)
		qm.PlayerToNpc("#bWhoooooa, it talks!");
	if (status == 2)
		qm.sendNextPrev("My master is strange. I guess I can't do anything about it now, since the pact has been made. *sigh* Well, good to meet you. We'll be seeing a lot of each other.");
	if (status == 3)
		qm.PlayerToNpc("#bEh? What do you mean? We'll be seeing a lot of each other? What pact?");
	if(status == 4)
		qm.sendNextPrev("What do you mean what do I mean?! You woke me from the Egg. You're my master! So of course it's your responsibility to take care of me and train me and help me become a strong Dragon. Obviously!");
	if (status == 5)
		qm.PlayerToNpc("#bWhaaat? A Dragon? You're a Dragon?! I don't get it... Why am I your master? What are you talking about?");
	if (status == 6)
		qm.sendNextPrev("What are YOU talking about? Your spirit made a pact with my spirit! We're pretty much the same person now. Do I really have to explain? As a result, you've become my master. We're bound by the pact. You can't change your mind... The pact cannot be broken.");
	if (status == 7)
		qm.PlayerToNpc("#bWait, wait, wait. Let me get this straight. You're saying I have no choice but to help you?");
	if (status == 8)
		qm.sendNextPrev("Yuuup! Heeeey...! What's with the face? You...don't want to be my master?");
	if (status == 9)
		qm.PlayerToNpc("#bNo... It's not that... I just don't know if I'm ready for a pet.");
	if (status == 10)
		qm.sendNextPrev("A p-p-pet?! Did you just call me a pet?! How dare... Why, I'm a Dragon! The strongest being in the world!");
	if (status == 11)
		qm.PlayerToNpc("#b...#b(You stare at him skeptically. He looks like a lizard. A puny little one, at that.)#k");
	if (status == 12)
		qm.sendAcceptDecline("Why are you looking at me like that?! Just watch! See what I can do with my power. Ready?");
	if (status == 13){
		qm.forceStartQuest();
		qm.sendNext("Command me to slay the #r#o1210100#s#k! Do it now! I'll show you how fast a Dragon can defeat the #o1210100#s! Goooo, charge!");
	}if (status == 14)
		qm.sendNextPrev("Wait a minute! Did you distribute your AP? I'm heavily affected by my master's #bINT and LUK#k! If you really want to see what I can do, distribute your AP and #bequip your Magician equipment#k before you use the skill!");
	if (status == 15){
		qm.evanTutorial("UI/tutorial/evan/11/0", -1);
		qm.dispose();
	}
}

function end(mode, type, selection) {
   	status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
		    qm.dispose();
			return;
		}
	}
	if(status == 0)
		qm.sendOk("Ha! What do you think of that?! My skills are amazing, right? You can use them as much as you want. That's what it means to be in a pact with me. Isn't it amazing?");
	if(status == 1){
		qm.forceCompleteQuest();
		qm.gainExp(1270);
		qm.getPlayer().gainSP(1, 0);
		qm.sendOk("Ohhh... I'm so hungry. I used my energy too soon after being born...");
		qm.dispose();
	}
}