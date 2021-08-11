/* 
 *  NPC     Naosuke
 *  Maps ;  Ninja Castle Hallway
 *
 */
var status = -1

function start() {
    cm.sendNext("Woah! Who are you?!");
}

function action(mode, type, selection) {
    if (mode == 1) {
	status++
    } else {
	if (status == 0) {
	    cm.sendOk("...see? What lies ahead is a treacherous path, one that's known to chew up and spilt out everyone that dare to go there. If I were you, I'd turn around and leave right now with my life in tact.");
	}
	cm.dispose();
	return;
    }
    if (status == 0) {
	cm.sendYesNo("What? You want to proceed further from this? Are you saying that you know what's going out there?");
    } else if (status == 1) {
	cm.sendNext("...Okay. If you are going there knowing what's really out there, then I won't stop you. I really hope you safely reach Tenshu and... beat those guys!")
    } else if (status == 2) {
	cm.warp(800040300, 0);
	cm.dispose();
    }
}