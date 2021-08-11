//ZPQ maps, center area then 1-1 through 16-6 increasing gradually
var mapList = new Array(280010000, 280010010, 280010011, 280010020, 280010030, 280010031, 280010040, 280010041, 280010050, 280010060,
    280010070, 280010071, 280010080, 280010081, 280010090, 280010091, 280010100, 280010101, 280010110, 280010120, 280010130, 280010140,
    280010150, 280011000, 280011001, 280011002, 280011003, 280011004, 280011005, 280011006);

function init() {
    em.setProperty("started", "false");
}

function monsterValue(eim, mobId) {
    return 1;
}

function setup() {
    em.setProperty("started", "true");

    var eim = em.newInstance("ZakumPQ");

    for (var i = 0; i < mapList.length; i++) {
	eim.setInstanceMap(mapList[i]).resetFully();
    }
    eim.getMapFactory().getMap(280010000).shuffleReactors();

    eim.startEventTimer(1800000);
	
    return eim;
}

function playerEntry(eim, player) {
    var map = em.getMapFactory().getMap(280010000);
    player.changeMap(map, map.getPortal(0));
}

function playerRevive(eim, player) {
}

function playerDead(eim, player) {
}

function scheduledTimeout(eim) {
    disposePlayerBelow(eim, 100, 280090000);
}

function changedMap(eim, player, mapid) {
    switch (mapid) {
	case 280010000:
	case 280010010:
	case 280010011:
	case 280010020:
	case 280010030:
	case 280010031:
	case 280010040:
	case 280010041:
	case 280010050:
	case 280010060:
	case 280010070:
	case 280010071:
	case 280010080:
	case 280010081:
	case 280010090:
	case 280010091:
	case 280010100:
	case 280010101:
	case 280010110:
	case 280010120:
	case 280010130:
	case 280010140:
	case 280010150:
	case 280011000:
	case 280011001:
	case 280011002:
	case 280011003:
	case 280011004:
	case 280011005:
	case 280011006:
	    return;
    }
    eim.unregisterPlayer(player);

    disposePlayerBelow(eim,0, 0);
}

function playerDisconnected(eim, player) {
    return -4;
}

function leftParty(eim, player) {
    playerExit(eim, player);
}

function disbandParty(eim) {
    //boot whole party and end
    disposePlayerBelow(eim,100, 280090000);
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);

    var map = eim.getMapFactory().getMap(280090000);
    player.changeMap(map, map.getPortal(0));

	disposePlayerBelow(eim,0, 0);
}

//for offline players
function removePlayer(eim, player) {
    eim.unregisterPlayer(player);
}

function clearPQ(eim) {
    //ZPQ does nothing special with winners
    disposePlayerBelow(eim,100, 280090000);
}

function players(eim) { //not efficient
	var z = em.newCharList();
	for (var i = 0; i < mapList.length; i++) {
		var map = eim.getMapFactory().getMap(mapList[i]);
		if (map != null) {
			var iter = map.getCharactersThreadsafe().iterator();
			while (iter.hasNext()) {
				var chaz = iter.next();
				if (chaz.getEventInstance() != null && chaz.getEventInstance().getName().equals("ZakumPQ")) {
					z.add(chaz);
				}
			}
		}
	}
	return z;
}

function disposePlayerBelow(eim, size, mapid) {
	var z = players(eim);
	var map = eim.getMapFactory().getMap(mapid);
	if (z.size() <= size) {
		var iter = z.iterator();
		while (iter.hasNext()) {
			var cha = iter.next();
			eim.unregisterPlayer(cha);
			if (mapid > 0) {
				cha.changeMap(map, map.getPortal(0));
			}
		}
		em.setProperty("started", "false");
		eim.dispose();
		return true;
	}
	return false;
}

function allMonstersDead(eim) {}
function cancelSchedule() {}