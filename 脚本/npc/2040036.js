/*
	Red Balloon - LudiPQ 1st stage NPC
**/

var status;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {

	if( cm == null )
		return;
	
    var eim = cm.getEventInstance();

    if(eim == null)
    	return;

    var stage1status = eim.getProperty("stage1status");

    if (stage1status == null) {
	if (cm.isLeader()) { // Leader
	    var stage1leader = eim.getProperty("stage1leader");
	    if (stage1leader == "done") {

		if (cm.haveItem(4001022, 25)) { // Clear stage
		    cm.sendNext("恭喜！你已经通过了第一阶段。快点现在，到了第二阶段。");
		    cm.removeAll(4001022);
		    clear(1, eim, cm);
		    cm.givePartyExp(2100, eim.getPlayers());
		    cm.dispose();
		} else { // Not done yet
		    cm.sendNext("你确定你有收集了 #r25张 #t4001022##k？？");
		}
		cm.dispose();
	    } else {
		cm.sendOk("欢迎来到第一阶段。#b遗弃之塔PQ#k 请收集#r#t4001022##k 来找我即可完成任务。");
		eim.setProperty("stage1leader","done");
		cm.dispose();
	    }
	} else { // Members
	    cm.sendNext("欢迎来到第一阶段。#b遗弃之塔PQ#k 请收集#r#t4001022##k 给你的队长，然后叫队长来找我即可完成任务。");
	    cm.dispose();
	}
    } else {
	cm.sendNext("恭喜！你已经通过了第一阶段。快点现在，到了第二阶段。");
	cm.dispose();
    }
}

function clear(stage, eim, cm) {
    eim.setProperty("stage" + stage.toString() + "status","clear");
    
    cm.showEffect(true, "quest/party/clear");
    cm.playSound(true, "Party1/Clear");
    cm.environmentChange(true, "gate");
}