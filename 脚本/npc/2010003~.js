
/* Neve
	Orbis: Orbis Park (200000200)
	
	Refining NPC: 
	* Gloves, level 70-80 all classes
*/

var status = 0;
var selectedType = -1;
var selectedItem = -1;
var item;
var mats;
var matQty;
var cost;

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 1)
	status++;
    else
	cm.dispose();
    if (status == 0 && mode == 1) {
	var selStr = "嗨，我是奈夫 有什么我可以帮助你的？？#b"
	var options = new Array("做一双/升级剑士手套","做一双/升级弓箭手手套","做一双/升级法师手套","做一双/升级盗贼手套");
	for (var i = 0; i < options.length; i++){
	    selStr += "\r\n#L" + i + "# " + options[i] + "#l";
	}
			
	cm.sendSimple(selStr);
    }
    else if (status == 1 && mode == 1) {
	selectedType = selection;
	if (selectedType == 0){ //warrior glove
	    var selStr = "需要什么吗？？#b";
	    var gloves = new Array ("#t1082103##k - 剑士 等级. 70#b","#t1082104##k - 剑士 等级. 70#b","#t1082105##k - 剑士 等级. 70#b",
		"#t1082114##k - 剑士 等级. 80#b","#t1082115##k - 剑士 等级. 80#b","#t1082116##k - 剑士 等级. 80#b","#t1082117##k - 剑士 等级. 80#b");
	    for (var i = 0; i < gloves.length; i++){
		selStr += "\r\n#L" + i + "# " + gloves[i] + "#l";
	    }
	    cm.sendSimple(selStr);
	}
	else if (selectedType == 1){ //bowman glove
	    var selStr = "需要什么吗？？#b";
	    var gloves = new Array ("#t1082106##k - 弓箭手 等级. 70#b","#t1082107##k - 弓箭手 等级. 70#b","#t1082108##k - 弓箭手 等级. 70#b",
		"#t1082109##k - 弓箭手 等级. 80#b","#t1082110##k - 弓箭手 等级. 80#b","#t1082111##k - 弓箭手 等级. 80#b","#t1082112##k - 弓箭手 等级. 80#b");
	    for (var i = 0; i < gloves.length; i++){
		selStr += "\r\n#L" + i + "# " + gloves[i] + "#l";
	    }
	    cm.sendSimple(selStr);
	}
	else if (selectedType == 2){ //mage glove
	    var selStr = "需要什么吗？？#b";
	    var gloves = new Array ("#t1082098##k - 法师 等级. 70#b","#t1082099##k - 法师 等级. 70#b","#t1082100##k - 法师 等级. 70#b",
		"#t1082121##k - 法师 等级. 80#b","#t1082122##k - 法师 等级. 80#b","#t1082123##k - 法师 等级. 80#b");
	    for (var i = 0; i < gloves.length; i++){
		selStr += "\r\n#L" + i + "# " + gloves[i] + "#l";
	    }
	    cm.sendSimple(selStr);
	}
	else if (selectedType == 3){ //thief glove
	    var selStr = "需要什么吗？？#b";
	    var gloves = new Array ("#t1082095##k - 盗贼 等级. 70#b","#t1082096##k - 盗贼 等级. 70#b","#t1082097##k - 盗贼 等级. 70#b",
		"#t1082118##k - 盗贼 等级. 80#b","#t1082119##k - 盗贼 等级. 80#b","#t1082120##k - 盗贼 等级. 80#b");
	    for (var i = 0; i < gloves.length; i++){
		selStr += "\r\n#L" + i + "# " + gloves[i] + "#l";
	    }
	    cm.sendSimple(selStr);
	}
    }
    else if (status == 2 && mode == 1) {
	selectedItem = selection;

	if (selectedType == 0){ //warrior glove
	    var itemSet = new Array(1082103,1082104,1082105,1082114,1082115,1082116,1082117,1082118);
	    var matSet = new Array(new Array(4005000,4011000,4011006,4000030,4003000),new Array(1082103,4011002,4021006),new Array(1082103,4021006,4021008),new Array(4005000,4005002,4021005,4000030,4003000),new Array(1082114,4005000,4005002,4021003),new Array(1082114,4005002,4021000),new Array(1082114,4005000,4005002,4021008));
	    var matQtySet = new Array(new Array(2,8,3,70,55),new Array(1,6,4),new Array(1,8,3),new Array(2,1,8,90,60),new Array(1,1,1,7),new Array(1,3,8),new Array(1,2,1,4));
	    var costSet = new Array(90000,90000,100000,100000,110000,110000,120000);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	}
	else if (selectedType == 1){ //bowman glove
	    var itemSet = new Array(1082106,1082107,1082108,1082109,1082110,1082111,1082112);
	    var matSet = new Array(new Array(4005002,4021005,4011004,4000030,4003000),new Array(1082106,4021006,4011006),new Array(1082106,4021007,4021008),new Array(4005002,4005000,4021000,4000030,4003000),new Array(1082109,4005002,4005000,4021005),new Array(1082109,4005002,4005000,4021003),new Array(1082109,4005002,4005000,4021008));
	    var matQtySet = new Array(new Array(2,8,3,70,55),new Array(1,5,3),new Array(1,2,3),new Array(2,1,8,90,60),new Array(1,1,1,7),new Array(1,1,1,7),new Array(1,2,1,4));
	    var costSet = new Array(90000,90000,100000,100000,110000,110000,120000);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	}
	else if (selectedType == 2){ //mage glove
	    var itemSet = new Array(1082098,1082099,1082100,1082121,1082122,1082123);
	    var matSet = new Array(new Array(4005001,4011000,4011004,4000030,4003000),new Array(1082098,4021002,4021007),new Array(1082098,4021008,4011006),new Array(4005001,4005003,4021003,4000030,4003000),new Array(1082121,4005001,4005003,4021005),new Array(1082121,4005001,4005003,4021008));
	    var matQtySet = new Array(new Array(2,6,6,70,55),new Array(1,6,2),new Array(1,3,3),new Array(2,1,8,90,60),new Array(1,1,1,7),new Array(1,2,1,4));
	    var costSet = new Array(90000,90000,100000,100000,110000,120000);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	}
	else if (selectedType == 3){ //thief glove
	    var itemSet = new Array (1082095,1082096,1082097,1082118,1082119,1082120);
	    var matSet = new Array(new Array(4005003,4011000,4011003,4000030,4003000),new Array(1082095,4011004,4021007),new Array(1082095,4021007,4011006),new Array(4005003,4005002,4011002,4000030,4003000),new Array(1082118,4005003,4005002,4021001),new Array(1082118,4005003,4005002,4021000));
	    var matQtySet = new Array(new Array(2,6,6,70,55),new Array(1,6,2),new Array(1,3,3),new Array(2,1,8,90,60),new Array(1,1,1,7),new Array(1,2,1,8));
	    var costSet = new Array(90000,90000,100000,100000,110000,120000);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	}
		
	var prompt = "你想要做一双 #t" + item + "#? 在这种情况下, 我为了要做出最棒的品质，我建议你确保装备栏空间足够。#b";

	if (mats instanceof Array){
	    for(var i = 0; i < mats.length; i++){
		prompt += "\r\n#i"+mats[i]+"# " + matQty[i] + " #t" + mats[i] + "#";
	    }
	}
	else {
	    prompt += "\r\n#i"+mats+"# " + matQty + " #t" + mats + "#";
	}
		
	if (cost > 0)
	    prompt += "\r\n#i4031138# " + cost + " 金币";
		
	cm.sendYesNo(prompt);
    }
    else if (status == 3 && mode == 1) {
	var complete = true;
				
	if (cm.getMeso() < cost) {
	    cm.sendNext("抱歉我只接受金币.");
	} else {
	    if (mats instanceof Array) {
		for (var i = 0; complete && i < mats.length; i++) {
		    if (matQty[i] == 1)	{
			complete = cm.haveItem(mats[i]);
		    } else {
			complete = cm.haveItem(mats[i], matQty[i]);
		    }
		}
	    } else {
		complete = cm.haveItem(mats, matQty);
	    }
	}
			
	if (!complete)
	    cm.sendOk("很抱歉由于你的材料不足，所以我不想帮你做了。");
	else {
	    if (mats instanceof Array) {
		for (var i = 0; i < mats.length; i++){
		    cm.gainItem(mats[i], -matQty[i]);
		}
	    }
	    else
		cm.gainItem(mats, -matQty);
					
	    cm.gainMeso(-cost);
	    cm.gainItem(item, 1);
	    cm.sendOk("制作完毕。");
	}
	cm.dispose();
    }
}
