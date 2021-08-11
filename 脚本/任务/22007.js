
var status = -1;

function end(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("Hm? That's strange. The Incubator wasn't installed properly. Try again.");
		    qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("Oh, did you bring the #t4032451#? Here, give it to me. I'll give you the Incubator then.");
	if (status == 1)
		qm.sendYesNo("Alright, here you go. I have no idea how you use it, but it's yours... \r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 360 exp");
	if (status == 2){
		qm.forceCompleteQuest();
		qm.gainExp(360);
		if (qm.haveItem(4032451)) {
			qm.gainItem(4032451, -1);
		}
		qm.evanTutorial("UI/tutorial/evan/9/0" , 1);
		qm.dispose();
		}
	}