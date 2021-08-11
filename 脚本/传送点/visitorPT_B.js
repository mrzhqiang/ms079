function enter(pi) {
	if (!pi.isLeader()) {
		pi.playerMessage("Only the leader can go in this portal.");
		return;
	}
	var ei = pi.getPlayer().getEventInstance();
	if (ei == null || ei.getProperty("mode") == null) {
		pi.warp(502029000,0);
	} else {
		var zeroMap = 502030000 + (parseInt(ei.getProperty("mode")) * 16);
		var canPass = pi.getMap().getAllMonstersThreadsafe().size() == 0;
		if (canPass) {
			switch(pi.getMapId() - zeroMap) {
				case 5:
				case 8:
					canPass = ei.getProperty("stage" + (pi.getMapId() - zeroMap)).equals("0");
					break;
			}
		}
		if (!canPass) {
			pi.playerMessage("The portal is locked. All monsters must be defeated.");
		} else {
			var nextMap = (pi.getMapId() + 1);
			switch(pi.getMapId() - zeroMap) {
				case 13:
				case 14:
				case 15:
					nextMap = (zeroMap + (pi.getMapId() - zeroMap) - 4);
					break;
				case 8:
				case 9:
				case 10:
					nextMap = (zeroMap + (pi.getMapId() - zeroMap) + 5);
					break;
				case 11:
					nextMap = 502029000;
					break;
					
			}
			pi.warpParty(nextMap, 0);
		}
	}
}