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
	cm.sendSimple("Whoa..! Are you here for job advance? \n\r #L0##bI would like to be a Mechanic!#k#l#l");
    } else if (status == 1) {
	cm.sendOk("Please talk to me in the Resistance hideout.");
	cm.safeDispose();
    }
}