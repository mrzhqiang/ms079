
function act() {
	var r = rm.getMap().getReactorByName("eak");
	r.forceHitReactor(r.getState() + 1);
	rm.dropItems();
}