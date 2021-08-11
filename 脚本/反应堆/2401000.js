function act() {
    rm.changeMusic("Bgm14/HonTale");
    rm.spawnMonster(8810026, 71, 260);
    rm.mapMessage("隨著一聲怒吼，闇黑龍王出現了。");
	//rm.scheduleWarp(43200, 240000000);
/*	if (!rm.getPlayer().isGM()) {
		rm.getMap().startSpeedRun();
	}*/
}