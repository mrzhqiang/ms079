function init() {
}

function setup(mapid) {

    var eim = em.newInstance("Christmas" + mapid);

	eim.setProperty("stage", "0");
	eim.setProperty("ok", "0");
	eim.setProperty("mode", mapid);
    var map = eim.setInstanceMap(889100001 + (10 * parseInt(mapid)));
    map.resetFully();

    var mob = em.getMonster(9400326 + (parseInt(mapid) * 5)); // Tylus
    eim.registerMonster(mob);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(-189, 30));

    eim.startEventTimer(600000);
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
}

function changedMap(eim, player, mapid) {
    if (mapid != 889100001 && mapid != 889100011 && mapid != 889100021) {
	playerExit(eim,player);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function scheduledTimeout(eim) {
	if (eim.getProperty("stage").equals("1")) {
		allMonstersDead(eim);
	} else {
		eim.setProperty("stage", "1");
		eim.setProperty("ok", "1");
		eim.getMapInstance(0).killAllMonsters(true);
		var mob = em.getMonster(9400319 + parseInt(eim.getProperty("mode"))); // Tylus
		eim.restartEventTimer(600000);
		eim.registerMonster(mob);
		var map = eim.setInstanceMap(889100001 + parseInt(eim.getProperty("mode") * 10));
		map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(-189, 30));
		eim.setProperty("ok", "0");
	}
}

function monsterValue(eim, mobId) {
    if ((mobId == 9400322 || mobId == 9400327 || mobId == 9400332) && !eim.getProperty("ok").equals("1")) {
	allMonstersDead(eim);
    }
    return 1;
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);

    if (eim.disposeIfPlayerBelow(0, 0)) {
	em.setProperty("state", "0");
    }
}

function allMonstersDead(eim) {
    eim.disposeIfPlayerBelow(100, 889100002 + (10 * parseInt(eim.getProperty("mode"))));
}

function playerRevive(eim, player) {
    return false;
}

function clearPQ(eim) {}
function leftParty (eim, player) {}
function disbandParty (eim) {}
function playerDead(eim, player) {}
function cancelSchedule() {}