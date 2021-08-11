var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("Are you scared of the #o9300385#es? Don't tell anyone you're related to me. That's shameful.");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendAcceptDecline("It's strange. The chickens are acting funny. They used to hatch way more #t4032451#s. Do you think the Foxes have something to do with it? If so, we better hurry up and do something.");
	else if (status == 1){
		qm.forceStartQuest();
		qm.sendNext("Right? Let us go and defeat those Foxes. Go on ahead and defeat #r10 #o9300385#es#k in #b#m100030103##k first. I'll follow you and take care of what's left behind. Now, hurry over to #m100030103#!");
	} else if (status == 2){
		qm.evanTutorial("UI/tutorial/evan/10/0", 1);
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
	if (status == 0)
		qm.sendNext("Did you defeat the Cunning Foxes?");
	if (status == 1)
		qm.PlayerToNpc("#bWhat happened to slaying the Foxes left behind?");
	if (status == 2)
		qm.sendNextPrev("Oh, that? Haha. I did chase them, sort of, but I wanted to make sure that they do not catch up to you. I wouldn't want you eaten by a #o9300385# or anything. So I just let them be.");
	if (status == 3)
		qm.PlayerToNpc("#bAre you sure you weren't just hiding because you were scared of the Foxes?");
	if (status == 4)
		qm.sendNextPrev("What? No way! Sheesh, I fear nothing!");
	if (status == 5)
		qm.PlayerToNpc("#bWatch out! There's a #o9300385# right behind you!");
	if (status == 6)
		qm.sendNextPrev("Eeeek! Mommy!");
	if (status == 7)
		qm.PlayerToNpc("#b...");
	if (status == 8)
		qm.sendNextPrev("...");
	if (status == 9)
		qm.sendNextPrev("You little brat! I'm your older brother. Don't you mess with me! Your brother has a weak heart, you know. Don't surprise me like that!");
	if (status == 10)
		qm.PlayerToNpc("#b(This is why I don't want to call you Older Brother...)");
	if (status == 11)
		qm.sendNextPrev("Hmph! Anyway, I'm glad you were able to defeat the #o9300385#es. As a reward, I'll give you something an adventurer gave me a long time ago. Here you are. \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#i1372043# 1 #t1372043# \r\n#i2022621# 25 #t2022621# \r\n#i2022622# 25 #t2022622#s \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 910 exp");
	if (status == 12){
		qm.forceCompleteQuest();
		qm.gainItem(1372043, 1);
		qm.gainItem(2022621, 25);
		qm.gainItem(2022622, 25);
		qm.gainExp(910);
		qm.sendNextPrev("#bThis is a weapon that Magicians use. It's a Wand#k. You probably won't really need it, but it'll make you look important if you carry it around. Hahahahaha.");
	} if (status == 13){
		qm.sendPrev("Anyway, the Foxes have increased, right? How weird is that? Why are they growing day by day? We should really look into it and get to the bottom of this.");
		qm.dispose();
		}
	}