/*
	Description: 	Quest - Hungry Baby Dragon
*/

var status = -1;

function start(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 3) {
	    qm.sendNext("*gasp* How can you refuse to feed your Dragon? This is child abuse!");
	    qm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	qm.sendNext("Yo, master. Now that I've shown you what I can do, it's your turn. Prove to me...that you can find food! I'm starving. You can use my power now, so you have to take care of me.");
    } else if (status == 1) {
	qm.forceStartQuest();
	qm.sendNextPrevS("Eh, I still don't get what's going on, but I can't let a poor little critter like you starve, right? Food, you say? what do you want to eat?", 2);
    } else if (status == 2) {
	qm.sendNextPrev("Hi, I was just born a few minutes ago. How would I know what I eat? All I know is that I'm a Dragon... I'm YOUR Dragon. And you're my master. You have to treat me well!");
    } else if (status == 3) {
	qm.askAcceptDecline("I guess we're supposed to learn together. But I'm hungry. Master, I want food. Remember, I'm a baby! I'll start crying soon!");
    } else if (status == 4) {
	qm.forceStartQuest();
	qm.sendOkS("#b(Mir the baby Dragon appears to be extremely hungry. You must feed him. Maybe your Dad can give you advice on what dragons eat.)", 2);
    }
}

function end(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	qm.sendNext("What is it, Evan? You want to know what Dragons eat? Why do you... Huh? You found a Dragon?");
    } else if (status == 1) {
	qm.sendNextS("#b(You show Mir to Dad.)#k", 2);
    } else if (status == 2) {
	qm.sendNextPrev("Eh...that's a Dragon? Are you sure it's not just a big lizard? Well, all life is precious, so I guess you can keep it...");
    } else if (status == 3) {
	qm.sendNextS("#b(Dad doesn't seem to belive that Mir is a Dragon. Well, he is pretty small. Would Dad believe it if he heard Mir talk?)", 2);
    } else if (status == 4) {
	qm.sendNextPrev("If it were a real Dragon, it would be too dangerous to keep. What if it breathed fire? I don't really think it's a Dragon, but maybe we should ask an adventurer to come kill it, just in case.");
    } else if (status == 5) {
	qm.sendNextS("#b(What?! Kill Mir?! But he didn't do anything wrong!!)", 2);
    } else if (status == 6) {
	qm.sendNextPrev("Of course, I'm pretty sure it's not a Dragon. Dragons only appear in Leafre on the Ossyria Continent.");
    } else if (status == 7) {
	qm.sendNextS("#bHa... Ha... You're definitely right! I doubt he's a Dragon. He's probably just a lizard! Definitely!#k", 2);
    } else if (status == 8) {
	qm.sendNextPrev("Yeah, I'm pretty sure. It's a bizarre-looking lizard, but it doesn't look dangerous. Guess you can keep it.");
    } else if (status == 9) {
	qm.sendNextS("#b(For his own safety, you better not let anyone know that Mir is a Dragon.)#k", 2);
    } else if (status == 10) {
	qm.sendOk("Oh, you said you were looking for something to feed the lizard? I'm not sure... Let me think about it for a moment.");
    } else if (status == 11) {
	qm.gainExp(180);
	qm.forceCompleteQuest();
	qm.dispose();
    }
}