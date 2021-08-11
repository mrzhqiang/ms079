function enter(pi) {
	if (pi.getPlayer().getParty() != null && pi.getMap().getAllMonstersThreadsafe().size() == 0 && pi.isLeader()) {
		var chars = pi.getMap().getCharactersThreadsafe();
		for (var i = 0; i < chars.size(); i++) {
			var item = (chars.get(i).getJob() % 1000) / 100 + 2022651;
			if (item == 2022651) {
				item = 2022652;
			}
			pi.gainItem(item, 1, false, 0, 0, "", chars.get(i).getClient());
		}
		//pi.gainNX(3000);
		pi.warpParty(240080050);
		pi.playPortalSE();
	} else {
		pi.playerMessage(5,"This portal is not available.");
	}
}