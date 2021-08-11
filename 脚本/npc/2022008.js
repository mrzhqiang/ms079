function action(mode, type, selection) {
    cm.sendNext("This is Rex, sealed up.");
    if (cm.isQuestActive(3122)) {
	cm.forceStartQuest(3122, "1");
    }
    cm.dispose();
}