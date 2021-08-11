var status = -1;

function action(mode, type, selection) {
	cm.sendPlayerToNpc("Damien! Answer me!");
	cm.forceCompleteQuest(23201);
	cm.forceStartQuest(23202);
	cm.dispose();
}