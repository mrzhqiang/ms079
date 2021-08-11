function act() {
	rm.getReactor().forceTrigger();
	rm.getReactor().delayedDestroyReactor(1000);
	rm.mapMessage("Rex has been summoned.");
	rm.spawnMonster(9300281);
}