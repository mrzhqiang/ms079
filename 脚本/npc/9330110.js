/* Kedrick
	Fishking King NPC
*/

var status = -1;
var sel;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
		if (status == 0) {
			cm.dispose();
			return;
		}
	status--;
    }

    if (status == 0) {
	cm.sendSimple("我能为您做什么吗？？\n\r #L4#教我怎么钓鱼。#l \n\r #L5#使用500个鲑鱼换取 #i1142071#标准国字勋章 [期限 : 30 天]#l");
    } else if (status == 1) {
	sel = selection;
	if (sel == 4) {
	    cm.sendOk("买着钓竿然后做钓鱼椅子每1分钟就会有东西。");
	    cm.safeDispose();
	} else if (sel == 5) {
	    if (cm.haveItem(4031648, 500)) {
		if (cm.canHold(1142071)) {
		    cm.gainItem(4031648, -500);
		    cm.gainItemPeriod(1142071, 1, 30);
		    cm.sendOk("恭喜拿到了 #b#i1142071##k!")
		} else {
		    cm.sendOk("请确认装备栏是否有足够。");
		}
	    } else {
		cm.sendOk("请给我 500个 #i4031648:# 我才能给你 #i1142071#")
	    }
	    cm.safeDispose();
	}
    }
}