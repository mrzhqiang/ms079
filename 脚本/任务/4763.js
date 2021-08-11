var status = -1;

function start(mode, type, selection) {
	if (mode == -1) {
		qm.dispose();
	} else {
		if (mode == 1)
			status++;
		else
			status--;
		if (status == 0) {
			if(qm.getQuestStatus(4763)==2){
			qm.sendOk("你已经领取过奖励，继续努力到20级可以获得新奖励喔");
						qm.completeQuest();
			qm.dispose();
			}else{
			qm.sendNext("恭喜你当前等级已经到达#b15#k级。");
			}
		} else if (status == 1) {
			qm.sendOk("恭喜你获得系统奖励！\r\n\r\n#fUI/UIWindow.img/QuestIcon/4/0#\r\n#v2022174# 100#v2000017# 100");
			qm.gainItem(2022174, 100);
			qm.gainItem(2000017, 100);
			qm.completeQuest();
			qm.dispose();
		} 
	}
}