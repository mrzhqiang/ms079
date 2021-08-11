var baseid = 260020600;
var dungeonid = 260020630;
var dungeons = 30;

function enter(pi) {
    if (pi.getMapId() == baseid) {
	if (pi.getParty() != null) {
	    if (pi.isLeader()) {
		for (var i = 0; i < dungeons; i++) {
		    if (pi.getPlayerCount(dungeonid + i) == 0) {
			pi.warpParty(dungeonid + i);
			return;
		    }
		}
	    } else {
			pi.playerMessage(5, "您不是組隊隊長！");
	    }
	} else {
	    for (var i = 0; i < dungeons; i++) {
		if (pi.getPlayerCount(dungeonid + i) == 0) {
		    pi.warp(dungeonid + i);
		    return;
		}
	    }
	}
		pi.playerMessage(5, "所有的地下城都在使用中，請稍後再嘗試。");
    } else {
		pi.playPortalSE();
		pi.warp(baseid, "MD00");
    }
}
