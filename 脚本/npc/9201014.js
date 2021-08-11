var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	cm.dispose();
	return;
    }
    if (status == 0) {
	if (cm.getPlayer().getMarriageId() > 0) {
	    cm.sendSimple("怎么了，看起来很伤心....\r\n#b#L0#我想要离婚。#l\r\n#L1#我想要从我装备栏删除我的戒指。#l#k");
	} else  {
	    cm.sendSimple("嗨，我可以为您做什么？？ \r\n#L1#我想要从我装备栏删除我的戒指。#l#k");
	}
    } else if (status == 1) {
	if (selection == 0) {
	    cm.sendYesNo("离婚？你确定吗？你想离婚？这不是玩笑吧...？");
	} else {
	    var selStr = "你想要删除什么戒指，让我看看。";
	    var found = false;
	    for (var i = 1112300; i < 1112312; i++) {
		if (cm.haveItem(i)) {
		    found = true;
		    selStr += "\r\n#L" + i + "##v" + i + "##t" + i + "##l";
			}
	    }
	    for (var i = 2240004; i < 2240016; i++) {
		if (cm.haveItem(i)) {
		    found = true;
		    selStr += "\r\n#L" + i + "##v" + i + "##t" + i + "##l";
			}
	    }
	    if (!found) {
		cm.sendOk("身上没有任何戒指。");
		cm.dispose();
	    } else {
		cm.sendSimple(selStr);
	    }
	}
    } else if (status == 2) {
	if (selection == -1) {
	    var cPlayer = cm.getClient().getChannelServer().getPlayerStorage().getCharacterById(cm.getPlayer().getMarriageId());
	    if (cPlayer == null) {
	        cm.sendNext("请确定你的伴侣在线上。");
	    } else {
	    	cPlayer.dropMessage(1, "你的伴侣想要跟你离婚。");
	    	cPlayer.setMarriageId(0);
	    	cm.setQuestRecord(cPlayer, 160001, "0");
	    	cm.setQuestRecord(cm.getPlayer(), 160001, "0");
	    	cm.setQuestRecord(cPlayer, 160002, "0");
	    	cm.setQuestRecord(cm.getPlayer(), 160002, "0");
	    	cm.getPlayer().setMarriageId(0);
                for (var i = 1112300; i < 1112312; i++) {
                cm.gainItem(i, -1);
	        }
	    	cm.sendNext("成功离婚了。");
	    }
	} else {
	    if (selection >= 1112300 && selection < 1112312) {
		cm.gainItem(selection, -1);
		cm.sendOk("你成功移除了戒指。");
	    } else if (selection >= 2240004 && selection < 2240016) {
		cm.gainItem(selection, -1);
		cm.sendOk("你的订婚戒指已被删除。");
	    }
	}		
	cm.dispose();
    }
}