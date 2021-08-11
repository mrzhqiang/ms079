function enter(pi) {
	var ei = pi.getPlayer().getEventInstance();
	if (ei == null || ei.getProperty("current_instance") == null || ei.getProperty("current_instance").equals("0")) {
		pi.warp(502029000,0);
	} else {
		pi.warp(parseInt(ei.getProperty("current_instance")), 0);
	}
}