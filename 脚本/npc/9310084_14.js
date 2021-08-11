var status = 0;
var itemList = 
Array(     
			Array(2340000,500,1,1), //祝福
			Array(4000463,500,1,1), //祝福
			Array(2000005,850,50,1), //超级药水
			Array(2049100,500,1,1) //混沌
);

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1) {
        status++;
    } else {
        if (status == 0) {
            cm.sendOk("天空副本通关抽奖：#v2049100#混沌卷轴/#v2340000#祝福卷轴/#v2000005#超级药水x50.国庆纪念币随机抽取其一.");
            cm.dispose();
        }
        status--;
    }
    if (status == 0) {
        if (cm.haveItem(4170006, 1)) {
            cm.sendYesNo("天空副本通关抽奖：#v2049100#混沌卷轴/#v2340000#祝福卷轴/#v2000005#超级药水x50.国庆纪念币随机抽取其一.");
        } else {
            cm.sendOk("天空副本通关抽奖：#v2049100#混沌卷轴/#v2340000#祝福卷轴/#v2000005#超级药水x50.国庆纪念币随机抽取其一..你背包里有1个#b#t4170005##k吗?");
            cm.safeDispose();
        }
    } else if (status == 1) {
        var chance = Math.floor(Math.random() * +900);
        var finalitem = Array();
        for (var i = 0; i < itemList.length; i++) {
            if (itemList[i][1] >= chance) {
                finalitem.push(itemList[i]);
            }
        }
        if (finalitem.length != 0) {
            var item;
            var random = new java.util.Random();
            var finalchance = random.nextInt(finalitem.length);
            var itemId = finalitem[finalchance][0];
            var quantity = finalitem[finalchance][2];
            var notice = finalitem[finalchance][3];
            item = cm.gainGachaponItem(itemId, quantity, "天空副本福利抽奖", notice);
            if (item != -1) {
                cm.gainItem(4170006, -1);
                cm.sendOk("你获得了 #b#t" + item + "##k " + quantity + "个。");
            } else {
                cm.sendOk("你确实有#b#t4170005##k吗？如果是，请你确认在背包的装备，消耗，其他窗口中是否有一格以上的空间。");
            }
            cm.safeDispose();
        } else {
            cm.sendOk("今天的运气可真差，什么都没有拿到。但是作为鼓励，送给你5颗#v4001322#作为奖励.");
            cm.gainItem(4170006, -1);
            cm.gainItem(4001322, 5);
            cm.safeDispose();
        }
    }
}