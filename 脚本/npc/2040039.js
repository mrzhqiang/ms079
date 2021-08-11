/*
	Lime Balloon - LudiPQ 4th stage NPC
*/

var exp = 3360;

function action(mode, type, selection) {
    var eim = cm.getEventInstance();
    var stage4status = eim.getProperty("stage4status");

    if (stage4status == null) {
	if (cm.isLeader()) { // Leader
	    var stage4leader = eim.getProperty("stage4leader");
	    if (stage4leader == "done") {

		if (cm.haveItem(4001022,6)) { // Clear stage
		    cm.sendNext("恭喜！你已经通过了第四阶段。快点现在，到第5阶段。");
		    cm.removeAll(4001022);
		    clear(4,eim,cm);
		    cm.givePartyExp(exp);
		} else { // Not done yet
		    cm.sendNext("你确定你有收集了 #r6张 #t4001022##k？？");
		}
		cm.safeDispose();
	    } else {
		cm.sendOk("欢迎来到第四阶段。#b遗弃之塔PQ#k 请收集#r#t4001022##k 来找我即可完成任务。");
		eim.setProperty("stage4leader","done");
		cm.safeDispose();
	    }
	} else { // Members
	    cm.sendNext("欢迎来到第四阶段。#b遗弃之塔PQ#k 请收集#r#t4001022##k 给你的队长，然后叫队长来找我即可完成任务。");
	    cm.safeDispose();
	}
    } else {
	cm.sendNext("恭喜！你已经通过了第四阶段。快点现在，到第5阶段。");
	cm.safeDispose();
    }
}

function clear(stage, eim, cm) {
    eim.setProperty("stage" + stage.toString() + "status","clear");

    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
    cm.environmentChange(true, "gate");
}