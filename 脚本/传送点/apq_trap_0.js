function enter(pi) {
	var map = pi.getPlayer().getMap();
	var reactor = map.getReactorByName("gate00");
	var state = reactor.getState();
	if (state >= 4) {
		pi.warp(670010600, 2);
		return true;
	} else {
		pi.getClient().getSession().write(org.rise.tools.MaplePacketCreator.serverNotice(5, "这门关上了."));
		return false;
	}
}