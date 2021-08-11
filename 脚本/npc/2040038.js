
/*
	Yellow Balloon - LudiPQ 3rd stage NPC
*/

var status = -1;
var exp = 2940;
			
function action(mode, type, selection) {
    var eim = cm.getEventInstance();
    var stage3status = eim.getProperty("stage3status");

    if (stage3status == null) {
	if (cm.isLeader()) { // Leader
	    var stage3leader = eim.getProperty("stage3leader");
	    if (stage3leader == "done") {

		if (cm.haveItem(4001022, 32)) { // Clear stage
		    cm.sendNext("恭喜！你已经通过了第三阶段。快点现在，到第4阶段。");
		    cm.removeAll(4001022);
		    clear(3,eim,cm);
		    cm.givePartyExp(exp, eim.getPlayers());
		} else { // Not done yet
		    cm.sendNext("你确定你有收集了 #r32张 #t4001022##k？？");
		}
	    } else {
		cm.sendOk("欢迎来到第三阶段。#b遗弃之塔PQ#k 请收集#r#t4001022##k 来找我即可完成任务。");
		eim.setProperty("stage3leader","done");
	    }
	} else { // Members
	    cm.sendNext("欢迎来到第三阶段。#b遗弃之塔PQ#k 请收集#r#t4001022##k 给你的队长，然后叫队长来找我即可完成任务。");
	}
    } else {
	cm.sendNext("恭喜！你已经通过了第三阶段。快点现在，到第4阶段。");
    }
    cm.safeDispose();
}

function clear(stage, eim, cm) {
    eim.setProperty("stage" + stage.toString() + "status","clear");

    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
    cm.environmentChange(true, "gate");
}