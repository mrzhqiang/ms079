var minPlayers = 1;

function init() {
em.setProperty("state", "0");
	em.setProperty("leader", "true");
}

function setup(eim, leaderid) {
em.setProperty("state", "1");
	em.setProperty("leader", "true");
    var eim = em.newInstance("MV" + leaderid);
        eim.setInstanceMap(674030000).resetFully();
        eim.setInstanceMap(674030200).resetFully();
        eim.setInstanceMap(674030300).resetFully();
    eim.startEventTimer(1800000); //30 mins
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
}

function playerRevive(eim, player) {
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    if (mapid != 674030000 && mapid != 674030200 && mapid != 674030300) {
	eim.unregisterPlayer(player);

	if (eim.disposeIfPlayerBelow(0, 0)) {
		em.setProperty("state", "0");
		em.setProperty("leader", "true");
	}
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    if (mobId == 9400589) { //MV
	eim.broadcastPlayerMsg(6, "MV has been beaten!");
    	eim.restartEventTimer(60000); //1 mins
	eim.schedule("warpWinnersOut", 55000);
    }
    return 1;
}

function warpWinnersOut(eim) {
	eim.restartEventTimer(300000); //5 mins
	var party = eim.getPlayers();
	var map = eim.getMapInstance(2);
	for (var i = 0; i < party.size(); i++) {
		party.get(i).changeMap(map, map.getPortal(0));
	}
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);

    if (eim.disposeIfPlayerBelow(0, 0)) {
	em.setProperty("state", "0");
		em.setProperty("leader", "true");
	}
}

function end(eim) {
    eim.disposeIfPlayerBelow(100, 674030100);
	em.setProperty("state", "0");
		em.setProperty("leader", "true");
}

function clearPQ(eim) {
    end(eim);
}

function allMonstersDead(eim) {
}

function leftParty (eim, player) {
    // If only 2 players are left, uncompletable:
    var party = eim.getPlayers();
    if (party.size() < minPlayers) {
	end(eim);
    }
    else
	playerExit(eim, player);
}
function disbandParty (eim) {
	end(eim);
}
function playerDead(eim, player) {}
function cancelSchedule() {}