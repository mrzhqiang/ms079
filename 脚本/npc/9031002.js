var status = -1;
var sel = -1;

function action(mode, type, selection) {
    if (mode == 1) {
	status++;
    } else {
	if (status == 0) {
	    cm.dispose();
	}
	status--;
    }
    if (status == 0) {
	cm.sendSimple("#b#L0#Learn/Unlearn Mining#l\r\n#L1#Trade Ore Fragments#l");
    } else if (status == 1) {
	sel = selection;
	if (sel == 0) {
	    if (cm.getPlayer().getProfessionLevel(92020000) > 0 || cm.getPlayer().getProfessionLevel(92030000) > 0 || cm.getPlayer().getProfessionLevel(92040000) > 0) {
		cm.sendOk("Please unlearn Smithing/Accessory Crafting/Alchemy first.");
		cm.dispose();
		return;
	    }
	    if (cm.getPlayer().getProfessionLevel(92010000) > 0) {
		cm.sendYesNo("Are you sure you wish to unlearn Mining? You will lose all your EXP/levels in Mining.");
	    } else if (cm.getPlayer().getProfessionLevel(92000000) > 0) {
		cm.sendOk("You cannot learn or unlearn Mining because you already have Herbalism.");
		cm.dispose();
	    } else {
		cm.sendYesNo("Would you like to learn Mining?");
	    }
	} else if (sel == 1) {
	    if (!cm.haveItem(4011010, 100)) {
		cm.sendOk("You need 100 Ore Fragments.");
 	    } else if (!cm.canHold(2028067, 1)) {
		cm.sendOk("Please make some USE space.");
	    } else {
		cm.sendOk("Thank you.");
		cm.gainItem(2028067, 1);
		cm.gainItem(4011010, -100);
	    } 
	    cm.dispose();
	}
    } else if (status == 2) {
	if (sel == 0) {
	    if (cm.getPlayer().getProfessionLevel(92010000) > 0) {
		cm.sendOk("You have unlearned Mining.");
		cm.teachSkill(92010000, 0, 0);
	    } else {
		cm.sendOk("You have learned Mining.");
		cm.teachSkill(92010000, 0x1000000, 0); //00 00 00 01
		if (cm.canHold(1512000,1)) {
			cm.gainItem(1512000,1);
		}
	    }
	    cm.dispose();
	}
    }
}