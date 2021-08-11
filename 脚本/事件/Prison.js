var minPlayers = 2;

function init() {
em.setProperty("state", "0");
	em.setProperty("leader", "true");
}

function setup(level, leaderid) {
em.setProperty("state", "1");
	em.setProperty("leader", "true");
    var eim = em.newInstance("Prison" + leaderid);
        eim.setInstanceMap(921160100).resetPQ(level);
        eim.setInstanceMap(921160200).resetPQ(level);
        eim.setInstanceMap(921160300).resetPQ(level);
        eim.setInstanceMap(921160310).resetPQ(level);
        eim.setInstanceMap(921160320).resetPQ(level);
        eim.setInstanceMap(921160330).resetPQ(level);
        eim.setInstanceMap(921160340).resetPQ(level);
        eim.setInstanceMap(921160350).resetPQ(level);
        eim.setInstanceMap(921160400).resetPQ(level);
        eim.setInstanceMap(921160500).resetPQ(level);
		eim.setInstanceMap(921160600).resetPQ(level);
		var map = eim.setInstanceMap(921160700);
		map.resetPQ(level);
		map.spawnNpc(9020006, new java.awt.Point(-2161, -186));
		var mob1 = em.getMonster(9300454);
		eim.registerMonster(mob1);
		mob1.changeLevel(level);
		map.spawnMonsterOnGroundBelow(mob1, new java.awt.Point(-2161, -186));
    eim.startEventTimer(1200000); //20 mins
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
		eim.getMapInstance(10).setSpawns(false);
		eim.getMapInstance(10).killAllMonsters(true);
		eim.restartEventTimer(timeLeft);
	} else {
		end(eim);
	}
}

function changedMap(eim, player, mapid) {
    if (mapid < 921160100 || mapid > 921160700) {
        eim.unregisterPlayer(player);

	if (eim.disposeIfPlayerBelow(0, 0)) {
		em.setProperty("state", "0");
		em.setProperty("leader", "true");
	}
    } else if (mapid == 921160600) {
		if (eim.getProperty("kentaSaving") == null) {
			var timeLeft = 1200000 - (java.lang.System.currentTimeMillis() - parseInt(eim.getProperty("entryTimestamp")));
			eim.setProperty("kentaSaving", "" + timeLeft);
			eim.restartEventTimer(180000);
		}
	}
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
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
    eim.disposeIfPlayerBelow(100, 921160000);
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