function init() {
}

function setup(mapid) {

    var eim = em.newInstance("Olivia" + mapid);

	eim.setProperty("stage", "0");
	eim.setProperty("mode", mapid);
    var map = eim.setInstanceMap(682010100 + (parseInt(mapid)));
    map.resetFully();
	map.getPortal(2).setScriptName("oliviaOut");

    eim.startEventTimer(600000);
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
}

function changedMap(eim, player, mapid) {
    if (mapid != 682010100 && mapid != 682010101 && mapid != 682010102) {
	playerExit(eim,player);
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function scheduledTimeout(eim) {
	end(eim);
}

function monsterValue(eim, mobId) {
    return 1;
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);

    if (eim.disposeIfPlayerBelow(0, 0)) {
	em.setProperty("state", "0");
    }
}

function allMonstersDead(eim) {
}

function end(eim) {
    eim.disposeIfPlayerBelow(100, 682000000);
}

function playerRevive(eim, player) {
    return false;
}

function clearPQ(eim) {}
function leftParty (eim, player) {}
function disbandParty (eim) {}
function playerDead(eim, player) {}
function cancelSchedule() {}