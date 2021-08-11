var sel;
var status = -1;

function start() {
    cm.sendSimple("All your balls for catching monsters here!\r\n#fUI/UIWindow.img/QuestIcon/4/0# \n\r  \n\r #b #L3#Trade 300,000 meso per Basic ball#l \r\n #L4#Trade 600,000 meso per Great ball#l \r\n #L5#Trade 1,200,000 meso per Ultra ball#l \r\n\r\n #L6#Heal Berry (5,000,000 meso)#l \r\n #L7#Cure Berry (10,000,000 meso)#l \r\n #L8#Red Candy (15,000,000 meso)#l \r\n #L9#Blue Candy (15,000,000 meso)#l \r\n #L10#Green Candy (15,000,000 meso)#l \r\n #L11#Dark Chocolate (20,000,000 meso)#l \r\n #L12#White Chocolate (20,000,000 meso)#l \r\n\r\n #L20#EXP Share (permanent) (75,000,000 meso)#l \r\n #L21#Everstone (permanent) (25,000,000 meso)#l#k");
}

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
	if (status == 0) {
	sel = selection;
	switch(sel) {
	    case 3: //points
		var intPoints = cm.getPlayer().getMeso();
	        if (intPoints < 300000) {
		    cm.sendOk("You need at least 300 000 meso.");
		    cm.dispose();
	        } else {
		    cm.sendGetNumber("How many would you like? (1 Basic ball = 300 000 meso) (Current Meso: " + intPoints + ") (Current Balls: " + cm.getPlayer().itemQuantity(3992017) + ")", 1, 1, intPoints / 300000);
	        }
		break;
	    case 4: //points
		var intPoints = cm.getPlayer().getMeso();
	        if (intPoints < 600000) {
		    cm.sendOk("You need at least 600 000 meso.");
		    cm.dispose();
	        } else {
		    cm.sendGetNumber("How many would you like? (1 Great ball = 600 000 meso) (Current Meso: " + intPoints + ") (Current Balls: " + cm.getPlayer().itemQuantity(3992018) + ")", 1, 1, intPoints / 600000);
	        }
		break;
	    case 5: //points
		var intPoints = cm.getPlayer().getMeso();
	        if (intPoints < 1200000) {
		    cm.sendOk("You need at least 1 200 000 meso.");
		    cm.dispose();
	        } else {
		    cm.sendGetNumber("How many would you like? (1 Ultra ball = 1 200 000 meso) (Current Meso: " + intPoints + ") (Current Balls: " + cm.getPlayer().itemQuantity(3992019) + ")", 1, 1, intPoints / 1200000);
	        }
		break;
	    case 6:
		var intPoints = cm.getPlayer().getMeso();
	        if (intPoints < 5000000) {
		    cm.sendOk("You need at least 5 000 000 meso.");
	        } else if (!cm.canHold(4140102,1)) {
		    cm.sendOk("Please make space.");
		} else {
		    cm.gainMeso(-5000000);
		    cm.gainItem(4140102, 1);
		}
		cm.dispose();
		break;
	    case 7:
		var intPoints = cm.getPlayer().getMeso();
	        if (intPoints < 10000000) {
		    cm.sendOk("You need at least 10 000 000 meso.");
	        } else if (!cm.canHold(4140101,1)) {
		    cm.sendOk("Please make space.");
		} else {
		    cm.gainMeso(-10000000);
		    cm.gainItem(4140101, 1);
		}
		cm.dispose();
		break;
	    case 8:
		var intPoints = cm.getPlayer().getMeso();
	        if (intPoints < 15000000) {
		    cm.sendOk("You need at least 15 000 000 meso.");
	        } else if (!cm.canHold(4032444,1)) {
		    cm.sendOk("Please make space.");
		} else {
		    cm.gainMeso(-15000000);
		    cm.gainItem(4032444, 1);
		}
		cm.dispose();
		break;
	    case 9:
		var intPoints = cm.getPlayer().getMeso();
	        if (intPoints < 15000000) {
		    cm.sendOk("You need at least 15 000 000 meso.");
	        } else if (!cm.canHold(4032445,1)) {
		    cm.sendOk("Please make space.");
		} else {
		    cm.gainMeso(-15000000);
		    cm.gainItem(4032445, 1);
		}
		cm.dispose();
		break;
	    case 10:
		var intPoints = cm.getPlayer().getMeso();
	        if (intPoints < 15000000) {
		    cm.sendOk("You need at least 15 000 000 meso.");
	        } else if (!cm.canHold(4032446,1)) {
		    cm.sendOk("Please make space.");
		} else {
		    cm.gainMeso(-15000000);
		    cm.gainItem(4032446, 1);
		}
		cm.dispose();
		break;
	    case 11:
		var intPoints = cm.getPlayer().getMeso();
	        if (intPoints < 20000000) {
		    cm.sendOk("You need at least 20 000 000 meso.");
	        } else if (!cm.canHold(4031110,1)) {
		    cm.sendOk("Please make space.");
		} else {
		    cm.gainMeso(-20000000);
		    cm.gainItem(4031110, 1);
		}
		cm.dispose();
		break;
	    case 12:
		var intPoints = cm.getPlayer().getMeso();
	        if (intPoints < 20000000) {
		    cm.sendOk("You need at least 20 000 000 meso.");
	        } else if (!cm.canHold(4031109,1)) {
		    cm.sendOk("Please make space.");
		} else {
		    cm.gainMeso(-20000000);
		    cm.gainItem(4031109, 1);
		}
		cm.dispose();
		break;
	    case 20:
		var intPoints = cm.getPlayer().getMeso();
	        if (intPoints < 75000000) {
		    cm.sendOk("You need at least 75 000 000 meso.");
	        } else if (!cm.canHold(3994185,1)) {
		    cm.sendOk("Please make space.");
		} else {
		    cm.gainMeso(-75000000);
		    cm.gainItem(3994185, 1);
		}
		cm.dispose();
		break;
	    case 21:
		var intPoints = cm.getPlayer().getMeso();
	        if (intPoints < 25000000) {
		    cm.sendOk("You need at least 25 000 000 meso.");
	        } else if (!cm.canHold(3800088,1)) {
		    cm.sendOk("Please make space.");
		} else {
		    cm.gainMeso(-25000000);
		    cm.gainItem(3800088, 1);
		}
		cm.dispose();
		break;
	}
	} else {
	    if (sel == 3) {
		var intPoints = cm.getPlayer().getMeso();
		if (selection >= 1 && selection <= (intPoints / 300000)) {
			if (selection > (intPoints / 300000)) {
				cm.sendOk("You can only get max " + (intPoints / 300000) + ". 1 Ball = 300000 meso.");
			} else if (!cm.canHold(3992017, selection)) {
				cm.sendOk("Please make space in SETUP tab.");
			} else {
				cm.gainItem(3992017, selection);
				cm.gainMeso(-300000 * selection);
			}
		}
	    } else if (sel == 4) {
		var intPoints = cm.getPlayer().getMeso();
		if (selection >= 1 && selection <= (intPoints / 600000)) {
			if (selection > (intPoints / 600000)) {
				cm.sendOk("You can only get max " + (intPoints / 600000) + ". 1 Ball = 600000 meso.");
			} else if (!cm.canHold(3992018, selection)) {
				cm.sendOk("Please make space in SETUP tab.");
			} else {
				cm.gainItem(3992018, selection);
				cm.gainMeso(-600000 * selection);
			}
		}
	    } else if (sel == 5) {
		var intPoints = cm.getPlayer().getMeso();
		if (selection >= 1 && selection <= (intPoints / 1200000)) {
			if (selection > (intPoints / 1200000)) {
				cm.sendOk("You can only get max " + (intPoints / 1200000) + ". 1 Ball = 1200000 meso.");
			} else if (!cm.canHold(3992019, selection)) {
				cm.sendOk("Please make space in SETUP tab.");
			} else {
				cm.gainItem(3992019, selection);
				cm.gainMeso(-1200000 * selection);
			}
		}
	    }
	    cm.dispose();
	}
    } else {
	cm.dispose();
    }
}