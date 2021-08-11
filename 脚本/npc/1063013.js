var status = -1;

function action(mode, type, selection) {
    if (cm.isQuestActive(2236)) {
	cm.forceCompleteQuest(2236);
	cm.removeAll(4032263);
	cm.sendOk("Quest completed.");
    }
    cm.dispose();
}