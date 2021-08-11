/* 	
 * 	不夜城 收集香炉活动-龙山寺师父
 */

var status = -1;
var time = 1;
var mode = false;

function action(mode, type, selection) {
    if(!mode) {
		cm.sendNext("新年活动NPC维修中");
		cm.dispose();
		return;
	}
	if(cm.getClient().getChannel() != 1) {
		cm.sendNext("新年活动只能再频道一唷");
		cm.dispose();
		return;
	}
	if (mode == 1) {
        status++;
    } else {
        cm.dispose();
        return;
    }
    if (status == 0) {
        cm.sendNext("您好！我是#p9330107#，目前我们正在举办新年活动，如果您施舍给我香炉祭拜神明，诚意够的话神明将会给各位一个大大的惊喜来当作回报的。");
    } else if (status == 1) {
        cm.sendSimple("每当人们施舍越多，会有越大的期望。。。 \n\r #b#L0#我要施舍香炉#l#k \n\r #b#L1#观察看目前香炉的收集量#l#k \n\r #b#L2#这活动有什么意义呢？#l#k \n\r #b#L3#我要领取打活动用的武器#l#k");
    } else if (status == 2) {
        if (selection == 1) {
            cm.sendNext("香炉目前的进度 \n\r #B" + cm.getKegs() + "# \n\r 如果您施舍给我香炉祭拜神明，诚意够的话神明将会给各位一个大大的惊喜来当作回报的...");
            cm.safeDispose();
		} else if (selection == 2) {
			cm.sendNext("这活动很好玩的，新年嘛就是要开开心心的阿~\r\n您说是吧？？");
			cm.safeDispose();
		} else if (selection == 3) {
			if (cm.getBossLog('time') < 1) {
				cm.sendNext("那就给您吧~");
				cm.gainItemPeriod(1472081,1,1);
				cm.gainItem(2070020,1500);
				cm.setBossLog("time");
				cm.safeDispose();
			} else {
				cm.sendNext("小兄弟，别这么贪心嘛~一天只能领一次的啊！");
				cm.safeDispose();
			}
        } else if (selection == 0) {
            cm.sendGetNumber("小兄弟您已经带上香炉啦？那么您要给我几个 #b#t4000516##k呢？？ \n\r", 0, 0, 5000);
        }
    } else if (status == 3) {
        var num = selection;
        if (!cm.haveItem(4000516) || num == 0) {
            cm.sendOk("呜呜小兄弟您居然欺骗我。。。\r\n请不要欺骗我。。。");
        } else if (cm.haveItem(4000516, num)) {
            cm.gainItem(4000516, -num);
            cm.giveKegs(num);
            cm.sendOk("我不会忘记您的大恩大德的！！！");
        }
        cm.safeDispose();
    }
}