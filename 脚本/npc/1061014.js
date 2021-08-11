/* Mu Young
	Boss Balrog
*/


var status = -1;

function action(mode, type, selection) {
    switch (status) {
	case -1:
	    status = 0;
	    switch (cm.getChannelNumber()) {
		default:
		    cm.sendNext("目前模式为 #i3994116# 如果你想加入这个模式请按下一步  条件是 等级 50 ~ 等级 120 / 远征队人数 1 ~ 30 个");
		    break;
	    }
	    break;
	case 0:
	    var em = cm.getEventManager("BossBalrog");

	    if (em == null) {
		cm.sendOk("目前副本出了一点问题，请联系GM！");
		cm.safeDispose();
		return;
	    }

	var prop = em.getProperty("state");
	if (prop == null || prop.equals("0")) {
		var squadAvailability = cm.getSquadAvailability("BossBalrog");
		if (squadAvailability == -1) {
		    status = 1;
		    cm.sendYesNo("现在可以申请远征队，你想成为远征队队长吗？");

		} else if (squadAvailability == 1) {
		    // -1 = Cancelled, 0 = not, 1 = true
		    var type = cm.isSquadLeader("BossBalrog");
		    if (type == -1) {
				cm.sendOk("已经结束了申请。");
				cm.safeDispose();
		    } else if (type == 0) {
			var memberType = cm.isSquadMember("BossBalrog");
			if (memberType == 2) {
			    cm.sendOk("在远征队的制裁名单。");
			    cm.safeDispose();
			} else if (memberType == 1) {
			    status = 5;
			    cm.sendSimple("你要做什么? \r\n#b#L0#加入远征队#l \r\n#b#L1#退出远征队#l \r\n#b#L2#查看远征队名单#l");
			} else if (memberType == -1) {
			    cm.sendOk("远征队员已经达到30名，请稍后再试。");
			    cm.safeDispose();
			} else {
			    status = 5;
			    cm.sendSimple("你要做什么? \r\n#b#L0#查看远征队名单#l \r\n#b#L1#加入远征队#l \r\n#b#L2#退出远征队#l");
			}
		    } else { // Is leader
			status = 10;
			cm.sendSimple("你现在想做什么？\r\n#b#L0#查看远征队成员。#l \r\n#b#L1#管理远征队成员。#l \r\n#b#L2#编辑限制列表。#l \r\n#r#L3#进入地图。#l");
		    // TODO viewing!
		    }
	    } else {
			var eim = cm.getDisconnected("BossBalrog");
			if (eim == null) {
				cm.sendOk("远征队的挑战已经开始.");
				cm.safeDispose();
			} else {
				cm.sendYesNo("你要继续进行远征任务吗？");
				status = 2;
			}
	    }
	} else {
			var eim = cm.getDisconnected("BossBalrog");
			if (eim == null) {
				cm.sendOk("远征队的挑战已经开始.");
				cm.safeDispose();
			} else {
				cm.sendYesNo("你要继续进行远征任务吗？");
				status = 2;
			}
		}
	    break;
	case 1:
	    if (mode == 1) {
		    var lvl = cm.getPlayerStat("LVL");
		    if (lvl >= 50 && lvl <= 120) {
			if (cm.registerSquad("BossBalrog", 5, " 已经成为了远征队队长。如果你想加入远征队，请重新打开对话申请加入远征队。")) {
				cm.sendOk("你已经成为了远征队队长。接下来的5分钟，请等待队员们的申请。");
			} else {
				cm.sendOk("未知错误.");
			}
		    } else {
			cm.sendNext("有一个远征队成员的等级不是50到120之间。");
		    }
	    } else {
		cm.sendOk("如果你想再次申请远征队的话请告诉我。")
	    }
	    cm.safeDispose();
	    break;
	case 2:
		if (!cm.reAdd("BossBalrog", "BossBalrog")) {
			cm.sendOk("由于未知的错误，操作失败。");
		}
		cm.safeDispose();
		break;
	case 5:
	    if (selection == 0) {
		if (!cm.getSquadList("BossBalrog", 0)) {
		    cm.sendOk("由于未知的错误，操作失败。");
		    cm.safeDispose();
		} else {
		    cm.dispose();
		}
	    } else if (selection == 1) { // join
		var ba = cm.addMember("BossBalrog", true);
		if (ba == 2) {
		    cm.sendOk("远征队员已经达到30名，请稍后再试。");
		    cm.safeDispose();
		} else if (ba == 1) {
		    cm.sendOk("申请加入远征队成功，请等候队长指示。");
		    cm.safeDispose();
		} else {
		    cm.sendOk("你已经参加了远征队，请等候队长指示。");
		    cm.safeDispose();
		}
	    } else {// withdraw
		var baa = cm.addMember("BossBalrog", false);
		if (baa == 1) {
		    cm.sendOk("成功退出远征队。");
		    cm.safeDispose();
		} else {
		    cm.sendOk("你没有参加远征队。");
		    cm.safeDispose();
		}
	    }
	    break;
	case 10:
	    if (selection == 0) {
		if (!cm.getSquadList("BossBalrog", 0)) {
		    cm.sendOk("由于未知的错误，操作失败。");
		}
		cm.safeDispose();
	    } else if (selection == 1) {
		status = 11;
		if (!cm.getSquadList("BossBalrog", 1)) {
		    cm.sendOk("由于未知的错误，操作失败。");
		}
		cm.safeDispose();
	    } else if (selection == 2) {
		status = 12;
		if (!cm.getSquadList("BossBalrog", 2)) {
		    cm.sendOk("由于未知的错误，操作失败。");
		}
		cm.safeDispose();
	    } else if (selection == 3) { // get insode
		if (cm.getSquad("BossBalrog") != null) {
		    var dd = cm.getEventManager("BossBalrog");
		    dd.startInstance(cm.getSquad("BossBalrog"), cm.getMap());
		    cm.dispose();
		} else {
		    cm.sendOk("由于未知的错误，操作失败。");
		    cm.safeDispose();
		}
	    }
	    break;
	case 11:
	    cm.banMember("BossBalrog", selection);
	    cm.dispose();
	    break;
	case 12:
	    if (selection != -1) {
		cm.acceptMember("BossBalrog", selection);
	    }
	    cm.dispose();
	    break;
    }
}