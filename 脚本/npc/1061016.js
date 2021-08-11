var status = -1;
var itemids = Array(2040728, 2040729, 2040730, 2040731, 2040732, 2040733, 2040734, 2040735, 2040736, 2040737, 2040738, 2040739);

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode != 1) {
		cm.dispose();
		return;
	}
	status++;
	if (status == 0) {
		cm.sendSimple("又来了？你已经来好多次了。看样子蛮闲的嘛！看起来你有事要拜托我？已经取得巴洛古的皮了？\r\n\r\n#r#L1#兑换一些东西#l#k");
	} else if (status == 1) {
		var selStr = "要做成何种卷轴? 随着卷轴的种类，所需的皮件个数也不同。\r\n\r\n#b";
		for (var i = 0; i < itemids.length; i++) {
			selStr += "#L" + i + "##i" + itemids[i] + "##z" + itemids[i] + "##l\r\n";
		}
		cm.sendSimple(selStr);
	} else if (status == 2) {
		if (!cm.canHold(itemids[selection], 1)) {
			cm.sendOk("请空出一些栏位。");
		} else if (cm.itemQuantity(4001261) < 1) {
			cm.sendOk("你没有足够的#b#t4001261##k。");
		} else {
			cm.gainItem(4001261, -1);
			cm.gainItem(itemids[selection], 1);
			cm.sendOk("感谢光临，欢迎下次再来~");
		}
		cm.dispose();
	}
}