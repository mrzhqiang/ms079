/*
	Name: 旅馆主人
	Place: 各大村庄
 */

var status = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
		var Editing = true//false 开始
          if(Editing){
          cm.sendOk("暂停运作");
          cm.dispose();
          return;
		  }
	if (cm.haveItem(4032226, 10)) {
	    cm.sendYesNo("你有一些 #b#t4032226##k\r\n你想要尝试运气！？");
	} else {
	    cm.sendOk("很抱歉由于你没有#b10个#t4032226##k所以不能尝试。");
	    cm.safeDispose();
	}
    } else if (status == 1) {
	var item;
	if (Math.floor(Math.random() * 30) == 0) {
	    var rareList = new Array(3010054, 2022483, 2210029);
		
	    item = cm.gainGachaponItem(rareList[Math.floor(Math.random() * rareList.length)], 1, "枫叶旅馆");
	} else {
	    var itemList = new Array(2022484, 2022485, 2022486, 2022487);
		
	    item = cm.gainGachaponItem(itemList[Math.floor(Math.random() * itemList.length)], 1);
	}

	if (item != -1) {
	    cm.gainItem(4032226, -10);
	    cm.sendOk("您已获得 #b#t" + item + "##k.");
	} else {
	    cm.sendOk("请检查看看您是否有#b#t4032226# 10个#k，或者道具拦已满。");
	}
	cm.safeDispose();
    }
}