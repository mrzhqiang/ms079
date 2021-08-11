/**
-- Odin JavaScript --------------------------------------------------------------------------------
	Alcaster - El Nath Market (211000100)
-- By ---------------------------------------------------------------------------------------------
	Unknown/Information/xQuasar
-- Version Info -----------------------------------------------------------------------------------
	1.3 - Fixed up completely [xQuasar]
	1.2 - Add a missing text part [Information]
	1.1 - Recoded to official [Information]
	1.0 - First Version by Unknown
---------------------------------------------------------------------------------------------------
**/

var selected;
var amount;
var totalcost;
var item = new Array(2050003,2050004,4006000,4006001);
var cost = new Array(300,400,5000,5000);
var msg = new Array("圣水","万能疗伤","魔法石","召唤石");
var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    cm.sendNext("你需要的话再来找我。.");
	    cm.safeDispose();
	    return;
	}
	status--;
    }

    if (status == 0) {
	if (cm.getQuestStatus(3035) == 2) {
	    var selStr;
	    for (var i = 0; i < item.length; i++){
		selStr += "\r\n#L" + i + "# #b#t" + item[i] + "# (价格: "+cost[i]+" 金币)#k#l";
	    }
	    cm.sendSimple("谢谢你购买 #b#t4031056##k "+selStr);
	}
	else {
	    cm.sendNext("如果你帮助我，作为奖励我会把我最棒的物品卖给你。");
	    cm.safeDispose();
	}
    } else if (status == 1) {
	selected = selection;
	cm.sendGetNumber("#b#t"+item[selected]+"##k 真的是你需要的道具？这个道具 "+msg[selected]+". 它可能不是获取最简单的项目，但我会给你一个很好的协议就可以了。它会花费你 #b"+cost[selected]+" 金币#k 你想购买多少？？", 0, 1, 100);
    } else if (status == 2) {
	amount = selection;
	totalcost = cost[selected] * amount;
	if (amount == 0) {
	    cm.sendOk("如果你不打算买任何东西的话，我也没有什么可卖。");
	    cm.dispose();
	}
	cm.sendYesNo("你真的想要买 #r"+amount+" #t"+item[selected]+"##k? 费用是 "+cost[selected]+" 金币 每个 #t"+item[selected]+"#, 总共费用是 #r"+totalcost+" 金币#k");
    } else if(status == 3) {
	if(cm.getMeso() < totalcost || !cm.canHold(item[selected])) {
	    cm.sendNext("你确定你的金币足够吗，如果没有至少也要有 #r"+totalcost+"#k 金币.");
	    cm.dispose();
	}
	cm.sendNext("谢谢。如果你发现自己需要的物品的道路，确保这里所下降。我可能已经得到了旧历年，但我仍然可以轻松的魔法物品。.");
	cm.gainMeso(-totalcost);
	cm.gainItem(item[selected], amount);
	cm.safeDispose();
    }
}
