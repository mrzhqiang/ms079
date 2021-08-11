//var baseid = 541020610;
//var dungeonid = 541020620;
//var dungeons = 19;

function enter(pi) {
    if (pi.getMapId() == 251010402) {
	if (pi.getParty() != null) {
	    if (pi.isLeader()) {
		//		for (var i = 0; i < dungeons; i++) {
		//		    if (pi.getPlayerCount(dungeonid + i) == 0) {
		//			pi.warpParty(dungeonid + i);
		pi.warpParty(251010410);
		return true;
	    //		    }
	    //		}
	    } else {
		pi.playerMessage(5, "需要一個隊伍。");
		return false;
	    }
	} else {
	    //	    for (var i = 0; i < dungeons; i++) {
	    //		if (pi.getPlayerCount(dungeonid + i) == 0) {
	    //		    pi.warp(dungeonid + i);
	    pi.warp(251010410, 0);
	    return true;
	//		}
	//	    }
	}
	pi.playerMessage(5, "所有的地下城都在使用中，請稍後再嘗試。");
	return false;
    } else {
	pi.playPortalSE();
	pi.warp(251010402, "MD00");
	return true;
    }
}
