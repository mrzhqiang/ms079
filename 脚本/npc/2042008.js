var status = 0;
function start() {
    cm.sendYesNo("请问你想要离开？？");
}

function action(mode, type, selection) {
    if (mode != 1) {
        if (mode == 0)
        cm.sendOk("改变主意再来找我。");
        cm.dispose();
        return;
    }
    status++;
    if (status == 1) {
		cm.warp(980030000,0);
        cm.dispose();
    }
}