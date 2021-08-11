var status = -1;

function action(mode, type, selection) {
	if (mode == 1) {
		status++;
	} else {
		cm.dispose();
		return;
	}
	if (status == 0) {
		cm.sendNextNoESC("Ook! Ook!");
	} else if (status == 1) {
		cm.EnableUI(0);
		cm.DisableUI(false);
		cm.dispose();
	}
}