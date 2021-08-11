/* global cm */
//var 爱心 = "#fUI/GuildMark.img/Mark/Etc/00009001/14#";

var 爱心 = "#fUI/GuildMark.img/Mark/Etc/00009001/14#";

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
            var text = "";
            for (i = 0; i < 10; i++) {
                text += "";
            }
    text += "#L36##fUI/GuildMark.img/Mark/Etc/00009001/14#快捷传送#l#L27##fUI/GuildMark.img/Mark/Etc/00009001/14#副本传送#l#L29##fUI/GuildMark.img/Mark/Etc/00009001/14#BOSS重返#l\r\n\r\n"
	text += "#L1005##fUI/GuildMark.img/Mark/Etc/00009001/14#物品装备#l#L32##fUI/GuildMark.img/Mark/Etc/00009001/14#道具回收#l#L30##fUI/GuildMark.img/Mark/Etc/00009001/14#查询爆率#l\r\n\r\n"
    text += "#L51##fUI/GuildMark.img/Mark/Etc/00009001/14#每日狩猎#l#L52##fUI/GuildMark.img/Mark/Etc/00009001/14#每日副本#l#L53##fUI/GuildMark.img/Mark/Etc/00009001/14#每日BOSS#l\r\n\r\n"
	text += "#L28##fUI/GuildMark.img/Mark/Etc/00009001/14#征集喇叭#l#L7##fUI/GuildMark.img/Mark/Etc/00009001/14#查看排行#l#L2##fUI/GuildMark.img/Mark/Etc/00009001/14#在线奖励#l\r\n\r\n"
	text += "#L39##fUI/GuildMark.img/Mark/Etc/00009001/14#精灵吊坠#l#L40##fUI/GuildMark.img/Mark/Etc/00009001/14#枫叶兑换#l#L41##fUI/GuildMark.img/Mark/Etc/00009001/14#中介兑换#l\r\n\r\n"
	text += "#L13##fUI/GuildMark.img/Mark/Etc/00009001/14#豆屋娱乐#l#L22##fUI/GuildMark.img/Mark/Etc/00009001/14#快捷商店#l#L12##fUI/GuildMark.img/Mark/Etc/00009001/14#皇家大全#l\r\n\r\n"
	  
            if (cm.getPlayer().isGM()) {
            //   text += " \r\n\\r\n\r\n\t\t#r以下功能，仅管理员可见，普通玩家看不见\r\n"
            //   text += "#L36#快捷传送#l\t#L37#快速转职#l\r\n"
            //   text += "#L1002#刷新当前地图#l#L1003#刷新个人状态#l\r\n"
            //   text += "#L1005#重载副本#l#L1006#重载爆率#l#L1007#重载反应堆#l#L1008#重载传送点#l\r\n"
            //   text += "#L1009#重载任务#l#L1010#重载商店#l#L1011#重载封包头#l#L1004#查看管理员指令#l\r\n"
            }
            cm.sendSimple(text);
			
        } else if (selection == 1) {//传送自由市场
	    cm.warp(910000000);
            cm.dispose();
           
        } else if (selection == 2) {//在线时间奖励
            cm.openNpc(9900004,2);

        } else if (selection == 3) { //等级领取奖励
            cm.openNpc(9900004,3);

        } else if (selection == 4) {//充值礼包领取
            cm.openNpc(9900004,4);

        } else if (selection == 5) {//每日签到
            cm.openNpc(9900004,5);

        } else if (selection == 6) {//每日任务一系列。
            cm.openNpc(9900004,6);

        } else if (selection == 7) {//综合排行
            cm.openNpc(9900004,7);

        } else if (selection == 8) {//限时答题
            cm.openNpc(9900004,8);

        } else if (selection == 9) {//装备制造
            cm.openNpc(9000018,0);

        } else if (selection == 10) {//副本兑换
            cm.openNpc(9310084,0);
        } else if (selection == 11) {//黄金猪抽奖
            cm.openNpc(9900004,11);

        } else if (selection == 12) {//皇家综合
            cm.openNpc(9900004,12);

        } else if (selection == 13) {
            cm.warp(809030000);//豆豆屋

        } else if (selection == 11111) {//
            cm.openShop(84);//NPCID是：2040051
            cm.dispose();

        } else if (selection == 12111) {//
            cm.openShop(30);//NPCID:1200002
            cm.dispose();

        } else if (selection == 13111) {//
            cm.openShop(39);//NPCID:2070002墨铁
            cm.dispose();

        } else if (selection == 14) {//带新人奖励
            cm.sendOk("暂不开放");
		cm.dispose();
        //    cm.openNpc(9900004,14);

        } else if (selection == 15) {//经验兑换
            cm.openNpc(9900004,15);

        } else if (selection == 16) {//杀怪兑换
	    cm.openNpc(9900004,16);

        } else if (selection == 17) {//七星兑换
            cm.sendOk("暂不开放");
		cm.dispose();
		//cm.openNpc(9900004,17);

        } else if (selection == 18) {//开心钓鱼
            cm.openNpc(9330045,0);

        } else if (selection == 19) {//红鸾宫入口
	    cm.warp(700000000);
            cm.dispose();

        } else if (selection == 20) {//
            cm.openNpc(9900004,20);

        } else if (selection == 21) {//
            cm.openShop(81);//NPCID是：2040051
            cm.dispose();

        } else if (selection == 22) {//
            cm.openShop(39);//NPCID:2070002墨铁
            cm.dispose();
        } else if (selection == 23) {//七星注力
            cm.openNpc(9900004,23);
        } else if (selection == 24) {//老虎机
            cm.openNpc(9900004,24);
        } else if (selection == 25) {//血衣
		cm.sendOk("暂不开放");
            //cm.openNpc(9900004,25);
		cm.dispose();
        } else if (selection == 26) {//盖楼
            cm.openNpc(9900004,26);
        } else if (selection == 27) {//副本传送
            cm.openNpc(9900004,27);
        } else if (selection == 28) {//征集喇叭
            cm.openNpc(9900004,28);

        } else if (selection == 29) {//重返BOSS
            cm.openNpc(9900004,29);

        } else if (selection == 30) {//查询爆率
            cm.openNpc(9900004,30);

        } else if (selection == 31) {//飞天猪
            cm.openNpc(9900004,31);

        } else if (selection == 32) {//回收
            cm.openNpc(9900004,32);

        } else if (selection == 33) {//消耗类兑换
            cm.openNpc(9900004,33);

        } else if (selection == 34) {//赌博
            cm.openNpc(9900004,34);

        } else if (selection == 35) {//赌博
            cm.openNpc(9900004,35);

        } else if (selection == 34) {//赌博
            cm.openNpc(9900004,34);

        } else if (selection == 34) {//赌博
            cm.openNpc(9900004,34);
		} else if (selection == 39) {//精灵吊坠
            cm.openNpc(9900004,39);
		} else if (selection == 40) {//枫叶兑换
            cm.openNpc(9000041,0);
		} else if (selection == 41) {//中介兑换
            cm.openNpc(9000040,0);
		} else if (selection == 51) {//每日副本
            cm.openNpc(9900004,101);
		} else if (selection == 52) {//每日副本
            cm.openNpc(9900004,100);
		} else if (selection == 53) {//每日副本
            cm.openNpc(9900004,103);
        } else if (selection == 36) {//
            cm.openNpc(9900004, 36);
        } else if (selection == 37) {//
            cm.openNpc(9900004, 37);
        } else if (selection == 1002) {//
            cm.刷新地图();
            cm.dispose();
        } else if (selection == 1003) {//
            cm.刷新状态();
            cm.dispose();
        } else if (selection == 1004) {//
            cm.sendOk("暂不开放，请等待功能完成");
            cm.dispose();
        } else if (selection == 1005) {//
            cm.openNpc(9900004, 1005);
        } else if (selection == 1006) {//
            cm.sendOk("暂不开放，请等待功能完成");
            cm.dispose();
        } else if (selection == 1007) {//
            cm.sendOk("暂不开放，请等待功能完成");
            cm.dispose();
        } else if (selection == 1008) {//
            cm.sendOk("暂不开放，请等待功能完成");
            cm.dispose();
        } else if (selection == 1009) {//
            cm.sendOk("暂不开放，请等待功能完成");
            cm.dispose();
        } else if (selection == 1010) {//
            cm.sendOk("暂不开放，请等待功能完成");
            cm.dispose();
        } else if (selection == 1011) {//
            cm.sendOk("暂不开放，请等待功能完成");
            cm.dispose();
        } else if (selection == 38) {//
            cm.openNpc(9900004, 38);
        } else if (selection == 1013) {//
            cm.sendOk("暂不开放，请等待功能完成");
            cm.dispose();
        } else if (selection == 1014) {//
            cm.sendOk("暂不开放，请等待功能完成");
            cm.dispose();
        } else if (selection == 1015) {//
            cm.sendOk("暂不开放，请等待功能完成");
            cm.dispose();
        }
    }
}


