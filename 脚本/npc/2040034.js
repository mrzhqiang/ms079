/*
	Red Sign - 101st Floor Eos Tower (221024500)
*/

var status = -1;
var minLevel = 35; // 35
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

    if (status == 0) {
	cm.removeAll(4001022);
	cm.removeAll(4001023);
	if (cm.getParty() == null) { // No Party
	    cm.sendSimple("你貌似没有达到要求...:\r\n\r\n#r要求: " + minPartySize + " 玩家成员, 每个人的等级必须在 " + minLevel + " 到 等级 " + maxLevel + ".#b\r\n#L0#我要兑换有裂痕的眼镜#l");
	} else if (!cm.isLeader()) { // Not Party Leader
	    cm.sendSimple("如果你想做任务，请 #b队长#k 跟我谈.#b\r\n#L0#我要兑换有裂痕的眼镜#l");
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
		var em = cm.getEventManager("LudiPQ");
		if (em == null) {
		    cm.sendSimple("找不到脚本请联络GM#b\r\n#L0#我要兑换有裂痕的眼镜#l");
		} else {
		    var prop = em.getProperty("state");
		    if (prop.equals("0") || prop == null) {
			em.startInstance(cm.getParty(), cm.getMap());
			cm.removeAll(4001022);
			cm.removeAll(4001023);
			cm.dispose();
			return;
		    } else {
			cm.sendSimple("其他队伍已经在里面做 #r组队任务了#k 请尝试换频道或者等其他队伍完成。#b\r\n#L0#我要兑换有裂痕的眼镜#l");
		    }
		}
	    } else {
		cm.sendSimple("你的队伍貌似没有达到要求...:\r\n\r\n#r要求: " + minPartySize + " 玩家成员, 每个人的等级必须在 " + minLevel + " 到 等级 " + maxLevel + ".#b\r\n#L0#我要兑换有裂痕的眼镜#l");
	    }
	}
    } else { //broken glass
	var cmp = cm.getPlayer().getOneInfo(1202, "cmp");
	if (cm.haveItem(1022073,1)) {
	    cm.sendOk("做好了。");
	} else if (!cm.canHold(1022073,1)) {
	    cm.sendOk("请空出一些装备拦空间。");
	} else if (cmp != null && parseInt(cmp) >= 35) {
	    if (cm.getPlayer().getOneInfo(1202, "have") == null || cm.getPlayer().getOneInfo(1202, "have").equals("0")) {
	    	cm.gainItem(1022073, 1, true); //should handle automatically for "have"
	    } else {
		cm.sendOk("你已经有#t1022073#了.");
	    }
	} else {
	    cm.sendOk("你还没有做35次PQ 目前做了: " + (cmp == null ? "0" : cmp) + "次");
	}
	cm.dispose();

    }
}
