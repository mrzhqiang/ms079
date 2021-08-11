function start() {
	var num = cm.getMap().getNumPlayersInArea(0);
	if (num == cm.getMap().getCharactersThreadsafe().size()) {
		cm.playerMessage(5, "The doors have been unlocked.");
		if (cm.getPlayer().getEventInstance() != null) {
			cm.getPlayer().getEventInstance().setProperty("stage8", "0");
		}
	} else {
		cm.playerMessage(5, "Not enough weight is on the switch.");
	}
	cm.dispose();
}

function action(mode, type, selection) {
}