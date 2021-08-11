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
            text += "#e#r你好！在我这里可以帮你晋升锻造部分道具，以下是我可以为您制作的道具列表.\r\n\r\n"//3
            text += "#L1##e#d#v2070024#无限飞镖锻造.\r\n"//3
            text += "#L2##e#d#v2070026#黄金闪镖锻造.(不可交易)#l\r\n"//3
            text += "#L3##e#d#v2070023#烈焰飞镖锻造#l\r\n"//3
            text += "#L4##e#d#v2060008#弓专用优质箭矢,攻击提升为+3#l\r\n"//3
            text += "#L5##e#d#v2060009#弓专用强化箭矢,攻击提升为+5#l\r\n"//3
            text += "#L6##e#d#v2060010#弓专用锋利箭矢,攻击提升为+7#l\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
		cm.openNpc(9000041, 11);
        } else if (selection == 2) {
		cm.openNpc(9000041, 12);
        } else if (selection == 3) {
		cm.openNpc(9000041, 63);
        } else if (selection == 4) {
		cm.openNpc(9000041, 14);
        } else if (selection == 5) {
		cm.openNpc(9000041, 15);
        } else if (selection == 6) {
		cm.openNpc(9000041, 16);
        } else if (selection == 7) {
		cm.openNpc(9000018, 67);
        } else if (selection == 8) {
		cm.openNpc(9000018, 68);
        } else if (selection == 9) {
		cm.openNpc(9000018, 69);
        } else if (selection == 10) {
		cm.openNpc(9000018, 610);
        } else if (selection == 11) {
		cm.openNpc(9000018, 611);
        } else if (selection == 12) {
		cm.openNpc(9000018, 612);
        } else if (selection == 13) {
		cm.openNpc(9000018, 613);
        } else if (selection == 14) {
		cm.openNpc(9000018, 614);
        } else if (selection == 15) {
		cm.openNpc(9000018, 615);
        } else if (selection == 16) {
		cm.openNpc(9000018, 616);
	}
    }
}


