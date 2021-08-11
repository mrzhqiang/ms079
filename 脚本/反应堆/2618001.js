function act() {
	rm.getMap(rm.getMapId() + 2).getReactorByName(rm.getMapId() == 926100200 ? "rnj32_out" : "jnr32_out").hitReactor(rm.getClient());
}