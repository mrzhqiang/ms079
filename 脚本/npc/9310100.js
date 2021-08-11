function start() {
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
            text += "#e#d您好！这里是杀怪数量兑换物品处\r\n\r\n兑换后扣除对应杀怪次数。.#l\r\n\r\n"//3
            text += "#e#d您当前的杀怪数量为：" + cm.getPlayer().getsg() + ".#l\r\n\r\n"//3
          //  text += "#L1##r我要用1万点杀怪数量兑换#k300W冒险币#l\r\n\r\n"//3
          //  text += "#L2##r我要用5万点杀怪次数兑换#k七星之一(玉衡)#l\r\n\r\n"//3
            text += "#L3##r我要用10W点杀怪次数兑换#k杀怪王勋章#l\r\n\r\n"//3
            cm.sendSimple(text);
        } else if (selection == 1) {
            if (cm.getPlayer().getsg() >= 1) {
                cm.getPlayer().gainsg(-1);
               // cm.gainItem();//物品自己加
                cm.gainMeso(3000000);
                cm.sendOk("兑换成功！");
                cm.worldMessage(6, "玩家：[" + cm.getName() + "]用1万点[杀怪数量]兑换了300万冒险币，再接再厉哦！");
                cm.dispose();
            } else {
                cm.sendOk("您的杀怪数量不足1万");
                cm.dispose();
            }
        } else if (selection == 2) {
            if (cm.getPlayer().getsg() >= 50000) {
                cm.getPlayer().gainsg(-50000);
               cm.gainItem(4032169,1);//物品自己加
                cm.sendOk("兑换成功！");
                cm.worldMessage(6, "玩家：[" + cm.getName() + "]用5万点[杀怪数量]兑换了七星之一(玉衡)，再接再厉哦！");
                cm.dispose();
            } else {
                cm.sendOk("您的杀怪数量不足5万");
                cm.dispose();
            }
        } else if (selection == 3) {
            if (cm.getPlayer().getsg() >= 100000) {
                cm.getPlayer().gainsg(-100000);
                //cm.getPlayer().modifyCSPoints(1, 1000, true);
                cm.gainItem(1142650, 10, 10, 10, 10, 1000, 1000, 8, 8, 50, 50, 15, 15, 5, 5, 0, 0, 0);
                //cm.gainMeso(100000);
                cm.sendOk("兑换成功！");
                cm.worldMessage(6, "玩家：[" + cm.getName() + "]用10万[杀怪数量]兑换了杀怪王勋章，再接再厉哦！");
                cm.dispose();
            } else {
                cm.sendOk("您的杀怪数量不足10万");
                cm.dispose();
            }
        } else if (selection == 1) {
            if (cm.getPlayer().getsg() >= 1000) {
                cm.getPlayer().gainsg(-1000);
                cm.getPlayer().modifyCSPoints(1, 1000, true);
               // cm.gainItem();//物品自己加
                cm.gainMeso(100000);
                cm.sendOk("兑换成功！");
                cm.worldMessage(6, "玩家：[" + cm.getName() + "]用1000点[杀怪数量]兑换了1000点卷，再接再厉哦！");
                cm.dispose();
            } else {
                cm.sendOk("您的材料不足！");
                cm.dispose();
           } 
        }
    }
}


