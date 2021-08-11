var status = -1;

function action(mode, type, selection) {
    if (cm.isQuestActive(3900)) {
	cm.forceCompleteQuest(3900);
	cm.gainExp(300);
	cm.playerMessage(5, "You drank from the oasis.");
    }
    cm.dispose();
}