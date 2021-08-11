importPackage(Packages.tools);
importPackage(Packages.tools.packet);

//pvp mode: 12 = "1.5 event", 13 = "2x event"

function init() {
}

function setup(mapid) { //mapid = ((level, 0-3) (type, 0-3) (mapid)) in one string
    var eim = em.newInstance("PVP" + mapid.replace(" ", "").replace(" ", ""));
	eim.setProperty("lvl", mapid.split(" ")[0]);
	eim.setProperty("type", mapid.split(" ")[1]);
	var map = parseInt(mapid.split(" ")[2]);
	eim.setProperty("map", "" + map);
	eim.setProperty("started", "0");
	eim.setProperty("champion", "0");
	
	eim.setProperty("ice", "0");
	eim.setProperty("icegage", "0");
	
	eim.setProperty("red", "0");
	eim.setProperty("blue", "0");
	eim.setProperty("redflag", "0");
	eim.setProperty("blueflag", "0");
    eim.createInstanceMap(map).resetFully();
    return eim;
}

function playerEntry(eim, player) {
	var type = parseInt(eim.getProperty("type"));
    var map = eim.getMapInstance(0);
	player.changeTeam(type == 2 || map.getAndSwitchTeam() ? 0 : 1);
    player.changeMap(map, map.getPortal(type == 0 ? 0 : (type == 3 ? (player.getTeam() == 0 ? 3 : 1) : (player.getTeam() == 0 ? 2 : 3))));
	eim.setProperty("" + player.getId(), "0");
	eim.broadcastPlayerMsg(-7, player.getName() + " has entered.");
	player.getStat().recalcLocalStats(player);
	player.getStat().heal(player);
	
	var i = getMaxPlayerCount(type);
	broadcastType(eim, player);
	if (eim.getProperty("started").equals("0")) {
		eim.broadcastPacket(UIPacket.getMidMsg("Current: " + eim.getPlayerCount() + "/ Needed to Start: " + i, true, 1));
		eim.broadcastPacket(UIPacket.getMidMsg("Currently recruiting players for Battle Mode.", true, 0));
		if (eim.getPlayerCount() >= (player.isGM() ? 2 : i)) {
			eim.broadcastPacket(UIPacket.clearMidMsg());
			eim.broadcastPacket(MaplePacketCreator.getPVPScore(0, false));
			eim.broadcastPacket(MaplePacketCreator.getPVPMode(type));
			eim.broadcastPacket(MaplePacketCreator.getPVPMode(12)); //PVP 1.5 EVENT!
			eim.broadcastPacket(MaplePacketCreator.enablePVP(true));
			eim.setProperty("started", "1");
			doItemDrop(eim);
			eim.startEventTimer(type == 2 ? 420000 : 600000);
			eim.schedule("championCheck", 300000);
			if (type == 2) {
				var players = eim.getPlayers();
				var ice = players.get(java.lang.Math.floor(java.lang.Math.random() * players.size()));
				eim.setProperty("ice", ice.getId() + "");
				ice.changeTeam(1);
				eim.applySkill(ice, ice.getStat().getSkillByJob(1105, ice.getJob()));
				ice.getStat().recalcLocalStats(ice);
				ice.getStat().heal(ice);
			} else if (type == 3) {
				map.spawnAutoDrop(2910000, map.getGuardians().get(0).left);
				map.spawnAutoDrop(2910001, map.getGuardians().get(1).left);
				eim.broadcastPacket(MaplePacketCreator.getCapturePosition(map));
				eim.broadcastPacket(MaplePacketCreator.resetCapture());
			}
			updateScoreboard(eim, true);
		}
	} else {
		if (type != 0) {
			player.getClient().getSession().write(MaplePacketCreator.getPVPPoints(parseInt(eim.getProperty("red")), parseInt(eim.getProperty("blue"))));
		}
		player.getClient().getSession().write(MaplePacketCreator.getPVPScore(0, false));
	}
}

function broadcastType(eim) {
	var type = parseInt(eim.getProperty("type"));
	var lvl = parseInt(eim.getProperty("lvl"));
	var players = eim.getPlayers();
	var c1 = eim.newPair()
	var c2 = eim.newPair();
	for (var xx = 0; xx < players.size(); xx++) {
		eim.addToPair(players.get(xx).getTeam() == 0 ? c1 : c2, players.get(xx).getId(), players.get(xx).getName());
	}
	eim.broadcastTeamPacket(MaplePacketCreator.getPVPType(type, c1, 1, !eim.getProperty("started").equals("0"), lvl), 0);
	eim.broadcastTeamPacket(MaplePacketCreator.getPVPTeam(c1), 0);
	
	eim.broadcastTeamPacket(MaplePacketCreator.getPVPType(type, c2, 2, !eim.getProperty("started").equals("0"), lvl), 1);
	eim.broadcastTeamPacket(MaplePacketCreator.getPVPTeam(c2), 1);
}

function broadcastType(eim, player) {
	if (player == null) {
		broadcastType(eim);
		return;
	}
	var type = parseInt(eim.getProperty("type"));
	var lvl = parseInt(eim.getProperty("lvl"));
	var players = eim.getPlayers();
	var c1 = eim.newPair()
	var c2 = eim.newPair();
	for (var xx = 0; xx < players.size(); xx++) {
		eim.addToPair(players.get(xx).getTeam() == 0 ? c1 : c2, players.get(xx).getId(), players.get(xx).getName());
	}
	eim.broadcastTeamPacket(MaplePacketCreator.getPVPType(type, player.getTeam() == 0 ? c1 : c2, player.getTeam() + 1, !eim.getProperty("started").equals("0"), lvl), player.getTeam());
	eim.broadcastTeamPacket(MaplePacketCreator.getPVPTeam(player.getTeam() == 0 ? c1 : c2), player.getTeam());
}


function getMaxPlayerCount(type) {
	switch(type) {
		case 0:
			return 4;
		case 1:
		case 3:
			return 6;
		case 2:
			return 10;
	}
	return 0;
}

function doItemDrop(eim) {
    var map = eim.getMapInstance(0);
	var d = map.getFootholds().getAllRelevants();
	var num = java.lang.Math.floor(java.lang.Math.random() * 10) + 5;
	for (var i = 0; i < num; i++) {
		var r = java.lang.Math.floor(java.lang.Math.random() * 50) + 2900000;
		if (r != 2900049 && r != 2900043 && r != 2900042 && r != 2900039 && r != 2900038 && r != 2900037 && r != 2900029 && r != 2900016 && r != 2900015) {
			map.spawnAutoDrop(r, new java.awt.Point(d.get(java.lang.Math.floor(java.lang.Math.random() * d.size())).getPoint1()));
		}
	}
	eim.schedule("doItemDrop", 60000);
}

function addPVPScore(eim, player, score) {
	var x = parseInt(eim.getProperty("" + player.getId()));
	eim.setProperty("" + player.getId(), "" + (x + score));
	
	if (parseInt(eim.getProperty("ice")) == player.getId()) {
		var xx = java.lang.Math.min(100, parseInt(eim.getProperty("icegage")) + x);
		eim.setProperty("icegage", "" + xx);
		player.getClient().getSession().write(MaplePacketCreator.getPVPIceGage(xx));
		player.applyIceGage(xx);
	}
	
	var expAmount = (((x + score) / 10) | 0) - ((x / 10) | 0);
	if (expAmount > 0) { //
		player.gainExp((expAmount * player.getClient().getChannelServer().getExpRate() * calcFluctuation(x + score, calcExpGain(player), false)) | 0, true, false, true);
	}
}

function calcExpGain(player) {
	return (player.getNeededExp() / (player.getLevel() * 100)) | 0;
}

function calcFluctuation(score, exp, addScore) {
	if (score <= 500) {
		return 1.0 * exp * (addScore ? ((score / 10) | 0) : 1);
	} else if (score <= 1000) {
		return 0.5 * exp * (addScore ? ((score / 10) | 0) : 1);
	} else if (score <= 1500) {
		return 0.1 * exp * (addScore ? ((score / 10) | 0) : 1);
	} else {
		return 0.05 * exp * (addScore ? ((score / 10) | 0) : 1);
	}
}

function calcTotal(score, exp) {
	return exp * (calcFluctuation(java.lang.Math.min(score, 500), 1, true) + calcFluctuation(java.lang.Math.min(score, 1000), 1, true) + calcFluctuation(java.lang.Math.min(score, 1500), 1, true) + calcFluctuation(java.lang.Math.max(score - 1500, 0), 1, true));
}

function getWinningTeam(eim) {
	var score1 = parseInt(eim.getProperty("red"));
	var score2 = parseInt(eim.getProperty("blue"));
	return (score1 > score2 ? 0 : 1);
}

function playerDead(eim, player) {
	player.getClient().getSession().write(MaplePacketCreator.getPVPKilled(player.getName() + " has been defeated."));
	if (parseInt(eim.getProperty("ice")) == player.getId()) {
		eim.setProperty("ice", "0");
		eim.setProperty("red", "99999");
		eim.broadcastPlayerMsg(-7, "The Ice Knight has been defeated.");
		end(eim);
		return;
	}
	eim.broadcastPlayerMsg(-7, player.getName() + " has been defeated.");
	var type = parseInt(eim.getProperty("type"));
	if (type != 3) {
		if (player.getTeam() == 0) {
			var i = parseInt(eim.getProperty("blue"));
			eim.setProperty("blue", "" + (i+1));
		} else {
			var i = parseInt(eim.getProperty("red"));
			eim.setProperty("red", "" + (i+1));
		}
	} else {
		var map = eim.getMapInstance(0);
		if (parseInt(eim.getProperty("redflag")) == player.getId()) {
			eim.setProperty("redflag", "0");
			eim.broadcastPlayerMsg(-7, "The Red Flag has been dropped!");
			map.spawnAutoDrop(2910000, player.getPosition());
			eim.broadcastPacket(MaplePacketCreator.getCapturePosition(map));
			eim.broadcastPacket(MaplePacketCreator.resetCapture());
		} else if (parseInt(eim.getProperty("blueflag")) == player.getId()) {
			eim.setProperty("blueflag", "0");
			eim.broadcastPlayerMsg(-7, "The Blue Flag has been dropped!");
			map.spawnAutoDrop(2910001, player.getPosition());
			eim.broadcastPacket(MaplePacketCreator.getCapturePosition(map));
			eim.broadcastPacket(MaplePacketCreator.resetCapture());
		}
	}
	player.cancelAllBuffs();
	player.clearAllCooldowns();
	player.dispelDebuffs();
}

function championCheck(eim) {
	var highestScore = 0;
	var champion = null;
	var players = eim.getPlayers();
	for (var i = 0; i < players.size(); i++) {
		if (parseInt(eim.getProperty("" + players.get(i).getId())) > highestScore) {
			highestScore = parseInt(eim.getProperty("" + players.get(i).getId()));
			champion = players.get(i);
		}
	}
	if (champion != null) {
		eim.setProperty("champion", champion.getId() + "");
		eim.broadcastPlayerMsg(-7, champion.getName() + " has been made the champion.");
		champion.getClient().getSession().write(MaplePacketCreator.showOwnChampionEffect());
		champion.getMap().broadcastMessage(champion, MaplePacketCreator.showChampionEffect(champion.getId()), false);
	}
}

function updateScoreboard(eim) {
	updateScoreboard(eim, true);
}

function updateScoreboard(eim, score) {
	var players = eim.getPlayers();
	var c = eim.newPair_chr();
	var ty = parseInt(eim.getProperty("type"));
	var lv = parseInt(eim.getProperty("lvl"));
	for (var i = 0; i < players.size(); i++) {
		eim.addToPair_chr(c, parseInt(eim.getProperty("" + players.get(i).getId())), players.get(i));
	}
	if (score == true || score == null) {
		eim.broadcastPacket(MaplePacketCreator.getPVPScoreboard(c, ty));
		if (ty != 0 && ty != 2) {
			eim.broadcastPacket(MaplePacketCreator.getPVPPoints(parseInt(eim.getProperty("red")), parseInt(eim.getProperty("blue"))));
			if (ty == 3 && (parseInt(eim.getProperty("blue")) >= 3 || parseInt(eim.getProperty("red")) >= 3)) {
				end(eim);
			}
		}
	} else {
		var winningTeam = (ty == 0 ? 0 : getWinningTeam(eim));
		for (var i = 0; i < players.size(); i++) {
			var x = parseInt(eim.getProperty("" + players.get(i).getId()));
			var total = calcTotal(x, calcExpGain(players.get(i)));
			total += ((total * (ty != 0 && players.get(i).getTeam() == winningTeam ? 60 : 30)) / 100) | 0;
			if (parseInt(eim.getProperty("champion")) == players.get(i).getId()) {
				total += ((total * 20) / 100) | 0; //guessing
			}
			if (lv < 2 && players.get(i).getLevel() >= 120) { //rookie or gladiator
				total /= 2;
				x /= 2;
			}
			var exp = (total * 2 * players.get(i).getClient().getChannelServer().getExpRate()) | 0;
			players.get(i).gainExp(exp, true, true, true);
			players.get(i).setTotalBattleExp(players.get(i).getTotalBattleExp() + (((x / 10) * 1.5) | 0)); //PVP 1.5 EVENT!
			players.get(i).setBattlePoints(players.get(i).getBattlePoints() + (((x / 10) * 1.5) | 0));
			players.get(i).getClient().getSession().write(MaplePacketCreator.getPVPResult(c, exp, winningTeam + 1, players.get(i).getTeam() + 1));
		}
	}
}
			
function playerRevive(eim, player) {
}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
	if (mapid == parseInt(eim.getProperty("map"))) {
	    return;
	}
	playerExit(eim, player);
}

function playerDisconnected(eim, player) {
	if (eim.getPlayerCount() > 0 && !eim.getProperty("started").equals("2") && (eim.getPlayerCount() < eim.getProperty("started").equals("1") ? 4 : 1 || parseInt(eim.getProperty("ice")) == player.getId())) {
		end(eim);
	}
	player.setMap(player.getClient().getChannelServer().getMapFactory().getMap(960000000));
    return 0;
}

function monsterValue(eim, mobId) {
    return 1;
}

function playerExit(eim, player) {
	player.getClient().getSession().write(UIPacket.clearMidMsg());
    eim.unregisterPlayer(player);
	if (eim.getPlayerCount() > 0 && !eim.getProperty("started").equals("2") && (eim.getPlayerCount() < eim.getProperty("started").equals("1") ? 2 : 1 || parseInt(eim.getProperty("ice")) == player.getId())) {
		end(eim);
	} else {
		eim.disposeIfPlayerBelow(0, 0);
	}
}

function end(eim) {
	eim.setProperty("started", "2");
	eim.broadcastPacket(MaplePacketCreator.getPVPMode(6));
	eim.broadcastPacket(MaplePacketCreator.enablePVP(false));
	updateScoreboard(eim, false);
	var players = eim.getPlayers();
	for (var i = 0; i < players.size(); i++) {
		players.get(i).getClient().getSession().write(UIPacket.clearMidMsg());
		players.get(i).cancelAllBuffs();
		players.get(i).dispelDebuffs();
		players.get(i).changeRemoval();
		players.get(i).clearAllCooldowns();
		players.get(i).getStat().heal(players.get(i));
	}
    eim.disposeIfPlayerBelow(100, 960000000);
	for (var i = 0; i < players.size(); i++) {
		players.get(i).getStat().recalcLocalStats(players.get(i));
		players.get(i).getStat().heal(players.get(i));
	}
}

function clearPQ(eim) {
    end(eim);
}

function allMonstersDead(eim) {
}

function leftParty (eim, player) {
    // If only 2 players are left, uncompletable:
}
function disbandParty (eim) {
}
function cancelSchedule() {}