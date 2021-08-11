function enter(pi) {
    var eim = pi.getPlayer().getEventInstance();
    if (eim != null && pi.getPlayer().getCarnivalParty() != null) {
	if (pi.getPlayer().getCarnivalParty().getTeam() == 0) {
	    if (eim.getProperty("Red_Stage").equals("B")) {
		pi.warp(eim.getMapInstance(5).getId(), 1);
	    } else if (eim.getProperty("Red_Stage").equals("BC")) {
		pi.warp(eim.getMapInstance(0).getId(), 1);
	    } else {
	    	pi.warp(eim.getMapInstance(5 + parseInt(eim.getProperty("Red_Stage"))).getId(), 1);
	    }
	} else {
	    if (eim.getProperty("Blue_Stage").equals("B")) {
		pi.warp(eim.getMapInstance(5).getId(), 1);
	    } else if (eim.getProperty("Blue_Stage").equals("BC")) {
		pi.warp(eim.getMapInstance(0).getId(), 1);
	    } else {
	    	pi.warp(eim.getMapInstance(10 + parseInt(eim.getProperty("Blue_Stage"))).getId(), 1);
	    }
	}
    }
}