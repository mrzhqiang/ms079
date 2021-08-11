/*
	Encrypted Slate of the Squad - Leafre Cave of life
*/

var status = -1;
var minLevel = 80; // 35
var maxLevel = 200; // 65

var minPartySize = 1;
var maxPartySize = 6;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	    return;
	}
	status--;
    }

//    if (cm.getMapId() == 240050400) {
    if (status == 0) {
	if (cm.getParty() == null) { // No Party
	    cm.sendOk("很抱歉，你好像没有组队。。。");
	} else if (!cm.isLeader()) { // Not Party Leader
	    cm.sendOk("如果你想尝试任务请找你的 #b队长#k 来和我说话。#b");
	} else {
	    // Check if all party members are within PQ levels
	    var party = cm.getParty().getMembers();
	    var mapId = cm.getMapId();
	    var next = true;
	    var levelValid = 0;
	    var inMap = 0;
	    var it = party.iterator();

	    while (it.hasNext()) {
		var cPlayer = it.next();
		if ((cPlayer.getLevel() >= minLevel) && (cPlayer.getLevel() <= maxLevel)) {
		    levelValid += 1;
		} else {
		    next = false;
		}
		if (cPlayer.getMapid() == mapId) {
		    inMap += (cPlayer.getJobId() == 900 ? 6 : 1);
		}
	    }
	    if (party.size() > maxPartySize || inMap < minPartySize) {
		next = false;
	    }
	    if (next) {
		var em = cm.getEventManager("HontalePQ");
		if (em == null) {
		    cm.sendSimple("找不到脚本，请联系GM！！#b");
		} else {
		    var prop = em.getProperty("state");
		    if (prop.equals("0") || prop == null) {
			em.startInstance(cm.getParty(), cm.getMap());
			cm.removeAll(4001022);
			cm.removeAll(4001023);
			cm.dispose();
			return;
		    } else {
			cm.sendSimple("#b(一座石碑，上面写着看不懂的文字……。)#b");
			cm.dispose();
		    }
		}
	    } else {
		cm.sendOk("很抱歉，你的组队好像没有符合需求:\r\n\r\n#r需求: " + minPartySize + " 需要六个人且等级都必须在 " + minLevel + " 到 " + maxLevel + ".#b");
		cm.dispose();
	    }
	}
	} else {
	if (cm.getMapId() == 240050300) {
        if (cm.isLeader() && cm.haveItem(4001093, 6)) {
             cm.showEffect(true, "quest/party/clear");
             cm.playSound(true, "Party1/Clear");
             cm.gainItem(4001093, -6);
             cm.warpParty(240050400);
			} else {
             cm.sendOk("请叫你的队长带着6个蓝色钥匙来找我");
			 cm.dispose();
              }
        }
        }
}