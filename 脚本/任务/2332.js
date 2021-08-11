var status = -1;

function start(mode, type, selection) {
	qm.getMap().killAllMonsters(true);
	qm.spawnMonster(3300008,1);
	qm.sendNext("Please, eliminate the Prime Minister!!!");
	qm.forceStartQuest(2333);
	qm.forceCompleteQuest();
	qm.dispose();
}

function end(mode, type, selection) {
		qm.gainItem(4032386,1);
		qm.forceCompleteQuest();
		qm.dispose();
}
	