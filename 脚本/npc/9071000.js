var items = Array(1182000, 1182002, 1182004, 1012270, 1162008);
var coins = Array(10, 30, 50, 30, 50);
var status = 0;

function start() {
    var selStr = "Welcome to Monster Park! You will have so much fun here. To enter, go to the Gate.\r\n\r\n#b";
    for (var i = 0; i < items.length; i++) {
	selStr += "#L" + i + "##i" + items[i] + "# for " + coins[i] + " Monster Park Coins (Permanent)#l\r\n";
    }
    cm.sendSimple(selStr);
}

function action(mode, type, selection) {
    if (mode == 1 && selection >= 0 && selection < items.length) {
	if (!cm.canHold(items[selection])) {
	    cm.sendOk("Please make room.");
	} else if (!cm.haveItem(4310020, coins[selection])) {
	    cm.sendOk("You don't have enough coins.");
	} else {
	    cm.gainItem(4310020, -coins[selection]);
	    cm.gainItem(items[selection], 1);
	}
    }
    cm.dispose();
}