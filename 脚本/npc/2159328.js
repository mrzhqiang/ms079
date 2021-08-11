var status = -1;

function action(mode, type, selection) {
	cm.sendPlayerToNpc("Mother! Where are you?!");
	cm.forceCompleteQuest(23200);
	cm.forceStartQuest(23201);
	cm.dispose();
}