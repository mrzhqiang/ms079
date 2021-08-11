/*每日收集任务
 * 作者：西门
 */

//任务一收集物品
var 数量1 = 50
var 物品1 = 4000016
//任务二收集物品
var 数量2 = 50
var 物品2 = 4000016
//任务三收集物品
var 数量3 = 50
var 物品3 = 4000016
//任务四收集物品
var 数量4 = 50
var 物品4 = 4000016
//任务五收集物品
var 数量5 = 50
var 物品5 = 4000016



var 正在进行中 = "#fUI/UIWindow/Quest/Tab/enabled/1#";
var 完成 = "#fUI/UIWindow/Quest/Tab/enabled/2#";
var 换行符 = "\r\n"
var status = 0;
function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == -1) {
        cm.dispose();
    } else {
        if (mode == 0) {
            cm.dispose();
            return;
        }
        if (mode == 1)
            status++;
        if (status == 0) {
            var txt = "";
            //104000000 明珠港  100000000  射手村 101000000 魔法密林  102000000 勇士部落   103000000 废弃都市
            //收集任务一
            if (cm.getMapId() == 104000000){
                txt = "您好,道友我是【明珠港】每日收集任务NPC.\r\n\r\n现在服务器时间为:" + cm.getHour() + "时:" + cm.getMin() + "分:" + cm.getSec() + "秒\r\n\r\n";
                if (cm.getPlayer().getBossLog('sjwc1') == 0){//sjwc = 收集完成
                    if (cm.getPlayer().getBossLog('sjzt1') == 0){//sj = 收集状态   判断任务状态
                        txt += "是否接受跑商任务。#r\r\n#L1#【开始跑商】#l\r\n\r\n跑商任务奖励：大量经验、#z4310028#、大量点卷"+换行符;
                    }else{
                        txt += "#L2##b收集" + 数量1 + "个#z" + 物品1 + "# #v"+物品1+"#"+正在进行中+换行符;
                    }
                }else{
                cm.sendOk("#b道友,您已经完成今日的收集任务了哦,请明日在继续吧. " +换行符+换行符+"今日任务：" + 数量1 + "个#z" + 物品1 + "# #v" + 物品1 + "#" + 完成 + 换行符);
                cm.dispose();
                }
            }
            
            //收集任务二
            if (cm.getMapId() == 100000000){
                txt = "您好,道友我是【射手村】每日收集任务NPC.\r\n\r\n现在服务器时间为:" + cm.getHour() + "时:" + cm.getMin() + "分:" + cm.getSec() + "秒\r\n\r\n";
                if (cm.getPlayer().getBossLog('sjwc2') == 0){//sjwc = 收集完成
                    if (cm.getPlayer().getBossLog('sjzt2') == 0){//sj = 收集状态   判断任务状态
                        txt += "是否接受跑商任务。#r\r\n#L3#【开始跑商】#l\r\n\r\n跑商任务奖励：大量经验、#z4310028#、大量点卷"+换行符;
                    }else{
                        txt += "#L4##b收集" + 数量2 + "个#z" + 物品2 + "# #v"+物品2+"#"+正在进行中+换行符;
                    }
                }else{
                    cm.sendOk("#b道友,您已经完成今日的收集任务了哦,请明日在继续吧. " +换行符+换行符+"今日任务：" + 数量2 + "个#z" + 物品2 + "# #v" + 物品2 + "#" + 完成 + 换行符);
                    cm.dispose();
                }
            }
            
            //收集任务三
            if (cm.getMapId() == 101000000){
                txt = "您好,道友我是【魔法密林】每日收集任务NPC.\r\n\r\n现在服务器时间为:" + cm.getHour() + "时:" + cm.getMin() + "分:" + cm.getSec() + "秒\r\n\r\n";
                if (cm.getPlayer().getBossLog('sjwc3') == 0){//sjwc = 收集完成
                    if (cm.getPlayer().getBossLog('sjzt3') == 0){//sj = 收集状态   判断任务状态
                        txt += "是否接受跑商任务。#r\r\n#L5#【开始跑商】#l\r\n\r\n跑商任务奖励：大量经验、#z4310028#、大量点卷"+换行符;
                    }else{
                        txt += "#L6##b收集" + 数量3 + "个#z" + 物品3 + "# #v"+物品3+"#"+正在进行中+换行符;
                    }
                }else{
                    cm.sendOk("#b道友,您已经完成今日的收集任务了哦,请明日在继续吧. " +换行符+换行符+"今日任务：" + 数量3 + "个#z" + 物品3 + "# #v" + 物品3 + "#" + 完成 + 换行符);
                    cm.dispose();
                }
            }
            
            //收集任务四
            if (cm.getMapId() == 102000000){
                txt = "您好,道友我是【勇士部落】每日收集任务NPC.\r\n\r\n现在服务器时间为:" + cm.getHour() + "时:" + cm.getMin() + "分:" + cm.getSec() + "秒\r\n\r\n";
                if (cm.getPlayer().getBossLog('sjwc4') == 0){//sjwc = 收集完成
                    if (cm.getPlayer().getBossLog('sjzt4') == 0){//sj = 收集状态   判断任务状态
                        txt += "是否接受跑商任务。#r\r\n#L7#【开始跑商】#l\r\n\r\n跑商任务奖励：大量经验、#z4310028#、大量点卷"+换行符;
                    }else{
                        txt += "#L8##b收集" + 数量4 + "个#z" + 物品4 + "# #v"+物品4+"#"+正在进行中+换行符;
                    }
                }else{
                    cm.sendOk("#b道友,您已经完成今日的收集任务了哦,请明日在继续吧. " +换行符+换行符+"今日任务："+ 数量4 + "个#z" + 物品4 + "# #v" + 物品4 + "#" + 完成 + 换行符);
                    cm.dispose();
                }
            }
            
            //收集任务五
            if (cm.getMapId() == 103000000){
                txt = "您好,道友我是【废弃都市】每日收集任务NPC.\r\n\r\n现在服务器时间为:" + cm.getHour() + "时:" + cm.getMin() + "分:" + cm.getSec() + "秒\r\n\r\n";
                if (cm.getPlayer().getBossLog('sjwc5') == 0){//sjwc = 收集完成
                    if (cm.getPlayer().getBossLog('sjzt5') == 0){//sj = 收集状态   判断任务状态
                        txt += "是否接受跑商任务。#r\r\n#L9#【开始跑商】#l\r\n\r\n跑商任务奖励：大量经验、#z4310028#、大量点卷"+换行符;
                    }else{
                        txt += "#L10##b收集" + 数量5 + "个#z" + 物品5 + "# #v"+物品5+"#"+正在进行中+换行符;
                    }
                }else{
                    cm.sendOk("#b道友,您已经完成今日的收集任务了哦,请明日在继续吧. " +换行符+换行符+"今日任务："+ 数量5 + "个#z" + 物品5 + "# #v" + 物品5 + "#" + 完成 + 换行符);
                    cm.dispose();
                }
            }else{//无地图
                txt = "跑商任务奖励：大量经验、#z4310028#、大量点卷\r\n\r\n#r跑商任务可以在自由的每日任务NPC处接受，每日只能做5次哟!";
                cm.sendOk(txt);
                cm.dispose();
            }
            //显示出来
                cm.sendSimple(txt); 
        }

        //-------------------------------------------------------------------------------------
        //-------------------------------------------------------------------------------------
        //收集任务一
        else if (selection == 1) {
            cm.setBossLog("sjzt1");
            cm.sendOk("#b收集" + 数量1 + "个#z" + 物品1 + "# #v"+物品1+"# 收集任务接受成功" + 换行符 + "快去打猎吧道友！");
            cm.dispose();
        } else if (selection == 2) {
            if (cm.haveItem(物品1,数量1)){
                cm.setBossLog("sjwc1");
                cm.gainItem(物品1, -数量1);
                //cm.gainItem(4001126, 200);
                //cm.gainDY(100);
                cm.gainMeso(200000);
                cm.sendOk("#b收集" + 数量1 + "个#z" + 物品1 + "# #v"+物品1+"# 已完成.");
                cm.dispose();
            }else{
                cm.sendOk("#b道友,您貌似还没有收集完我需要的物品哦" + 换行符 + "请收集" + 数量1 + "个#z" + 物品1 + "# #v"+物品1+"#交给我!");
                cm.dispose();
            } 
        } 
        //收集任务二
        else if (selection == 3) {
            cm.setBossLog("sjzt2");
            cm.sendOk("#b收集" + 数量2 + "个#z" + 物品2 + "# #v"+物品2+"# 收集任务接受成功." + 换行符 + "快去打猎吧道友！");
            cm.dispose();
        } else if (selection == 4) {
            if (cm.haveItem(物品2,数量2)){
                cm.setBossLog("sjwc2");
                cm.gainItem(物品2, -数量2);
                //cm.gainItem(4001126, 200);
                //cm.gainDY(100);
                cm.gainMeso(200000);
                cm.sendOk("#b收集" + 数量2 + "个#z" + 物品2 + "# #v"+物品2+"# 已完成.");
                cm.dispose();
            }else{
                cm.sendOk("#b道友,您貌似还没有收集完我需要的物品哦" + 换行符 + "请收集" + 数量2 + "个#z" + 物品2 + "# #v"+物品2+"#交给我!");
                cm.dispose();
            } 
        } 
        //收集任务三
        else if (selection == 5) {
            cm.setBossLog("sjzt3");
            cm.sendOk("#b收集" + 数量3 + "个#z" + 物品3 + "# #v"+物品3+"# 收集任务接受成功." + 换行符 + "快去打猎吧道友！");
            cm.dispose();
        } else if (selection == 6) {
            if (cm.haveItem(物品3,数量3)){
                cm.setBossLog("sjwc3");
                cm.gainItem(物品3, -数量3);
                //cm.gainItem(4001126, 200);
                //cm.gainDY(100);
                cm.gainMeso(200000);
                cm.sendOk("#b收集" + 数量3 + "个#z" + 物品3 + "# #v"+物品3+"# 已完成.");
                cm.dispose();
            }else{
                cm.sendOk("#b道友,您貌似还没有收集完我需要的物品哦" + 换行符 + "请收集" + 数量3 + "个#z" + 物品3 + "# #v"+物品3+"#交给我!");
                cm.dispose();
            } 
        } 
        //收集任务四
        else if (selection == 7) {
            cm.setBossLog("sjzt4");
            cm.sendOk("#b收集" + 数量4 + "个#z" + 物品4 + "# #v"+物品4+"# 收集任务接受成功." + 换行符 + "快去打猎吧道友！");
            cm.dispose();
        } else if (selection == 8) {
            if (cm.haveItem(物品4,数量4)){
                cm.setBossLog("sjwc4");
                cm.gainItem(物品4, -数量4);
                //cm.gainItem(4001126, 200);
                //cm.gainDY(100);
                cm.gainMeso(200000);
                cm.sendOk("#b收集" + 数量4 + "个#z" + 物品4 + "# #v"+物品4+"# 已完成.");
                cm.dispose();
            }else{
                cm.sendOk("#b道友,您貌似还没有收集完我需要的物品哦" + 换行符 + "请收集" + 数量4 + "个#z" + 物品4 + "# #v"+物品4+"#交给我!");
                cm.dispose();
            } 
        } 
        //收集任务五
        else if (selection == 9) {
            cm.setBossLog("sjzt5");
            cm.sendOk("#b收集" + 数量5 + "个#z" + 物品5 + "# #v"+物品5+"# 收集任务接受成功." + 换行符 + "快去打猎吧道友！");
            cm.dispose();
        } else if (selection == 10) {
            if (cm.haveItem(物品5,数量5)){
                cm.setBossLog("sjwc5");
                cm.gainItem(物品5, -数量5);
                //cm.gainItem(4001126, 200);
                //cm.gainDY(100);
                cm.gainMeso(200000);
                cm.sendOk("#b收集" + 数量5 + "个#z" + 物品5 + "# #v"+物品5+"# 已完成.");
                cm.dispose();
            }else{
                cm.sendOk("#b道友,您貌似还没有收集完我需要的物品哦" + 换行符 + "请收集" + 数量5 + "个#z" + 物品5 + "# #v"+物品5+"#交给我!");
                cm.dispose();
            } 
        } 
//----------------------------------------------------------------------------------------------------------------------------------------------------
    }
}