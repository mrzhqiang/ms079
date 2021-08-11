/*
 * 4th Job Phoenix
 */

function init() {
}

function monsterValue(eim, mobId) {
    return 1;
}

function setup() {
    var eim = em.newInstance("s4nest");

    var map = eim.setInstanceMap(924000100);
    map.resetFully();
    eim.startEventTimer(1200000);

    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapFactory().getMap(924000100);
    player.changeMap(map, map.getPortal(0));
}

function playerDead(eim, player) {
}

function playerRevive(eim, player) {
}

function scheduledTimeout(eim) {
    eim.disposeIfPlayerBelow(100, 240010700);
}

function changedMap(eim, player, mapid) {
    if (mapid != 924000100) {
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
    eim.disposeIfPlayerBelow(100, 240010700);
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    var map = eim.getMapFactory().getMap(240010700);
    player.changeMap(map, map.getPortal(0));
}

function clearPQ(eim) {
    eim.disposeIfPlayerBelow(100, 240010700);
}

function allMonstersDead(eim) {
//has nothing to do with monster killing
}

function cancelSchedule() {
}