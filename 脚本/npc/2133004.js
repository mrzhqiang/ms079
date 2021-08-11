var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    switch(cm.getPlayer().getMapId()) {
	case 930000500:
	    if (!cm.haveItem(4001163)) {
	    	cm.sendNext("把#b#t4001163##k带来给我。");
	    } else {
                cm.warpParty(930000600);
	    }
	    break;
    }
    cm.dispose();
}