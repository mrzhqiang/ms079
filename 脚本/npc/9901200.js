importPackage(java.util);
importPackage(java.lang);
var status = -1;
var sr = null;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode != 1) {
		cm.dispose();
		return;
	}
	status++;
	if (status == 0) {
		sr = cm.getSpeedRun("Pink_Bean");
		if (sr.getLeft().equals("")) {
			cm.sendOk("No speedruns have been done yet.");
			cm.dispose();
		} else {
			cm.sendSimple(sr.getLeft());
		}
	} else if (sr != null && selection > 0 && cm.getSR(sr, selection)) {
		status = -1;	
	}
}