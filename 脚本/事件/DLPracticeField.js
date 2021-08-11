/**
	Dark Lord's Practice Field, 4th job Quest
**/

function init() {
}

function monsterValue(eim, mobId) {
    return 1;
}

function setup() {
    var eim = em.newInstance("DLPracticeField");

    var map = eim.setInstanceMap(910300000);
    map.respawn(true);
    eim.startEventTimer(1200000);

    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapFactory().getMap(910300000);
    player.changeMap(map, map.getPortal(0));
}

function playerDead(eim, player) {
}

function playerRevive(eim, player) {
}

function scheduledTimeout(eim) {
    eim.disposeIfPlayerBelow(100, 211042300);
}

function changedMap(eim, player, mapid) {
    if (mapid != 910300000) {
	eim.unregisterPlayer(player);

	eim.disposeIfPlayerBelow(0, 0);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function leftParty(eim, player) {
    // If only 2 players are left, uncompletable:
    playerExit(eim, player);
}

function disbandParty(eim) {
    eim.disposeIfPlayerBelow(100, 103000003);
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    var map = eim.getMapFactory().getMap(103000003);
    player.changeMap(map, map.getPortal(0));
}

function clearPQ(eim) {
    eim.disposeIfPlayerBelow(100, 103000003);
}

function allMonstersDead(eim) {
//has nothing to do with monster killing
}

function cancelSchedule() {
}