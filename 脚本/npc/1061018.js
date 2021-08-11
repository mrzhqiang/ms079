function start() {
	if (cm.getMap().getAllMonstersThreadsafe().size() == 0) {
		cm.warpPlayer(105100300, 105100301);
		cm.dispose();
	} else {
    cm.sendYesNo("你确定要离开这个地图嘛？？");
}
}

function action(mode, type, selection) {
    if (mode == 1) {
		cm.warpPlayer(105100300, 105100100);
    }
    cm.dispose();
}