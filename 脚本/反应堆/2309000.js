function act() {
	rm.getMap().getReactorByName("jump").forceHitReactor(rm.getReactor().getState());
}