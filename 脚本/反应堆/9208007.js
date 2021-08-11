/*
Stage 2: Spear destinations - Guild Quest

@Author Lerk
*/

function act() {
    rm.getPlayer().getEventInstance().getMapFactory().getMap(990000400).getReactorByName("speargate").hitReactor(rm.getClient());
}