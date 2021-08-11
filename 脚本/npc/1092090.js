function start() {
	if (cm.canHold(4031850,1)) {
		if (cm.haveItem(4031847, 1)) {
			cm.removeAll(4031847);
			cm.gainItem(4031848, 1);
			cm.sendOk("适当地装盛牛奶，牛奶瓶内已装1/3牛奶。");
			cm.dispose();
		} else if (cm.haveItem(4031848, 1)) {
			cm.removeAll(4031848);
			cm.gainItem(4031849, 1);
			cm.sendOk("适当地装盛牛奶，牛奶瓶内已装2/3牛奶。");
			cm.dispose();
		} else if (cm.haveItem(4031849, 1)) {
			cm.removeAll(4031849);
			cm.gainItem(4031850, 1);
			cm.sendOk("适当地装盛牛奶，牛奶瓶内已装满牛奶。");
			cm.dispose();
		} else {
			cm.sendOk("您好像没有牛奶瓶0.0");
			cm.dispose();
		}
		} else {
			cm.sendOk("你病了吗??");
			cm.dispose();
		}
}