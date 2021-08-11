var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("Hm, #p1013101# would have done it at the drop of a hat.");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("The #o1210100#s at the farm have been acting strange these past couple days. They've been angry and irritable for no reason. I was worried so I came out to the farm early this morning and sure enough, it seems like a few of these #o1210100#s got past the fence.");
	else if (status == 1)
		qm.sendAcceptDecline("Before I go and find the #o1210100#s, I should mend the broken fence. Luckily, it wasn't damaged too badly. I just need a few #t4032498#es to fix it right up. Will you bring me #b3#k #b#t4032498##k, Evan?");
	else if (status == 2){
		qm.forceStartQuest();
		qm.sendNext("Oh, that's very nice of you. You'll be able to find #b#t4032498#es#k from the nearby #r#o0130100#s#k. They're not too strong, but use your skills and items when you find yourself in danger.");
	}else if (status == 3){
		qm.evanTutorial("UI/tutorial/evan/6/0", 1);
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
			qm.sendNext("Ah, did you bring all the #t4032498#  That's my kid! What shall I give you as a reward... Let's see... Oh, right! \r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0# \r\n#i3010097# 1 #t3010097# \r\n#i2022621# 15 #t2022621#s \r\n#i2022622# 15 #t2022622#s \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 210 exp");
	if (status == 1){
		qm.forceCompleteQuest();
		qm.gainItem(4032498, -3);
		qm.gainItem(3010097, 1);
		qm.gainItem(2022621, 15);
		qm.gainItem(2022622, 15);
		qm.gainExp(210);
		qm.sendNextPrev("Here, I made this new chair from the wooden boards I had left over after fixing the fence. It may not seem like much, but it's sturdy. I'm sure it'll come in handy.");
		}
	if(status == 2){
		qm.evanTutorial("UI/tutorial/evan/7/0", 1);
		qm.dispose();
	}
}