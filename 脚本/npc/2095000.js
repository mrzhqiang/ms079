function action(mode, type, selection) {
    if (cm.getQuestStatus(6410) == 1) {
	cm.forceStartQuest(6411, "p2");
	cm.sendNext("Thank you!");
    } else {
	cm.sendNext("Please, free the monsters!");
    }
    cm.dispose();
}