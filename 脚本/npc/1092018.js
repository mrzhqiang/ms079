/**
	Trash Can - Aboard the Nautilus
**/

var status = -1;

function action(mode, type, selection) {
    if (status == -1) {
	status = 0;
	cm.sendNext("A half-written letter... maybe it's important! Should I take a look?");
    } else if (status == 0) {
	if (cm.haveItem(4031839)) {
	    cm.sendOk("I've already picked one up. I don't think I'll need to pick up another one.");
	    cm.safeDispose();
	} else {
	    cm.gainItem(4031839,1);
	    cm.sendOk("I can barely make this out... but it reads Kyrin.");
	    cm.safeDispose();
	}
    }
}