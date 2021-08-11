/* global cm */

﻿function start() {
    status = -1;

    action(1, 0, 0);
}
function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (status >= 0 && mode == 0) {

            cm.sendOk("感谢你的光临！");
            cm.dispose();
            return;
        }
        if (mode == 1) {
            status++;
        } else {
            status--;
        }
        if (status == 0) {
            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
            //显示物品ID图片用的代码是  #v这里写入ID#
            text += "#L1##r点我领取#l\r\n\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
            if (cm.getPlayer().getvip() == 0) {//判断是否领取过礼包了
                cm.getPlayer().setvip(1);
                cm.gainPet(5000002, "", 1, 1, 100, 30); //给予兔子
                cm.gainDY(1000);//给予抵用卷1000点
                cm.gainMeso(5000000);//给予金币500万
                cm.gainItem(1122017, 1, 1, 1, 1, 50, 50, 1, 1, 10, 10, 1, 1, 10, 10, 0);//给予精灵吊坠1个
                cm.gainItem(5030000, 1);//给予雇佣商人7天一个
                cm.sendOk("领取成功！");
                cm.worldMessage(6, "玩家：[" + cm.getName() + "]领取了明日谷补偿礼包一份！");
                cm.dispose();
            } else {
                cm.sendOk("礼包呢？哪去了？你是怎么打开的？！");
                cm.dispose();
            }
        }
    }
}


