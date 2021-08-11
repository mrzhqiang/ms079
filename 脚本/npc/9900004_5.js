/* global cm */

var 正在进行中 = "#fUI/UIWindow/Quest/Tab/enabled/1#";
var 完成 = "#fUI/UIWindow/Quest/Tab/enabled/2#";
var 正在进行中蓝 = "#fUI/UIWindow/MonsterCarnival/icon1#";
var 完成红 = "#fUI/UIWindow/MonsterCarnival/icon0#";
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
            //变量签到状态
            if (cm.getBossLog('playqd') > 0) {//取记录是否签到了
                var qdzt = "已签到";
            } else {
                var qdzt = "未签到";
            }

            var tex2 = "";
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
            text += "\t\t\t  欢迎来到#b" + cm.ms() + "\r\n\t\t\t  #k当前已签到天数：#b" + cm.getPlayer().getqiandao() + "天#k\r\n\t\t\t  今日签到状态：#b" + qdzt + "#k\r\n\r\n"

            if (cm.getBossLog('playqd')) {//判断是否存在签到记录
                text += "" + 完成红 + "点我签到" + 完成 + "#l\r\n\r\n" //存在提示这里
            } else {
                text += "#L1#" + 正在进行中蓝 + "点我签到" + 正在进行中 + "#l\r\n\r\n"//不存在提示这里
            }

            if (!cm.haveItem(4032398, 1)) {//判断是否有签到给的出席图章，有的话说明没领取奖励
                text += "" + 完成红 + "领取今日签到奖励" + 完成 + "#l\r\n\r\n" //不存在提示这里
            } else {
                text += "#L2#" + 正在进行中蓝 + "领取今日签到奖励" + 正在进行中 + "#l\r\n\r\n"//存在提示这里
            }
            if (cm.getqiandao() > 0){
            text += "#L3#" + 正在进行中蓝 + "连续签到3天领取签到奖励#l\r\n\r\n"//
            text += "#L4#" + 正在进行中蓝 + "连续签到5天领取签到奖励#l\r\n\r\n"//
            text += "#L5#" + 正在进行中蓝 + "连续签到7天领取签到奖励#l\r\n\r\n"//
            text += "#L6#" + 正在进行中蓝 + "连续签到15天领取签到奖励#l\r\n\r\n"//
            text += "#L7#" + 正在进行中蓝 + "连续签到20天领取签到奖励#l\r\n\r\n"//
            text += "#L8#" + 正在进行中蓝 + "连续签到25天领取签到奖励#l\r\n\r\n"//
            text += "#L9#" + 正在进行中蓝 + "连续签到30天领取签到奖励#l\r\n\r\n"//
            }
            cm.sendSimple(text);
        } else if (status == 1) {
            if (!cm.beibao(4, 1)) {
                cm.sendOk("其他栏空余不足1个空格！");
                cm.dispose();
            } else if (selection == 1) { //签到
                    if (cm.getBossLog('playqd') < 1) { //签到第一天奖励
                        cm.gainItem(4032398, 1);//给予出席图章1个,用于领取签到奖励
                        cm.setBossLog('playqd');//写入数据库，代表今日已签到//这个表24小时自动初始化，第二天可再次签到
                        cm.getPlayer().gainqiandao(1);//用gain叠加累计型,记录一次签到次数,set是设置次数，gain是叠加记录次数
                        cm.即时存档();
                        cm.sendOk("恭喜你签到成功，领取出席图章一枚\r\n快去领取签到奖励吧~！");
                        cm.喇叭(1, "[每日签到功能]：" + cm.getPlayer().getName() + "，今日已成功签到！");
                        cm.dispose();
                    } else {
                        cm.sendOk("你今日已经签到过了!");
                        cm.dispose();
                    }
                } else if (selection == 2) { //签到
                    if (cm.getMeso() < 2100000000) { //判断是否有1个出席图章
                        cm.gainItem(4001126, 100);//给予100个枫叶
                        cm.gainDY(100);//给予100点抵用卷
                        cm.gainMeso(1000000);//给予100万游戏币
                        cm.gainItem(4032398, -1);//扣除背包的出席图章道具
                        cm.即时存档();
                        cm.sendOk("领取成功！");
                        cm.喇叭(1, "[每日签到]：" + cm.getPlayer().getName() + "，成功领取了签到奖励！");
                        cm.dispose();
                    } else {
                        cm.sendOk("无法领取。\r\n可能的原因：你背包的钱已经超过21亿，所以领取失败，先存点起来再领");
                        cm.dispose();
                    }
                } else if (selection == 3) { //领取签到3天奖励
                    if (cm.getqiandao() == 3) { //判断是否签到了3天
                        cm.gainItem(4001126, 300);//给予300个枫叶
                        cm.gainDY(300);//给予300点抵用卷
                        cm.gainMeso(3000000);//给予300万游戏币
                        cm.getPlayer().gainqiandao(1);//领取之后要累计记录签到次数
                        cm.即时存档();
                        cm.sendOk("领取成功！");
                        cm.喇叭(1, "[每日签到]：" + cm.getPlayer().getName() + "，成功领取了连续签到3天奖励！");
                        cm.dispose();
                    } else {
                        cm.sendOk("领取失败!\r\n可能的原因1：您的签到天数，不足3天\r\n可能的原因2：您已领取过了！\r\n您当前已签到天数为："+ cm.getPlayer().getqiandao() +"天");
                        cm.dispose();
                    }
                } else if (selection == 4) { //领取签到5天奖励
                    if (cm.getqiandao() == 5) { //判断是否签到了5天
                        cm.gainItem(4001126, 500);//给予500个枫叶
                        cm.gainDY(500);//给予500点抵用卷
                        cm.gainMeso(5000000);//给予500万游戏币
                        cm.getPlayer().gainqiandao(1);//领取之后要累计记录签到次数
                        cm.即时存档();
                        cm.sendOk("领取成功！");
                        cm.喇叭(1, "[每日签到]：" + cm.getPlayer().getName() + "，成功领取了连续签到5天奖励！");
                        cm.dispose();
                    } else {
                        cm.sendOk("领取失败!\r\n可能的原因1：您的签到天数，不足5天\r\n可能的原因2：您已领取过了！\r\n您当前已签到天数为："+ cm.getPlayer().getqiandao() +"天");
                        cm.dispose();
                    }
                } else if (selection == 5) { //领取签到7天奖励
                    if (cm.getqiandao() == 7) { //判断是否签到了7天
                        cm.gainItem(4001126, 700);//给予700个枫叶
                        cm.gainDY(700);//给予700点抵用卷
                        cm.gainMeso(7000000);//给予700万游戏币
                        cm.getPlayer().gainqiandao(1);//领取之后要累计记录签到次数
                        cm.即时存档();
                        cm.sendOk("领取成功！");
                        cm.喇叭(1, "[每日签到]：" + cm.getPlayer().getName() + "，成功领取了连续签到7天奖励！");
                        cm.dispose();
                    } else {
                        cm.sendOk("领取失败!\r\n可能的原因1：您的签到天数，不足7天\r\n可能的原因2：您已领取过了！\r\n您当前已签到天数为："+ cm.getPlayer().getqiandao() +"天");
                        cm.dispose();
                    }
                } else if (selection == 6) { //领取签到15天奖励
                    if (cm.getqiandao() == 15) { //判断是否签到了15天
                        cm.gainItem(4001126, 1500);//给予1500个枫叶
                        cm.gainDY(1500);//给予1500点抵用卷
                        cm.gainMeso(15000000);//给予1500万游戏币
                        cm.getPlayer().gainqiandao(1);//领取之后要累计记录签到次数
                        cm.即时存档();
                        cm.sendOk("领取成功！");
                        cm.喇叭(1, "[每日签到]：" + cm.getPlayer().getName() + "，成功领取了连续签到15天奖励！");
                        cm.dispose();
                    } else {
                        cm.sendOk("领取失败!\r\n可能的原因1：您的签到天数，不足15天\r\n可能的原因2：您已领取过了！\r\n您当前已签到天数为："+ cm.getPlayer().getqiandao() +"天");
                        cm.dispose();
                    }
                } else if (selection == 7) { //领取签到20天奖励
                    if (cm.getqiandao() == 20) { //判断是否签到了20天
                        cm.gainItem(4001126, 2000);//给予2000个枫叶
                        cm.gainDY(2000);//给予2000点抵用卷
                        cm.gainMeso(20000000);//给予2000万游戏币
                        cm.getPlayer().gainqiandao(1);//领取之后要累计记录签到次数
                        cm.即时存档();
                        cm.sendOk("领取成功！");
                        cm.喇叭(1, "[每日签到]：" + cm.getPlayer().getName() + "，成功领取了连续签到20天奖励！");
                        cm.dispose();
                    } else {
                        cm.sendOk("领取失败!\r\n可能的原因1：您的签到天数，不足20天\r\n可能的原因2：您已领取过了！\r\n您当前已签到天数为："+ cm.getPlayer().getqiandao() +"天");
                        cm.dispose();
                    }
                } else if (selection == 8) { //领取签到25天奖励
                    if (cm.getqiandao() == 25) { //判断是否签到了25天
                        cm.gainItem(4001126, 2500);//给予2500个枫叶
                        cm.gainDY(2500);//给予2500点抵用卷
                        cm.gainMeso(25000000);//给予2500万游戏币
                        cm.getPlayer().gainqiandao(1);//领取之后要累计记录签到次数
                        cm.即时存档();
                        cm.sendOk("领取成功！");
                        cm.喇叭(1, "[每日签到]：" + cm.getPlayer().getName() + "，成功领取了连续签到25天奖励！");
                        cm.dispose();
                    } else {
                        cm.sendOk("领取失败!\r\n可能的原因1：您的签到天数，不足25天\r\n可能的原因2：您已领取过了！\r\n您当前已签到天数为："+ cm.getPlayer().getqiandao() +"天");
                        cm.dispose();
                    }
                } else if (selection == 9) { //领取签到30天奖励
                    if (cm.getqiandao() == 25) { //判断是否签到了30天
                        cm.gainItem(4001126, 3000);//给予3000个枫叶
                        cm.gainDY(3000);//给予3000点抵用卷
                        cm.gainMeso(30000000);//给予3000万游戏币
                        cm.getPlayer().gainqiandao(1);//领取之后要累计记录签到次数
                        cm.即时存档();
                        cm.sendOk("领取成功！");
                        cm.喇叭(1, "[每日签到]：" + cm.getPlayer().getName() + "，成功领取了连续签到30天奖励！");
                        cm.dispose();
                    } else {
                        cm.sendOk("领取失败!\r\n可能的原因1：您的签到天数，不足30天\r\n可能的原因2：您已领取过了！\r\n您当前已签到天数为："+ cm.getPlayer().getqiandao() +"天");
                        cm.dispose();
                    }
                }
            }
        }
    }




