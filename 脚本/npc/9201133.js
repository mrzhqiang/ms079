var status = -1;
function action(mode, type, selection) {
    if (mode == 1) {
		status++;
    } else {
		cm.dispose();
		return;
    }
    if (status == 0) {
		if (cm.getPlayer().getMapId() == 105050400) {
			if (cm.getPlayer().getLevel() < 40 && cm.haveItem(4032496)) {
				cm.sendYesNo("你想到达地狱大公散步的地方?");
			} else {
				cm.sendOk("你需要少于40级并且需要恶魔猎手的饰品进入.");
				cm.dispose();
			}
		} else if (cm.getPlayer().getMapId() == 677000011) { //warp to another astaroth map.
			if (cm.getParty() == null) {
				cm.sendOk("你必须在次开组队.");
			} else {
				var party = cm.getParty().getMembers();
				var mapId = cm.getMapId();
				var next = true;
				var levelValid = 0;
				var inMap = 0;
				var it = party.iterator();
				while (it.hasNext()) {
					var cPlayer = it.next();
					if ((cPlayer.getLevel() >= 10 && cPlayer.getLevel() < 40) || cPlayer.getJobId() == 900) {
						levelValid += 1;
					} else {
						next = false;
					}
					if (cPlayer.getMapid() == mapId) {
						inMap += 1;
					}
				}
				if (party.size() < 2 || inMap < 2) {
					next = false;
				}
				if (!next) {
					cm.sendOk("在同一张地图上，您需要你的组队人数为2人.");
					cm.dispose();
					return;
				}
			}
			cm.warp(677000013,0);
			cm.dispose();
		} else if (cm.getPlayer().getMapId() == 677000013) { //warp to another astaroth map.
			if (cm.getPlayer().getLevel() < 40 && cm.haveItem(4032496)) {
				if (cm.getParty() == null) {
					cm.sendOk("你必须在这里开组队.");
				} else {
					var party = cm.getParty().getMembers();
					var mapId = cm.getMapId();
					var next = true;
					var levelValid = 0;
					var inMap = 0;
					var it = party.iterator();
					while (it.hasNext()) {
						var cPlayer = it.next();
						if ((cPlayer.getLevel() >= 10 && cPlayer.getLevel() < 40) || cPlayer.getJobId() == 900) {
							levelValid += 1;
						} else {
							next = false;
						}
						if (cPlayer.getMapid() == mapId) {
							inMap += 1;
						}
					}
					if (party.size() < 2 || inMap < 2) {
						next = false;
					}
					if (next) {
						if (cm.getMap(677000012).getCharactersSize() > 0) {
							cm.sendOk("有人试图打败地狱大公已经.");
						} else {
							cm.warpParty(677000012);
						}
					} else {
						cm.sendOk("在同一张地图上，在同一张地图上，您需要你的组队人数为2人.");
					}
				}
			} else {
				cm.warp(105050400,0);
			}
			cm.dispose();
		} else {
			if (cm.getParty() != null) {
				cm.warpParty(677000011);
			} else {
				cm.warp(677000011,0);
			}
			cm.dispose();
		}
	} else {
		cm.warp(677000010,0);
		cm.dispose();
	}
}