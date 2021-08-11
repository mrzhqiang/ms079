var em;

function enter(pi) {

	if (pi.getMapId() == 222020100) {
	    if (pi.getClient().getChannelServer().getEventSM().getEventManager("elevator").getProperty("isDown").equals("true")) {
		pi.playPortalSE();
		pi.warp(222020110, "sp");
	    } else {
		pi.playerMessage("電梯尚未到達");
	    }
	} else { // 222020200
	    if (pi.getClient().getChannelServer().getEventSM().getEventManager("elevator").getProperty("isUp").equals("true")) {
		pi.playPortalSE();
		pi.warp(222020210, "sp");
	    } else {
		pi.playerMessage("電梯尚未到達");
	    }
	}
}
