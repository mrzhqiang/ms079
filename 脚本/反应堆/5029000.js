function act() {
	var eim = rm.getPlayer().getEventInstance();
	if (eim != null && eim.getProperty("stage5") != null) {
		var e = parseInt(eim.getProperty("stage5"));
		if (e == 1) {
			rm.mapMessage("The doors have been unlocked.");
		} else if (e > 1) {
			rm.spawnMonster(9420024 + (parseInt(eim.getProperty("mode")) * 6));
			rm.spawnMonster(9420027 + (parseInt(eim.getProperty("mode")) * 6));
			rm.spawnMonster(9420029 + (parseInt(eim.getProperty("mode")) * 6));
			rm.mapMessage("Some monsters have been summoned.");
		} else if (e <= 0) {
			return;
		}
		eim.setProperty("stage5", "" + (e-1));
	}
}