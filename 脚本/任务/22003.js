var status = -1;

function start(mode, type, selection) {
    status++;
	if (mode != 1) {
	    if(type == 1 && mode == 0)
		    status -= 2;
		else{
			qm.sendNext("Good kids listen to their mothers. Now, Evan, be a good kid and talk to me again.");
			qm.dispose();
			return;
		}
	}
	if (status == 0)
		qm.sendAcceptDecline("Your #bDad#k forgot his Lunch Box when he left for the farm this morning. Will you #bdeliver this Lunch Box#k to your Dad in #b#m100030300##k, honey?");	
	else if (status == 1){
		qm.forceStartQuest();
		qm.sendNext("Heehee, my Evan is such a good kid! Head #bleft after you exit the house#k. Rush over to your dad. I'm sure he's starving.");
		if(!qm.haveItem(4032448))
			qm.gainItem(4032448, 1);
	}else if (status == 3)
		qm.sendNextPrev("Come back to me if you happen to lose the Lunch Box. I'll make his lunch again.");
	else if (status == 4){
		qm.evanTutorial("UI/tutorial/evan/5/0" , 1);
		qm.dispose();
	}
}

function end(mode, type, selection) {

}