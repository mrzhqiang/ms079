var status = 0;
var menu;
var payment = false;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0 && status == 0) {
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == 0) {
	if (cm.haveItem(4031242)) {
	    menu = "#L0##b我想使用 #t4031242##k 去 #b#m230030200##k.#l\r\n#L1#我想去 #b#m251000000##k 费用 #b10000金币#k.#l";
	} else {
	    menu = "#L0#我想去 #b#m230030200##k 费用 #b1000金币#k#l\r\n#L1#我想去 #b#m251000000##k 费用 #b10000金币#k#l";
	    payment = true;
	}
	cm.sendSimple ("此计程车可以让您不用徒步走路就可以前往外面的世界 \r\n#b那么你今天想去哪里??#k\r\n"+menu);
    } else if (status == 1) {
	if (selection == 0) {
	    if(payment == true) {
		if(cm.getMeso() < 1000) {
		    cm.sendOk("很抱歉，您没有足够的金币...");
		    cm.dispose();
		} else {
		    cm.gainMeso(-1000);
		}
	    } else {
		cm.gainItem(4031242,-1);
	    }
	    cm.warp(230030200);
	    cm.dispose();
	} else if (selection == 1) {
	    if(cm.getMeso() < 10000) {
		cm.sendOk("很抱歉，您没有足够的金币...");
		cm.dispose();
	    }
	    cm.gainMeso(-10000);
	    cm.warp(251000100);
	    cm.dispose();
	}
    }
}
