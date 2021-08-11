function enter(pi) {
	if (pi.getMap().getAllMonstersThreadsafe().size() == 0) {
		pi.warp(502029000,0);
	} else {
		pi.playerMessage("最终的访问者阻止您的方式.");
	}
}