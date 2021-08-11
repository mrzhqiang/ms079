function enter(pi) {
    var map = pi.getMapId();
    var shortval = (map / 100) - 9800400;
    
    // Checking
    var time = 0;
    var instance = "";

    switch (shortval) {
	case 10:
	    instance = "WitchTower_EASY";
	    time = 130000;
	    break;
	case 11:
	    instance = "WitchTower_EASY";
	    time = 80000;
	    break;
	case 20:
	    instance = "WitchTower_Med";
	    time = 130000;
	    break;
	case 21:
	    instance = "WitchTower_Med";
	    time = 80000;
	    break;
	case 30:
	    instance = "WitchTower_Hard";
	    time = 510000;
	    break;
	case 31:
	    instance = "WitchTower_Hard";
	    time = 460000;
	    break;
    }
    var eventinstance = pi.getEventManager(instance).getInstance(pi.getName());
    if (eventinstance != null) {
	if (eventinstance.getTimeLeft() > time) {
	    pi.playerMessage(5, "You have been moved to another location due to usage of illegal 3rd party programme.");
	    pi.warp(980040000, 0);
	    return true;
	}
	switch (shortval) {
	    case 11:
	    case 21:
	    case 31:
		eventinstance.restartEventTimer(180000);
		break;
	}
    }
    //980042100
    for (var i = 0; i < 10; i++) {
	var mapto = map + 100 + i;
	pi.getMap(mapto);
	if (pi.getPlayerCount(mapto) == 0) {
	    pi.warp(mapto, 0);
	    return true;
	}
    }
    pi.playerMessage(5, "All of the maps are currently in use.");
    return false;
}