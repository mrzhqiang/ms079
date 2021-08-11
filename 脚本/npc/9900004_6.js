function start() {
    status = -1;

    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    }
    else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        }
        else {
            status--;
        }
        if (status == 0) {
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
			//显示物品ID图片用的代码是  #v这里写入ID#
            text += "#e#r这里是每日任务区：\r\n部分任务需要70级才可以领取。\r\n\r\n"//3
            text += "#L1# 每日副本任务.#l\r\n\r\n"//3
            text += "#L2# 每日杀怪任务.#l\r\n\r\n"//3
            text += "#L3# 每日赏金任务.#l\r\n\r\n"//3
            text += "#L4# 每日BOSS任务.#l\r\n\r\n"//3
            text += "#L5# 每日跑环任务.#l#L6# 跑环任务状态查询.#l\r\n\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
		cm.openNpc(9900004, 100);
        } else if (selection == 2) {
		cm.openNpc(9900004, 101);
        } else if (selection == 3) {
		cm.openNpc(9900004, 102);
        } else if (selection == 4) {
		cm.openNpc(9900004, 103);
        } else if (selection == 5) {
		cm.openNpc(9900004, 104);
        } else if (selection == 6) {
		cm.openNpc(9900004, 105);
	}
    }
}


