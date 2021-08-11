
function action(mode, type, selection) {
    if (cm.isQuestActive(23005) && cm.haveItem(4032783)) {
	cm.sendNext("You pin the poster to the message board.");
	cm.forceStartQuest(23006, "1");
	cm.gainItem(4032783, -1);
    } else {
    	cm.sendOk("It's a message board for Edelstein''s Free Market. Supposedly, anyone can put up a poster, but the board is covered with propaganda about the Black Wings.");
    }
    cm.dispose();
}