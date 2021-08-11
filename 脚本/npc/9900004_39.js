/* global cm */

//var 爱心 = "#fEffect/CharacterEff/1022223/4/0#";

var 爱心 = "#fUI/GuildMark.img/Mark/Etc/00009001/14#";
var 红色箭头 = "#fUI/UIWindow/Quest/icon6/7#";
var 正方形 = "#fUI/UIWindow/Quest/icon3/6#";
var 蓝色箭头 = "#fUI/UIWindow/Quest/icon2/7#";
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
                text += "#e#b精灵吊坠，经验加成30%，立刻生效，升级必备哦！\r\n\r\n";
                text += "" +  "#L1##r#v1122017##z1122017#\t时间：3小时\t点卷：300点\r\n\r\n"//3
                text += "" +  "#L2##r#v1122017##z1122017#\t时间：10小时\t点卷：600点\r\n\r\n"//3
                text += "" +  "#L3##r#v1122017##z1122017#\t时间： 1天\t点卷：1200点\r\n\r\n"//3
                text += "" +  "#L4##r#v1122017##z1122017#\t时间： 7天\t点卷：6000点\r\n\r\n"//3
                cm.sendSimple(text);
            }
        } else if (selection == 1) {
            if (!cm.beibao(1, 1)) {//前面的1对应装备第一栏，也就是装备 后面就是格数
                cm.sendOk("装备栏空余不足1个空格！");//判断不等于，就提示对话
                cm.dispose();
            } else if (cm.getPlayer().getCSPoints(1) >= 300) {
                cm.gainNX(-300);
                cm.gainItem(1122017, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3);
                cm.喇叭(1, "[" + cm.getPlayer().getName() + "]成功购买精灵坠子3小时使用权！");
                cm.dispose();
            } else {
                cm.sendOk("点卷不足无法换购！");
                cm.dispose();
            }
        } else if (selection == 2) {
            if (!cm.beibao(1, 1)) {//前面的1对应装备第一栏，也就是装备 后面就是格数
                cm.sendOk("装备栏空余不足1个空格！");//判断不等于，就提示对话
                cm.dispose();
            } else if (cm.getPlayer().getCSPoints(1) >= 600) {
                cm.gainNX(-600);
                cm.gainItem(1122017, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10);
                cm.喇叭(1, "[" + cm.getPlayer().getName() + "]成功购买精灵坠子10小时使用权！");
                cm.dispose();
            } else {
                cm.sendOk("道具不足无法换购！");
                cm.dispose();
            }
        } else if (selection == 3) {
            if (!cm.beibao(1, 1)) {//前面的1对应装备第一栏，也就是装备 后面就是格数
                cm.sendOk("装备栏空余不足1个空格！");//判断不等于，就提示对话
                cm.dispose();
            } else if (cm.getPlayer().getCSPoints(1) >= 1200) {
                cm.gainNX(-1200);
                cm.gainItem(1122017, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 24);
                cm.喇叭(1, "[" + cm.getPlayer().getName() + "]成功购买精灵坠子1天使用权！");
                cm.dispose();
            } else {
                cm.sendOk("道具不足无法换购！");
                cm.dispose();
            }
        } else if (selection == 4) {
            if (!cm.beibao(1, 1)) {//前面的1对应装备第一栏，也就是装备 后面就是格数
                cm.sendOk("装备栏空余不足1个空格！");//判断不等于，就提示对话
                cm.dispose();
            } else if (cm.getPlayer().getCSPoints(1) >= 6000) {
                cm.gainNX(-6000);
                cm.gainItem(1122017, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 168);
                cm.喇叭(1, "[" + cm.getPlayer().getName() + "]成功购买精灵坠子7天使用权！");
                cm.dispose();
            } else {
                cm.sendOk("点卷不足无法换购！");
                cm.dispose();
            }
        }
    }
}


