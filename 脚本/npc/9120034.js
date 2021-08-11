/*
	Noran
 */

var status = -1;

function start() {
    cm.sendSimple("How can I help you? \r #b#L0#I want to release seal on Sealed Warrior Stone.#l \r #L1#I want to create item.#l");
}

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else if (status == 1) {
	status--;
	selection = 0;
    } else {
	cm.dispose();
	return;
    }
	
    switch (status) {
	case 0:
	    if (selection == 0) {
		cm.sendNext("My name is Noran, technical personnel. Here, everyone is talking about you. If you could defeat mechanic monster, I wish to help you too. With blaze technology, more powerful item can be created.")
	    } else {
		status = 9;
		cm.sendSimple("How can I help you? \r #b#L0#Magic Throwing Knife#l \r #L1#Armor-Piercing Bullet#l");
	    }
	    break;
	case 1:
	    cm.sendNextPrev("It's been said that Blaze has succeeded to collect energy floating in universe. If this is true, massive energy can be obtained. Small portion of this energy can be extracted from Sealed Warrior Stone but it must be unsealed in order to be used. Bring it to me and i will release it's seal.");
	    break;
	case 2:
	    cm.sendSimple("Give me the sealed stone \r #b#L0#Give Sealed Warrior Stone and service fee. 1,000 Silver Coin#l \r #L1#Give Sealed Wiseman Stone and service fee. 1,000 Silver Coin#l \r #L2#Give Sealed Saint Stone and service fee. 1,000 Silver Coin#l");
	    break;
	case 3:
	    if (selection == 0) {
		if (cm.haveItem(4020010, 1) && cm.haveItem(4032181, 1000)) {
		    cm.gainItem(4032169, 1);
		    cm.gainItem(4020010, -1);
		    cm.gainItem(4032181, -1000);
		} else {
		    cm.sendNext("Eh? You don't have required materials. \n\r You need Sealed Warrior Stone and 1,000 Silver Coin to create Warrior Stone.");
		}
	    } else if (selection == 1) {
		if (cm.haveItem(4020011, 1) && cm.haveItem(4032181, 1000)) {
		    cm.gainItem(4032170, 1);
		    cm.gainItem(4020011, -1);
		    cm.gainItem(4032181, -1000);
		} else {
		    cm.sendNext("Eh? You don't have required materials. \n\r You need Sealed Wiseman Stone and 1,000 Silver Coin to create Wiseman Stone.");
		}
	    } else {
		if (cm.haveItem(4020012, 1) && cm.haveItem(4032181, 1000)) {
		    cm.gainItem(4032171, 1);
		    cm.gainItem(4020011, -1);
		    cm.gainItem(4032181, -1000);
		} else {
		    cm.sendNext("Eh? You don't have required materials. \n\r You need Sealed Saint Stone and 1,000 Silver Coin to create Saint stone.");
		}
	    }
	    cm.dispose();
	    break;
	case 10:
	    if (selection == 0) {
		if (cm.haveItem(4032168, 1) && cm.haveItem(4032181, 2500) && cm.haveItem(4032171, 1) && cm.haveItem(2070006, 1) && (cm.getMeso() >= 500000000)) {
		    cm.gainItem(4032171, -1);
		    cm.gainItem(4032168, -1);
		    cm.gainItem(2070006, -1);
		    cm.gainItem(4032181, -2500);
		    cm.gainMeso(-500000000);
		    cm.gainItem(2070019, 1);
		} else {
		    cm.sendNext("Eh? You don't have required materials.\n\r You need Nano Plant (Omega), Wiseman Stone, 1 Ilbi Throwing-Stars, Silver Coin 2,500 Pieces and 500,000,000 meso to create Magic Throwing Knife.");
		}
	    } else {
		if (cm.haveItem(4032168, 1) && cm.haveItem(4032181, 2500) && cm.haveItem(4032170, 1) && cm.haveItem(2330003, 1) && (cm.getMeso() >= 500000000)) {
		    cm.gainItem(4032170, -1);
		    cm.gainItem(4032168, -1);
		    cm.gainItem(2330003, -1);
		    cm.gainItem(4032181, -2500);
		    cm.gainMeso(-500000000);
		    cm.gainItem(2330007, 1);
		} else {
		    cm.sendNext("Eh? You don't have required materials.\n\r You need Nano Plant (Omega), Saint Stone, 1 Vital Bullet, Silver Coin 2,500 Pieces and 500,000,000 meso to create Armor Piercing bullet.");
		}
	    }
	    cm.dispose();
	    break;
    }
}