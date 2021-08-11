function enter(pi) {
    var reac = pi.getMap().getReactorByName("goldkey3");
    if (reac.getState() == 0) {
	reac.hitReactor(pi.getClient());
    }
}