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
	cm.sendSimple("#b#L0#Exchange Medals#l\r\n#L1#Go to the Audience Room#l");
    } else if (status == 1) {
	if (selection == 0) {
	    cm.openShop(200);
	} else if (selection == 1) {
	    cm.warp(211070000);
	}
	cm.dispose();
    }
}