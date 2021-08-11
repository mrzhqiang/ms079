var status = -1;

function start(mode, type, selection) {
	if (qm.getPlayer().getCurrentRep() >= 0 && qm.getPlayer().getTotalRep() >= qm.getPlayer().getCurrentRep()) {
		qm.forceCompleteQuest();
		qm.gainExp(3000);
		qm.sendNext("棒极了！");
	} else {
		qm.sendNext("请用掉一些声望！");
	}
	qm.dispose();
}
function end(mode, type, selection) {
	if (qm.getPlayer().getCurrentRep() >= 0 && qm.getPlayer().getTotalRep() >= qm.getPlayer().getCurrentRep()) {
		qm.forceCompleteQuest();
		qm.gainExp(3000);
		qm.sendNext("棒极了！");
	} else {
		qm.sendNext("请用掉一些声望！");
	}
	qm.dispose();
}