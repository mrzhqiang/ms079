var baseid = 684000000;
var dungeonid = 684000110;
var dungeons = 10;

function enter(pi) {
try {
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
		pi.playerMessage(5, "你不是队长.");
	    }
	} else {
	    for (var i = 0; i < dungeons; i++) {
		if (pi.getPlayerCount(dungeonid + i) == 0) {
		    pi.warp(dungeonid + i);
		    return;
		}
	    }
	}
	pi.playerMessage(5, "所有的小型地下城是在马上使用，请稍后再试.");
    } else {
	pi.playPortalSE();
	pi.warp(baseid, "MD00");
    }
} catch (e) {
    pi.playerMessage("Error: " + e);
}
}