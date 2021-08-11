var status = -1;

function action(mode, type, selection) {
    if (cm.isQuestActive(2215)) {
	cm.forceCompleteQuest(2215);
	cm.sendNext("Quest complete.");
    }
    cm.dispose();
}