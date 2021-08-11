/* Tara
	Ludibrium : Tara and Sarah's House (220000303)
	
	Refining NPC: 
	* Shoes - All classes, 30-50, stimulator (4130001) available on upgrades
	* Price is 90% of locations on same items
*/

var status = -1;
var selectedType = -1;
var selectedItem = -1;
var item;
var mats;
var matQty;
var cost;
var stimulator = false;
var stimID = 4130001;

function action(mode, type, selection) {
	if (status == 0 && mode == 0) {
	cm.dispose();
	return;
    } else if (status >= 1 && mode == 0) {
	cm.sendNext("需要的时候可以来找我。");
	cm.dispose();
	return;
    }
    if (mode == 1) {
	status++;
    } else {
	status--;
    }
    if (status == 0) {
	var selStr = "嗨，我是#p2040021# 今天可以为你做点什么？？#b"
	var options = new Array("什么是催化剂?","做一双剑士鞋子","做一双弓箭手鞋子","做一双法师鞋子","做一双盗贼鞋子",
	    "做一双剑士鞋子使用催化剂","做一双弓箭手鞋子使用催化剂","做一双法师鞋子使用催化剂","做一双盗贼鞋子使用催化剂");
	for (var i = 0; i < options.length; i++){
	    selStr += "\r\n#L" + i + "# " + options[i] + "#l";
	}
			
	cm.sendSimple(selStr);
    } else if (status == 1) {
	selectedType = selection;
	var selStr;
	var shoes = Array();
	if (selectedType > 4)
	{
	    stimulator = true;
	    selectedType -= 4;
	}
	else
	    stimulator = false;
	if (selectedType == 0){ // what is stim
	    cm.sendNext("催化剂是一种特殊的药水，我可以加入到创建某些项目的进程。它给它统计中，就好像从一个怪物下降。然而，它可能有没有变化，而且也有可能为项低于平均水平。还有没有得到任何项目使用刺激的时候，所以请明智的选择有10％的机会。");
	    cm.dispose();
	    return;
	}
	if (selectedType == 1){ //warrior shoe
	    selStr = "很好，那么你想做哪一个？？#b";
	    shoes = new Array ("#t1072003##k - 剑士 等级. 30#b","#t1072039##k - 剑士 等级. 30#b","#t1072040##k - 剑士 等级. 30#b","#t1072041##k - 剑士 等级. 30#b",
		"#t1072002##k - 剑士 等级. 35#b","#t1072112##k - 剑士 等级. 35#b","#t1072113##k - 剑士 等级. 35#b",
		"#t1072000##k - 剑士 等级. 40#b","#t1072126##k - 剑士 等级. 40#b","#t1072127##k - 剑士 等级. 40#b",
		"#t1072132##k - 剑士 等级. 50#b","#t1072133##k - 剑士 等级. 50#b","#t1072134##k - 剑士 等级. 50#b","#t1072135##k - 剑士 等级. 50#b");;
	}
	else if (selectedType == 2){ //bowman shoe
	    selStr = "很好，那么你想做哪一个？？#b";
	    shoes = new Array ("#t1072079##k - 弓箭手 等级. 30#b","#t1072080##k - 弓箭手 等级. 30#b","#t1072081##k - 弓箭手 等级. 30#b","#t1072082##k - 弓箭手 等级. 30#b","#t1072083##k - 弓箭手 等级. 30#b",
		"#t1072101##k - 弓箭手 等级. 35#b","#t1072102##k - 弓箭手 等级. 35#b","#t1072103##k - 弓箭手 等级. 35#b",
		"#t1072118##k - 弓箭手 等级. 40#b","#t1072119##k - 弓箭手 等级. 40#b","#t1072120##k - 弓箭手 等级. 40#b","#t1072121##k - 弓箭手 等级. 40#b",
		"#t1072122##k - 弓箭手 等级. 50#b","#t1072123##k - 弓箭手 等级. 50#b","#t1072124##k - 弓箭手 等级. 50#b","#t1072125##k - 弓箭手 等级. 50#b");
	}
	else if (selectedType == 3){ //magician shoe
	    selStr = "很好，那么你想做哪一个？？#b";
	    shoes = new Array ("#t1072075##k - 法师 等级. 30#b","#t1072076##k - 法师 等级. 30#b","#t1072077##k - 法师 等级. 30#b","#t1072078##k - 法师 等级. 30#b",
		"#t1072089##k - 法师 等级. 35#b","#t1072090##k - 法师 等级. 35#b","#t1072091##k - 法师 等级. 35#b",
		"#t1072114##k - 法师 等级. 40#b","#t1072115##k - 法师 等级. 40#b","#t1072116##k - 法师 等级. 40#b","#t1072117##k - 法师 等级. 40#b",
		"#t1072140##k - 法师 等级. 50#b","#t1072141##k - 法师 等级. 50#b","#t1072142##k - 法师 等级. 50#b","#t1072143##k - 法师 等级. 50#b");
	}
	else if (selectedType == 4){ //thief shoe
	    selStr = "很好，那么你想做哪一个？？#b";
	    shoes = new Array ("#t1072032##k - 盗贼 等级. 30#b","#t1072033##k - 盗贼 等级. 30#b","#t1072035##k - 盗贼 等级. 30#b","#t1072036##k - 盗贼 等级. 30#b",
		"#t1072104##k - 盗贼 等级. 35#b","#t1072105##k - 盗贼 等级. 35#b","#t1072106##k - 盗贼 等级. 35#b",
		"#t1072107##k - 盗贼 等级. 40#b","#t1072108##k - 盗贼 等级. 40#b","#t1072109##k - 盗贼 等级. 40#b","#t1072110##k - 盗贼 等级. 40#b",
		"#t1072128##k - 盗贼 等级. 50#b","#t1072130##k - 盗贼 等级. 50#b","#t1072129##k - 盗贼 等级. 50#b","#t1072131##k - 盗贼 等级. 50#b");
	}
		
	if (selectedType != 0)
	{
	    for (var i = 0; i < shoes.length; i++){
		selStr += "\r\n#L" + i + "# " + shoes[i] + "#l";
	    }
	    cm.sendSimple(selStr);
	}
    } else if (status == 2) {
	selectedItem = selection;
	if (selectedType == 1){ //warrior shoe
	    var itemSet = new Array(1072003,1072039,1072040,1072041,1072002,1072112,1072113,1072000,1072126,1072127,1072132,1072133,1072134,1072135);
	    var matSet = new Array(new Array(4021003,4011001,4000021,4003000),new Array(4011002,4011001,4000021,4003000),
		new Array(4011004,4011001,4000021,4003000),new Array(4021000,4011001,4000021,4003000),new Array(4011001,4021004,4000021,4000030,4003000),new Array(4011002,4021004,4000021,4000030,4003000),new Array(4021008,4021004,4000021,4000030,4003000),
		new Array(4011003,4000021,4000030,4003000,4000103),new Array(4011005,4021007,4000030,4003000,4000104),new Array(4011002,4021007,4000030,4003000,4000105),new Array(4021008,4011001,4021003,4000030,4003000),
		new Array(4021008,4011001,4011002,4000030,4003000),new Array(4021008,4011001,4011005,4000030,4003000),new Array(4021008,4011001,4011006,4000030,4003000));
	    var matQtySet = new Array(new Array(4,2,45,15),new Array(4,2,45,15),new Array(4,2,45,15),new Array(4,2,45,15),new Array(3,1,30,20,25),new Array(3,1,30,20,25),new Array(2,1,30,20,25),
		new Array(4,100,40,30,100),new Array(4,1,40,30,100),new Array(4,1,40,30,100),new Array(1,3,6,65,45),new Array(1,3,6,65,45),new Array(1,3,6,65,45),new Array(1,3,6,65,45));
	    var costSet = new Array(20000,20000,20000,20000,22000,22000,25000,38000,38000,38000,50000,50000,50000,50000);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	}
	else if (selectedType == 2){ //bowman shoe
	    var itemSet = new Array(1072079,1072080,1072081,1072082,1072083,1072101,1072102,1072103,1072118,1072119,1072120,1072121,1072122,1072123,1072124,1072125);
	    var matSet = new Array(new Array(4000021,4021000,4003000),new Array(4000021,4021005,4003000),new Array(4000021,4021003,4003000),
		new Array(4000021,4021004,4003000),new Array(4000021,4021006,4003000),new Array(4021002,4021006,4000030,4000021,4003000),new Array(4021003,4021006,4000030,4000021,4003000),new Array(4021000,4021006,4000030,4000021,4003000),
		new Array(4021000,4003000,4000030,4000106),new Array(4021006,4003000,4000030,4000107),new Array(4011003,4003000,4000030,4000108),new Array(4021002,4003000,4000030,4000099),new Array(4011001,4021006,4021008,4000030,4003000,4000033),
		new Array(4011001,4021006,4021008,4000030,4003000,4000032),new Array(4011001,4021006,4021008,4000030,4003000,4000041),new Array(4011001,4021006,4021008,4000030,4003000,4000042));
	    var matQtySet = new Array(new Array(50,2,15),new Array(50,2,15),new Array(50,2,15),new Array(50,2,15),new Array(50,2,15),
		new Array(3,1,15,30,20),new Array(3,1,15,30,20),new Array(3,1,15,30,20),new Array(4,30,45,100),new Array(4,30,45,100),new Array(5,30,45,100),new Array(5,30,45,100),
		new Array(3,3,1,60,35,80),new Array(3,3,1,60,35,150),new Array(3,3,1,60,35,100),new Array(3,3,1,60,35,250));
	    var costSet = new Array(19000,19000,19000,19000,19000,19000,20000,20000,20000,32000,32000,40000,40000,50000,50000,50000,50000);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	}
	else if (selectedType == 3){ //magician shoe
	    var itemSet = new Array(1072075,1072076,1072077,1072078,1072089,1072090,1072091,1072114,1072115,1072116,1072117,1072140,1072141,1072142,1072143,1072136,1072137,1072138,1072139);
	    var matSet = new Array(new Array(4021000,4000021,4003000),new Array(4021002,4000021,4003000),new Array(4011004,4000021,4003000),new Array(4021008,4000021,4003000),new Array(4021001,4021006,4000021,4000030,4003000),new Array(4021000,4021006,4000021,4000030,4003000),
		new Array(4021008,4021006,4000021,4000030,4003000),new Array(4021000,4000030,4000110,4003000),new Array(4021005,4000030,4000111,4003000),new Array(4011006,4021007,4000030,4000100,4003000),new Array(4021008,4021007,4000030,4000112,4003000),
		new Array(4021009,4011006,4021000,4000030,4003000),new Array(4021009,4011006,4021005,4000030,4003000),new Array(4021009,4011006,4021001,4000030,4003000),new Array(4021009,4011006,4021003,4000030,4003000));
	    var matQtySet = new Array(new Array(2,50,15),new Array(2,50,15),new Array(2,50,15),new Array(1,50,15),new Array(3,1,30,15,20),new Array(3,1,30,15,20),new Array(2,1,40,25,20),new Array(4,40,100,25),new Array(4,40,100,25),new Array(2,1,40,100,25),new Array(2,1,40,100,30),
		new Array(1,3,3,60,40),new Array(1,3,3,60,40),new Array(1,3,3,60,40),new Array(1,3,3,60,40));
	    var costSet = new Array(18000,18000,18000,18000,20000,20000,22000,30000,30000,35000,40000,50000,50000,50000,50000);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	}
	else if (selectedType == 4){ //thief shoe
	    var itemSet = new Array(1072032,1072033,1072035,1072036,1072104,1072105,1072106,1072107,1072108,1072109,1072110,1072128,1072130,1072129,1072131);
	    var matSet = new Array(new Array(4011000,4000021,4003000),new Array(4011001,4000021,4003000),new Array(4011004,4000021,4003000),new Array(4011006,4000021,4003000),new Array(4021000,4021004,4000021,4000030,4003000),new Array(4021003,4021004,4000021,4000030,4003000),
		new Array(4021002,4021004,4000021,4000030,4003000),new Array(4021000,4000030,4000113,4003000),new Array(4021003,4000030,4000095,4003000),new Array(4021006,4000030,4000096,4003000),new Array(4021005,4000030,4000097,4003000),new Array(4011007,4021005,4000030,4000114,4003000),
		new Array(4011007,4021000,4000030,4000115,4003000),new Array(4011007,4021003,4000030,4000109,4003000),new Array(4011007,4021001,4000030,4000036,4003000));
	    var matQtySet = new Array(new Array(3,50,15),new Array(3,50,15),new Array(2,50,15),new Array(2,50,15),new Array(3,1,30,15,20),new Array(3,1,30,15,20),new Array(3,1,30,15,20),
		new Array(5,45,100,30),new Array(4,45,100,30),new Array(4,45,100,30),new Array(4,45,100,30),new Array(2,3,50,100,35),new Array(2,3,50,100,35),new Array(2,3,50,100,35),new Array(2,3,50,80,35));
	    var costSet = new Array(19000,19000,19000,21000,20000,20000,20000,40000,32000,35000,35000,50000,50000,50000,50000);
	    item = itemSet[selectedItem];
	    mats = matSet[selectedItem];
	    matQty = matQtySet[selectedItem];
	    cost = costSet[selectedItem];
	}
		
	//Ludi fee is -10%, array not changed unlike 2040016 and 2040020
	cost = cost * .9;
		
	var prompt = "你想要做一双 #t" + item + "#? 在这种情况下，为了要做出好品质的装备。请确保您有空间在您的装备栏！#b";
		
	if(stimulator)
	    prompt += "\r\n#i"+stimID+"# 1 #t" + stimID + "#";

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
    } else if (status == 3) {
	var complete = true;
				
	if (cm.getMeso() < (cost)) {
	    cm.sendOk("我只接受金币。");
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
			
	if (stimulator){ //check for stimulator
	    if (!cm.haveItem(stimID)) {
		complete = false;
	    }
	}
			
	if (!complete)
	    cm.sendOk("由于你没有足够的材料，所以我不帮忙做了。");
	else {
	    if (mats instanceof Array) {
		for (var i = 0; i < mats.length; i++){
		    cm.gainItem(mats[i], -matQty[i]);
		}
	    } else {
		cm.gainItem(mats, -matQty);
	    }
	    cm.gainMeso(-cost);
	    if (stimulator) { //check for stimulator
		cm.gainItem(stimID, -1);
		var deleted = Math.floor(Math.random() * 10);
		if (deleted != 0) {
		    cm.gainItem(item, 1, true);
		    cm.sendOk("完成。善待你的鞋子，免得你使鞋子坏掉.");
		} else {
		    cm.sendOk("不幸的是，催化剂...抵触你的鞋子。我很抱歉是我的疏失.....");
		}
	    } else { //just give basic item
		cm.gainItem(item, 1);
		cm.sendOk("完成。善待你的鞋子，免得你使鞋子坏掉.");
	    }
	}
	cm.safeDispose();
    }
}
