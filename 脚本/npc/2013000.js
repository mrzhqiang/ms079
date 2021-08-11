var status = -1;
var minLevel = 51; // 35
var maxLevel = 200; // 65

var minPartySize = 5;
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
	if (cm.getMapId() == 920010000) { //inside orbis pq
		cm.sendOk("我们必须拯救他 需要20个云的碎片");
		cm.dispose();
		return;
	}
    if (status == 0) {
	for (var i = 4001044; i < 4001064; i++) {
		cm.removeAll(i); //holy
	}
	if (cm.getParty() == null) { // No Party
	    cm.sendSimple("你貌似没有达到要求...:\r\n\r\n#r要求: " + minPartySize + " 玩家成员, 每个人的等级必须在 " + minLevel + " 到 等级 " + maxLevel + ".#b\r\n#L0#我要用40个女神的羽翼兑换女神手镯#l");
	} else if (!cm.isLeader()) { // Not Party Leader
	    cm.sendSimple("如果你想做任务，请 #b队长#k 跟我谈.#b\r\n#L0#我要用40个女神的羽翼兑换女神手镯#l");
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
		var em = cm.getEventManager("OrbisPQ");
		if (em == null) {
		    cm.sendSimple("找不到脚本请联络GM#b\r\n#L0#我要用40个女神的羽翼兑换女神手镯#l");
		} else {
		    var prop = em.getProperty("state");
		    if (prop.equals("0") || prop == null) {
			em.startInstance(cm.getParty(), cm.getMap());
			cm.dispose();
			return;
		    } else {
			cm.sendSimple("其他队伍已经在里面做 #r组队任务了#k 请尝试换频道或者等其他队伍完成。#b\r\n#L0#我要用40个女神的羽翼兑换女神手镯#l");
		    }
		}
	    } else {
		cm.sendSimple("你的队伍貌似没有达到要求...:\r\n\r\n#r要求: " + minPartySize + " 玩家成员, 每个人的等级必须在 " + minLevel + " 到 等级 " + maxLevel + ".#b\r\n#L0#我要用40个女神的羽翼兑换女神手镯#l");
	    }
	}
    } else { //broken glass
	if (!cm.canHold(1082232,1)) {
	    cm.sendOk("做好了。");
	} else if (cm.haveItem(4001158,40)) {
	    cm.gainItem(1082232, 1, true);
	    cm.gainItem(4001158, -40, true); 
	} else {
	    cm.sendOk("你没有40个 #t4001158#.");
	}
	cm.dispose();

    }
}