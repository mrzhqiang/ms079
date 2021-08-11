var status = -1;

function start(mode, type, selection) {
	qm.sendNext("我不会放弃的！");
	qm.gainExp(2000);
	qm.forceCompleteQuest();
	qm.dispose();
}
function end(mode, type, selection) {
	qm.dispose();
}
