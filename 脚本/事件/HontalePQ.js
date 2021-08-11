var minPlayers = 6;

function init() {
    em.setProperty("state", "0");
}

function monsterValue(eim, mobId) {
    return 1;
}

function setup() {
    em.setProperty("state", "1");

    var eim = em.newInstance("HontalePQ");
    
    var map = eim.setInstanceMap(240050100);
    map.resetFully();
//    map.getPortal("next00").setScriptName("lpq1");
    map = eim.setInstanceMap(240050101);
    map.resetFully();
//    map.getPortal("next00").setScriptName("lpq2");
    eim.setInstanceMap(922010201).resetFully();
    map = eim.setInstanceMap(240050102);
    map.resetFully();
//    map.getPortal("next00").setScriptName("lpq3");
    map = eim.setInstanceMap(240050103);
    map.resetFully();
//    map.getPortal("next00").setScriptName("lpq4");
    eim.setInstanceMap(240050104).resetFully();
    eim.setInstanceMap(240050105).resetFully();
    eim.setInstanceMap(240050200).resetFully();
    map = eim.setInstanceMap(240050300);
    map.resetFully();
//    map.getPortal("next00").setScriptName("lpq7");
    map = eim.setInstanceMap(240050310);
    map.resetFully();
//    map.getPortal("next00").setScriptName("lpq8");

    eim.startEventTimer(60 * 1000 * 60);

    return eim;
}

function scheduledTimeout(eim) {
    eim.disposeIfPlayerBelow(100, eim.getProperty("cleared") == null ? 240050000 : 240050400);

    em.setProperty("state", "0");
}

function changedMap(eim, player, mapid) {
    switch (mapid) {
	case 240050100:
	case 240050101:
	case 240050102:
	case 240050103:
	case 240050104:
	case 240050105:
	case 240050200:
	case 240050300:
	case 240050310:
	case 240050500:
	    return;
    }
    eim.unregisterPlayer(player);

    if (eim.disposeIfPlayerBelow(0, 0)) {
	em.setProperty("state", "0");
    }
}

function playerEntry(eim, player) {
    var map = em.getMapFactory().getMap(240050100);
    player.changeMap(map, map.getPortal(0));
}

function playerRevive(eim, player) {
}

function playerDisconnected(eim, player) {
    return -3;
}

function leftParty(eim, player) {			
    // If only 2 players are left, uncompletable
    if (eim.disposeIfPlayerBelow(minPlayers, eim.getProperty("cleared") == null ? 240050000 : 240050400)) {
	em.setProperty("state", "0");
    } else {
	playerExit(eim, player);
    }
}

function disbandParty(eim) {
    // Boot whole party and end
    eim.disposeIfPlayerBelow(100, eim.getProperty("cleared") == null ? 240050000 : 240050400);

    em.setProperty("state", "0");
}

function playerExit(eim, player) {
    var map = em.getMapFactory().getMap(eim.getProperty("cleared") == null ? 240050000 : 240050400);

    eim.unregisterPlayer(player);
    player.changeMap(map, map.getPortal(0));
}

// For offline players
function removePlayer(eim, player) {
    eim.unregisterPlayer(player);
}

function clearPQ(eim) {
    eim.disposeIfPlayerBelow(100, eim.getProperty("cleared") == null ? 240050000 : 240050400);

    em.setProperty("state", "0");
}

function finish(eim) {
    eim.disposeIfPlayerBelow(100, eim.getProperty("cleared") == null ? 240050000 : 240050400);

    em.setProperty("state", "0");
}

function timeOut(eim) {
    eim.disposeIfPlayerBelow(100, eim.getProperty("cleared") == null ? 240050000 : 240050400);

    em.setProperty("state", "0");
}

function cancelSchedule() {}
function playerDead() {}
function allMonstersDead(eim) {}