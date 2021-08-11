var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("Oh, what? Aren't you going to have breakfast? Breakfast is the most important meal of the day! Talk to me again if you change your mind. If you don't, I'm going to eat it myself.");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("Did you feed #p1013102#? You should have some breakfast now then, Evan. Today's breakfast is a #t2022620#. I've brought it with me. Hee hee. I was going to eat it myself if you didn't agree to feed #p1013102#.");
	else if (status == 1)
		qm.sendAcceptDecline("Here, I'll give you this #bSandwich#k, so #bgo talk to mom when you finish eating#k. She says she has something to tell you.");
	else if (status == 2){
		qm.forceStartQuest();
		qm.PlayerToNpc("#b(Mom has something to say? Eat your #t2022620# and head back inside the house.)#k");
		qm.gainItem(2022620, 1);
	}else if (status == 3){
		qm.evanTutorial("UI/tutorial/evan/3/0" , 1);
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
		qm.sendNext("Did you eat your breakfast, Evan? Then, will you do me a favor?\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#i1003028# 1 #t1003028#  \r\n#i2022621# 5 #t2022621#s \r\n#i2022622# 5 #t2022622# \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 60 exp");
	 if (status == 1){
		qm.forceCompleteQuest();
		qm.evanTutorial("UI/tutorial/evan/4/0" , 1);
		qm.gainItem(1003028, 1);
		qm.gainItem(2022621, 5);
		qm.gainItem(2022622, 5);
		qm.gainExp(60);
		qm.dispose();
	}
}