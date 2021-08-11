var minPlayers = 2;

function init() {
em.setProperty("state", "0");
	em.setProperty("leader", "true");
}

function setup(level, leaderid) {
em.setProperty("state", "1");
	em.setProperty("leader", "true");
    var eim = em.newInstance("Iceman" + leaderid);
        eim.setInstanceMap(932000100).resetPQ(level);
        eim.setInstanceMap(932000200).resetPQ(level);
        var map = eim.setInstanceMap(932000300);
		map.resetPQ(level);
		var mob = em.getMonster(9300441);
		eim.registerMonster(mob);
		mob.changeLevel(level);
		map.spawnMonsterOnGroundBelow(mob, map.getPortal(0).getPosition());
		map.spawnNpc(2159017, map.getPortal(0).getPosition());
        eim.setInstanceMap(932000400).resetPQ(level);
    eim.startEventTimer(1200000); //20 mins
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
}

function playerRevive(eim, player) {
    eim.unregisterPlayer(player);
	if (eim.disposeIfPlayerBelow(0, 0)) {
		em.setProperty("state", "0");
		em.setProperty("leader", "true");
	}
    return true;
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    if (mapid < 932000100 || mapid > 932000400) {
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
	player.changeMap(em.getChannelServer().getMapFactory().getMap(932000000), em.getChannelServer().getMapFactory().getMap(932000000).getPortal(0));
	if (eim.disposeIfPlayerBelow(0, 0)) {
		em.setProperty("state", "0");
		em.setProperty("leader", "true");
	}
}

function end(eim) {
    eim.disposeIfPlayerBelow(100, 932000000);
	em.setProperty("state", "0");
		em.setProperty("leader", "true");
}

function clearPQ(eim) {
    end(eim);
}

function allMonstersDead(eim) {
}

function leftParty (eim, player) {
	end(eim);
}
function disbandParty (eim) {
	end(eim);
}
function playerDead(eim, player) {}
function cancelSchedule() {}