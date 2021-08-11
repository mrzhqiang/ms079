function start() {
	cm.sendYesNo("你真的现在要出去吗?");
}

function action(mode, type, selection) {
    if (mode == 1) {
	cm.warp(701010320, 0);
    }
    cm.dispose();
}