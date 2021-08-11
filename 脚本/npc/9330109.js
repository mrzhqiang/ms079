/* Kedrick
	Fishking King NPC
*/

var status = -1;
var sel;
var t = Math.floor(Math.random()*2);
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
	if( t == 0 ) {
            cm.sendSimple("我能为您做什么吗？？#b\n\r #L1#买普通鱼饵。#l \n\r #L3#使用高级的鱼饵。#l#k");
        } else {
            cm.sendSimple("我能为您做什么吗？？#b\n\r #L1#使用高级鱼饵。#l \n\r #L3#买普通鱼饵。#l#k");  
        }
  } else if (status == 1) {
	sel = selection;
	if ( ( t == 1 && sel == 3  )  || (sel == 1 && t == 0) ) {
	    cm.sendYesNo("请问确定要花 6万 金币 买 120 个普通鱼饵？？");
	} else if (sel == 3) {
            cm.dispose();
	    return;
            if (cm.canHold(2300001,120) && cm.haveItem(5350000,1)) {
		if (!cm.haveItem(2300001)) {
		    cm.gainItem(2300001, 120);
		    cm.gainItem(5350000,-1);
		    cm.sendNext("开心钓鱼吧！");
		} else {
		    cm.sendNext("真贪心！等用完再来找我！");
		}
	    } else {
		cm.sendOk("请确认是否有高级的鱼饵罐头，或者检查您的道具栏有没有满了。");
	    }
	    cm.safeDispose();
	}
    } else if (status == 2) {
	if ( ( t == 1 && sel == 3  )  || (sel == 1 && t == 0) ) {    
            if (cm.canHold(2300000,120) && cm.getMeso() >= 60000) {
		if (!cm.haveItem(2300000)) {
		    cm.gainMeso(-60000);
		    cm.gainItem(2300000, 120);
		    cm.sendNext("开心钓鱼吧！");
		} else {
		    cm.sendNext("真贪心！等用完再来找我！");
		}
	    } else {
		cm.sendOk("请确认是否有足够的金币，或者检查您的道具栏有没有满了。");
	    }
	    cm.safeDispose();
	}
    }
}
