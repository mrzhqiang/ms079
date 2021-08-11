var minPlayers = 4;

function init() {
em.setProperty("state", "0");
	em.setProperty("leader", "true");
}

function setup(eim, leaderid) {
	em.getProperties().clear();
em.setProperty("state", "1");
	em.setProperty("leader", "true");
    var eim = em.newInstance("Juliet" + leaderid);
	em.setProperty("stage", "0");
	em.setProperty("stage1", "0");
	em.setProperty("stage3", "0");
	em.setProperty("stage4", "0");
	em.setProperty("stage5", "0");
	var y;
	for (y = 0; y < 4; y++) { //stage number
		em.setProperty("stage6_" + y, "0");
		for (var b = 0; b < 10; b++) {
			for (var c = 0; c < 4; c++) {
				//em.broadcastYellowMsg("stage6_" + y + "_" + b + "_" + c + " = 0");
				em.setProperty("stage6_ " + y + "_" + b + "_" + c + "", "0");
			}
		}
	}
	var i;
	for (y = 0; y < 4; y++) { //stage number
		for (i = 0; i < 10; i++) {		
			var found = false;
			while (!found) {
				for (var x = 0; x < 4; x++) {
					if (!found) {
						var founded = false;
						for (var z = 0; z < 4; z++) { //check if any other stages have this value set already.
							//em.broadcastYellowMsg("stage6_" + z + "_" + i + "_" + x + " check");
							if (em.getProperty("stage6_" + z + "_" + i + "_" + x + "") == null) {
								em.setProperty("stage6_" + z + "_" + i + "_" + x + "", "0");
							} else if (em.getProperty("stage6_" + z + "_" + i + "_" + x + "").equals("1")) {
								founded = true;
								break;
							}
						}
						if (!founded && java.lang.Math.random() < 0.25) {
							//em.broadcastYellowMsg("stage6_" + z + "_" + i + "_" + x + " = 1");
							em.setProperty("stage6_" + y + "_" + i + "_" + x + "", "1");
							found = true;
							break;
						}
					}
				}
			}
			//BUT, stage6_0_0_0 set, then stage6_1_0_0 also not set!
		}
	}
	em.setProperty("stage7", "0"); //whether they were killed or not.
        eim.setInstanceMap(926110000).resetFully();
        map = eim.setInstanceMap(926110001);
        map.resetFully();
        map.setSpawns(false);
	eim.setInstanceMap(926110100).resetFully();
	eim.setInstanceMap(926110200).resetFully();
        var map = eim.setInstanceMap(926110201);
	map.resetFully();
        map = eim.setInstanceMap(926110202);
	map.resetFully();
	map = eim.setInstanceMap(926110203);
	map.resetFully();
        map.setSpawns(false);
	map.spawnNpc(2112010, new java.awt.Point(200, 188)); //urete MADMAN
	eim.setInstanceMap(926110300).resetFully();
	eim.setInstanceMap(926110301).resetFully();
	eim.setInstanceMap(926110302).resetFully();
	eim.setInstanceMap(926110303).resetFully();
	eim.setInstanceMap(926110304).resetFully();
	eim.setInstanceMap(926110400).resetFully();
	eim.setInstanceMap(926110401).resetFully();
	eim.setInstanceMap(926110500).resetFully(); //spawn urete based on properties ?????
	eim.setInstanceMap(926110600).resetFully(); //spawn romeo&juliet OR fallen romeo/juliet based on properties???

    eim.startEventTimer(60 * 1000 * 60); //60 mins
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
    player.tryPartyQuest(1205);
}

function playerRevive(eim, player) {
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    if (mapid < 926110000 || mapid > 926110600) {
	eim.unregisterPlayer(player);

	if (eim.disposeIfPlayerBelow(0, 0)) {
		em.setProperty("state", "0");
		em.setProperty("leader", "true");
	}
    }
    if (mapid == 926110401 && (em.getProperty("summoned") == null || em.getProperty("summoned").equals("0"))) { //last stage
	var mobId = 9300139;
	if (em.getProperty("stage").equals("2")) {
	    mobId = 9300140;
	}
	var mob = em.getMonster(mobId);
	eim.registerMonster(mob);
	em.setProperty("summoned", "1");
	eim.getMapInstance(mapid).spawnMonsterOnGroundBelow(mob, new java.awt.Point(240,150));
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    if (mobId == 9300137 || mobId == 9300138) {
	em.setProperty("stage7", "1");
	eim.broadcastPlayerMsg(5, "The one you were protecting has been killed.");
    } else if (mobId == 9300139 || mobId == 9300140) { //boss
	//13 = boss, 14 = urete, 15 = romeo&juliet
	eim.getMapInstance(13).spawnNpc(2112003, new java.awt.Point(-416, -126));
	if (em.getProperty("stage7").equals("0")) {
		eim.getMapInstance(14).spawnNpc(2112012, new java.awt.Point(232, 150));
		eim.getMapInstance(15).spawnNpc(2112018, new java.awt.Point(111, 128));
		eim.getMapInstance(15).spawnNpc(2112012, new java.awt.Point(320, 128));
	} else {
		eim.getMapInstance(14).spawnNpc(2112011, new java.awt.Point(232, 150));
		eim.getMapInstance(15).spawnNpc(2112009, new java.awt.Point(111, 128));
		eim.getMapInstance(15).spawnNpc(2112008, new java.awt.Point(211, 128));
	}
    }
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
    eim.disposeIfPlayerBelow(100, 926110700);
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