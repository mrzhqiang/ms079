var status = -1;

function start(mode, type, selection) {
	qm.sendNext("Find the Crimsonwood Keystone.");
	qm.forceStartQuest();
	if (!qm.isQuestActive(8223) && !qm.isQuestFinished(8223)) {
		qm.forceStartQuest(8223);
	}
	qm.dispose();
}
function end(mode, type, selection) {
	if (!qm.isQuestFinished(8223)) {
		qm.sendNext("Please, find it!");
	} else {
		qm.forceCompleteQuest();
		qm.sendNext("Good job. Now we can proceed.");
	}
	qm.dispose();
}
