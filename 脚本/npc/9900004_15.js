
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
            text += "#e#d您好！这里是剩余经验兑换物品处,等级必须到达70级才可开始使用\r\n\r\n不同等级的经验可兑换不同的物品哦~！.#l\r\n\r\n"//3
            text += "#e#d您当前的剩余经验值为：" + cm.getPlayer().getExp() + ".#l\r\n\r\n"//3

            text += "#L1##r我要用剩余经验值兑换一个#k兵法书（孙子）#r(达到70级需要200万经验值)#l\r\n\r\n"//3

            text += "#L2##r我要用剩余经验值兑换一个#k火红玫瑰#r(达到90级需要500万经验值)#l\r\n\r\n"//3

            text += "#L3##r我要用剩余经验值兑换一个#k心跳停止糖#r(达到120级需要1000万经验值)#l\r\n\r\n"//3

            text += "#L4##r我要用剩余经验值兑换一个#k七星之一(天权)#r(达到130级需要5000万经验值)#l\r\n\r\n"//3

            text += "#L5##r我要用剩余经验值兑换一个#k组队闯天涯勋章#r(达到143级需要1亿经验值)#l\r\n\r\n"//3

            cm.sendSimple(text);
        } else if (selection == 1) {//成品范例
            if (cm.getPlayer().getExp() >= 2000000 && cm.getPlayer().getLevel() >= 70) {
                cm.gainItem(2370000, 1);// 给物品
                cm.getPlayer().gainExp(-2000000, true, true, true);
                cm.刷新状态();
                cm.sendOk("兑换成功！");
                cm.喇叭(1, "玩家：[" + cm.getName() + "]用200万剩余经验值兑换了兵法书（孙子）一个，再接再厉哦！");
                cm.dispose();
            } else {
                cm.sendOk("您的剩余经验值不足，或您的等级不足70级！");
                cm.dispose();
            }
        } else if (selection == 2) {//下面是范例，需要你改
            if (cm.getPlayer().getExp() >= 5000000 && cm.getPlayer().getLevel() >= 90) {
                cm.gainItem(2022154, 1);// 给物品
                cm.getPlayer().gainExp(-5000000, true, true, true);
                cm.刷新状态();
                cm.sendOk("兑换成功！");
                cm.喇叭(1, "玩家：[" + cm.getName() + "]用500万剩余经验值兑换了火红玫瑰一个，再接再厉哦！");
                cm.dispose();
            } else {
                cm.sendOk("您的剩余经验值不足，或您的等级不足90级！");
                cm.dispose();
            }
        } else if (selection == 3) {//下面是范例，需要你改
            if (cm.getPlayer().getExp() >= 10000000 && cm.getPlayer().getLevel() >= 120) {
                cm.gainItem(2022245, 1);// 给物品
                cm.getPlayer().gainExp(-10000000, true, true, true);
                cm.刷新状态();
                cm.sendOk("兑换成功！");
                cm.喇叭(1, "玩家：[" + cm.getName() + "]用1000万剩余经验值兑换了心跳停止糖一个，再接再厉哦！");
                cm.dispose();
            } else {
                cm.sendOk("您的剩余经验值不足，或您的等级不足120级！");
                cm.dispose();
            }
        } else if (selection == 4) {//下面是范例，需要你改
            if (cm.getPlayer().getExp() >= 50000000 && cm.getPlayer().getLevel() >= 130) {
                cm.gainItem(4031674, 1);// 给物品
                cm.getPlayer().gainExp(-50000000, true, true, true);
                cm.刷新状态();
                cm.sendOk("兑换成功！");
                cm.喇叭(1, "玩家：[" + cm.getName() + "]用3000万剩余经验值兑换了七星之一 天权 一个，再接再厉哦！");
                cm.dispose();
            } else {
                cm.sendOk("您的剩余经验值不足，或您的等级不足130级！");
                cm.dispose();
            }
        } else if (selection == 5) {//下面是范例，需要你改
            if (cm.getPlayer().getExp() >= 100000000 && cm.getPlayer().getLevel() >= 143) {
                cm.gainItem(1142299, 12, 12, 12, 12, 1000, 1000, 10, 10, 50, 50, 15, 15, 5, 5, 0, 0,0);
                cm.getPlayer().gainExp(-100000000, true, true, true);
                cm.刷新状态();
                cm.sendOk("兑换成功！");
                cm.喇叭(1, "玩家：[" + cm.getName() + "]用1亿剩余经验值兑换了 组队闯天涯 - 相伴共冒险勋章 一个，再接再厉哦！");
                cm.dispose();
            } else {
                cm.sendOk("您的剩余经验值不足，或您的等级不足143级！");
                cm.dispose();
            }
        } else if (selection == 2) {//下面是范例，需要你改
            if (cm.getPlayer().getExp() >= 100000 && cm.getPlayer.getLevel >= 50) {
                cm.gainItem(1113166, 1);// 给物品
                cm.sendOk("兑换成功！");
                cm.喇叭(1, "玩家：[" + cm.getName() + "]用10万剩余经验值兑换了xxxxx一个，再接再厉哦！");
                cm.dispose();
            } else {
                cm.sendOk("您的材料不足！");
                cm.dispose();
            }
        }
    }
}


