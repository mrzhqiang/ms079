var fullhp = 6000000;

function init() {
em.setProperty("state", "0");
em.setProperty("balrogState", "0");
	em.setProperty("leader", "true");
}

function setup(eim, leaderid) {
em.setProperty("state", "1");
em.setProperty("balrogState", "0");
	em.setProperty("leader", "true");
    // Setup the instance when invoked, EG : start PQ
    var eim = em.newInstance("BossBalrog_NORMAL" + leaderid);
	var map = eim.setInstanceMap(105100300);
	map.resetFully();
	eim.setInstanceMap(105100301).resetFully();

    var mob = em.getMonster(8830007);
    var mob2 = em.getMonster(8830004); //left hand is invincible at first
    var mob3 = em.getMonster(8830002);
    var modified = em.newMonsterStats();
modified.setOHp(fullhp); //so they cant possibly kill this
modified.setOMp(mob.getMobMaxMp());
modified.setOExp(0);
    var modified2 = em.newMonsterStats();
modified2.setOHp(fullhp); //so they cant possibly kill this
modified2.setOMp(mob2.getMobMaxMp());
modified2.setOExp(0);
    var modified3 = em.newMonsterStats();
modified3.setOHp(fullhp); //so they cant possibly kill this
modified3.setOMp(mob3.getMobMaxMp());
modified3.setOExp(0);
	mob.setOverrideStats(modified);
	mob2.setOverrideStats(modified2);
	mob3.setOverrideStats(modified3);
    eim.registerMonster(mob);
    eim.registerMonster(mob2);
    eim.registerMonster(mob3);
    map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(416, 258));
    map.spawnMonsterOnGroundBelow(mob2, new java.awt.Point(416, 258));
    map.spawnMonsterOnGroundBelow(mob3, new java.awt.Point(416, 258));
    eim.startEventTimer(3600000);
	eim.schedule("checkHP", 600000);
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
    eim.applyBuff(player, 2022536);
    if (player.haveItem(1302014)) {
	eim.applyBuff(player, 2022537);
    }
}

function changedMap(eim, player, mapid) {
    if (mapid != 105100300 && mapid != 105100301) {
	playerExit(eim,player);
    }
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
	player.dispelBuff(2022536);
	player.dispelBuff(2022537);
    	if (eim.disposeIfPlayerBelow(0, 0)) {
		em.setProperty("state", "0");
	em.setProperty("leader", "true");
em.setProperty("balrogState", "0");
	}
}

function scheduledTimeout(eim) {
    end(eim);
}

function allMonstersDead(eim) {
}


function warpWinnersOut(eim) {
	var party = eim.getPlayers();
	var map = eim.getMapInstance(1);
	for (var i = 0; i < party.size(); i++) {
		party.get(i).changeMap(map, map.getPortal(0));
		party.get(i).dispelBuff(2022536);
		party.get(i).dispelBuff(2022537);
	}
}

function playerDead(eim, player) {
    // Happens when player dies
}

function playerRevive(eim, player) {
    // Happens when player's revived.
    // @Param : returns true/false
	return false;
}

function playerDisconnected(eim, player) {
    return 0;
    // return 0 - Deregister player normally Dispose instance if there are zero player left
    // return x that is > 0 - Deregister player normally + Dispose instance if there x player or below
    // return x that is < 0 - Deregister player normally + Dispose instance if there x player or below, if it's leader = boot all
}

function monsterValue(eim, mobid) {
    // Invoked when a monster that's registered has been killed
    // return x amount for this player - "Saved Points"
	if (em.getProperty("balrogState").equals("1") && eim.getMapInstance(0).getMonsterById(8830007) == null && eim.getMapInstance(0).getMonsterById(8830001) == null && eim.getMapInstance(0).getMonsterById(8830002) == null) {
		eim.broadcastPlayerMsg(6, "Balrog has been beaten!");
		eim.getMapInstance(0).changeEnvironment("balog/clear/stone", 3);
    		eim.restartEventTimer(605000); //10 mins + 5 sec
    		eim.schedule("warpWinnersOut", 5000);
	}
	return 1;
}

function leftParty(eim, player) {
    // Happens when a player left the party
}

function disbandParty(eim, player) {
    // Happens when the party is disbanded by the leader.
}

function end(eim) {
    eim.disposeIfPlayerBelow(100, 105100100);
	em.setProperty("state", "0");
	em.setProperty("leader", "true");
em.setProperty("balrogState", "0");
}

function clearPQ(eim) {
    end(eim);
}

function removePlayer(eim, player) {
    // Happens when the funtion NPCConversationalManager.removePlayerFromInstance() is invoked
}

function registerCarnivalParty(eim, carnivalparty) {
    // Happens when carnival PQ is started. - Unused for now.
}

function onMapLoad(eim, player) {
    // Happens when player change map - Unused for now.
}

function cancelSchedule() {
}

function checkHP(eim) {
	var map = eim.getMapInstance(0);
	var mobs = map.getAllMonstersThreadsafe();
	var hpDone = 0;
	for (var i = 0; i < mobs.size(); i++) {
		hpDone += (fullhp - mobs.get(i).getHp());
	}
	if (hpDone > 120000) { //advance
    		var mob = em.getMonster(8830006);
		eim.registerMonster(mob);
		map.spawnMonsterOnGroundBelow(mob, new java.awt.Point(416, 258));
		map.killMonster(8830004);
		map.killMonster(8830006);
		var mob1 = em.getMonster(8830007); //purple state not used
    		var mob2 = em.getMonster(8830001);
    		var mob3 = em.getMonster(8830002);
		eim.registerMonster(mob1);
		eim.registerMonster(mob2);
		eim.registerMonster(mob3);
		map.killMonster(8830007);
		map.killMonster(8830002);
		map.spawnMonsterOnGroundBelow(mob1, new java.awt.Point(416, 258));
		map.spawnMonsterOnGroundBelow(mob2, new java.awt.Point(416, 258));
		map.spawnMonsterOnGroundBelow(mob3, new java.awt.Point(416, 258));
em.setProperty("balrogState", "1");
	} else {
		eim.broadcastPlayerMsg(6, "扎昆太强，消灭了你.");
		end(eim);
	}
}