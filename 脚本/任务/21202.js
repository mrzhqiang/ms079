var status = -1;
var skills = Array(21001003, 21000000, 21100000, 21100002, 21100004, 21100005, 21110002);
//polearm booster, combo ability, polearm mastery, final charge, combo smash, combo drain, full swing

function start(mode, type, selection) {
	qm.sendNext("You want a pole arm? Hah! You don't look strong at all. Way outta your league. If you want a pole arm, prove me wrong by hunting #r#o9001012#s#k to the west of here, and find 30 #b#t4032311##k!");
	qm.forceStartQuest();
    qm.dispose();
}

function end(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		status--;
	}
	if (status == 0) {
		qm.sendNext("Hah! You have proven your worth.. and you shall get what you want; the best pole arm possible!");
	} else if (status == 1) {
	if (qm.getPlayerStat("RSP") > (qm.getPlayerStat("LVL") - 30) * 3) {
	    qm.sendNext("You still have way too much #bSP#k with you. You can't earn a new title like that. I strongly urge you to use more SP on your 1st and second level skills.");
	    qm.dispose();
	    return;
	}
		qm.sendNextS("My memories are returning...", 2);
		qm.changeJob(2110);
		qm.gainItem(1142130, 1);
		qm.gainItem(4032311, -30);
		qm.forceCompleteQuest(21201);
	for (var i = 0; i < skills.length; i++) {
		qm.teachSkill(skills[i], qm.getPlayer().getSkillLevel(skills[i]));
	}
		qm.forceCompleteQuest();
	} else if (status == 2) {
		qm.sendOk("Haha! You've got what you want, now leave!");
		qm.dispose();
	}
}