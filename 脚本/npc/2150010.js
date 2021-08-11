function start() {
    cm.sendSimple("#b\r\n#L0#Edelstein#l#k");
}

function action(mode,type,selection) {
    if (mode == 1) { //or 931000400 + selection..?
	switch(selection) {
	    case 0:
		cm.warp(310000010,0);
		break;
	}
    }
    cm.dispose();
}