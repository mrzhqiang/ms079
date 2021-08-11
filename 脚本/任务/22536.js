var status = -1;
//this quest is NELLA INVESTIGATION
function start(mode, type, selection) {
	qm.sendNext("Let's talk to Nella of Kerning City.");
	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.gainExp(4000);
	qm.forceCompleteQuest();
	qm.dispose();
}