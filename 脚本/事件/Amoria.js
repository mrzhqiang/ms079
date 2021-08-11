var minPlayers = 6;
var stg2_combo0 = Array("5", "4", "3", "3", "2");
var stg2_combo1 = Array("0", "0", "1", "0", "1"); //unique combos only
var stg2_combo2 = Array("0", "1", "1", "2", "2");

function init() {
em.setProperty("state", "0");
	em.setProperty("leader", "true");
}

function setup(eim, leaderid) {
em.setProperty("state", "1");
	em.setProperty("leader", "true");
    var eim = em.newInstance("Amoria" + leaderid);
	em.setProperty("apq1", "0");
	//2nd stage areas ..5 people distributed in the 3 areas
	em.setProperty("apq2", "0");
	em.setProperty("apq2_tries", "0");
	em.setProperty("apq3", "0");
	em.setProperty("apq3_tries", "0");
	em.setProperty("apq4", "0");
	em.setProperty("apq5", "0");
	var rand_combo = java.lang.Math.floor(java.lang.Math.random() * stg2_combo0.length);
	var rand_num = java.lang.Math.random();
	var combo0 = rand_num < 0.33 ? true : false;
	var combo1 = rand_num < 0.66 ? true : false;
	em.setProperty("apq2_0", combo0 ? stg2_combo0[rand_combo] : (combo1 ? stg2_combo1[rand_combo] : stg2_combo2[rand_combo]));
	em.setProperty("apq2_1", combo0 ? stg2_combo1[rand_combo] : (combo1 ? stg2_combo2[rand_combo] : stg2_combo0[rand_combo]));
	em.setProperty("apq2_2", combo0 ? stg2_combo2[rand_combo] : (combo1 ? stg2_combo0[rand_combo] : stg2_combo1[rand_combo]));
	var i = 0; //stage 3, 9 boxes = 5 random are 1
	for (var x = 0; x < 9; x++) {
		em.setProperty("apq3_" + x, "0");
	}
	while (i < 5) {
		for (var x = 0; x < 9; x++) {
			if (em.getProperty("apq3_" + x).equals("0") && java.lang.Math.random() < 0.2 && i < 5) {
				em.setProperty("apq3_" + x, "1");
				i++;
			}
		}
	}

        var map1 = eim.setInstanceMap(670010200);
	map1.resetFully();
	map1.getPortal("go00").setPortalState(false);
	map1.getPortal("go01").setPortalState(false);
	map1.getPortal("go02").setPortalState(false);
        var map2 = eim.setInstanceMap(670010300);
	map2.resetFully();
	map2.getPortal("next00").setPortalState(false); //disable NEXT
        var map3 = eim.setInstanceMap(670010400);
	map3.resetFully();
	map3.getPortal("next00").setPortalState(false); //disable NEXT
        var map4 = eim.setInstanceMap(670010500);
	map4.resetFully();
	map4.getPortal("next00").setPortalState(false); //disable NEXT
        eim.setInstanceMap(670010600).resetFully();
        eim.setInstanceMap(670010700).resetFully();
        eim.setInstanceMap(670010800).resetFully();

    eim.startEventTimer(3600000); //1 hr
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
}

function playerRevive(eim, player) {
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    if (mapid < 670010200 || mapid > 670010800) {
	eim.unregisterPlayer(player);

	if (eim.disposeIfPlayerBelow(0, 0)) {
		em.setProperty("state", "0");
		em.setProperty("leader", "true");
	}
    } else if (mapid == 670010800) {
	if (em.getProperty("apq5").equals("0")) {
	    eim.restartEventTimer(60000); //1 min bonus
	    em.setProperty("apq5", "1");
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

    if (eim.disposeIfPlayerBelow(0, 0)) {
	em.setProperty("state", "0");
		em.setProperty("leader", "true");
	}
}

function end(eim) {
    eim.disposeIfPlayerBelow(100, 670011000);
	em.setProperty("state", "0");
		em.setProperty("leader", "true");
}

function clearPQ(eim) {
    end(eim);
}

function allMonstersDead(eim) {
}

function leftParty (eim, player) {
    // If only 2 players are left, uncompletable:
    var party = eim.getPlayers();
    if (party.size() < minPlayers) {
	end(eim);
    }
    else
	playerExit(eim, player);
}
function disbandParty (eim) {
	end(eim);
}
function playerDead(eim, player) {}
function cancelSchedule() {}