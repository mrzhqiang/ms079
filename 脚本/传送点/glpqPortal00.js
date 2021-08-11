function enter(pi) {
    if (java.lang.Math.floor(((pi.getPlayer().getJob() % 1000) / 100) * 100 - (pi.getPlayer().getJob() % 100)) == 100) {
	pi.warp(610030510,0);
    } else {
	pi.playerMessage(5, "Only warriors may enter this portal.");
	//pi.playerMessage(5, "Your job: " + (((pi.getPlayer().getJob() % 1000) / 100) * 100 - (pi.getPlayer().getJob() % 100)));
    }
}