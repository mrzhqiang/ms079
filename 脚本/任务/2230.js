var status = -1;

function start(mode, type, selection) {
}
function end(mode, type, selection) {
	qm.sendNext("Snail Roon is no longer available.");
	qm.forceCompleteQuest();
	qm.dispose();
/*	if (mode == -1) {
		qm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		
		if (status == 0)
			qm.sendSimple("Hello, traveler... You have finally come to see me. Have you fulfilled your duties? \r\n #b#L0#What duties? Who are you?#l#k");
		else if (status == 1) {
			qm.sendNext("Have you found a small egg in your pocket? That egg is your duty, your responsibility. Life is hard when you're all by yourself. In times like this, there's nothing quite like having a friend that will be there for you at all times. Have you heard of a #bpet#k?\r\nPeople raise pets to ease the burden, sorrow, and loneliness, because knowing that you have someone, or something in this matter, on your side will really bring a peace of mind. But everything has consequences, and with it comes responsibility...");
		} else if (status == 2) {
			qm.sendNextPrev("Raising a pet requires a huge amount of responsibility. Remember a pet is a form of life, as well, so you'll need to feed it, name it, share your thoughts with it, and ultimately form a bond. That's how the owners get attached to these pets.");
		} else if (status == 3) {
			qm.sendNextPrev("I wanted to instill this in you, and that's why I sent you a baby that I cherish. The egg you have brought is #bRune Snail#k, a creature that is born through the power of Mana. Since you took great care of it as you brought the egg here, the egg will hatch soon.");
		} else if (status == 4) {
			qm.sendNextPrev("Rune Snail is a pet of many skills. It'll pick up items, feed you with potions, and do other things that will astound you. The downside is that since Rune Snail was born out of power of Mana, it's lifespan is very short. Once it turns into a doll, it'll never be able to be revived.");
		} else if (status == 5) {
			qm.sendYesNo("Now do you understand? Every action comes with consequences, and pets are no exception. The egg of the snail shall hatch soon.");
		} else if (status == 6) {
			qm.gainPet(5000054, "Snail Roon", 1, 0, 100, 18000); // rune snail * 1
			if (qm.haveItem(4032086,1)) {
				qm.gainItem(4032086, -1); // Mysterious Egg * -1
			}
			qm.forceCompleteQuest();
			qm.sendNext("This snail will only be alive for #b5 hours#k. Shower it with love. Your love will be reciprocated in the end.");
			qm.dispose();
		}
	}
*/
}
