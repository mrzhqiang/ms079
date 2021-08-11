var status = -1;

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
            cm.sendYesNo("确定要离开吗?");
        } else if (status == 1) {
            cm.warp(501030104);
		if (cm.getPlayerCount(501030105) == 0) {
		cm.getMap(501030104).resetReactors();
		}
            cm.dispose();
        }
    } 