var minPlayers = 3;

function init() {
	em.setProperty("instanceId", "1");
}

function setup(eim, leaderid) {
    var eim = em.newInstance("Rex" + leaderid + em.getProperty("instanceId"));
	em.setProperty("instanceId", parseInt(em.getProperty("instanceId")) + 1);
        eim.createInstanceMap(921120005).resetFully();
        eim.createInstanceMap(921120100).resetFully();
        eim.createInstanceMap(921120200).resetFully();
        eim.createInstanceMap(921120300).resetFully();
        eim.createInstanceMap(921120400).resetFully();
        eim.createInstanceMap(921120500).resetFully();
        eim.createInstanceMap(921120600).resetFully();
	eim.setProperty("HP", "50000");

    eim.schedule("talkMob", 5000);
    eim.startEventTimer(1800000); //30 mins
    return eim;
}

function talkMob(eim) {
	var map = eim.getMapInstance(0);
	var mob = em.getMonster(9300275);
	eim.registerMonster(mob);
	map.spawnMonsterWithEffectBelow(mob, new java.awt.Point(-451, 154), 12);
} 

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
}

function playerRevive(eim, player) {
    eim.unregisterPlayer(player);
    eim.disposeIfPlayerBelow(0, 0);
    return true;
}

function scheduledTimeout(eim) {
    eim.disposeIfPlayerBelow(100, 921120001);
}

function changedMap(eim, player, mapid) {
    if (mapid < 921120005 || mapid > 921120600) {
        eim.unregisterPlayer(player);

        eim.disposeIfPlayerBelow(0, 0);
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

    eim.disposeIfPlayerBelow(0, 0);
}

function end(eim) {
    eim.disposeIfPlayerBelow(100, 921120001);
}

function clearPQ(eim) {
    eim.disposeIfPlayerBelow(100, 921120001);
}

function allMonstersDead(eim) {
}

function leftParty (eim, player) {
    // If only 2 players are left, uncompletable:
    var party = eim.getPlayers();
    if (party.size() < minPlayers) {
	eim.disposeIfPlayerBelow(100, 921120001);
    }
    else
	playerExit(eim, player);
}
function disbandParty (eim) {
	eim.disposeIfPlayerBelow(100, 921120001);
}
function playerDead(eim, player) {}
function cancelSchedule() {}