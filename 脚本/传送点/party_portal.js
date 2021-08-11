function enter(pi) {
    var eim = pi.getPlayer().getEventInstance();
    switch(pi.getMapId()) {
	case 910340100:
    	    if (eim.getProperty("1stageclear") == null) { // do nothing; send message to player
		pi.playerMessage(5, "The portal is blocked.");
    	    } else {
 		pi.warp(910340200, 0);
            }
	    break;
	case 910340200:
    	    if (eim.getProperty("2stageclear") == null) { // do nothing; send message to player
		pi.playerMessage(5, "The portal is blocked.");
    	    } else {
 		pi.warp(910340300, 0);
            }
	    break;
	case 910340300:
    	    if (eim.getProperty("3stageclear") == null) { // do nothing; send message to player
		pi.playerMessage(5, "The portal is blocked.");
    	    } else {
 		pi.warp(910340400, 0);
            }
	    break;
	case 910340400:
    	    if (eim.getProperty("4stageclear") == null) { // do nothing; send message to player
		pi.playerMessage(5, "The portal is blocked.");
    	    } else {
 		pi.warp(910340500, 0);
            }
	    break;
    }
}