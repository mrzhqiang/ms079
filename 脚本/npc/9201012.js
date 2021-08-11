var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
	cm.sendYesNo("找我有什么事情？？ 你想要预定一个婚礼？？");
    } else if (status == 1) {
	if (cm.getPlayer().getMarriageId() <= 0) {
	    cm.sendOk("好像发生了错误了，您好像还没有跟任何人结婚！");
	} else if (!cm.canHold(4150000,60)) {
	    cm.sendOk("请空出一些其它栏位吧！！");
	} else if (!cm.haveItem(5251004,1) && !cm.haveItem(5251005,1) && !cm.haveItem(5251006,1)) {
	    cm.sendOk("很抱歉，您好像没有#b#t521004#或者#t5251005#或者#t5251006##k！！");
	} else {
	    var chr = cm.getMap().getCharacterById(cm.getPlayer().getMarriageId());
	    if (chr == null) {
		cm.sendOk("请确认您的另一半是否在同一张地图内。");
		cm.dispose();
		return;
	    }
	    var marr = cm.getQuestRecord(160001);
	    var data = marr.getCustomData();
	    if (data == null) {
		marr.setCustomData("0");
	        data = "0";
	    }
	    if (data.equals("0")) {
		marr.setCustomData("1");
		cm.setQuestRecord(chr, 160001, "1");
		var num = 0;
		if (cm.haveItem(5251006,1)) {
		    cm.gainItem(5251006,-1);
		    num = 60;
		} else if (cm.haveItem(5251005,1)) {
		    cm.gainItem(5251005,-1);
		    num = 30;
		} else if (cm.haveItem(5251004,1)) {
		    cm.gainItem(5251004,-1);
		    num = 10;
		}
		cm.setQuestRecord(cm.getPlayer(), 160002, num + "");
		cm.setQuestRecord(chr, 160002, num + "");
		cm.sendNext("您现在已经可以开始结婚了，但是在结婚之前先邀请一些贵宾前来参加您的婚礼吧，让他们知道您与您的另一半爱情是很美的！！");
//		cm.gainItemPeriod(4150000,num,1);
	    } else {
		cm.sendOk("我想您已经是结过婚，或者是个单身汉。");
	    }
	}
	cm.dispose();
    }
}