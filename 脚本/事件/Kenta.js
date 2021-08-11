var minPlayers = 2;

function init() {
em.setProperty("state", "0");
	em.setProperty("leader", "true");
}

function setup(level, leaderid) {
em.setProperty("state", "1");
	em.setProperty("leader", "true");
    var eim = em.newInstance("Kenta" + leaderid);
        eim.setInstanceMap(923040100).resetPQ(level);
        eim.setInstanceMap(923040200).resetPQ(level);
        var map = eim.setInstanceMap(923040300);
	map.resetFully(false);
	map.setSpawns(false);
	map.resetSpawnLevel(level);
	map.spawnNpc(9020004, new java.awt.Point(68, 598));
		var mob0 = em.getMonster(9300460);
		eim.registerMonster(mob0);
		map.spawnMonsterOnGroundBelow(mob0, new java.awt.Point(68, 598));

		map = eim.setInstanceMap(923040400);
		map.resetPQ(level);
		map.spawnNpc(9020004, new java.awt.Point(-161, 123));
		var mob1 = em.getMonster(9300461);
		eim.registerMonster(mob1);
		mob1.changeLevel(level);
		map.spawnMonsterOnGroundBelow(mob1, new java.awt.Point(400, 123));
		var mob2 = em.getMonster(9300468);
		eim.registerMonster(mob2);
		mob2.changeLevel(level);
		map.spawnMonsterOnGroundBelow(mob2, new java.awt.Point(-1000, 123)); //TODOO: find out REAL positions
    eim.startEventTimer(1200000); //30 mins
	eim.setProperty("entryTimestamp", "" + java.lang.System.currentTimeMillis());
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
}

function playerRevive(eim, player) {
    eim.unregisterPlayer(player);
	if (eim.disposeIfPlayerBelow(0, 0)) {
		em.setProperty("state", "0");
		em.setProperty("leader", "true");
	}
    return true;
}

function scheduledTimeout(eim) {
	if (eim.getProperty("kentaSaving") != null && !eim.getProperty("kentaSaving").equals("0")) {
		var timeLeft = parseInt(eim.getProperty("kentaSaving"));
		eim.setProperty("kentaSaving", "0");
		eim.getMapInstance(2).setSpawns(false);
		eim.getMapInstance(2).killAllMonsters(true);
		eim.restartEventTimer(timeLeft);
	} else {
		end(eim);
	}
}

function changedMap(eim, player, mapid) {
    if (mapid < 923040100 || mapid > 923040400) {
        eim.unregisterPlayer(player);

	if (eim.disposeIfPlayerBelow(0, 0)) {
		em.setProperty("state", "0");
		em.setProperty("leader", "true");
	}
    } else if (mapid == 923040300) {
		if (eim.getProperty("kentaSaving") == null) {
			var timeLeft = 1200000 - (java.lang.System.currentTimeMillis() - parseInt(eim.getProperty("entryTimestamp")));
			eim.setProperty("kentaSaving", "" + timeLeft);
			eim.restartEventTimer(180000);
			eim.getMapInstance(2).setSpawns(true);
		}
	}
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
	if (mobId == 9300460) {
		end(eim);
	}
    return 1;
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
	if (eim.disposeIfPlayerBelow(0, 0)) {
		em.setProperty("state", "0");
		em.setProperty("leader", "true");
	}
}

function end(eim) {
    eim.disposeIfPlayerBelow(100, 923040000);
	em.setProperty("state", "0");
		em.setProperty("leader", "true");
}

function clearPQ(eim) {
    end(eim);
}

function allMonstersDead(eim) {
}

function leftParty (eim, player) {
	end(eim);
}
function disbandParty (eim) {
	end(eim);
}
function playerDead(eim, player) {}
function cancelSchedule() {}