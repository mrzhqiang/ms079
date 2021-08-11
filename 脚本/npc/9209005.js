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
            text += "#e#d你好!我是鲨鱼标本商人!#v3994093#买入价格为#r200万#d冒险币.\r\n本周的卖出价格为#r200万#d冒险币。价格有浮动，入市需谨慎！嘿嘿！\r\n"//3
            text += "#L1##e#k#v4031975#买入#v3994093##l\r\n"//3
            text += "#L2##e#k#v4031975#卖出#v3994093##l\r\n"//3
            //text += "#L4##e#d#v4031975#黄金枫叶系列#l\r\n"//3
            //text += "#L5##e#d#v4031975#神龙武器系列#l\r\n"//3
            //text += "#L6##e#d#v4031975#死灵武器系列#l\r\n"//3
            //text += "#L7##e#d#v4031975#永恒武器系列#l\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
		cm.openNpc(9209005, 1);
        } else if (selection == 2) {
		cm.openNpc(9209005, 2);
        } else if (selection == 3) {
		cm.openNpc(9000018, 2);
        } else if (selection == 4) {
		cm.openNpc(9000018, 3);
        } else if (selection == 5) {
		cm.openNpc(9000018, 4);
        } else if (selection == 6) {
		cm.openNpc(9000018, 5);
        } else if (selection == 7) {
		cm.openNpc(9000018, 6);
	}
    }
}


