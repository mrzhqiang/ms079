var status = -1;

function start(mode, type, selection) {
	qm.sendOk("Go to #bChief Tatamo#k in Leafre and bring back a Dragon Moss Extract.");
	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
    	status++;
	if (status == 0) {
		if (qm.haveItem(4032531,1)) {
			qm.sendNext("Great! Please wait till I mix these ingredients together...");
		} else {
			qm.sendOk("Please go to #bChief Tatamo#k of Leafre and bring back a Dragon Moss Extract.");
			qm.forceStartQuest();
			qm.dispose();
		}
	} else {
		qm.teachSkill(qm.getPlayer().getStat().getSkillByJob(1026, qm.getPlayer().getJob()), 1, 0); // Maker
		qm.gainExp(11000);
		qm.removeAll(4032531);
		qm.sendOk("There we go! You have learned the Soaring skill and will be able to fly, using great amounts of MP.");
		qm.forceCompleteQuest();
		qm.dispose();
	}
}
	