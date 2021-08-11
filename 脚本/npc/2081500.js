/*  NPC : 萨穆埃尔
	海盗 4转 任务脚本
	地图代码 (240010501)
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }

    if (status == 0) {
	if (!(cm.getJob() == 511 || cm.getJob() == 521)) {
	    cm.sendOk("为什么你要见我??还有你想要问我关于什么事情??");
	    cm.dispose();
	    return;
	} else if (cm.getPlayer().getLevel() < 120) {
	    cm.sendOk("你等级尚未到达120级.");
	    cm.dispose();
	    return;
	} else {
		if (cm.getJob() == 511){
		    cm.sendSimple("恭喜你有资格4转. \r\n请问你想4转吗??\r\n#b#L0#我想成为拳霸.#l\r\n#b#L1#像我想一下...#l");
		} else if (cm.getJob() == 521){
		    cm.sendSimple("恭喜你有资格4转. \r\n请问你想4转吗??\r\n#b#L0#我想成为枪神.#l\r\n#b#L1#像我想一下...#l");
	    } else {
		cm.sendOk("好吧假如你想要4转麻烦再来找我");
	    cm.dispose();
		return;
	    }
	}
    } else if (status == 1) {
	if (selection == 1) {
		cm.sendOk("好吧假如你想要4转麻烦再来找我");
	    cm.dispose();
	    return;
	}
	if (cm.getPlayerStat("RSP") > (cm.getPlayerStat("LVL") - 120) * 3) {
	    cm.sendOk("你的技能点数还没点完..");
	    cm.dispose();
	    return;
	} else if (!cm.haveItem(4031348, 1)){
		cm.sendOk("我需要#t4031348# 1张。");
		cm.dispose();
		return;
	} else {
	    if (cm.canHold(2280003)) {
		cm.gainItem(2280003, 1);

		if (cm.getJob() == 511) {
		    cm.changeJob(512);
		    cm.teachSkill(5121007,0,10);
		    cm.teachSkill(5121001,0,10);
		    cm.teachSkill(5121002,0,10);
		    cm.teachSkill(5121009,0,10);
			cm.gainItem(4031348, -1);
		    cm.sendNext("恭喜你转职为 #b拳霸#k.我送你一些神秘小礼物^^");
		} else if (cm.getJob() == 521) {
		    cm.changeJob(522);
		    cm.teachSkill(5221004,0,10);
		    cm.teachSkill(5220001,0,10);
		    cm.teachSkill(5220002,0,10);
		    cm.teachSkill(5220011,0,10);
			cm.gainItem(4031348, -1);
		    cm.sendNext("恭喜你转职为 #b枪神#k.我送你一些神秘小礼物^^");
		}
	    } else {
		cm.sendOk("你没有多的栏位请清空再来尝试一次!");
		cm.safeDispose();
		return;
	    }
	}
	
    } else if (status == 2) {
	if (cm.getJob() == 512) {
	    cm.sendNext("不要忘记了这一切都取决于你练了多少.");
	} else {
	    cm.sendNext("不要忘记了这一切都取决于你练了多少.");
	}
    } else if (status == 3) {
	cm.sendNextPrev("我已你为荣.");
    } else if (status == 4) {
	cm.dispose();
    }
}