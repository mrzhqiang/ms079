function init() {
}

function setup(leaderid) {
    var eim = em.newInstance("MiniDungeon" + leaderid);

   var map = eim.createInstanceMap(749040100);
	//it is possible to make THREE minidungeons just like this
	//maps 749040101 and 749040102; harder monsters than this map but not by much
	map.setHPDec(25);
	map.setReturnMapId(910000000); //fm
	map.setForcedReturnMap(910000000);
	map.getPortal(3).setScriptName("exitMRYetty");
	map.toggleGDrops();
    eim.startEventTimer(3600000); // 1 hr
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
    if (mapid != 749040100) {
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
	var iter = eim.getMapInstance(0).getCharactersThreadsafe().iterator();
	var map = eim.getMapFactory().getMap(910000000);
	while (iter.hasNext()) {
		var chr = iter.next();
		eim.unregisterPlayer(chr);
		chr.changeMap(map, map.getPortal(0));
	}
    eim.dispose();
}

function clearPQ(eim) {}
function allMonstersDead(eim) {}
function leftParty (eim, player) {}
function disbandParty (eim) {}
function playerDead(eim, player) {}
function cancelSchedule() {}