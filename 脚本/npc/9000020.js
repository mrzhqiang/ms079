importPackage(net.sf.cherry.server.maps);

var status = 0;

var maps = Array(
Array(500000000,3000,300),
Array(702000000,3000,300),
//Array(600000000,3000,300),
Array(540000000,3000,300),
Array(800000000,3000,300),
Array(701000000,3000,300),
Array(702100000,3000,300),
Array(550000000,3000,300)
);//旅游地图部分
var selectedMap = -1;
var cost = 0;

function start() {
	status = -1;
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode == -1) {
		cm.dispose();
	} else {
	if ((status <= 2 && mode == 0) || (status == 5 && mode == 1)){
		cm.dispose();
		return;
	}
	if (mode == 1)
		status++;
	else
		status--;
	if (cm.getChar().getMapId() != 500000000 && cm.getChar().getMapId() != 702000000 && cm.getChar().getMapId() != 800000000 && cm.getChar().getMapId() != 600000000 && cm.getChar().getMapId() != 540000000 && cm.getChar().getMapId() != 550000000 && cm.getChar().getMapId() != 541000000) {
		if (status == 0) {
			status = 1;
			if (cm.getJob() == 0) {
				cm.sendNext("为了从繁忙的日常中解脱，去享受一趟旅游怎么样？不仅可以体验新颖的异国文化，还能学到不少东西的机会！我们冒险岛旅游公司为您准备了，丰富有趣的#b世界旅游#k套餐。谁说环游世界很贵？请放一万个心。我们的#b冒险岛世界旅游套餐#k只需要#b2900金币#k就可以享受全程。");
			} else {
				cm.sendNext("为了从繁忙的日常中解脱，去享受一趟旅游怎么样？不仅可以体验新颖的异国文化，还能学到不少东西的机会！我们冒险岛旅游公司为您准备了，丰富有趣的#b世界旅游#k套餐。谁说环游世界很贵？请放一万个心。我们的#b冒险岛世界旅游套餐#k只需要#b3000金币#k就可以享受全程。");
			}
		} else if (status == 1) {
			cm.sendSimple("现在就可以去往 #b泰国的水上市场,少林寺,日本古代神社#k游览一番。在各旅游地我都会为大家提供满意热诚的服务。那么请准备好，新手可以9折优惠。\r\n#b#L0#查看旅游线路.#k#l");
		} else if (status == 2) {
var selStr = "现在就可以去往 #b泰国的水上市场,少林寺,日本古代神社#k游览一番。在各旅游地我都会为大家提供满意热诚的服务。那么请准备好，新手可以9折优惠。#b";
				if (cm.getJob() == 0) {
					for (var i = 0; i < maps.length; i++) {
						selStr += "\r\n#L" + i + "##m" + maps[i][0] + "# ("+maps[i][2]+"金币)#l";
					}
				}else{
					for (var i = 0; i < maps.length; i++) {
						selStr += "\r\n#L" + i + "##m" + maps[i][0] + "# ("+maps[i][1]+"金币)#l";
					}
				}
				cm.sendSimple(selStr);
		} else if (status == 3) {
			selectedMap = selection;
			if (cm.getJob() == 0) {
				cost = maps[selectedMap][2];
			} else {
				cost = maps[selectedMap][1];
			}
			cm.sendYesNo("你已经决定好，确定要去 #b#m" + maps[selectedMap][0] + "##k吗？那么你将要付给我 #b" + cost +"金币#k. 你真的想去？");
		} else if (status == 4) {
			if (cm.getMeso() < cost) {
				cm.sendPrev("天啦,你钱不够! 这实在是太恐怖了!我不能带你去.");
			} else {
				cm.gainMeso(-cost);
				cm.saveLocation("WORLDTOUR");
				cm.warp(maps[selectedMap][0], 0);
				cm.dispose();
			}
		}	
	} else if (cm.getChar().getMapId() == 500000000 || cm.getChar().getMapId() == 702000000 || cm.getChar().getMapId() == 800000000 || cm.getChar().getMapId() == 600000000 || cm.getChar().getMapId() == 540000000 || cm.getChar().getMapId() == 550000000 || cm.getChar().getMapId() == 551000000 || cm.getChar().getMapId() == 541000000 || cm.getChar().getMapId() == 220000000 || cm.getChar().getMapId() == 240000000) {
		if (status == 0) {
			cm.sendSimple ("世界旅游怎么样？很有趣吧。\r\n#L0##b返回：#m" + cm.getSavedLocation("WORLDTOUR") + "# #k#l\r\n#L1##b继续观光#k#l");
		} else if (status == 1) {
			if (selection == 0) {
				cm.sendOk("好的，如果需要到别的地方旅游请记的告诉我。");
			} else if (selection == 1) {
				cm.sendOk("不想回去就再到处看看吧。等你想回去的时候再来告诉我。");
				cm.dispose();
			} 
		} else if (status == 2) {
			var map = cm.getSavedLocation("WORLDTOUR");
			if (map == -1) {
				map = 100000000;
			}
			cm.warp(map, 0);
			cm.dispose();
			}
		}
	}
}