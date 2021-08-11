var status = -1;

function start(mode, type, selection) {
	qm.sendOk("Go to #bChief Tatamo#k in Leafre and bring back a Ancient Dragon Wing Scale.");
	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
    	status++;
	if (status == 0) {
		if (qm.haveItem(4032969,1)) {
			qm.sendNext("Great! Please wait till I mix these ingredients together...");
		} else {
			qm.sendOk("Please go to #bChief Tatamo#k of Leafre and bring back an Ancient Dragon Wing Scale.");
			qm.forceStartQuest();
			qm.dispose();
		}
	} else {
		qm.teachSkill(80001089, 1, 0); // Maker
		qm.removeAll(4032969);
		qm.sendOk("There we go! You have learned the Soaring skill and will be able to fly, using great amounts of MP.");
		qm.forceCompleteQuest();
		qm.dispose();
	}
}
	