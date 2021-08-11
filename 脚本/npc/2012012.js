var status = -1;

function action(mode, type, selection) {
	if (cm.getQuestStatus(3004) == 1) {
        cm.sendNext("完成任务。");
		cm.forceCompleteQuest(3004);
    } else {
        cm.sendNext("请问有什么事情吗??");
		cm.dispose();
    }   
}

