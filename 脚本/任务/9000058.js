var status = -1;
var picked = 0;
var state = -1;

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
        cm.sendSimple("开店可以摆飞镖或弹丸哦~\r\n#b#L0#我要领取黑色小猪#l\r\n#b#L1#我要领取礼包#l\r\n#b#L2#我要打开蓝色小箱子#l\r\n#b#L3#当铺里的大蟾蜍钱包(100等以上才能领)解未来东京任务用#l\r\n#b#L4#我要骑银色猪猪!!#l\r\n#b#L5#我要进行忍影瞬杀的任务(四转盗贼限定)#l\r\n#b#L6#我要删除银或金宝箱空白道具(并且补偿一次道具)#l\r\n#b#L7#我要完成灯泡不能接的任务#k");
    } else if (status == 1) {
        if (selection == 0) {
            	if (!cm.haveItem(5000007, 1, true, true) && cm.canHold(5000007,1)) {
                    cm.gainPet(5000007, "黑色小猪", 1, 0, 100, 0);
		}
            cm.dispose();
        } else if (selection == 1) {
            	if (!cm.haveItem(5030000, 1, true, true) && cm.canHold(5030000,1)) {
                    cm.gainItem(5030000, 1);
                }
            	if (!cm.haveItem(5100000, 1, true, true) && cm.canHold(5100000,1)) {
                    cm.gainItem(5100000, 1);
		}
            	if (!cm.haveItem(5370000, 1, true, true) && cm.canHold(5370000,1)) {
                    cm.gainItem(5370000, 1);
		}
            	if (!cm.haveItem(5520000, 1, true, true) && cm.canHold(5520000,1)) {
                    cm.gainItem(5520000, 1);
                }
            	if (!cm.haveItem(5180000, 1, true, true) && cm.canHold(5180000,1)) {
                    cm.gainItem(5180000, 1);
                }
            	if (!cm.haveItem(5170000, 1, true, true) && cm.canHold(5170000,1)) {
                    cm.gainItem(5170000, 1);
		}
            cm.dispose();
		} else if (selection == 2) {
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
                } else {
                    cm.sendOk("你的等级还不够 菜逼巴");
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
                 cm.gainItem(2070018, -999);
                 cm.gainItem(5490000, 1);
                 cm.gainItem(4280000, 1);
                 cm.sendOk("恭喜你删除完毕并补偿了金宝箱");
                    cm.dispose();
        } else if (cm.haveItem(1432036)) {
                 cm.gainItem(1432036, -1);
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
			}
				cm.forceCompleteQuest(2127);
				cm.forceCompleteQuest(3083);
				cm.forceCompleteQuest(20527);
				cm.forceCompleteQuest(50246);
				cm.sendOk("完成任务。");
				cm.dispose();
			}
        }
    }