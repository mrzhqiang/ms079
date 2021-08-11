/**
	Orange Balloon - LudiPQ 2nd stage NPC
**/

var status;
var exp = 2520;
			
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    var eim = cm.getEventInstance();
    var stage2status = eim.getProperty("stage2status");
    
    if (stage2status == null) {
	if (cm.isLeader()) { // Leader
	    var stage2leader = eim.getProperty("stage2leader");
	    if (stage2leader == "done") {

		if (cm.haveItem(4001022, 15)) { // Clear stage
		    cm.sendNext("恭喜！你已经通过了第二阶段。快点现在，到第三阶段。");
		    cm.removeAll(4001022);
		    clear(2, eim, cm);
		    cm.givePartyExp(2520);
		    cm.dispose();
		} else { // Not done yet
		    cm.sendNext("你确定你有收集了 #r15张 #t4001022##k？？");
		}
		cm.dispose();
	    } else {
		cm.sendOk("欢迎来到第二阶段。#b遗弃之塔PQ#k 请收集#r#t4001022##k 来找我即可完成任务。");
		eim.setProperty("stage2leader","done");
		cm.dispose();
	    }
	} else { // Members
	    cm.sendNext("欢迎来到第二阶段。#b遗弃之塔PQ#k 请收集#r#t4001022##k 给你的队长，然后叫队长来找我即可完成任务。");
	    cm.dispose();
	}
    } else {
	cm.sendNext("恭喜！你已经通过了第二阶段。快点现在，到第三阶段。");
	cm.dispose();
    }
}

function clear(stage, eim, cm) {
    eim.setProperty("stage" + stage.toString() + "status","clear");
    
    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
    cm.environmentChange(true, "gate");
}