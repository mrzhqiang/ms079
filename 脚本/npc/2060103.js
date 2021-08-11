var status = 0;
var request;
function start() {
    status = -1;
    action(1, 0, 0);
}


function action(mode, type, selection) {
    if (mode == 1)
        status++;
    else
        cm.dispose();
    var em = cm.getEventManager("Ghost");
    if (em == null) {
	cm.sendOk("Please try again later.");
	cm.dispose();
	return;
    }
    switch(cm.getPlayer().getMapId()) {
	case 923020000:
    if (status == 0 && mode == 1) {
        var selStr = "Sign up for Dual Raid!";
	var found = false;
        for (var i = 0; i < 5; i++){
            if (getCPQField(i) != "") {
                selStr += "\r\n#b#L" + i + "# " + getCPQField(i) + "#l#k";
		found = true;
            }
        }
        if (cm.getParty() == null) {
            cm.sendNext("You are not in a party.");
	    cm.dispose();
        } else {
            if (cm.isLeader()) {
		if (found) {
                    cm.sendSimple(selStr);
		} else {
		    cm.sendNext("There are no rooms at the moment.");
		    cm.dispose();
		}
            } else {
                cm.sendNext("Please tell your party leader to speak with me.");
		cm.dispose();
            }
        }
    } else if (status == 1) {
	if (selection >= 0 && selection < 5) {
            if (cm.getEventManager("Ghost").getInstance("Ghost"+selection) == null) {
                if ((cm.getParty() != null && cm.getParty().getMembers().size() == 3) || cm.getPlayer().isGM()) {
                    if (checkLevelsAndMap(60, 200) == 1) {
                        cm.sendOk("A player in your party is not the appropriate level.");
                        cm.dispose();
                    } else if (checkLevelsAndMap(60, 200) == 2) {
                        cm.sendOk("Everyone in your party isnt in this map.");
                        cm.dispose();
                    } else {
                        cm.getEventManager("Ghost").startInstance(""+selection, cm.getPlayer());
                        cm.dispose();
                    }
                } else {
                    cm.sendOk("Your party is not the appropriate size.");
                }
            } else if (cm.getParty() != null && cm.getEventManager("Ghost").getInstance("Ghost"+selection).getPlayerCount() == cm.getParty().getMembers().size()) {
                if (checkLevelsAndMap(60, 200) == 1) {
                    cm.sendOk("A player in your party is not the appropriate level.");
                    cm.dispose();
                } else if (checkLevelsAndMap(60, 200) == 2) {
                    cm.sendOk("Everyone in your party isnt in this map.");
                    cm.dispose();
                } else {
                    //Send challenge packet here
                    var owner = cm.getChannelServer().getPlayerStorage().getCharacterByName(cm.getEventManager("Ghost").getInstance("Ghost"+selection).getPlayers().get(0).getParty().getLeader().getName());
                    owner.addCarnivalRequest(cm.getCarnivalChallenge(cm.getChar()));
                    //if (owner.getConversation() != 1) {
                        cm.openNpc(owner.getClient(), 2060103);
                    //}
                    cm.sendOk("Your challenge has been sent.");
                    cm.dispose();
                }
            } else {
                cm.sendOk("The two parties participating in Dual Raid must have an equal number of party member");
                cm.dispose();
            }
	} else {
	    cm.dispose();
	}
    }
	    break;
	case 923020100:
	case 923020200:
	case 923020300:
	case 923020400:
	case 923020500:
    if (status == 0) {
        request = cm.getNextCarnivalRequest();
        if (request != null) {
            cm.sendYesNo(request.getChallengeInfo() + "\r\nWould you like to battle this party at the Dual Raid?");
        } else {
            cm.sendYesNo("Would you like to get out?");
        }
    } else {
	if (request == null) {
	    cm.warp(923020001,0);
	    cm.dispose();
	    break;
	}
        try {
            cm.getChar().getEventInstance().registerCarnivalParty(request.getChallenger(), request.getChallenger().getMap(), 1);
            cm.dispose();
        } catch (e) {
            cm.sendOk("The challenge is no longer valid.");
        }
        status = -1;
    }
	    break;
	default:
	    if (status == 0) {
	    	cm.sendYesNo("Would you like to get out?");
	    } else {
		cm.warp(923020001,0);
	    }
	    break;
    }
}

function getCPQField(fieldnumber) {
    var status = "";
    var event1 = cm.getEventManager("Ghost");
    if (event1 != null) {
        var event = event1.getInstance("Ghost"+fieldnumber);
        if (event == null) {
            status = "Dual Raid Field "+(fieldnumber+1)+"(3v3)";
        } else if (event != null && (event.getProperty("started").equals("false"))) {
            var averagelevel = 0;
            for (i = 0; i < event.getPlayerCount(); i++) {
                averagelevel += event.getPlayers().get(i).getLevel();
            }
            averagelevel /= event.getPlayerCount();
            status = event.getPlayers().get(0).getParty().getLeader().getName()+"/"+event.getPlayerCount()+"users/Avg. Level "+averagelevel;
        }
    }
    return status;
}

function checkLevelsAndMap(lowestlevel, highestlevel) {
    var party = cm.getParty().getMembers();
    var mapId = cm.getMapId();
    var valid = 0;
    var inMap = 0;

    var it = party.iterator();
    while (it.hasNext()) {
        var cPlayer = it.next();
        if (!(cPlayer.getLevel() >= lowestlevel && cPlayer.getLevel() <= highestlevel) && cPlayer.getJobId() != 900) {
            valid = 1;
        }
        if (cPlayer.getMapid() != mapId) {
            valid = 2;
        }
    }
    return valid;
}