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
            text += "#e#r这里是副本奖励抽奖处.通关副本，即可获得对应的副本抽奖蛋.\r\n\r\n"//3
            text += "#L2##e#d#v4170013#月妙副本蛋抽奖#l\r\n"//3
            text += "#L3##e#d#v4170002#废气副本蛋抽奖#l\r\n"//3
            text += "#L4##e#d#v4170005#玩具副本蛋抽奖#l\r\n"//3
            text += "#L5##e#d#v4170006#天空副本蛋抽奖#l\r\n"//3
            text += "#L8##e#d#v4170009#海盗副本蛋抽奖#l\r\n"//3
            text += "#L7##e#d#v4170016#罗密欧朱丽叶副本蛋抽奖#l\r\n"//3
            text += "#L6##e#d#v4170001#毒雾副本蛋抽奖#l\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
		cm.openNpc(9900004, 70);
        } else if (selection == 6) {
		cm.openNpc(9310084, 15);
        } else if (selection == 5) {
		cm.openNpc(9310084, 14);
        } else if (selection == 2) {
		cm.openNpc(9310084, 11);
        } else if (selection == 3) {
		cm.openNpc(9310084, 12);
        } else if (selection == 7) {
		cm.openNpc(9310084, 16);
        } else if (selection == 4) {
		cm.openNpc(9310084, 13);
        } else if (selection == 8) {
		cm.openNpc(9310084, 66);
	}
    }
}


