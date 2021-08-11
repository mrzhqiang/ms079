var status = -1;
var picked = 0;
var state = -1;
var item;

function start() {
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status >= 2 || status == 0) {
            cm.dispose();
            return;
        }
        status--;
    }

    if (status == 0) {
		if (!cm.isQuestFinished(29933)) {
            NewPlayer();
        }
        cm.sendSimple("开店可以摆飞镖或弹丸哦~\r\n#b#L2#我要打开蓝色小箱子#l\r\n#b#L3#当铺里的大蟾蜍钱包(100等以上才能领)解未来东京任务用#l\r\n#b#L4#我要骑银色猪猪!!#l\r\n#b#L5#我要进行忍影瞬杀的任务(四转盗贼限定)#l\r\n#b#L6#我要删除银或金宝箱空白道具(并且补偿一次道具)#l\r\n#b#L7#我要完成灯泡不能接的任务#l\r\n#b#L8#我领取广播道具#ll\r\n#b#L9#我领取爱心广播道具#\l\r\n#b#L10#我领取骷篓广播道具\l\r\n#b#L11#我领取精灵商人\l\r\n#b#L12#我要打恰吉#k");
    } else if (status == 1) {
		if (selection == 2) {
                if (cm.haveItem(4031307, 1) == true)
                    {
                    cm.gainItem(4031307 ,-1);
                    cm.gainItem(2020020 ,100);
                    cm.sendOk("#b蛋糕不要吃太多~旅游愉快~");
                    cm.dispose();
                    } else {
                    cm.sendOk("#b检查一下背包有没有蓝色礼物盒哦");
                    cm.dispose();
                    }
        } else if (selection == 3) { 
                var level = cm.getPlayerStat("LVL");
                if (level >= 100) {
                    cm.gainItem(5252002, 1);
					cm.dispose();
                } else {
                    cm.sendOk("你的等级还不够。");
					cm.dispose();
		}
            cm.dispose();
        } else if (selection == 4) {
                var level = cm.getPlayerStat("LVL");                            
                if (cm.haveItem(4000264, 400) && cm.haveItem(4000266, 400) && cm.haveItem(4000267, 400) &&(level >= 120)) {

                    cm.gainItem(4000264 ,-400);
                    cm.gainItem(4000266 ,-400);
                    cm.gainItem(4000267 ,-400);                    
                    cm.gainItem(1902001 ,1);
                    cm.sendOk("#b好好珍惜野猪~~");
                    cm.dispose();
                    } else {
                    cm.sendOk("请检查一下背包有没有金色皮革４００个、木头肩护带４００个、骷髅肩护带４００个,或者是你等级不够");                  
                }
            cm.dispose();
		} else if (selection == 8) { //广播
				var level = cm.getPlayerStat("LVL");
                if (level >= 1 &&cm.getPlayer().getBossLog('1') < 1) {
				 cm.setBossLog('1');
                    cm.gainItem(5072000 ,5);
					cm.dispose();
                } else {
                    cm.sendOk("1天只能领一次或你的等级还不够。");
					cm.dispose();
		}
		} else if (selection == 9) { //广播
			var level = cm.getPlayerStat("LVL");
                if (level >= 30&&cm.getPlayer().getBossLog('30') < 1) {
				 cm.setBossLog('30');
                    cm.gainItem(5073000 ,10);
					cm.dispose();
                } else {
                    cm.sendOk("1天只能领一次或你的等级还不够 30等才能领爱心广播唷。");
					cm.dispose();
		}
		} else if (selection == 10) { //广播
			var level = cm.getPlayerStat("LVL");
                if (level >= 70&&cm.getPlayer().getBossLog('70') < 1) {
				 cm.setBossLog('70');
                    cm.gainItem(5074000 ,5);
					cm.dispose();
                } else {
                    cm.sendOk("1天只能领一次或你的等级还不够 70等才能领骷篓广播唷。");
					cm.dispose();
		}
		} else if (selection == 11) { //商人
			var level = cm.getPlayerStat("LVL");
                if (level >= 10 &&cm.getPlayer().getBossLog('sell') < 1) {
				 cm.setBossLog('sell');
                    cm.gainItem(5030000 ,1);
					cm.dispose();
                } else {
                    cm.sendOk("1天只能领一次或你的等级还不够 70等才能领骷篓广播唷。");
					cm.dispose();
		}
		
        } else if (selection == 5) {
                 if (cm.getPlayerStat("LVL") >= 120 && cm.getJob() == 412) {
                    cm.warp(910300000, 3);
                    cm.spawnMonster(9300088, 6, -572, -1894)
                    cm.dispose();
        } else if (cm.getJob() == 422) {
                    cm.warp(910300000, 3);
                    cm.spawnMonster(9300088, 6, -572, -1894)
                    cm.dispose();
                    } else {
                    cm.sendOk("这是跟盗贼有关的事情哦,或者你没有达到120等");
                    cm.dispose();
                }
        } else if (selection == 6) {
                 if (cm.haveItem(2070018)) {
					cm.removeAll(2070018);
					cm.gainItem(5490000, 1);
					cm.gainItem(4280000, 1);
					cm.sendOk("恭喜你删除完毕并补偿了金宝箱");
					cm.dispose();
				} else if (cm.haveItem(1432036)) {
					cm.removeAll(1432036);
					cm.gainItem(5490001, 1);
					cm.gainItem(4280001, 1);
					cm.sendOk("恭喜你删除完毕并补偿了银宝箱");
                    cm.dispose();
                } else {
                    cm.sendOk("抱歉你没有空白道具...");
                    cm.dispose();
				}
		} else if (selection == 7) {
			if (cm.getQuestStatus(29507) == 1) {
				cm.gainItem(1142082, 1);
				cm.forceCompleteQuest(29507);
				cm.sendOk("完成任务。");
			}
				cm.forceCompleteQuest(3083);
				cm.forceCompleteQuest(8248);
				cm.forceCompleteQuest(8249);
				cm.forceCompleteQuest(8510);
				cm.forceCompleteQuest(20527);
				cm.forceCompleteQuest(50246);
				cm.sendOk("完成任务。");
				cm.dispose();
				} else if (selection == 12) {
				    if (mode == 1) {
					cm.warp(229010000);
					cm.dispose();
				}
			}
        }
}

function NewPlayer() {
		if (!cm.haveItem(5000007, 1, true, true) && cm.canHold(5000007, 1)) {
			cm.gainPet(5000007, "黑色小猪", 1, 0, 100, 0);
		}
		if (!cm.haveItem(2450000, 1, true, true) && cm.canHold(2450000,1)) {
			cm.gainItem(2450000, 10); //经验加倍
		}
		if (!cm.haveItem(1002419, 1, true, true) && cm.canHold(1002419,1)) {
			cm.gainItemPeriod(1002419, 1, 30);
		}
		if (!cm.haveItem(5030000, 1, true, true) && cm.canHold(5030000,1)) {
			cm.gainItemPeriod(5030000, 1, 30);
		}
		if (!cm.haveItem(5100000, 1, true, true) && cm.canHold(5100000,1)) {
			cm.gainItem(5100000, 1);
		}
		if (!cm.haveItem(5370000, 1, true, true) && cm.canHold(5370000,1)) {
			cm.gainItemPeriod(5370000, 1, 7);
		}
		
		if (!cm.haveItem(5180000, 1, true, true) && cm.canHold(5180000,1)) {
			cm.gainItemPeriod(5180000, 1, 28);
		}
		if (!cm.haveItem(5170000, 1, true, true) && cm.canHold(5170000,1)) {
			cm.gainItemPeriod(5170000, 1, 30);
		}
		cm.forceCompleteQuest(29933); //完成新手奖励
		cm.sendOk("欢迎来到 屁屁谷 请使用 @help/@帮助 了解各式指令\r\n\r\n\r\n游戏愉快^^");
		cm.dispose();
		return;
}
