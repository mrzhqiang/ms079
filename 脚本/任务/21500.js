var status = -1;

function start(mode, type, selection) {
	qm.sendNext("Haha.. come to #b#m140000000##k. I want to give you something...");
	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.sendNext("I finally acknowledge you as my owner. Please, take this skill. It is Echo of Hero.");
	qm.teachSkill(20001005,1,0);
	qm.forceCompleteQuest();
	qm.dispose();
}