var exitMap = 0;
var waitingMap = 1;
var reviveMap = 2;
var winnerMap = 3;
var loserMap = 4;
var bossMap = 5;
var redFirstMap = 6;
var blueFirstMap = 11;

function init() {
}

function monsterValue(eim, mobId) {
    return 1;
}

function setup(mapid) {
    //mapid = 0-4
    var map = (parseInt(mapid) + 1) * 100;
    var eim = em.newInstance("Ghost" + mapid);
    eim.setInstanceMap(923020001); // <exit>
    eim.setInstanceMap(map+923020000);
    eim.setInstanceMap(map+923020009);
    eim.setInstanceMap(923020010);
    eim.setInstanceMap(923020020);
    eim.setInstanceMap(map+923020090).resetFully();
    for (var i = 0; i < 5; i++) {
	eim.setInstanceMap(923020010+map+i).resetFully();
    }//red, THEN blue
    for (var i = 0; i < 5; i++) {
	eim.setInstanceMap(923020020+map+i).resetFully();
    }
    eim.setProperty("forfeit", "false");
    eim.setProperty("blue", "-1");
    eim.setProperty("red", "-1");
    eim.setProperty("started", "false");
    eim.setProperty("boss", "false");
    eim.setProperty("finished", "false");

    //ghost status
    eim.setProperty("Red_Stage", "1");
    eim.setProperty("Blue_Stage", "1");
    eim.setProperty("redTeamDamage", "0");
    eim.setProperty("blueTeamDamage", "0");
    return eim;
}

function broadcastUpdate(eim, player, onlypoint) {
    player.writePoint("PRaid_Point", eim.getProperty(player.getId() + "-Ghost_Point"));
    if (onlypoint) {
	return;
    }
    if (eim.getProperty("boss").equals("false")) {
        player.writeEnergy("PRaid_Team", player.getCarnivalParty().getTeam() == 0 ? "redTeam" : "blueTeam");
        player.writeStatus("Red_Stage", eim.getProperty("Red_Stage"));
        player.writeStatus("Blue_Stage", eim.getProperty("Blue_Stage"));
    } else if (eim.getProperty("finished").equals("false")) {
        player.writeStatus("Red_Stage", "B");
        player.writeStatus("Blue_Stage", "B");
        player.writeStatus("redTeamDamage", eim.getProperty("redTeamDamage"));
        player.writeStatus("blueTeamDamage", eim.getProperty("blueTeamDamage"));
    } else {
        player.writeStatus("Red_Stage", "BC");
        player.writeStatus("Blue_Stage", "BC");
	var pp = eim.getProperty(player.getId() + "-Ghost_Point");
	var pb = eim.getProperty(player.getId() + "-PRaid_Bonus");
	player.writeEnergy("PRaid_Point", parseInt(pp)*20);
	player.writeEnergy("PRaid_Bonus", pb);
	eim.setProperty(player.getId() + "-PRaid_Total", ((parseInt(pp)*20 + parseInt(pb))) + "");
	player.writeEnergy("PRaid_Total", eim.getProperty(player.getId() + "-PRaid_Total"));
	player.writeEnergy("PRaid_Team", "");
    }
}

function playerEntry(eim, player) {
    player.disposeClones();
    player.changeMap(eim.getMapInstance(waitingMap), eim.getMapInstance(waitingMap).getPortal(0));
    eim.setProperty(player.getId() + "-Ghost_Point", "0");//ghost point

    eim.setProperty(player.getId() + "-PRaid_Bonus", "0");
    eim.setProperty(player.getId() + "-PRaid_Total", "0");
}


function registerCarnivalParty(eim, carnivalParty) {
    if (eim.getProperty("red").equals("-1")) {
        eim.setProperty("red", carnivalParty.getLeader().getId() + "");
        // display message about recieving invites for next 3 minutes;
	//eim.restartEventTimer(180000);
        eim.schedule("end", 3 * 60 * 1000); // 3 minutes
    } else {
        eim.setProperty("blue", carnivalParty.getLeader().getId() + "");
	//eim.restartEventTimer(10000);
        eim.schedule("start", 10000);
    }
}
function playerDead(eim, player) {
}

function leftParty(eim, player) {
    disbandParty(eim);
}

function disbandParty(eim) {
    //if (eim.getProperty("started").equals("true")) {
    //    warpOut(eim);
    //} else {
	disposeAll(eim);
    //}
}

function disposeAll(eim) {
    	var iter = eim.getPlayers().iterator();
    	while (iter.hasNext()) {
	    var player = iter.next();
            eim.unregisterPlayer(player);
            player.changeMap(eim.getMapInstance(exitMap), eim.getMapInstance(exitMap).getPortal(0));
	    if (player.getCarnivalParty() != null) {
            	player.getCarnivalParty().removeMember(player);
	    }
        }
        eim.dispose();
}

function playerExit(eim, player) {
    eim.unregisterPlayer(player);
    player.getCarnivalParty().removeMember(player);
    player.changeMap(eim.getMapInstance(exitMap), eim.getMapInstance(exitMap).getPortal(0));
    eim.disposeIfPlayerBelow(0, 0);
}

//for offline players
function removePlayer(eim, player) {
    eim.unregisterPlayer(player);
    player.getCarnivalParty().removeMember(player);
    player.getMap().removePlayer(player);
    player.setMap(eim.getMapInstance(exitMap));
    eim.disposeIfPlayerBelow(0, 0);
}


function getParty(eim, property) {
    var chr = em.getChannelServer().getPlayerStorage().getCharacterById(parseInt(eim.getProperty(property)));
    if (chr == null || chr.getCarnivalParty() == null) {
	eim.broadcastPlayerMsg(5, "The leader of the party " + property + " was not found.");
	disposeAll(eim);
	return null;
    } else {
	return chr.getCarnivalParty();
    }
}

function start(eim) {
    eim.setProperty("started", "true");
    eim.startEventTimer(30 * 60 * 1000);
    var blueParty = getParty(eim, "blue");
    var redParty = getParty(eim, "red");
    if (blueParty == null || redParty == null) { //already taken care of
	return;
    }
    blueParty.warp(eim.getMapInstance(blueFirstMap), 1);
    redParty.warp(eim.getMapInstance(redFirstMap), 1);
}

function monsterKilled(eim, chr, cp) { 
    //if (eim.getProperty("boss").equals("false")) {
    //	eim.setProperty(chr.getId() + "-Ghost_Point", (parseInt(eim.getProperty(chr.getId() + "-Ghost_Point")) + cp) + "");
    //	broadcastUpdate(eim, chr);
    //} else {//give to whole partyy
    	var iter = eim.getPlayers().iterator();
    	while (iter.hasNext()) {
	    var player = iter.next();
	    if (player.getCarnivalParty().getTeam() == chr.getCarnivalParty().getTeam()) {
	    	eim.setProperty(player.getId() + "-Ghost_Point", (parseInt(eim.getProperty(player.getId() + "-Ghost_Point")) + cp) + "");
	    	broadcastUpdate(eim, player, true);
	    }
	}
    //}
}

function monsterValue(eim, mobId) {
    return 0;
}

function monsterDamaged(eim, chr, mobId, damage) {
    if (mobId == 9700037) { //boss
	var b = parseInt(eim.getProperty("blueTeamDamage"));
	var r = parseInt(eim.getProperty("redTeamDamage"));
	if (chr.getCarnivalParty().getTeam() == 0) {
	    eim.setProperty("redTeamDamage", (r+damage)+"");
	} else {
	    eim.setProperty("blueTeamDamage", (b+damage)+"");
	}
	eim.setProperty(chr.getId() + "-PRaid_Bonus", parseInt(eim.getProperty(chr.getId() + "-PRaid_Bonus")) + (damage / 100));
    	var iter = eim.getPlayers().iterator();
    	while (iter.hasNext()) {
	    broadcastUpdate(eim, iter.next(), false);
	}
	if ((b >= 2000000 && chr.getCarnivalParty().getTeam() == 1) || (r >= 2000000 && chr.getCarnivalParty().getTeam() == 0)) { //half hp
	    eim.setProperty("finished", "true"); //must be boss
	    var blueParty = getParty(eim, "blue");
	    var redParty = getParty(eim, "red");
    	    if (b > r) {
        	blueParty.setWinner(true);
    	    } else if (r > b) {
        	redParty.setWinner(true);
    	    }
    	    var iter = eim.getPlayers().iterator();
    	    while (iter.hasNext()) {
	        iter.next().endPartyQuest(1303);
	    }
    	    blueParty.displayMatchResult();
    	    redParty.displayMatchResult();
    	    eim.schedule("warpOut", 10000);
	}
    }
    return 0;
}


function end(eim) {
    if (!eim.getProperty("started").equals("true")) {
        disposeAll(eim);
    }
}

function warpOut(eim) {
    if (!eim.getProperty("started").equals("true")) {
	if (eim.getProperty("blue").equals("-1")) {
            disposeAll(eim);
	}
    } else {
	var blueParty = getParty(eim, "blue");
	var redParty = getParty(eim, "red");
	if (blueParty == null || redParty == null) {
	    return;
	}	
    	if (blueParty.isWinner()) {
    	    blueParty.warp(eim.getMapInstance(winnerMap), 0);
    	    redParty.warp(eim.getMapInstance(loserMap), 0);
    	} else {
    	    redParty.warp(eim.getMapInstance(winnerMap), 0);
    	    blueParty.warp(eim.getMapInstance(loserMap), 0);
    	}
    	eim.disposeIfPlayerBelow(100,0);
    }
}

function scheduledTimeout(eim) {
    eim.stopEventTimer();
    if (!eim.getProperty("started").equals("true")) {
	if (eim.getProperty("blue").equals("-1")) {
            disposeAll(eim);
	}
    } else {
	var b = parseInt(eim.getProperty("blueTeamDamage"));
	var r = parseInt(eim.getProperty("redTeamDamage"));
	eim.setProperty("finished", "true"); //must be boss
	var blueParty = getParty(eim, "blue");
	var redParty = getParty(eim, "red");
	if (eim.getProperty("boss").equals("false")) {
	    b = parseInt(eim.getProperty("Blue_Stage"));
  	    r = parseInt(eim.getProperty("Red_Stage"));
	}
    	if (b > r) {
            blueParty.setWinner(true);
    	} else if (r > b) {
            redParty.setWinner(true);
    	}
    	blueParty.displayMatchResult();
    	redParty.displayMatchResult();
	eim.schedule("warpOut", 10000);
    }
}

function playerRevive(eim, player) {
	player.addHP(50);
player.changeMap(eim.getMapInstance(reviveMap), eim.getMapInstance(reviveMap).getPortal(0));
	return true;
}

function playerDisconnected(eim, player) {
    player.setMap(eim.getMapInstance(exitMap));
    eim.unregisterPlayer(player);
    player.getCarnivalParty().removeMember(player);
    eim.broadcastPlayerMsg(5, player.getName() + " has quit the Party Quest.");
    disposeAll(eim);
}

function onMapLoad(eim, chr) {
    if (chr.getMapId() == eim.getMapInstance(exitMap).getId()) {
	playerExit(eim, chr);
	return;
    }
    if (!eim.getProperty("started").equals("true")) {
        disposeAll(eim);
    } else if (eim.getProperty("boss").equals("false")) {
    	if (chr.getCarnivalParty().getTeam() == 0) {
	    if (chr.getMapId() == eim.getMapInstance(redFirstMap).getId()) {
		chr.tryPartyQuest(1303);
	    } else if (chr.getMapId() == eim.getMapInstance(redFirstMap+1).getId()) {
		eim.setProperty("Red_Stage", "2");
	    } else if (chr.getMapId() == eim.getMapInstance(redFirstMap+2).getId()) {
		eim.setProperty("Red_Stage", "3");
	    } else if (chr.getMapId() == eim.getMapInstance(redFirstMap+3).getId()) {
		eim.setProperty("Red_Stage", "4");
	    } else if (chr.getMapId() == eim.getMapInstance(redFirstMap+4).getId()) {
		eim.setProperty("Red_Stage", "5");
	    } else if (chr.getMapId() == eim.getMapInstance(bossMap).getId()) {
		eim.setProperty("Red_Stage", "B");
		eim.setProperty("Blue_Stage", "B");
		eim.setProperty("boss", "true");
		eim.setProperty("redTeamDamage", "100000");
		eim.broadcastPlayerMsg(6, "Red Team has reached the boss map first and will get a 5% boost.");
        	var iter = eim.getPlayers().iterator();
        	while (iter.hasNext()) {
		    var player = iter.next();
		    player.changeMap(chr.getMap(), chr.getMap().getPortal(player.getCarnivalParty().getTeam() == 0 ? "redTeam" : "blueTeam"));
        	}
	    }
    	} else {
	    if (chr.getMapId() == eim.getMapInstance(blueFirstMap).getId()) {
		chr.tryPartyQuest(1303);
	    } else if (chr.getMapId() == eim.getMapInstance(blueFirstMap+1).getId()) {
		eim.setProperty("Blue_Stage", "2");
	    } else if (chr.getMapId() == eim.getMapInstance(blueFirstMap+2).getId()) {
		eim.setProperty("Blue_Stage", "3");
	    } else if (chr.getMapId() == eim.getMapInstance(blueFirstMap+3).getId()) {
		eim.setProperty("Blue_Stage", "4");
	    } else if (chr.getMapId() == eim.getMapInstance(blueFirstMap+4).getId()) {
		eim.setProperty("Blue_Stage", "5");
	    } else if (chr.getMapId() == eim.getMapInstance(bossMap).getId()) {
		eim.setProperty("Blue_Stage", "B");
		eim.setProperty("Red_Stage", "B");
		eim.setProperty("boss", "true");
		eim.setProperty("blueTeamDamage", "100000");
		eim.broadcastPlayerMsg(6, "Blue Team has reached the boss map first and will get a 5% boost.");
        	var iter = eim.getPlayers().iterator();
        	while (iter.hasNext()) {
		    var player = iter.next();
		    player.changeMap(chr.getMap(), chr.getMap().getPortal(player.getCarnivalParty().getTeam() == 0 ? "redTeam" : "blueTeam"));
        	}
	    }
	}
        var iter = eim.getPlayers().iterator();
       	while (iter.hasNext()) {
	    broadcastUpdate(eim, iter.next(), false);
       	}
    } else if (!eim.getProperty("finished").equals("false")) {
	broadcastUpdate(eim, chr, false);
	chr.gainExp(parseInt(eim.getProperty(chr.getId() + "-PRaid_Total")) * em.getChannelServer().getExpRate(), true, true, false);
    }

}

function cancelSchedule() {
}

function clearPQ(eim) {
}

function allMonstersDead(eim) {
}

function changedMap(eim, chr, mapid) {
}
