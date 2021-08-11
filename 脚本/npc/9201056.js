/*
	NLC Taxi
*/

var status = 0;
var goToMansion = false;


function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (status >= 2 && mode == 0) {
	cm.sendOk("Alright, see you next time.");
	cm.dispose();
	return;
    }
    if (mode == 1)
	status++;
    else
	status--;
    if (status == -1) {
	cm.dispose();
    }
    else if (cm.getMapId() == 682000000) {
	if (status == 0) {
	    cm.sendSimple ("Where to, boss? \r\n#L0#New Leaf City#l\r\n#L1#Haunted Mansion#l");
	}
	else if (status == 1) {
	    if (selection == 0) {
		goToMansion = false;
		cm.sendYesNo ("You want to go to New Leaf City?");
	    } else {
		goToMansion = true;
		cm.sendYesNo ("You're sure you want to enter the Mansion?");
	    }
	}
	else if (status == 2) {
	    var map;
	    if (goToMansion){
		map = 682000100;
	    } else {
		map = 600000000;
	    }
	    cm.warp (map, 0);
	    cm.dispose();
	}
    }
	
    else {
	if (status == 0){
	    cm.sendYesNo ("Would you like to go to the haunted mansion?");
	}	else if (status == 1){
	    cm.sendNext ("Alright, see you next time.");
	} else if (status == 2) {
	    cm.warp (682000000, 0);
	    cm.dispose();
					
	}
    }
}