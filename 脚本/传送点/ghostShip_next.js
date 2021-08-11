function enter(pi) {
    if (!pi.isLeader()) {
	pi.playerMessage(5, "The leader must be here");
    } else {
	if (pi.getMap().getAllMonstersThreadsafe().size() != 0) {
	    pi.playerMessage(5, "Please, clear all of the monsters!");
	    return;
	}
	if (((pi.getMapId() % 10) | 0) == 4) { //last stage
	    if (pi.getMap().getReactorByName("switch0").getState() < 1 || pi.getMap().getReactorByName("switch1").getState() < 1) {
		pi.playerMessage(5, "Both switches must be turned on.");
		return;
	    }
	    var bossroom = pi.getMapId() + 66;//90-14 = 76, 90-24=66
	    if (((bossroom % 100) | 0) != 90) {
		bossroom += 10;
	    }
	    pi.warpParty(bossroom, 0);
	} else {
	    pi.warpParty(pi.getMapId() + 1, ((pi.getMapId() % 10) | 0) == 3 ? 1 : 2);
	}
    }
}