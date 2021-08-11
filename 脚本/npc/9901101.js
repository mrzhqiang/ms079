var status = -1;
var selected = 0;
var itemids = Array(3010054, 3010014, 3010068, 1112127, 2290057, 2290084, 2290096, 2290011, 2049100, 2040506, 2040303, 2040710, 2040807);
var quantitys = Array(1,1,1,1,1,1,1,1,50,1,1,1,1);
var expires = Array(-1,-1,-1,7,-1,-1,-1,-1,-1,-1,-1,-1,-1);
var ytickets = Array(200,200,200,400,400,400,400,400,400,800,800,800,800);
var wtickets = Array(4000,4000,4000,8000,8000,8000,8000,8000,8000,16000,16000,16000,16000);

function start() {
	action(1, 0, 0);
}

function action(mode, type, selection) {
	if (mode != 1) {
		cm.dispose();
		return;
	}
	status++;
	if (status == 0) {
		cm.sendSimple("Hello, #h0#. I am in charge of the Premium Minidungeons.\r\n\r\n#b#L0#Enter Premium Minidungeon#l#k\r\n#r#L1#Redeem Premium Minidungeon items#l#k");
	} else if (status == 1) {
		selected = selection;
		if (selection == 0){
			if (cm.haveItem(5451000)) {
				cm.sendYesNo("Hello, donator! I see you have #v5451000##t5451000#. Would you like to enter the Premium Minidungeon? #eYou will not be able to use potions. Be prepared.#n");
			} else {
				cm.sendYesNo("You will need 30,000,000 mesos to enter this map. Proceed? #eYou will lose HP periodically in the map and you will not be able to use potions. Be prepared.#n");
			}
		} else {
			var selStr = "Well, okay. These are what you can redeem using your #v5220020##t5220020#(Rare) and #v5220010##t5220010#(Normal)...\r\n\r\n#b";
			for (var i = 0; i < itemids.length; i++) {
				selStr += "#L" + i + "##i" + itemids[i] + "##z" + itemids[i] + "# x " + quantitys[i] + " for #r#v5220020# x " + ytickets[i] + "#k and #r#v5220010# x " + wtickets[i] + "#b" + (expires[i] > 0 ? (" (lasts for " + expires[i] + " days)") : "") + "#l\r\n";
			}
			cm.sendSimple(selStr);
		}
	} else if (status == 2) {
		if (selected == 0) {
			var em = cm.getEventManager("MiniDungeon");
			if (em == null) {
				cm.sendOk("The Minidungeon is currently not available.");
			} else if (!cm.haveItem(5451000) && cm.getPlayer().getMeso() < 30000000) {
				cm.sendOk("Please check your mesos.");
			} else {
				if (!cm.haveItem(5451000)) {
					cm.gainMeso(-30000000);
				}
				em.startInstance_CharID(cm.getPlayer());
			}
			cm.dispose();
		} else {
			if (!cm.canHold(itemids[selection], quantitys[selection])) {
				cm.sendOk("Please make room");
			} else if (cm.itemQuantity(5220020) < ytickets[selection] || cm.itemQuantity(5220010) < wtickets[selection]) {
				cm.sendOk("You don't have enough tickets.");
			} else {
				cm.gainItem(5220020, -ytickets[selection]);
				cm.gainItem(5220010, -wtickets[selection]);
				if (expires[selection] > 0) {
					cm.gainItemPeriod(itemids[selection], quantitys[selection], expires[selection]);
				} else {
					cm.gainItem(itemids[selection], quantitys[selection]);
				}
				cm.sendOk("Thank you for your redemption~");
			}
			cm.dispose();
		}
	}
}