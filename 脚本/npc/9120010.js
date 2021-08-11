/**
-- Odin JavaScript --------------------------------------------------------------------------------
	Faito(Showa Exchange Quest) - Showa Town(801000300)
-- By ---------------------------------------------------------------------------------------------
	Information
-- Version Info -----------------------------------------------------------------------------------
	1.0 - First Version by Information
	    - Base from Sean360 script, thanks
---------------------------------------------------------------------------------------------------
**/

var status = -1;
var eQuestChoices = new Array (4000064,4000065,4000066,4000075,4000077,4000089,4000090,4000091,4000092,4000093,4000094); 

var eQuestPrizes = new Array();

eQuestPrizes[0] = new Array ([2000000,1],  // Red Potion
    [2000006,1],	// Mana Elixir
    [2000003,5], 	// Blue Potion
    [2000002,5],	// White Potion
    [4020006,2],	// Topaz Ore
    [4020000,2],	// Garnet Ore
    [4020004,2],	// Opal Ore
    [2000003,10],	// Blue Potion
    [2000003,20],	// Blue Potion
    [2000002,10],	// White Potion
    [2000002,20],	// White Potion
    [2022026,15],	// Yakisoba
    [2022024,15],	// Takoyaki (Octopus Ball)
    [1002393,1]);	// Pink Bandana
eQuestPrizes[1] = new Array ([2000006,1],	// Mana Elixir
    [2000002,5],	// White Potion
    [4020006,2],	// Topaz Ore
    [2000002,10],	// White Potion
    [2000003,10],	// Blue Potion
    [2000002,20],	// White Potion
    [2000003,20],	// Blue Potion
    [2022024,15],	// Takoyaki (Octopus Ball)
    [2022026,15]);	// Yakisoba
eQuestPrizes[2] = new Array ([2000006,1],	// Mana Elixir
    [2000002,5],	// White Potion
    [2000003,5],	// Blue Potion
    [4020000,2],	// Garnet Ore
    [2000003,10],	// Blue Potion
    [2000002,10],	// White Potion
    [2000003,20],	// Blue Potion
    [2000002,20],	// White Potion
    [2022024,15],	// Takoyaki (Octopus Ball)
    [1002393,1]);	// Pink Bandana
eQuestPrizes[3] = new Array ([2060003,1000],	// Red Arrow for Bow
    [4010004,2],	// Silver Ore
    [4010006,2],	// Gold Ore
    [2022022,5],	// Tempura (Dish)
    [2022022,10],	// Tempura (Dish)
    [2022022,15],	// Tempura (Dish)
    [2022019,5],	// Kinoko Ramen (Pig Bone)
    [2022019,10],	// Kinoko Ramen (Pig Bone)
    [2022019,15],	// Kinoko Ramen (Pig Bone)
    [2001002,15],	// Red Bean Sundae
    [2001001,15],	// Ice Cream Pop
    [1102040,1],	// Yellow Adventurer Cape
    [1102043,1]);	// Brown Adventurer Cape
eQuestPrizes[4] = new Array ([2000003,1],	//Blue Potion
    [2022019,5],	// Kinoko Ramen (Pig Bone)
    [2000006,5],	// Mana Elixir
    [4010002,2],	// Mithril Ore
    [4010003,2],	// Adamantium Ore
    [2000006,10],	// Mana Elixir
    [2000006,15],	// Mana Elixir
    [2022019,10],	// Kinoko Ramen (Pig Bone)
    [2022019,15],	// Kinoko Ramen (Pig Bone)
    [2060003,1000],	// Red Arrow for Bow
    [2061003,1000],	// Blue Arrow for Crossbow
    [1082150,1],	// Grey Work Gloves
    [1082149,1]);	// Brown Work Gloves
eQuestPrizes[5] = new Array ([2000006,1],	// Mana Elixir
    [2000003,5],	// Blue Potion
    [2000002,5],	// White Potion
    [2000003,10],	// Blue Potion
    [2000003,20],	// Blue Potion
    [2000002,10],	// White Potion
    [2000002,15],	// White Potion
    [2060003,1000],	// Red Arrow for Bow
    [2061003,1000],	// Blue Arrow for Crossbow
    [2022026,15],	// Yakisoba
    [1002395,1]);	// Purple Bandana
eQuestPrizes[6] = new Array ([2022019,5],	// Kinoko Ramen (Pig Bone)
    [2000006,5],	// Mana Elixir
    [4010003,2],	// Adamantium Ore
    [2022019,10],	// Kinoko Ramen (Pig Bone)
    [2022019,15],	// Kinoko Ramen (Pig Bone)
    [2000006,10],	// Mana Elixir
    [2000006,15],	// Mana Elixir
    [2060003,1000],	// Red Arrow for Bow
    [2061003,1000]);	// Blue Arrow for Crossbow
eQuestPrizes[7] = new Array ([2000003,1],	// Blue Potion
    [2000006,1],	// Mana Elixir
    [2022019,1],	// Kinoko Ramen (Pig Bone)
    [2000006,5],	// Mana Elixir
    [4010002,2],	// Mithril Ore
    [4020001,2],	// Amethyst Ore
    [2022019,10],	// Kinoko Ramen (Pig Bone)
    [2022019,15],	// Kinoko Ramen (Pig Bone)
    [2000006,10],	// Mana Elixir
    [2000006,15],	// Mana Elixir
    [2060003,1000],	// Red Arrow for Bow
    [2061003,1000]);	// Blue Arrow for Crossbow
eQuestPrizes[8] = new Array ([2022019,5],	// Kinoko Ramen (Pig Bone)
    [2022022,5],	// Tempura (Dish)
    [4010006,2],	// Gold Ore
    [2022019,10],	// Kinoko Ramen (Pig Bone)
    [2022019,15],	// Kinoko Ramen (Pig Bone)
    [2022022,10],	// Tempura (Dish)
    [2022022,15],	// Tempura (Dish)
    [2001002,15],	// Red Bean Sundae
    [2001001,15],	// Ice Cream Pop
    [1102043,1]);	// Brown Adventurer Cape
eQuestPrizes[9] = new Array ([4010004,5],	// Silver Ore
    [2022019,5],	// Kinoko Ramen (Pig Bone)
    [2022022,15],	// Tempura (Dish)
    [2022019,15],	// Kinoko Ramen (Pig Bone)
    [2001002,15],	// Red Bean Sundae
    [2001001,15],	// Ice Cream Pop
    [1102043,1]);	// Brown Adventurer Cape
eQuestPrizes[10] = new Array   ([2000006,1],	// Mana Elixir
    [4020008,15],	// Black Crystal Ore
    [2022018,5],	// Kinoko Ramen (Roasted Pork)
    [2022018,10],	// Kinoko Ramen (Roasted Pork)
    [2022018,15],	// Kinoko Ramen (Roasted Pork)
    [2022000,10],	// Pure Water
    [2022000,20],	// Pure Water
    [2022025,15]);	// Takoyaki (Jumbo)
var requiredItem  = 0;
var lastSelection = 0;
var prizeItem     = 0;
var prizeQuantity = 0;
var info;
var itemSet;
var reward;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.sendOk("真的吗？让我知道，如果你改变了主意。");
	    cm.safeDispose();
	    return;
	}
	status--;
    }

    if (status == 0) {
	cm.sendYesNo("如果你正在寻找某人，可以精确定位各种物品的特点，你看一个现在。目前，我正在寻找的东西。你想听听我的故事吗？");
    } else if (status == 1) {
	var eQuestChoice = makeChoices(eQuestChoices);
	cm.sendSimple(eQuestChoice);
    } else if (status == 2){
	requiredItem = eQuestChoices[selection];
	reward = eQuestPrizes[selection];
	itemSet = (Math.floor(Math.random() * reward.length));
	prizeItem = reward[itemSet][0];
	prizeQuantity = reward[itemSet][1];
	if (!cm.canHold(prizeItem)){
	    cm.sendNext("什么？我不能给你奖励，确保你的道具栏有无满。");
	} else if (checkQuantity(requiredItem) >= 100){   // check they have >= 100 in Inventory
	    cm.gainItem(requiredItem,-100);
	    cm.gainItem(prizeItem,prizeQuantity);
	    cm.sendOk("嗯......如果不是因为这个小划痕......叹了口气。恐怕我只能认为这是一个标准的品质项目。那么，这里的 \r\n#t"+ prizeItem +"# 给你。");
	} else{
	    cm.sendOk("嘿，你觉得你在做什么？不是我去骗别人，不知道他在说什么！");
	}
	cm.safeDispose();
    }
}

function makeChoices(a){
    var result  = "我在寻找项目1,2,3... 太多了\r\n提。总之，如果你收集了相同的项目100个，\r\n然后我可以用类似的东西换它。什么？你可以\r\n不知道这一点，但我信守承诺我结束，所以你\r\n不用担心。现在，我们应该交易？\r\n";
    for (var x = 0; x< a.length; x++){
	result += " #L" + x + "##v" + a[x] + "##t" + a[x] + "##l\r\n";
    }
    return result;
}
function checkQuantity(itemId){
    var itemCount = 0;
    var iter = cm.getInventory(4).listById(itemId).iterator();
    while (iter.hasNext()) {
	itemCount += iter.next().getQuantity();
    }
    return itemCount;
}