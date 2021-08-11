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
    if (cm.getMapId() != 680000100) {
	cm.dispose();
	return;
    }
    if (status == 0) {
	cm.sendYesNo("您想进入结婚礼堂？？");
    } else if (status == 1) {

	    var marr = cm.getQuestRecord(160001);
	    var data = marr.getCustomData();
	    if (data == null) {
		marr.setCustomData("0");
	        data = "0";
	    }
	    if (data.equals("1")) {
		if (cm.getPlayer().getMarriageId() <= 0) {
		    cm.sendOk("好像发生了错误了，您好像还没有跟任何人结婚！");
		    cm.dispose();
		    return;
		}
	    	var chr = cm.getMap().getCharacterById(cm.getPlayer().getMarriageId());
	    	if (chr == null) {
		    cm.sendOk("请确认您的另一半是否在同一张地图内。");
		    cm.dispose();
		    return;
	    	}
		var maps = Array(680000110, 680000300, 680000401);
		for (var i = 0; i < maps.length; i++) {
		    if (cm.getMap(maps[i]).getCharactersSize() > 0) {
			cm.sendNext("礼堂内已经有另外一对新人正在举行婚礼，请稍后再来尝试。");
			cm.dispose();
			return;
		    }
		}
		var map = cm.getMap(680000110);
		cm.getPlayer().changeMap(map, map.getPortal(0));
		chr.changeMap(map, map.getPortal(0));
		cm.worldMessage(5, "<频道 " + cm.getClient().getChannel() + "> " + cm.getPlayer().getName() + " 与 " + chr.getName() + "即将步入礼堂，请大家快过来祝福他们。");
	    } else {
		if (cm.getMap(680000110).getCharactersSize() == 0) {
		    cm.sendNext("现在礼堂没有举办任何婚礼喔，请稍后再来。");
		    cm.dispose();
		    return;
		}
		if (cm.haveItem(4150000)) {
		    cm.warp(680000110,0);
			cm.removeAll(4150000);
		} else {
		    cm.sendOk("请确认是否有#b#t4150000##k。");
		}
	    }
	cm.dispose();
    }
}