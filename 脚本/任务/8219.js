var status = -1;

function start(mode, type, selection) {
	qm.sendNext("Find my bro.");
	qm.forceStartQuest();
	qm.dispose();
}
function end(mode, type, selection) {
	qm.sendNext("...");
	qm.gainItem(3992040,1);
	qm.gainExp(175000);
	qm.forceCompleteQuest();
	qm.dispose();
}
