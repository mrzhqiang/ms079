var status = 0;
var cost = 3000;
function start() {
    cm.sendYesNo("请问是否想回去勇士之村?? 3000金币一次~~~~");
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
		cm.sendOk("干没钱还敢回去勇士之村!!");
		cm.dispose();
		} else {
		cm.gainMeso(-cost);
		cm.warp(102000000, 0);
        cm.dispose();
    }
}
}
