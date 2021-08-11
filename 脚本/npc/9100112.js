/*
	Name:  经验转蛋机老头
	Place: 自由市场
*/

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
		status++;
    } else {
	    cm.dispose();
		return;
    }
    if (status == 0) {
	if (cm.getPlayer().getLevel() >= 51) {
	    cm.sendOk("我只可以使用到50级。");
	    cm.dispose();
	} else if (cm.haveItem(5220000)) {
	    cm.sendYesNo("你有一些 #b#t5220000##k\r\n你想要尝试运气！？");
	} else {
	    cm.sendOk("很抱歉由于你没有#b#t5220000##k所以不能尝试。");
	    cm.dispose();
	}
    } else if (status == 1) {
	var item;
	if (Math.floor(Math.random() * 300) == 0) {
	    var rareList = new Array(2370000, 2370001, 2370002, 2370003, 2370004, 2370005, 2370006, 2370007);
		
	    item = cm.gainGachaponItem(rareList[Math.floor(Math.random() * rareList.length)], 1, "兵法书经验转蛋机");
	} else {
	    var itemList = new Array(2370008, 2370009, 2370010, 2370011, 2370012);

	    item = cm.gainGachaponItem(itemList[Math.floor(Math.random() * itemList.length)], 1);
	}

	if (item != -1) {
	    cm.gainItem(5220000, -1);
	    cm.sendOk("您已获得 #b#t" + item + "##k.");
	} else {
	    cm.sendOk("请检查看看您是否有#t5220000##k，或者道具拦已满。");
	}
	cm.dispose();
    }
}