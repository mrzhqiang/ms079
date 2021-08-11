function action(mode, type, selection) {
    if (!cm.haveItem(4032649)) {
	cm.sendNext("You need the empty bottle for Ancient Glacial Water.");
    } else {
	cm.gainItem(4032649, -1);
	cm.gainItem(2022698, 1);
    }
    cm.dispose();
}