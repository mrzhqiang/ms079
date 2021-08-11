function act() {
	rm.mapMessage(6, "All stirges have disappeared.");
	rm.getMap().killAllMonsters(true);
}