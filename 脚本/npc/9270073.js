var status = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode != 1) {
		cm.dispose();
	} else {
		status++;
		if(status == 0){
			cm.sendNext("Hi There~ It's #bChristmas#k right now, do you wish to plant the Christmas Tree with me? With the Spirit from #bChristmas Decoration#k it can allow the tree to grow healthy! Please gather all the Christmas Decoration that you get from the monsters...");
		} else if (status == 1) {
			cm.sendSimple("Each time users collect the required Christmas Decoration, we can set the tree to grow to its maximum!\r\n#b#L0#Here, I brought the Christmas Decoration.#l#k\r\n#b#L1# Please show me the current status on collecting the Christmas Decoration.#l#k");
		} else if (status == 2) {
			if (selection == 0) {
				cm.sendGetNumber("Did you bring the Christmas Decoration with you? Then please give me the #bChristmas Decoration#k you have. How many are you willing to give me?", cm.itemQuantity(4001473), 0, cm.itemQuantity(4001473));
			} else {
				cm.sendOk("Status of the tree's growth\r\n#B" + cm.getDecorations() + "#\r\nIf we collect them all, the tree would grow to it's fullest.");
				cm.dispose();
			}
		} else if (status == 3) {
			if (selection < 0 || selection > cm.itemQuantity(4001473)) {
				selection = cm.itemQuantity(4001473);
			}
			if (selection == 0) {
				cm.sendOk("Please come back with some Christmas Decoration.");
			} else {
				cm.addDecorations(selection);
				cm.gainItem(4001473, -selection);
				cm.sendOk("Thank you for the Christmas Decoration.");
			}
			cm.dispose();
		}
	}
}