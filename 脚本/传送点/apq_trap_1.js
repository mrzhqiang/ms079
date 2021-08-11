function enter(pi) {
	var map = pi.getPlayer().getMap();
	var reactor = map.getReactorByName("gate01");
	var state = reactor.getState();
	if (state >= 4) {
		pi.warp(670010600, 4);
		return true;
	} else {
		pi.getClient().getSession().write(org.rise.tools.MaplePacketCreator.serverNotice(5, "门被关上了."));
		return false;
	}
}