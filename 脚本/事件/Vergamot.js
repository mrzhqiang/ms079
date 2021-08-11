function init() {
em.setProperty("state", "0");
	em.setProperty("leader", "true");
}

function setup(eim, leaderid) {
em.setProperty("state", "1");
	em.setProperty("leader", "true");
    var eim = em.newInstance("Vergamot" + leaderid);
    
    eim.setProperty("vergamotSummoned", "0");

    var map = eim.setInstanceMap(802000211);
    map.resetFully();

    var mob = em.getMonster(9400276);
    eim.registerMonster(mob);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(156, 258));

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
   end(eim);
}

function changedMap(eim, player, mapid) {
    if (mapid != 802000211) {
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
    eim.disposeIfPlayerBelow(100, 802000212);
em.setProperty("state", "0");
		em.setProperty("leader", "true");
}

function clearPQ(eim) {
    end(eim);
}

function allMonstersDead(eim) {
    var prop = eim.getProperty("vergamotSummoned");

    if (prop.equals("0")) {
	eim.setProperty("vergamotSummoned", "1");
	var map = eim.getMapInstance(0);
	var mob = em.getMonster(9400263);
	eim.registerMonster(mob);
	map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(559, 545));
    } else {
	var map = eim.getMapInstance(0);
	map.spawnNpc(9120026, new java.awt.Point(479, 485));
    }
}

function leftParty (eim, player) {}
function disbandParty (eim) {}
function playerDead(eim, player) {}
function cancelSchedule() {}