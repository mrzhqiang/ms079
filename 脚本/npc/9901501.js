var status = -1;
var items = Array(5062000, 5062000, 2022179, 2340000, 4020009, 1032062, 2040804, 2040029, 2040532, 2040516, 2040513, 2040501, 2040025, 2040321, 2040301, 2040317);
var itemsq = Array(1, 10, 1, 1, 10, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2);
var itemsa = Array(2550, 25500, 2000, 50000, 10000, 20000, 8000, 8000, 15000, 15000, 16000, 16000, 18000, 18000, 18000, 18000);

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
		cm.sendSimple("Hi. I like #rA-Cash#k. You have #r#e" + cm.getPlayer().getCSPoints(1) + "#n#k #rA-Cash#k. I could sure use some...\r\n\r\n#b#L0#Give me #rA-Cash#b and I'll give you an item.#l#k");
	} else if (status == 1) {
		var selStr = "Maybe you could trade me some #rA-Cash#k? I have lots of great items for you...#b\r\n\r\n";
		for (var i = 0; i < items.length; i++) {
			selStr += "#L" + i + "##v" + items[i] + "##t" + items[i] + "# x " + itemsq[i] + " #r(" + itemsa[i] + " A-Cash)#b#l\r\n";
		}
		cm.sendSimple(selStr + "#k");
	} else if (status == 2) {
		if (cm.getPlayer().getCSPoints(1) < itemsa[selection]) {
			cm.sendOk("You don't have enough #rA-Cash#k.. I NEED #rA-CASH#k!");
		} else if (!cm.canHold(items[selection], itemsq[selection])) {
			cm.sendOk("You don't have the inventory space to hold it. I must be legit and make this a fair trade... so hurry up and free your inventory so I can get my #rA-Cash#k!");
		} else {
			cm.getPlayer().modifyCSPoints(1, -itemsa[selection], true);
			cm.gainItem(items[selection], itemsq[selection]);
			cm.sendOk("Thanks a lot for the #rA-Cash#k! Hehe...");
		}
		cm.dispose();
	}
}