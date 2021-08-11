/* Konpei
	Showa - Nightmarish Last Days
*/

var flash;

function start() {
    flash = cm.haveItem(4000141);

    if (!flash) {
	cm.sendNext("Once you eliminate the boss, you'll have to show me the boss's flashlight as evidence. I won't believe it until you show me the flashlight! What? You want to leave this room?");
    } else {
	cm.sendNext("Hey, hey! It's dangerous to carry around a flashlight like that! It's going to cause a fire! I'll take care of it. cant' be too careful around here...")
    }
}

function action(mode, type, selection) {
    if (mode == 1) {
	if (!flash) {
	    cm.warp(801040000, 0);
	    cm.dispose();
	} else {
	    cm.warp(801040101, 0);
	    cm.dispose();
	}
    } else {
	cm.sendOk("I really admire your toughness! Well, if you decide to return to Showa Town, let me know~!");
    }
}