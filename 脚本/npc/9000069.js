/*
	Tia
*/

var status = -1;

var scroll_15 = new Array(
    2040020, // Helmet Def
    2040022, // Helmet HP
    2040044, // Helmet DEX
    2040314, // Earring Int
    2040340, 
    2040336, // Earring DEX
    2040338, // Earring LUK
    2040416, // Topwear Def
    2040432,
    2040434,
    2040436,
    2040523, // Overall Dex
    2040525, // Overall Def
    2040527, // Overall Int
    2040529, // Overall Luk
    2040541, 
    2040632,
    2040634, 
    2040616, // Bottom Def
    2040719, // Shoes Dex
    2040721, // Shoes Jump
    2040723, // Shoes Speed
    2040820, // Glove Dex
    2040822, // Glove Atk
    2040832, 
    2040911, // Shield Def
    2040938,
    2040940,
    2040942, 
    2041043, // Cape Magic Def
    2041045, // Cape Weapon Def
    2041047, // Cape HP
    2041049, // Cape MP
    2041051, // Cape STR
    2041053, // Cape INT
    2041055, // Cape DEX
    2041057, // Cape LUK
    2043012, // 1 Handed Sword
    2043107, // 1 Handed Axe
    2043207, // 1 Handed BW
    2043307, // Dagger
    2043404,
	2045306,
	2045206, 
    2043707, // Wand Magic
    2043807, // Staff Magic
    2044007, // 2 Handed Sword
    2044107, // 2 Handed Axe
    2044207, // 2 Handed BW
    2044307, // Spear Atk
    2044407, // Pole Arm ATK
    2044507, // Bow Atk
    2044607, // Crossbow Atk
    2044707, // Claw Atk
    2044812, // Knuckle Atk
    2044907, // Gun Atk
    2048007, // Pet EQ Speed
    2048009 // Pet EQ Jump
    );

var scroll_60 = new Array(
    2040001, // Helmet Def
    2040004, // Helmet HP
    2040029, // Helmet DEX
    2040301, // Earring Int
    2040326, 
    2040317,
    2040321,
    2040401, // Topwear Def
    2040418, 
    2040421, 
    2040425, 
    2040501, // Overall Dex
    2040504, // Overall Def
    2040513, // Overall Int
    2040516, // Overall Luk
    2040532, 
    2040618,
    2040621,
    2040601, // Bottom Def
    2040701, // Shoes Dex
    2040704, // Shoes Jump
    2040707, // Shoes Speed
    2040801, // Glove Dex
    2040804, // Glove Atk
    2040824, 
    2040901, // Shield Def
    2040924,
    2040927, 
    2040931,
    2041001, // Cape Magic Def
    2041004, // Cape Weapon Def
    2041007, // Cape HP
    2041010, // Cape MP
    2041013, // Cape STR
    2041016, // Cape INT
    2041019, // Cape DEX
    2041022, // Cape LUK
    2043001, // 1 Handed Sword
    2043101, // 1 Handed Axe
    2043201, // 1 Handed BW
    2043301, // Dagger Atk
    2043401,
	2045301,
	2045201,
    2043701, // Wand Magic
    2043801, // Staff Magic
    2044001, // 2 Handed Sword
    2044101, // 2 Handed Axe
    2044201, // 2 Handed BW
    2044301, // Spear ATK
    2044401, // Pole Arm ATK
    2044501, // Bow Atk
    2044601, // Crossbow Atk
    2044701, // Claw Atk
    2044801, // Knuckle Atk
    2044901, // Gun Atk
    2048001, // Pet Speed
    2048004 // Pet Jump
    );



var scroll_10 = new Array(
    2040002, // Helmet Def
    2040005, // Helmet HP
    2040031, // Helmet DEX
    2040302, // Earring Int
    2040328, 
    2040318,
    2040323,
    2040402, // Topwear Def
    2040419, 
    2040422, 
    2040427, 
    2040502, // Overall Dex
    2040505, // Overall Def
    2040514, // Overall Int
    2040517, // Overall Luk
    2040534, 
    2040619,
    2040622,
    2040602, // Bottom Def
    2040702, // Shoes Dex
    2040705, // Shoes Jump
    2040708, // Shoes Speed
    2040802, // Glove Dex
    2040805, // Glove Atk
    2040825, 
    2040902, // Shield Def
    2040925,
    2040928, 
    2040933,
    2041002, // Cape Magic Def
    2041005, // Cape Weapon Def
    2041008, // Cape HP
    2041011, // Cape MP
    2041014, // Cape STR
    2041017, // Cape INT
    2041020, // Cape DEX
    2041023, // Cape LUK
    2043002, // 1 Handed Sword
    2043102, // 1 Handed Axe
    2043202, // 1 Handed BW
    2043302, // Dagger Atk
    2043402,
	2045302,
	2045202,
    2043702, // Wand Magic
    2043802, // Staff Magic
    2044002, // 2 Handed Sword
    2044102, // 2 Handed Axe
    2044202, // 2 Handed BW
    2044302, // Spear ATK
    2044402, // Pole Arm ATK
    2044502, // Bow Atk
    2044602, // Crossbow Atk
    2044702, // Claw Atk
    2044802, // Knuckle Atk
    2044902, // Gun Atk
    2048002, // Pet Speed
    2048005 // Pet Jump
    );

var firstSel = 0;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 2) {
	    cm.sendOk("This is a serious matter! Don't bother me if you are unwilling to do it.");
	    cm.safeDispose();
	    return;
	} else if (status == 4 || status == 0 || status == 1) {
	    cm.dispose();
	    return;
	}
	status--;
    }
    if (status == 0) {
	cm.sendNext("Scrolls are always regarded as the important aspect of Mapler's equipment upgrade process. Are you one of those who strive for achieving perfection in the crafted material?#b\r\n#L0#Create a 65% Scroll#l\r\n#L1#Create a 15% scroll#l#k");
    } else if (status == 1) {
	firstSel = selection;
	cm.sendYesNo("Oh, hey there. You seems to be more interested than I do. I can always upgrade your scrolls with a little fee and some materials. Are you sure you want to do that? #bThere is a 10% chance that the scroll might be destroyed in the process of the creation.#k")
    } else if (status == 2) {
	cm.sendNext("Now here's the ingredients for resurrection of the ancient black magic scroll. \n\r #i4011006# #t4011006# x1 \n\r #i4021007# #t4021007# x1 \n\r #i4021008# #t4021008# x1 \n\r Mesos 500,000 \n\r\n\r Lastly, it will also require the 10% scroll.");
    } else if (status == 3) {
	var message = "Here's the list of scrolls that are within my powers to be created : \n\r";
if (firstSel == 0) {
	for (var i = 0; i < scroll_15.length; i++) {
		if ((scroll_15[i] != 2045206 && scroll_15[i] != 2045306) || cm.isGMS()) { //TODO JUMP
			message += " #L"+i+"##i"+(scroll_15[i]-1)+"# #t"+(scroll_15[i]-1)+"##l \n\r";
		}
	}
} else if (firstSel == 1) {
	for (var i = 0; i < scroll_15.length; i++) {
		if ((scroll_15[i] != 2045206 && scroll_15[i] != 2045306) || cm.isGMS()) { //TODO JUMP
			message += " #L"+i+"##i"+scroll_15[i]+"# #t"+scroll_15[i]+"##l \n\r";
		}
	}
}
	cm.sendSimple(message);
    } else if (status == 4) {
	var prompt = -1;

	if (selection >= 0 && selection <= scroll_15.length) {
	    if (!cm.haveItem(firstSel == 0 ? scroll_60[selection] : scroll_10[selection])) {
		prompt = firstSel == 0 ? scroll_60[selection] : scroll_10[selection];
	    } else if (cm.getMeso() < 500000) {
		prompt = -2;
	    } else if (!cm.haveItem(4011006)) {
		prompt = 4011006;
	    } else if (!cm.haveItem(4021008)) {
		prompt = 4021008;
	    } else if (!cm.haveItem(4021007)) {
		prompt = 4021007;
	    } else if (!cm.canHold(firstSel == 0 ? (scroll_15[selection]-1) : scroll_15[selection], 1)) {
		prompt = -3;
	    }
	} else { // Hack
	    cm.dispose();
	    return;
	}

	if (prompt == -1) {
	    cm.gainMeso(-500000);
	    cm.gainItem(4011006, -1);
	    cm.gainItem(4021008, -1);
	    cm.gainItem(4021007, -1);
	    cm.gainItem(firstSel == 0 ? scroll_60[selection] : scroll_10[selection], -1);
	    if (Math.floor(Math.random() * 100) < 90) {
	if (firstSel == 0) {
		cm.gainItem(scroll_15[selection]-1, 1);
		cm.sendOk("#i"+(scroll_15[selection]-1)+"# #t"+(scroll_15[selection]-1)+"# \n\r Handle this with care! It's an ancient scroll with mystical magic powers.");
	} else if (firstSel == 1) {
		cm.gainItem(scroll_15[selection], 1);
		cm.sendOk("#i"+scroll_15[selection]+"# #t"+scroll_15[selection]+"# \n\r Handle this with care! It's an ancient scroll with mystical magic powers.");
	}
	    } else {
		cm.sendOk("Woops! The scroll is destroyed!")
	    }
	    cm.safeDispose();
	} else {
	    if (prompt == -2) {
		cm.sendOk("It appears that you are short of 500,000 mesos.");
	    } else if (prompt == -3) {
		cm.sendOk("It seems that you do not have the appropriate USE space.");
	    } else {
		cm.sendOk("It appears that you are short of #i"+prompt+"# #t"+prompt+"#.");
	    }
	    cm.safeDispose();
	}
    }
}
