var status = -1;

function start(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
		qm.sendPlayerToNpc("Calm down! I just have to think. Okay, let's run down what's going on.");
	} else if (status == 1) {
		qm.sendPlayerToNpc("1. The rest of the Elves are still frozen, so the Black Mage's curse is still in place.");
	} else if (status == 2) {
		qm.sendPlayerToNpc("2. I'm the only one who's woken up. I don't know why, but I get the feeling that the Black Mage's seal is weakening.");
	} else if (status == 3) {
		qm.sendPlayerToNpc("3. I want to go outside and check on Maple World, but I'm only level 10. I can't believe it... Just how powerful was that curse? I'm still freezing!");
	} else if (status == 4) {
		qm.sendPlayerToNpc("Right, hold it together.. I need to make sure there's not anything wrong with me.");
	} else {
		qm.dispose();
	}
}
function end(mode, type, selection) {
	qm.forceCompleteQuest(29952);
	qm.dispose();
}
