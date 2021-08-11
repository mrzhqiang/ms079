/* Konpei
	Showa
*/

var flash;
var status = 0;

function start() {
    flash = cm.haveItem(4000141);
    action(1,0,0);
}

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	cm.sendOk("I really admire your toughness! Well, if you decide to return to Showa Town, let me know~!");
	cm.dispose();
	return;
    }

    if (status == 1) {
	if (flash) {
	    cm.sendNext("Oh wow, you did it! You know, that man sure stood firm. Hopefully this'll lead to some much-needed peace here, but I keep fearing for the worst. In any case, I'm just glad he's gone now.");
	} else {
	    cm.sendYesNo("Do you want to return to Showa Town?");
	}
    } else if (status == 2) {
	if (flash) {
	    cm.sendNext("That's right! The flashlight that the boss drops will be taken care of by me for future purposes. Now that we know who that really is, I feel like the peaceful days may be on its way. I have to admit, finding out the monster is indeed him... that caught me off guard.");
	} else {
	    cm.warp(801000000, 0);
	    cm.dispose();
	}
    } else if (status == 3) {
	cm.gainItem(4000141, -1);
	cm.gainItem(2000004, 200);
	cm.warp(801000000, 0);
	cm.dispose();
    }
}