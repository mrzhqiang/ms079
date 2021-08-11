/**
	4th Job Assassinate quest
**/

function init() {
}

function monsterValue(eim, mobId) {
    return 1;
}

function setup() {
    var eim = em.newInstance("Relic");

    var map = eim.getMapFactory().getMap(910200000);
    map.killMonster(9300091);
    map.respawn(true);
    map.resetReactors();
    eim.startEventTimer(1200000); // 20 minutes

    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapFactory().getMap(910200000);
    player.changeMap(map, map.getPortal(0));
}

function playerDead(eim, player) {
}

function playerRevive(eim, player) {
}

function scheduledTimeout(eim) {
    eim.disposeIfPlayerBelow(100, 101030104);
}

function changedMap(eim, player, mapid) {
    if (mapid != 910200000) {
	eim.unregisterPlayer(player);

	eim.disposeIfPlayerBelow(0, 0);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function leftParty(eim, player) {
    playerExit(eim, player);
}

function disbandParty(eim) {
    //boot whole party and end
    eim.disposeIfPlayerBelow(100, 101030104);
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    var map = eim.getMapFactory().getMap(101030104);
    player.changeMap(map, map.getPortal(0));
}

function clearPQ(eim) {
    eim.disposeIfPlayerBelow(100, 101030104);
}

function allMonstersDead(eim) {
//has nothing to do with monster killing
}

function cancelSchedule() {
}