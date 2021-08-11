var status = 0;
var cost = 3000;
function start() {
    cm.sendYesNo("请问是否想去日本神社?? 3000金币一次~~~~");
}

function action(mode, type, selection) {
    if (mode != 1) {
        if (mode == 0)
        cm.sendOk("既然你不要那就算了~~~");
        cm.dispose();
        return;
    }
    status++;
    if (status == 1) {
		if(cm.getMeso() < cost) {
		cm.sendOk("干没钱还敢去日本神社!!");
		cm.dispose();
		} else {
		cm.gainMeso(-cost);
		cm.warp(800000000, 0);
        cm.dispose();
    }
}
}
