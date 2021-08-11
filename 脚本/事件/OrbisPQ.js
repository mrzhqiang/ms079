var minPlayers = 6;
var stg2_combo0 = Array("5", "4", "3", "3", "2");
var stg2_combo1 = Array("0", "0", "1", "0", "1"); //unique combos only
var stg2_combo2 = Array("0", "1", "1", "2", "2");
var stg6_combo = Array("00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16");
var cx = Array(200, -300, -300, -300, 200, 200, 200, -300, -300, 200, 200, -300, -300, 200); //even = 200 odd = -300
var cy = Array(-2321, -2114, -2910, -2510, -1526, -2716, -717, -1310, -3357, -1912, -1122, -1736, -915, -3116);

function init() {
    em.setProperty("state", "0");
    em.setProperty("leader", "true");
}

function setup(eim, leaderid) {
    em.getProperties().clear();
    em.setProperty("state", "1");
    em.setProperty("leader", "true");
    var eim = em.newInstance("OrbisPQ" + leaderid);
    em.setProperty("stage", "0"); //center stage
    em.setProperty("pre", "0"); //first stage
    em.setProperty("finished", "0"); //first stage
    em.setProperty("stage2", "0"); //num.spawned in storage, 15 = done
    em.setProperty("stage3", "0"); //lobby
    em.setProperty("stage4", "0"); //sealed
    var rand_combo = java.lang.Math.floor(java.lang.Math.random() * stg2_combo0.length);
    var rand_num = java.lang.Math.random();
    var combo0 = rand_num < 0.33 ? true : false;
    var combo1 = rand_num < 0.66 ? true : false;
    em.setProperty("stage4_0", combo0 ? stg2_combo0[rand_combo] : (combo1 ? stg2_combo1[rand_combo] : stg2_combo2[rand_combo]));
    em.setProperty("stage4_1", combo0 ? stg2_combo1[rand_combo] : (combo1 ? stg2_combo2[rand_combo] : stg2_combo0[rand_combo]));
    em.setProperty("stage4_2", combo0 ? stg2_combo2[rand_combo] : (combo1 ? stg2_combo0[rand_combo] : stg2_combo1[rand_combo]));
    em.setProperty("stage6", "0"); //on way up ... hard

    for (var b = 0; b < stg6_combo.length; b++) { //stage6_001
        for (var y = 0; y < 4; y++) { //stage number
            em.setProperty("stage6_" + stg6_combo[b] + "" + (y + 1) + "", "0");
        }
    }
    for (var b = 0; b < stg6_combo.length; b++) { //stage6_001  
        var found = false;
        while (!found) {
            for (var x = 0; x < 4; x++) {
                if (!found) {
                    var founded = false;
                    for (var z = 0; z < 4; z++) { //check if any other stages have this value set already.
                        if (em.getProperty("stage6_" + stg6_combo[b] + "" + (z + 1) + "") == null) {
                            em.setProperty("stage6_" + stg6_combo[b] + "" + (z + 1) + "", "0");
                        } else if (em.getProperty("stage6_" + stg6_combo[b] + "" + (z + 1) + "").equals("1")) {
                            founded = true;
                            break;
                        }
                    }
                    if (!founded && java.lang.Math.random() < 0.25) {
                        em.setProperty("stage6_" + stg6_combo[b] + "" + (x + 1) + "", "1");
                        found = true;
                        break;
                    }
                }
            }
        }
    }
    //STILL not done yet! levers = 2 of them
    for (var i = 0; i < 5; i++) {
        em.setProperty("stage62_" + i + "", "0");
    }
    var found_1 = false;
    while (!found_1) {
        for (var i = 0; i < 5; i++) {
            if (em.getProperty("stage62_" + i + "") == null) {
                em.setProperty("stage62_" + i + "", "0");
            } else if (!found_1 && java.lang.Math.random() < 0.2) {
                em.setProperty("stage62_" + i + "", "1");
                found_1 = true;
            }
        }
    }
    var found_2 = false;
    while (!found_2) {
        for (var i = 0; i < 5; i++) {
            if (em.getProperty("stage62_" + i + "") == null) {
                em.setProperty("stage62_" + i + "", "0");
            } else if (!em.getProperty("stage62_" + i + "").equals("1") && !found_2 && java.lang.Math.random() < 0.2) {
                em.setProperty("stage62_" + i + "", "1");
                found_2 = true;
            }
        }
    }
    em.setProperty("stage7", "0"); //papa spawned
    em.setProperty("done", "0");
    eim.setInstanceMap(920010000).resetFully();
    eim.setInstanceMap(920010100).resetFully();
    eim.setInstanceMap(920010200).resetFully();
    eim.setInstanceMap(920010300).resetFully();
    var map = eim.setInstanceMap(920010400);
    map.resetFully();
    //  map.shuffleReactors();
    eim.setInstanceMap(920010500).resetFully();
    eim.setInstanceMap(920010600).resetFully();
    eim.setInstanceMap(920010601).resetFully();
    eim.setInstanceMap(920010602).resetFully();
    eim.setInstanceMap(920010603).resetFully();
    eim.setInstanceMap(920010604).resetFully();
    eim.setInstanceMap(920010700).resetFully();
    var map = eim.setInstanceMap(920010800);
    map.resetFully();
    //  map.shuffleReactors();
    eim.setInstanceMap(920010900).resetFully();
    eim.setInstanceMap(920010910).resetFully();
    eim.setInstanceMap(920010911).resetFully();
    eim.setInstanceMap(920010912).resetFully();
    eim.setInstanceMap(920010920).resetFully();
    eim.setInstanceMap(920010921).resetFully();
    eim.setInstanceMap(920010922).resetFully();
    eim.setInstanceMap(920010930).resetFully();
    eim.setInstanceMap(920010931).resetFully();
    eim.setInstanceMap(920010932).resetFully();
    eim.setInstanceMap(920011000).resetFully();
    eim.setInstanceMap(920011100).resetFully();
    eim.startEventTimer(3600000); //1 hr
    return eim;
}

function playerEntry(eim, player) {
    var map = eim.getMapInstance(0);
    player.changeMap(map, map.getPortal(0));
    eim.applyBuff(player, 2022093);
    player.tryPartyQuest(1203);
}

function playerRevive(eim, player) {}

function scheduledTimeout(eim) {
    end(eim);
}

function changedMap(eim, player, mapid) {
    if (mapid < 920010000 || mapid > 920011100) {
        eim.unregisterPlayer(player);

        if (eim.disposeIfPlayerBelow(0, 0)) {
            em.setProperty("state", "0");
            em.setProperty("leader", "true");
        }
    } else if (mapid == 920011100 && em.getProperty("done").equals("0")) { //bonus
        em.setProperty("done", "1");
        eim.restartEventTimer(60000); //minute
    }
}

function playerDisconnected(eim, player) {
    return 0;
}

function monsterValue(eim, mobId) {
    if (mobId == 9300049 && em.getProperty("stage7").equals("0")) { //dark nep
        eim.broadcastPlayerMsg(6, "运古精灵出现了");
        var mob = em.getMonster(9300039);
        eim.registerMonster(mob);
        em.setProperty("stage7", "0");
        eim.getMapInstance(12).spawnMonsterOnGroundBelow(mob, new java.awt.Point(-830, 563));
    } else if (mobId == 9300040) {
        var st = parseInt(em.getProperty("stage2"));
        if (st < 14) {
            eim.broadcastPlayerMsg(6, "红独角狮出现了");
            var mob = em.getMonster(9300040);
            em.setProperty("stage2", st + 1);
            eim.registerMonster(mob);
            eim.getMapInstance(3).spawnMonsterOnGroundBelow(mob, new java.awt.Point(cx[st], cy[st]));
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
    eim.disposeIfPlayerBelow(100, em.getProperty("done").equals("0") ? 920011200 : 920011300);
    em.setProperty("state", "0");
    em.setProperty("leader", "true");
}

function clearPQ(eim) {
    end(eim);
}

function allMonstersDead(eim) {}

function leftParty(eim, player) {
    end(eim);
}

function disbandParty(eim) {
    end(eim);
}

function playerDead(eim, player) {}

function cancelSchedule() {}