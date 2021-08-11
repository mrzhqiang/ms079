var status = -1;

function start(mode, type, selection) {
	qm.sendNext("Find 10 Gold Ore, 4 Typhon Feather, 1 Power Crystal Ore.");
	qm.forceStartQuest();
	qm.dispose();
}
function end(mode, type, selection) {
	qm.dispose();
}
