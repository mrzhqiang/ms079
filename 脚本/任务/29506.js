var status = -1;

function start(mode, type, selection) {
	qm.sendNext("谢谢你完成此任务\r\n如果还会看此任务请重登。");
	//qm.gainItem(1142078, 1);
	qm.forceCompleteQuest();
	qm.dispose();
}

function end(mode, type, selection) {
	qm.forceCompleteQuest();
	qm.dispose();
}
