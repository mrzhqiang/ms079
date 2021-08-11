
var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("Stop being lazy. Do you want to see your brother bitten by a dog? Hurry up! Talk to me again and accept the quest!");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendNext("Haha. I had a good laugh. Hahaha. But enough with that nonsense. Feed #p1013102#, would you?");
	else if (status == 1)
		qm.PlayerToNpc("#bWhat? That's Utah's job!#k");
	else if (status == 2)
		qm.sendAcceptDecline("You little brat! I told you to call me Older Brother! You know how much #p1013102# hates me. He'll bite me if I go near him. You feed him. He likes you.");
	else if (status == 3){
		qm.gainItem(4032447,1);
		qm.sendNext("Hurry up and head #bleft#k to feed #b#p1013102##k. He's been barking to be fed all morning.");
		qm.forceStartQuest();
   }else if (status == 4){
		qm.sendPrev("Feed #p1013102# and come back to see me.");
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
		qm.sendNext("#b(You place food in Bulldog's bowl.)#k");
	if (status == 1)
		qm.sendOk("#b(Bulldog is totally sweet. Utah is just a coward.)#k\r\n\r\n#fUI/UIWindow.img/QuestIcon/8/0# 35 exp");
	if (status == 2){
		qm.forceCompleteQuest();
		qm.gainItem(4032447, -1);
		qm.gainExp(35);
		qm.sendOk("#b(Looks like Bulldog has finished eating. Return to Utah and let him know.)#k");
		qm.dispose();
		}
	}