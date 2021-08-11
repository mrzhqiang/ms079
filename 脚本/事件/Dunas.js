function init() {
em.setProperty("state", "0");
	em.setProperty("leader", "true");
}

function setup(eim, leaderid) {
	em.setProperty("state", "1");
	em.setProperty("leader", "true");
    var eim = em.newInstance("Dunas" + leaderid);

    eim.setProperty("dunas_summoned", "false");

    var map = eim.setInstanceMap(802000411);
    map.resetFully();

    var mob = em.getMonster(9400277);
    eim.registerMonster(mob);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(2417, -6));

    eim.startEventTimer(14400000); // 4 hrs
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
}

function playerRevive(eim, player) {
    return false;
}

function scheduledTimeout(eim) {
    if (eim.disposeIfPlayerBelow(100, 802000412)) {
		em.setProperty("state", "0");
		em.setProperty("leader", "true");
	}
}

function changedMap(eim, player, mapid) {
    if (mapid != 802000411) {
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
    eim.disposeIfPlayerBelow(100, 0);
em.setProperty("state", "0");
		em.setProperty("leader", "true");
}

function clearPQ(eim) {
    end(eim);
}

function allMonstersDead(eim) {
    if (eim.getProperty("dunas_summoned").equals("false")) {
	eim.setProperty("dunas_summoned", "true");
	var mob = em.getMonster(9400270);
	eim.registerMonster(mob);
	eim.getMapInstance(0).spawnMonsterOnGroundBelow(mob, new java.awt.Point(2417, -6));
    } else {
	eim.getMapInstance(0).spawnNpc(9120026, new java.awt.Point(2169, 45));
    }
}

function leftParty (eim, player) {}
function disbandParty (eim) {}
function playerDead(eim, player) {}
function cancelSchedule() {}