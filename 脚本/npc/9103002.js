/**
-- Odin JavaScript --------------------------------------------------------------------------------
	Rolly - Ludibirum Maze PQ
-- By ---------------------------------------------------------------------------------------------
	Raz
-- Version Info -----------------------------------------------------------------------------------
	1.1 - Rewards by Sadiq
	1.0 - First Version by Raz
---------------------------------------------------------------------------------------------------
* */
/***
---------------------------------------------------------------------------------------------------
Reward list 
---------------------------------------------------------------------------------------------------
-100 Mana Elixers
-50 Watermelons 
-50 Pure Waters
-20 Strawberry Milk
-20 Coffee Milk
-20 Fruit Milk
-5 Red Bean Sundaes
-5 Ice Cream Sodas
-Emergency Rescue tube
-5 Elixers
-1 Power Elixer
Scroll for Bottomwear for DEF (60%) 
Dark Scroll for Bottomwear for DEF (30%) 
Scroll for Bottomwear for DEF (10%) 
Dark Scroll for Cape for Magic Def. (30%) 
Dark Scroll for Cape for Weapon Def. (70%) 
Scroll for Cape for Weapon Def. (60%) 
Dark Scroll for Cape for Weapon Def. (30%) 
Dark Scroll for Cape for Magic Def. (30%) 
Scroll for Cape for INT (10%) 
Scroll for Cape for DEX (10%) 
Scroll for Cape for Luk (10%)
Dark Scroll for Helmet for DEF (70%) 
Scroll for Helmet for DEF (60%) 
Dark Scroll for Helmet for DEF (30%) 
Scroll for Helmet for DEF (10%) 
Scroll for Overall Armor for DEF (60%) 
Dark Scroll for Overall Armor for DEF (30%) 
Scroll for Overall Armor for DEF (10%) 
Dark Scroll for Shield for DEF (70%) 
Scroll for Shield for DEF (60%) 
Dark Scroll for Shield for DEF (30%) 
Scroll for Shield for DEF (10%) 
Dark Scroll for Topwear for DEF (70%) 
Scroll for Topwear for DEF (60%) 
Dark Scroll for Topwear for DEF (30%) 
Scroll for Topwear for DEF (10%) 
-Blood Snowboard
-100 Dried Squids
-50  ramen
-100 Hot Dog Supremes
-20 Fat Sausage
-20 Grape Juice
-Red Hearted Earrings 
-Yellow Umbrella
------------------------------------------------------------------------------------------------------
***/

var status = 0;

var prizeIdScroll = Array(2040601, 2040001, 2040504, 2040901, 2040401, 2040605, 2041027, 2041029, 2041029, 2040511, 2040905, 2040405, 2040602, 2041017, 2041023, 2041020, 2040002, 2040902, 2040402);                                             
var prizeIdUse = Array(2000006, 2001000, 2022000, 2030009, 2030008, 2030009, 2001002, 2001001, 2000012, 2000005, 2020007, 2022018, 2020006, 2020008, 2020010);                                            
var prizeQtyUse = Array(100, 50, 50, 20, 20, 20, 5, 5, 5, 1, 100, 50, 100, 20, 20);
var prizeIdEquip = Array(1302016, 1032013, 1442017, 1322025);                                             

function getPrize(cm) { 
    var itemSetSel = Math.random();
    var itemSet;
    var itemSetQty;
    var hasQty = false;
    if (itemSetSel < 0.2)
	itemSet = prizeIdScroll;
    else if (itemSetSel < 0.1)
	itemSet = prizeIdEquip;
    else {
	itemSet = prizeIdUse;
	itemSetQty = prizeQtyUse;
	hasQty = true;
    }
    var sel = Math.floor(Math.random()*itemSet.length);
    var qty = 1;
    if (hasQty)
	qty = itemSetQty[sel];
    cm.gainItem(itemSet[sel], qty);
}

function start() {
    status = -1;
    action(1, 0, 0);
}

function action(mode, type, selection) {
    if (mode == 0) {
	cm.sendOk("If you wish to receive your rewards and return to Ludibrium, please let me know!");
	cm.dispose();

    } else {
	if (mode == 1)
	    status++;
	else
	    status--;
		
	if (status == 0) {
	    cm.sendYesNo("Your party gave a stellar effort and gathered up at least 30 coupons. For that, i have a present for each and every one of you. After receiving the present, you will be sent back to Ludibrium. Now, would you like to receive the present right now?");

	} else if (status == 1) {
	    cm.warp(809050017);
	    getPrize(cm);
	    cm.dispose();
	}
    }
}