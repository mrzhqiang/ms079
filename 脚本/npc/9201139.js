

function action(mode, type, selection) {
	if (cm.getPlayer().getEventInstance() != null && cm.getPlayer().getEventInstance().getProperty("stage").equals("0")) {
		cm.spawnMonster(9400641 + parseInt(cm.getPlayer().getEventInstance().getProperty("mode")));
		cm.getPlayer().getEventInstance().setProperty("stage", "1");
	}
    cm.dispose();
}