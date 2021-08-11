var status = 0;
var jobId;
var jobName;
var mapId


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 2) {
        cm.sendOk("请重试.");
        cm.dispose();
        return;
    }
    if (mode == 1)
        status++;
    else
        status--;
    if (status == 0) {
	if (cm.getMapId() == 912010200 || cm.haveItem(4031059, 1)) {
        if (cm.getQuestStatus(6370) == 1) {
			cm.removeAll(4031059);
            cm.teachSkill(5221006, 0, 10);
            cm.forceCompleteQuest(6370);
        } else if (cm.getQuestStatus(6330) == 1) {
			cm.removeAll(4031059);
            cm.teachSkill(5121003, 0, 10);
            cm.forceCompleteQuest(6330);
        }
        cm.warp(120000101, 0);
		cm.sendOk("恭喜完成任务！！");
        cm.dispose();
	}
	if (cm.getJob() == 0) {
		if (cm.getPlayer().getLevel() >= 10) {
		cm.sendNext("你要转职成为一位 #r海盗#k ?");
	    } else {
		cm.sendOk("你还不能转职成为 #r海盗#k 蔡B8.");
		cm.dispose();
	    }
	} else {
	    if (cm.getPlayer().getLevel() >= 30 && cm.getJob() == 500) { // 海盗
		if (cm.haveItem(4031012, 1)) {
			if (cm.haveItem(4031012, 1)) {
			status = 20;
			cm.sendNext("我看到你完成了测试. 想要继续转职请点下一页!");
		    } else {
			cm.sendOk("请去找 #r海盗转职教官#k.")
			cm.dispose();
		    }
		} else {
		    status = 10;
		    cm.sendNext("你已经可以转职了,要转职请点下一页.");
		}
	    } else if (cm.getPlayer().getLevel() >= 70 && cm.getJob() == 510 || cm.getJob() == 520) {
		if (cm.haveItem(4031059,1)) {
			cm.gainItem(4031057,1);
			cm.gainItem(4031059, -1);
			cm.warp(211000001, 0);
		} else {
		    cm.sendOk("嗨, #b#h0##k! 我需要一个 #b黑符#k. 快去找异次元空间拿给我");
		    cm.startQuest(100101);
		}
		cm.dispose();
		} else if (cm.isQuestActive(6370)) {
			var dd = cm.getEventManager("KyrinTrainingGroundC");
            if (dd != null) {
                dd.startInstance(cm.getPlayer());
            } else {
                cm.sendOk("未知的错误请稍后在尝试。");
            }
		} else if (cm.isQuestActive(6330)) {
            var dd = cm.getEventManager("KyrinTrainingGroundV");
            if (dd != null) {
                dd.startInstance(cm.getPlayer());
            } else {
                cm.sendOk("未知的错误请稍后在尝试。");
			}				
	    } else {
		cm.sendOk("你好,我是卡伊琳-海盗转职官.");
		cm.dispose();
	    }
	}
    } else if (status == 1) {
	cm.sendNextPrev("一旦转职了就不能反悔,如果不想转职请点上一页.");
    } else if (status == 2) {
	cm.sendYesNo("你真的要成为一位 #r海盗#k ?");
    } else if (status == 3) {
	if (cm.getJob() == 0) {
		cm.changeJob(500); // 海盗
		cm.resetStats(4, 4, 4, 4);
	}
	    cm.gainItem(1482014, 1);
	    cm.gainItem(1492014, 1);
	    cm.gainItem(2330000, 600);
	    cm.gainItem(2330000, 600);
	    cm.gainItem(2330000, 600);
	cm.sendOk("转职成功 ! 请去开创天下吧.");
	cm.dispose();
    } else if (status == 11) {
	cm.sendNextPrev("你可以选择你要转职成为一位 #r打手#k, #r枪手#k.")
    } else if (status == 12) {
	cm.askAcceptDecline("但是我必须先测试你,你准备好了吗 ?");
    } else if (status == 13) {
	cm.sendSimple("你想要成为什么 ? #b\r\n#L0#打手#l\r\n#L1#枪手#l#k");	
	} else if (status == 14) {
	var jobName;
	if (selection == 0) {
	    jobName = "打手";
		MapId = "108000502";
	} else if (selection == 1) {
	    jobName = "枪手";
		MapId = "108000500";
	}
	cm.sendYesNo("你真的要成为一位 #r" + jobName + "#k?");
	} else if (status == 15) {
	cm.warp(MapId);
	cm.sendOk("请去找 #b海盗转职教官#k . 他会帮助你的.");
	cm.dispose();
    } else if (status == 21) {
	cm.sendSimple("你想要成为什么 ? #b\r\n#L0#打手#l\r\n#L1#枪手#l#k");
    } else if (status == 22) {
	var jobName;
	if (selection == 0) {
	    jobName = "打手";
	    job = 510;
	} else if (selection == 1) {
	    jobName = "枪手";
	    job = 520;
	}
	cm.sendYesNo("你真的要成为一位 #r" + jobName + "#k?");
    } else if (status == 23) {
	cm.changeJob(job);
    if(cm.haveItem(4031857) && cm.haveItem(4031012, 1)) {
	cm.gainItem(4031857, -15);
	cm.gainItem(4031012, -1);
	cm.sendOk("转职成功 ! 请去开创天下吧.");
	cm.dispose();
	} else if (cm.haveItem(4031856) && cm.haveItem(4031012, 1)) {
	cm.gainItem(4031856, -15);
	cm.gainItem(4031012, -1);
	cm.sendOk("转职成功 ! 请去开创天下吧.");
	cm.dispose();
	} else {
	cm.removeAll(4031857);
	cm.removeAll(4031856);
	cm.removeAll(4031012);
	cm.sendOk("转职成功 ! 请去开创天下吧.");
	cm.dispose();
    }
}
}