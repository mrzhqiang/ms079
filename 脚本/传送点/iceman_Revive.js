function enter(pi) {
    if (pi.getPlayer().getEventInstance() != null) {
    	pi.gainExp_PQ(70, 1.0);
    	//pi.gainNX(1000);
		if (pi.canHold(4001529, 1)) { //TODO JUMP
			pi.gainItem(4001529, 1);
		}
    }
    pi.warp(932000000,0);
}