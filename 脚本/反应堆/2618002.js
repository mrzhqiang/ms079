function act() {
	rm.getMap(rm.getMapId() + 1).getReactorByName(rm.getMapId() == 926100200 ? "rnj31_out" : "jnr31_out").hitReactor(rm.getClient());
}