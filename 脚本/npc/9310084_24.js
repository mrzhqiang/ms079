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
            text += "#e#r这里是罗密欧朱丽叶副本道具兑换处：\r\n如果您有#v4001159#,#v4001160#和#v4170017#就可以进行兑换项链，拥有两个分开的项链可进行合成。\r\n\r\n"//3
            text += "#L2##e#d#v4001159#x20 + #v4170017#x20 兑换 #i1122116:# 罗密欧的吊坠.#l\r\n"//3
            text += "#L3##e#d#v4001160#x20 + #v4170017#x20 兑换 #i1122117:# 朱丽叶的吊坠.#l\r\n"//3
            text += "#L4##e#d#v1122116#x 1 + #v1122117#x 1 合成 #i1122118:#永恒爱情证物.#l\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
		cm.openNpc(9000017, 70);
        } else if (selection == 6) {
		cm.openNpc(9310059, 5);
        } else if (selection == 5) {
		cm.openNpc(9310059, 4);
        } else if (selection == 2) {
		cm.openNpc(9310084, 241);
        } else if (selection == 3) {
		cm.openNpc(9310084, 242);
        } else if (selection == 4) {
		cm.openNpc(9310084, 243);
	}
    }
}


