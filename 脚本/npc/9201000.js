var status = -1;
var firstSelection = -1;
var secondSelection = -1;
var ingredients_0 = Array(4011004, 4021007);
var ingredients_1 = Array(4011006, 4021007);
var ingredients_2 = Array(4011007, 4021007);
var ingredients_3 = Array(4021009, 4021007);
var mats = Array();
var mesos = Array(1000000, 2000000, 3000000);

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	if (cm.getPlayer().getGender() != 0) {
		cm.sendOk("很抱歉，我只能帮男性做戒指。");
		cm.dispose();
	}
	if (cm.getPlayer().getMarriageId() > 0) {
	    cm.sendNext("恭喜你结婚！！");
	    cm.dispose();
	} else {
	    cm.sendSimple("嗨，我可以为您做什么？？\r\n#b#L0#做一个月光戒指#l\r\n#L1#做一个星光戒指#l\r\n#L2#做一个金心戒指#l\r\n#L3#做一个钻石戒指#l#k");
	}
    } else if (status == 1) {
	firstSelection = selection;
	cm.sendSimple("我明白，你需要几克拉？？\r\n#b#L0#1 克拉#l\r\n#L1#2 克拉#l\r\n#L2#3 克拉#l#k");
    } else if (status == 2) {
	secondSelection = selection;
	var prompt = "在这种情况下，为了要做出好品质的装备。请确保您有空间在您的装备栏！#b";
	switch(firstSelection) {
	    case 0:
		mats = ingredients_0;
		break;
	    case 1:
		mats = ingredients_1;
		break;
	    case 2:
		mats = ingredients_2;
		break;
	    case 3:
		mats = ingredients_3;
		break;
	    default:
		cm.dispose();
		return;
	}
	for(var i = 0; i < mats.length; i++) {
	    prompt += "\r\n#i"+mats[i]+"##t" + mats[i] + "# x 1";
	}
	prompt += "\r\n#i4031138# " + mesos[secondSelection]; + " 金币";
	cm.sendYesNo(prompt);
    } else if (status == 3) {
	if (cm.getMeso() < mesos[secondSelection]) {
	    cm.sendOk("你没有钱，滚好嘛！？");
	} else {
	    var complete = true;
	    for (var i = 0; i < mats.length; i++) {
		if (!cm.haveItem(mats[i], 1)) {
		    complete = false;
		    break;
		}
	    }
	    if (!complete) {
		cm.sendOk("没有材料，滚好嘛！？");
	    } else {
		cm.sendOk("做完了，赶快去找你心爱的人求婚吧！！");
		cm.gainMeso(-mesos[secondSelection]);
		for (var i = 0; i < mats.length; i++) {
		    cm.gainItem(mats[i], -1);
		}
		cm.gainItem(2240004 + (firstSelection * 3) + secondSelection, 1);
                cm.gainItem(5251006, 1);
	    }
	}
	cm.dispose();
    }
}
