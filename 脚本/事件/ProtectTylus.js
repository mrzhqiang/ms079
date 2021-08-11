function init() {
    em.setProperty("state", "0");
}

function setup(eim, leaderid) {
    em.setProperty("state", "1");

    var eim = em.newInstance("ProtectTylus");

    var map = eim.setInstanceMap(921100300);
    map.killMonster(9300093);

    var mob = em.getMonster(9300093); // Tylus
    eim.registerMonster(mob);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(-358, -86));

    eim.startEventTimer(600000);
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapFactory().getMap(921100300);
    player.changeMap(map, map.getPortal(0));
}

function changedMap(eim, player, mapid) {
    if (mapid != 921100300) {
	playerExit(eim,player);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function scheduledTimeout(eim) {
    eim.disposeIfPlayerBelow(100, 921100301);

    em.setProperty("state", "0");
}

function monsterValue(eim, mobId) {
    if (mobId == 9300093) {
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
    eim.disposeIfPlayerBelow(100, 211000001);

    em.setProperty("state", "0");
}

function playerRevive(eim, player) {
    return false;
}

function clearPQ(eim) {}
function leftParty (eim, player) {}
function disbandParty (eim) {}
function playerDead(eim, player) {}
function cancelSchedule() {}