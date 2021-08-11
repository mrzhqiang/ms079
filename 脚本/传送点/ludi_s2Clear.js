function enter(pi) {
    var eim = pi.getPlayer().getEventInstance();
    
    // only let people through if the eim is ready
    if (eim.getProperty("stage4status") == null) { // do nothing; send message to player
	pi.playerMessage(5, "The portal is blocked.");
    } else {
	pi.warpParty(pi.getMapId() + 200, "st00");
    }
}