function enter(pi) {
    var reac = pi.getMap().getReactorByName("goldkey8");
    if (reac.getState() == 0) {
	reac.hitReactor(pi.getClient());
    }
}