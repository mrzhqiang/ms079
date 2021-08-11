var status = -1;
//this quest is WHERES BOOK
function start(mode, type, selection) {
	qm.sendNext("Go talk to Icarus in Kerning City.");
	qm.forceStartQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.gainExp(500);
	qm.forceCompleteQuest();
	qm.dispose();
}